package com.rxandroidsample.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rxandroidsample.R;
import com.rxandroidsample.data.model.Course;

import java.util.List;

/**
 * Created by Admin on 1/19/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    private List<Course> courses;

    public SearchAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearchVh(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchVh) holder).titleTv.setText(courses.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class SearchVh extends RecyclerView.ViewHolder {
        private TextView titleTv;

        public SearchVh(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
        }
    }

}
