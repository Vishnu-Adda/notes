package com.someapp.vishnu.mynotesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    static ArrayList<String> notes = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    /**
     * Inflating the expandable ellipses
     * @param menu
     *  menu returned when tapped
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When an item in the menu is clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        Opens up the note activity
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(), NotesActivity.class);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "Note added!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;

        }
    }

    /**
     * Initialize adapter for ListView, and bring up stored notes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(
                "com.someapp.vishnu.mynotesapp", Context.MODE_PRIVATE);

        String test = sharedPreferences.getString("text","test");

        if(test == "test") {

            notes = new ArrayList<String>();

        } else {

            try {
                notes = (ArrayList<String>) ObjectSerializer
                        .deserialize(sharedPreferences
                                .getString("text",
                                        ObjectSerializer
                                                .serialize(new ArrayList<String>())));

            } catch (Exception e) {

                e.printStackTrace();

            }
        }



        ListView listView = findViewById(R.id.notesListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

//        Open up the note when clicked in the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
                intent.putExtra("text", position);

                startActivity(intent);

            }
        });

//        Event handler for long clicks, prompting user to delete note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete this Note?")
                        .setMessage("Cannot be retrieved again")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                try {

                                    sharedPreferences.edit().putString("text", ObjectSerializer
                                            .serialize(notes)).apply();

                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                Toast.makeText(getApplicationContext(),
                                        "Note Deleted", Toast.LENGTH_SHORT).show();
                            }
                        } )
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }

        });

    }
}
