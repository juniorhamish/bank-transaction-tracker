package uk.co.dajohnston.accounts.model;

import java.util.List;

public class Category {

    private String name;
    private String colour;
    private List<String> matchers;

    public Category(String name, String colour) {
        this.name = name;
        this.colour = colour;
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

    public String getColour() {
        return colour;
    }

}
