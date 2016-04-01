package de.codecentric.recommendationService.json;

/**
 * Created by afitz on 23.03.16.
 */
public class Param {

    private String name;
    private String value;

//    public Param(String name, String value) {
//        this.name = name;
//        this.value = value;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //            { "name" : "user", "value" : "U001"},
//            { "name" : "product", "value" : "P002"}
}
