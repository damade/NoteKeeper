package com.example.android.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteActivity extends AppCompatActivity {
    //public static final String NOTES_POSITION  = NoteListActivity.NOTES_POSITION;

    public static final String NOTES_POSITION = "com.example.android.notekeeper.NOTES_POSITION";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.android.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.android.notekeeper.ORIGINAL_NOTE_TEXT";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.android.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final int POSITION_NOT_SET = -1;

    private NoteInfo mNote;

    private boolean mIsNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int mNotePostion;
    private boolean mIsCancelling;
    private String mOriginalNoteTitle;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = (Spinner) findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        if(savedInstanceState == null){
        saveOriginalNoteValue();
        }
        else{
            restoreOriginalNotesValues(savedInstanceState);
        }
        textNoteTitle = (EditText) findViewById(R.id.text_note_title);
        textNoteText = (EditText) findViewById(R.id.text_note_text);

        if(!mIsNewNote){
            displayNotes(spinnerCourses, textNoteTitle, textNoteText);
        }


        /*EditText textNoteTitle = (EditText) findViewById(R.id.text_note_title);
        EditText textNoteText = (EditText) findViewById(R.id.text_note_text);*/


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NoteListActivity nLA = new NoteListActivity();
                Intent intent = new Intent(NoteActivity.this,NoteListActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private void restoreOriginalNotesValues(Bundle savedInstanceState) {
    mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
    mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
    }

    private void saveOriginalNoteValue() {
        if(mIsNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
        }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsNewNote){
                DataManager.getInstance().removeNote(mNotePostion);
            }
            else{
                storePreviousValues();
            }
        }
        else{
        saveNote();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalNoteText);
        outState.putString(ORIGINAL_NOTE_TITLE,mOriginalNoteTitle);
    }

    private void storePreviousValues() {
        mNote.setCourse(DataManager.getInstance().getCourse(mOriginalNoteCourseId));
        mNote.setText(mOriginalNoteText);
        mNote.setTitle(mOriginalNoteTitle);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        mNote.setTitle(textNoteTitle.getText().toString());
        mNote.setText(textNoteText.getText().toString());
    }

    private void displayNotes(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> course = DataManager.getInstance().getCourses();
        int courseIndex = course.indexOf(mNote.getCourse());


        spinnerCourses.setSelection(courseIndex);
        textNoteText.setText(mNote.getText());
        textNoteTitle.setText(mNote.getTitle());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTES_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if(mIsNewNote) {
            createNewNote();
        }
        else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePostion = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePostion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }
        else if(id == R.id.action_cancel){
            mIsCancelling = true;
            finish();
        }
        else if(id == R.id.action_next){
            moveNext();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        if(mNotePostion >= lastNoteIndex){
            item.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();

        ++mNotePostion;
        mNote = DataManager.getInstance().getNotes().get(mNotePostion);

        saveOriginalNoteValue();

        displayNotes(spinnerCourses,textNoteTitle,textNoteText);
        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = "Checkout what i have done after learning on PluralSight course \"" +
                course.getTitle()+"\"\n"+ textNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
        }
}
