package com.rxandroidsample.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 1/20/2017.
 */

public class CourseItem {
    private List<Course> data;
    private int total;
    @SerializedName("per_page")
    private int perPage;

    public List<Course> getData() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}
