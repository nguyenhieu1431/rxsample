package com.rxandroidsample.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rxandroidsample.data.model.Course;

import java.util.List;

/**
 * Created by Admin on 1/20/2017.
 */

public class CourseAdapter extends RecyclerView.Adapter {
    private List<Course> courses;

    public CourseAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class CourseVh extends RecyclerView.ViewHolder {
        public CourseVh(View itemView) {
            super(itemView);
        }
    }
}
