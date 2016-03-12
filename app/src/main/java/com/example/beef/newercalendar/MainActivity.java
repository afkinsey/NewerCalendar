package com.example.beef.newercalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    long calDate;
    String dateStr = "";
    int prevYear = 1;
    int prevMonth = 1;
    int prevDayOfMonth = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView)findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if((year == prevYear)&&(month==prevMonth)&&(dayOfMonth==prevDayOfMonth)){
                    Toast.makeText(view.getContext(), "the double click worked", Toast.LENGTH_SHORT).show();
                    dateStr = concatDate(year, month, dayOfMonth);//returns the date formatted for a key so you know when you're going to add an event
                    //addEvents(dateStr);
                }else{
                    Toast.makeText(view.getContext(), "you did not double click this", Toast.LENGTH_SHORT).show();
                    prevYear=year;
                    prevDayOfMonth=dayOfMonth;
                    prevMonth=month;
                }
            }
        });


    }

    private String concatDate(int year, int month, int dayOfMonth){
        String concatHere = "";
        concatHere = concatHere + year + month + dayOfMonth;
        return concatHere;
    }






}
