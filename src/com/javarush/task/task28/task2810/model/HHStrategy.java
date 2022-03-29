package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {

    private static final String URL_FORMAT = "https://hh.ru/search/vacancy?text=java+%s&page=%d";
    //private static final String URL_FORMAT = "https://javarush.ru/testdata/big28data.html";

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        return Jsoup.connect(url).referrer("https://hh.ru/").userAgent("Mozilla/5.0").get();
    }

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy;
        Document doc = null;
        Elements elements = null;
        String title, salary = "", city, companyName, siteName = "", url;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                doc = getDocument(searchString, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc != null)
                elements = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy vacancy-serp__vacancy_standard_plus");
            if (elements == null || elements.size() == 0)
                return vacancies;
            for (Element el : elements
            ) {
                vacancy = new Vacancy();
                title = getText(el, "vacancy-serp__vacancy-title", true);
                city = getText(el, "vacancy-serp__vacancy-address", true);
                companyName = getText(el, "vacancy-serp__vacancy-employer", true);
                url = getText(el, "vacancy-serp__vacancy-title", false);
                try {
                    salary = getText(el, "vacancy-serp__vacancy-compensation", true);
                } catch (IndexOutOfBoundsException ignored) {
                }
                vacancy.setTitle(title);
                vacancy.setCity(city);
                vacancy.setCompanyName(companyName);
                vacancy.setUrl(url);
                vacancy.setSiteName("hh.ru");
                vacancy.setSalary(salary);
                vacancies.add(vacancy);
                salary = "";
            }
        }
        return vacancies;
    }

    private String getText(Element element, String value, boolean text) {
        Element elem = element.getElementsByAttributeValue("data-qa", value).get(0);
        return text
                ? elem.text()
                : elem.attr("href");
    }
}
