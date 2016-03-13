package com.example.beef.newercalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    long calDate;
    String dateStr = "";
    int prevYear = 1;
    int prevMonth = 1;
    int prevDayOfMonth = 1;
    String eventTitle = "Default Reminder Title";
    EditText input;//for the input in the dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateStr = concatDate(prevYear, prevMonth, prevDayOfMonth);
        displayEvents(dateStr);

        calendar = (CalendarView)findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                dateStr = concatDate(year, month, dayOfMonth);//returns the date formatted for a key so you know when you're going to add an event,
                // also works as a key for the set of events on that day
                if((year == prevYear)&&(month==prevMonth)&&(dayOfMonth==prevDayOfMonth)){
                    //Toast.makeText(view.getContext(), "the double click worked", Toast.LENGTH_SHORT).show();



                    //DIALOG adds an event if you click the positive button with the title given
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//this?
                    builder.setTitle("Set Event Title");

                    input = new EditText(view.getContext());
                    builder.setView(input);

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //Log.i("negative button", "was clicked");
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eventTitle = input.getText().toString();
                            addEvents(dateStr, eventTitle);
                            //Log.i("positive button", eventTitle);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //ENDDIALOG

                }else{
                    //Toast.makeText(view.getContext(), "Click any date twice in a row to add an event!", Toast.LENGTH_SHORT).show();
                    prevYear=year;
                    prevDayOfMonth=dayOfMonth;
                    prevMonth=month;
                }
                displayEvents(dateStr);
                //Toast.makeText(view.getContext(), "Display Events Called double check", Toast.LENGTH_LONG).show();
            }
        });


        ListView listv = (ListView)findViewById(R.id.listView);
        listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String delEventStr = (String)parent.getItemAtPosition(position);
                deleteEvents(dateStr, delEventStr);
                displayEvents(dateStr);
                return false;
            }
        });
    }

    private String concatDate(int year, int month, int dayOfMonth){
        String concatHere = "";
        concatHere = concatHere + year + month + dayOfMonth;
        return concatHere;
    }

    //use the String eventTitle to save event from dialog
    private void addEvents(String eventKey, String eventTitle){
        Toast.makeText(this, "Called addEvents, event to be added was" + eventTitle + "and event key was" + eventKey, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor myE;
        SharedPreferences addPreferences = getPreferences(MODE_PRIVATE);
        myE = addPreferences.edit();
        Set<String> copyThisSet = addPreferences.getStringSet(eventKey, null); //what happens if the key is null?
        Set<String> mySet = new HashSet<String>();

        if(copyThisSet!=null) {
            if (copyThisSet.size() != 0) {
                mySet.addAll(copyThisSet);
            }
        }
        displayEvents(eventKey);
        /*
        //if(!(mySet==null)) {
        if(!(mySet.size()==0)) {
            Iterator copyIt = copyThisSet.iterator();
            while (copyIt.hasNext()) {
                mySet.add(copyIt.next().toString());
            }
        }
        */



        mySet.add(eventTitle);
        myE.putStringSet(eventKey, mySet);
        myE.commit();
    }

    private void displayEvents(String dateKey) {//need input for the day you're displaying events for
        //Toast.makeText(this, "Display Events Called", Toast.LENGTH_LONG).show();

        SharedPreferences myPref = getPreferences(MODE_PRIVATE);

        Set<String> displayThisSet = myPref.getStringSet(dateKey, new HashSet<String>(Arrays.asList("No Events Entered")));

        ArrayList<String> displayList = new ArrayList<String>(displayThisSet);

        ArrayAdapter<String> displayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.event_item,
                displayList);
        ListView myList = (ListView)findViewById(R.id.listView);
        myList.setAdapter(displayAdapter);



        //Toast.makeText(this, "Display Events end reached", Toast.LENGTH_LONG).show();
    }

    private void deleteEvents(String dateKey, String eventTitle){
        //SharedPreferences myPref = getPreferences(MODE_PRIVATE);
        //Set<String> displayThisSet = myPref.getStringSet(dateKey, new HashSet<String>(Arrays.asList("No Events Entered")));

        SharedPreferences.Editor myE;
        SharedPreferences addPreferences = getPreferences(MODE_PRIVATE);
        myE = addPreferences.edit();
        Set<String> copyThisSet = addPreferences.getStringSet(dateKey, null); //what happens if the key is null?
        Set<String> mySet = new HashSet<String>();

        if(copyThisSet!=null) {
            if (copyThisSet.size() != 0) {
                mySet.addAll(copyThisSet);
            }
        }

        mySet.remove(eventTitle);
        if(mySet.size()==0){
            mySet.add("No Events Entered");
        }
        myE.putStringSet(dateKey, mySet);
        myE.commit();

    }
}
