package com.raaise.android.model;

import java.util.List;

public class CategoriesPojo {
    List<String> categoryIds;

    public CategoriesPojo(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getIds() {
        return categoryIds;
    }

    public void setIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
