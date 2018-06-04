package com.someapp.vishnu.mynotesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {

    EditText editText;
    boolean justAdded;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = getIntent();
        editText = findViewById(R.id.noteEditView);

        position = intent.getIntExtra("text", -1);

        if(position==-1) {

            justAdded = true;

        } else {

            editText.setText(MainActivity.notes.get(position));
            justAdded = false;

        }

    }

    public void onClick(View view) {

        if (justAdded) {

            MainActivity.notes.add(editText.getText().toString());

        } else {

            MainActivity.notes.set(position, editText.getText().toString());

        }

        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this
                .getSharedPreferences("com.someapp.vishnu.mynotesapp", Context.MODE_PRIVATE);

        try {
            sharedPreferences.edit()
                    .putString("text",
                            ObjectSerializer
                                    .serialize(MainActivity.notes))
                    .apply();
        } catch (Exception e) {

            e.printStackTrace();

        }

        Toast.makeText(getApplicationContext(),
                "Note saved!", Toast.LENGTH_SHORT).show();

    }

}
