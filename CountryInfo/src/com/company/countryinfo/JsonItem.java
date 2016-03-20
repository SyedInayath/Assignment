package com.company.countryinfo;

/*
 * ClassName    : JsonItem
 * Description  : Data class as per number of items from JSON response
 * 
 */
public class JsonItem {
    private String title;
    private String description;
    private String imageHref;

    public JsonItem(String title, String description, String imageHref) {
        this.title = title;
        this.description = description;
        this.imageHref = imageHref;
    }

    /* Getter methods */
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageHref() {
        return imageHref;
    }
}
