package com.example.android.notekeeper;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecylerAdapter extends RecyclerView.Adapter<CourseRecylerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<CourseInfo> mCourses;
    public CourseRecylerAdapter(Context mContext, List<CourseInfo> mCourses) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mCourses = mCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfo courses = mCourses.get(position);
        holder.mTextCourse.setText(courses.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,mCourses.get(mCurrentPosition).getTitle(),Snackbar.LENGTH_LONG).show();

                }
            });

        }
    }
}
