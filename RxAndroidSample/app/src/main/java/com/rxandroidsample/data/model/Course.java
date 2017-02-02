package com.rxandroidsample.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 1/19/2017.
 */

public class Course implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("smallDescription")
    @Expose
    private String smallDescription;
    @SerializedName("linkThumb")
    @Expose
    private String linkThumb;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discountPrice")
    @Expose
    private String discountPrice;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("rating")
    @Expose
    private String rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallDescription() {
        return smallDescription;
    }

    public void setSmallDescription(String smallDescription) {
        this.smallDescription = smallDescription;
    }

    public String getLinkThumb() {
        return linkThumb;
    }

    public void setLinkThumb(String linkThumb) {
        this.linkThumb = linkThumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return title;
    }
}
