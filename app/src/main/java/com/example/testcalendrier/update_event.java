package com.example.testcalendrier;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class update_event extends Fragment implements View.OnClickListener{
    Context context;
    Calendar beginTime = Calendar.getInstance();
    Calendar endTime=Calendar.getInstance();
    EditText editTitle;
    EditText editDateBegin;
    EditText editDateEnd;

    Button btn_select_dateBegin;
    Button btn_select_dateEnd;
    Button btn_validate;

    long calID;
    long startMillis = 0;
    long endMillis = 0;

    int ihourOfDay;
    int iminute ;
    int iyear ;
    int imonthOfYear;
    int idayOfMonth;
    Event event ;

    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());


    private int mYear, mMonth, mDay, mHour, mMinute;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_event, container, false);
        context = v.getContext();
        editTitle = (EditText) v.findViewById(R.id.editTitle);
        editDateBegin = (EditText) v.findViewById(R.id.editDateBegin);
        editDateEnd = (EditText) v.findViewById(R.id.editDateEnd);

        btn_select_dateBegin = (Button) v.findViewById(R.id.btn_select_dateBegin);
        btn_select_dateEnd = (Button) v.findViewById(R.id.btn_select_dateEnd);
        btn_validate = (Button) v.findViewById(R.id.btn_validate);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            calID = bundle.getLong("calID");
            editTitle.setText(bundle.getString("titleEvent"));
            startMillis = bundle.getLong("startMillis");
            endMillis = bundle.getLong("endMillis");
            event = (Event) bundle.getSerializable("event");

            beginTime.setTimeInMillis(startMillis);
            endTime.setTimeInMillis(endMillis);

            editDateBegin.setText(beginTime.getTime().toString());
            editDateEnd.setText(endTime.getTime().toString());
        }


        btn_select_dateBegin.setOnClickListener(this);
        btn_select_dateEnd.setOnClickListener(this);
        btn_validate.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_dateBegin:
                showdiag(beginTime,editDateBegin);
               // editDateBegin.setText(beginTime.getTime() +"");
                break;
            case R.id.btn_select_dateEnd:
                showdiag(endTime,editDateEnd);
               // editDateEnd.setText(endTime.getTime() +"");
                break;
            case R.id.btn_validate:
                String txt = "";
                txt = editTitle.getText().toString();
                event.setTitle(txt);
                event.setDateBegin(beginTime.getTimeInMillis());
                event.setDateEnd(endTime.getTimeInMillis());
                updateEvent(event);
                break;
        }


        }


public void showdiag(final Calendar calendar, final EditText edit){
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    iyear = year;
                    imonthOfYear = monthOfYear;
                    idayOfMonth = dayOfMonth;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    ihourOfDay = hourOfDay;
                                    iminute = minute;
                                    calendar.set(iyear, imonthOfYear, idayOfMonth, ihourOfDay, iminute);
                                    edit.setText(calendar.getTime() +"");

                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                                   }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();



}
    public void updateEvent(Event event){
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;
        // The new title for the event
        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DTSTART, event.getDateBegin());
        values.put(CalendarContract.Events.DTEND, event.getDateEnd());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.getEvent_ID());
        cr.update(updateUri, values, null, null);
    }



}
