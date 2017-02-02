package com.rxandroidsample.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rxandroidsample.R;
import com.rxandroidsample.course.CourseAdapter;
import com.rxandroidsample.data.model.Course;

import java.util.List;

/**
 * Created by Admin on 1/19/2017.
 */

public class CourseFragment extends android.support.v4.app.Fragment {
    private List<Course> mCourses;
    private CourseAdapter mAdapter;
    private RecyclerView mSearchRc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
