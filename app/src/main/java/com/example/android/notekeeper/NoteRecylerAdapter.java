package com.example.android.notekeeper;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecylerAdapter extends RecyclerView.Adapter<NoteRecylerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NoteInfo> mNotes;
    public NoteRecylerAdapter(Context mContext, List<NoteInfo> mNotes) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextTitle.setText(note.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public final TextView mTextTitle;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);
            mTextTitle = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(mContext,NoteActivity.class);
                        intent.putExtra(NoteActivity.NOTES_POSITION, mCurrentPosition);
                        mContext.startActivity(intent);

                }
            });

        }
    }
}
