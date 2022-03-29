package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.Provider;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private final Provider[] providers;

    public Controller(Provider... providers) {
        if (providers == null || providers.length == 0)
            throw new IllegalArgumentException();
        this.providers = providers;
    }

    public void scan() {
        List<Vacancy> result = new ArrayList<>();
        List<Vacancy> current;
        for (Provider p : providers
        ) {
            try {
                current = p.getJavaVacancies("Moscow");
            } catch (NullPointerException e) {
                current = new ArrayList<>();
            }
            result.addAll(current);
        }
        System.out.println(result.size());

    }

    @Override
    public String toString() {
        return "Controller{" +
                "providers=" + Arrays.toString(providers) +
                '}';
    }
}
