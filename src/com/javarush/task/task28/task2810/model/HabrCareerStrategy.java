package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerStrategy implements Strategy {

    //private static final String URL_FORMAT = "https://career.habr.com/vacancies?q=java+Dnepropetrovsk";
    private static final String URL_FORMAT = "https://career.habr.com/vacancies?page=%d&q=java+%s&type=all";

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, page, searchString);
        return Jsoup.connect(url).referrer("https://career.habr.com/").userAgent("Mozilla/5.0").get();
    }

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy;
        Document doc = null;
        Elements elements = null;
        String title, salary = "", city, companyName, siteName = "https://career.habr.com", url;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                doc = getDocument(searchString, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc != null)
                elements = doc.select("div.vacancy-card__inner");
            if (elements == null || elements.size() == 0)
                return vacancies;
            for (Element element : elements
            ) {
                vacancy = new Vacancy();
                title = element.select("div.vacancy-card__title").first().select("a").text();
                salary = getText(element, "div.basic-salary", false);
                city = element.select("span.preserve-line").first().select("a.link-comp").text();
                companyName = element.select("div.vacancy-card__company-title").first().select("a").first().text();
                url = siteName + element.select("div.vacancy-card__title").select("a").attr("href");
                vacancy.setTitle(title);
                vacancy.setSalary(salary);
                vacancy.setCity(city);
                vacancy.setSiteName(siteName);
                vacancy.setCompanyName(companyName);
                vacancy.setUrl(url);
                vacancies.add(vacancy);
            }
        }
        return vacancies;
    }

    private String getText(Element element, String cssQuery, boolean isHref) {
        return element.select(cssQuery).first().text();
    }
}
