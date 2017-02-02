package com.rxandroidsample;

import com.google.gson.JsonObject;
import com.rxandroidsample.data.model.Course;
import com.rxandroidsample.data.model.CourseItem;
import com.rxandroidsample.data.model.StubItem;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Admin on 1/18/2017.
 */

public interface Api {
    @GET("/posts")
    Observable<List<PostItem>> getPost();

    @POST("searchCourse")
    Observable<StubItem> getCourse(@Body JsonObject keyword, @Query("page") int page);
}
