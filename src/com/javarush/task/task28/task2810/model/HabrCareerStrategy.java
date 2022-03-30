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
        String title, salary = "", city, companyName, siteName = "", url;
        try {
            doc = getDocument(searchString, 0);
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
            title = getText(element, "div.vacancy-card__company-title", false);
            salary = getText(element, "div.basic-salary", false);
            //city = getText(element, "div.inline-list.a", false);

            System.out.println(salary);
        }
        return null;
    }

    private String getText(Element element, String cssQuery, boolean isHref) {
        return element.select(cssQuery).first().text();
    }
}
