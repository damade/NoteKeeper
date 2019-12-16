package com.example.android.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class NoteListActivity extends AppCompatActivity {
    private NoteRecylerAdapter mNoteRecylerAdapter;

    //private ArrayAdapter<NoteInfo> madapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this,NoteActivity.class);
                startActivity(intent);
            }
        });
        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //madapterNotes.notifyDataSetChanged();
        mNoteRecylerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerNotes.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecylerAdapter = new NoteRecylerAdapter(this,notes);
        recyclerNotes.setAdapter(mNoteRecylerAdapter);
    }

}
