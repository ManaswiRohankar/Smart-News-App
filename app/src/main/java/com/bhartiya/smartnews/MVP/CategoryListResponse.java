package com.bhartiya.smartnews.MVP;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CategoryListResponse {

    private String id;
    private String category_name;
    private String category_image;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<NewsListResponse> newsList = null;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String categoryName) {
        this.category_name = categoryName;
    }

    public String getCategoryImage() {
        return category_image;
    }

    public void setCategoryImage(String categoryImage) {
        this.category_image = categoryImage;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    public List<NewsListResponse> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsListResponse> newsList) {
        this.newsList = newsList;
    }

}