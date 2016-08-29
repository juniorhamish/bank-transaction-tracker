package uk.co.dajohnston.accounts.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private List<String> matchers = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category() {
        // Just for Jackson to deserialise
    }

    public String getName() {
        return name;
    }

    public List<String> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<String> matchers) {
        this.matchers = matchers;
    }

}
