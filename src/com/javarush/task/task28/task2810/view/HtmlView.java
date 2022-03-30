package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlView implements View {

    //private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";
    private final String filePath = "src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";
    private Controller controller;

    @Override
    public void update(List<Vacancy> vacancies) {
        updateFile(getUpdatedFileContent(vacancies));
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        Document doc = null;
        Element element = null;
        try {
            doc = getDocument();
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
        Element temp = getTemplate(doc);
        doc = clearVacancies(doc);
        for (Vacancy v : vacancies
        ) {
            Element tempClone = temp.clone();
            element = createHtmlVacancy(v, tempClone);
            doc.select("tr.template").first().before(element);
        }
        return doc.outerHtml();
    }

    private Element createHtmlVacancy(Vacancy vacancy, Element template) {
        Element result = template;
        for (Map.Entry<String, String> entry : cssQueries(vacancy).entrySet()
        ) {
            result = createHtmlCell(result, entry.getKey(), entry.getValue(), "");
        }
        result = createHtmlCell(result, "td.title", vacancy.getTitle(), vacancy.getUrl());
        return result;
    }

    private Map<String, String> cssQueries(Vacancy vacancy) {
        Map<String, String> result = new HashMap<>();
        result.put("td.city", vacancy.getCity());
        result.put("td.companyName", vacancy.getCompanyName());
        result.put("td.salary", vacancy.getSalary());
        return result;
    }

    private Element createHtmlCell(Element element, String cssQuery, String text, String href) {
        if (!"td.title".equals(cssQuery))
            element.select(cssQuery).first().text(text);
        else element.select("a").first().attr("href", href).text(text);
        return element;
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    private Element getTemplate(Document document) {
        if (document == null)
            return null;
        Element element = document.getElementsByClass("template").first();
        Element elementClone = element.clone();
        elementClone.removeClass("template");
        elementClone.removeAttr("style");
        return elementClone;
    }

    private Document clearVacancies(Document document) {
        if (document == null)
           return null;
        for (Element element : document.select("tr.vacancy").not("tr.template")
        ) {
            element.remove();
        }
        return document;
    }

    private void updateFile(String html) {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(fileWriter)
        ) {
            bw.write(html);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Some exception occurred");
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Novgorod");
    }
}
