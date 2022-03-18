package com.example.hamid_adeel_s2027894;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hamid_adeel_s2027894.Activities.CurrentIncidentsActivity;
import com.example.hamid_adeel_s2027894.Activities.JourneyPlannerActivity;
import com.example.hamid_adeel_s2027894.Activities.SearchByDateActivity;
import com.example.hamid_adeel_s2027894.Activities.SearchByRoadActivity;
import com.example.hamid_adeel_s2027894.Enums.ItemType;
import com.example.hamid_adeel_s2027894.Interface.NetworkCallBack;
import com.example.hamid_adeel_s2027894.Model.CurrentIncident;
import com.example.hamid_adeel_s2027894.Utils.MyNetworkHelper;
import com.example.hamid_adeel_s2027894.Utils.MyResponseParser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements OnClickListener
{



    //Decalring cardview variables
    long date;
    CardView cardCurrentIncident,cardSearchDate,cardSearchRoad,cardJourneyPlanner;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Method to intialize Views
        intializeViews();



        //Method to set click listeners for views
        setClickListeners();

    }

    private void setClickListeners() {
        //setting clicklistener for views
        cardCurrentIncident.setOnClickListener(this);
        cardSearchDate.setOnClickListener(this);
        cardSearchRoad.setOnClickListener(this);
        cardJourneyPlanner.setOnClickListener(this);
    }

    private void intializeViews() {
        //Intializing Views
        cardCurrentIncident=findViewById(R.id.cardCurrentIncident);
        cardSearchDate=findViewById(R.id.cardSearchDate);
        cardSearchRoad=findViewById(R.id.cardSearchRoad);
        cardJourneyPlanner=findViewById(R.id.cardJourneyPlanner);
    }


    //onclick listener for views
    @Override
    public void onClick(View view)
    {

        //checking which view is clicked
        if(view==cardCurrentIncident) //if current first button is clicked
        {
            //Go to CurrentIncidentsActivity Screen
            startActivity(new Intent(this, CurrentIncidentsActivity.class));
        }
        if(view==cardSearchDate) //if current 2nd button is clicked
        {

            //Show Dialog Box with Cardview
            showSearchDateDialog();
        }
        if(view==cardJourneyPlanner)
        {
            showJourneyPlannerDialog();
        }
        if(view==cardSearchRoad)
        {
            showSearchByRoadDialog();
        }
    }


    private void showSearchByRoadDialog()
    {

        //Method to Show Dialog Box

        //Creating Dialog instance
        Dialog dialog=getDialog(R.layout.lyt_dialog_road);
        dialog.show();

        //Intializing Views
        Spinner spinner=dialog.findViewById(R.id.spinner);
        TextInputEditText edittext=dialog.findViewById(R.id.edittext);
        TextInputLayout editTextLayout=dialog.findViewById(R.id.editTextLayout);
        Button btnCancel=dialog.findViewById(R.id.btnCancel);
        Button btnSearch=dialog.findViewById(R.id.btnSearch);

         //Setting on item click listener for Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                //Changing hint of text box depending on which option is slected

                if(i==0)
                    editTextLayout.setHint("Set of RoadWorks (Separated by comma)");
                else
                    editTextLayout.setHint("Enter Specific Road Name For Current Incidents");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        View.OnClickListener onClickListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                dialog.dismiss(); //hiding dialog
                if(view!=btnCancel)    //if clicked button is not cancelled button
                {


                    //If user didnt filled anything in textbox
                    //then show error

                    if(edittext.getText().toString().isEmpty())
                    {
                        edittext.setError("Required Field");
                        edittext.requestFocus();
                        return;
                    }



                    //go to next activity and pass data required by next screen
                    Intent n=new Intent(MainActivity.this, SearchByRoadActivity.class);

                    if(spinner.getSelectedItemPosition()==0)
                        n.putExtra("ItemType", ItemType.ROAD_WORK.toString());
                    else
                        n.putExtra("ItemType", ItemType.CURRENT_INCIDENT.toString());

                    n.putExtra("Data",edittext.getText().toString());
                    startActivity(n);
                }
            }
        };


        //setting click listeners
        btnSearch.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }


    private void showJourneyPlannerDialog()
    {

        //Method to Show Dialog Box with Calender

        //Creating Dialog instance

        Dialog dialog=getDialog(R.layout.lyt_dialog_date);
        dialog.show();

        //Intializing Views From Dialog
        TextView txtTitle=dialog.findViewById(R.id.txtTitle);
        Button btnSearch=dialog.findViewById(R.id.btnSearch);
        Button btnCancel=dialog.findViewById(R.id.btnCancel);
        CalendarView calendarView=dialog.findViewById(R.id.calenderView);
        date=calendarView.getDate();
        txtTitle.setText("Select Date of your journey");

        //Setting date change listener for calender view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {


                //Logic to get Selected date in mili seconds
                Calendar instance=Calendar.getInstance();
                instance.set(Calendar.YEAR,i);
                instance.set(Calendar.MONTH,i1);
                instance.set(Calendar.DAY_OF_MONTH,i2);
                date=instance.getTimeInMillis();
            }
        });
        View.OnClickListener onClickListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                dialog.dismiss(); //hiding dialog
                if(view!=btnCancel) //if clicked button is not cancelled button
                {

                    //go to next activity and pass data required by next screen

                    Intent n=new Intent(MainActivity.this, JourneyPlannerActivity.class);
                    n.putExtra("Date",date);
                    startActivity(n);
                }
            }
        };

        //setting click listeners
        btnSearch.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }



    private void showSearchDateDialog() {

        //Method to Show Dialog Box with Calender

        //Creating Dialog instance
        Dialog dialog=getDialog(R.layout.lyt_dialog_date);
        dialog.show();

        //Intializing Views From Dialog
        Button btnSearch=dialog.findViewById(R.id.btnSearch);
        Button btnCancel=dialog.findViewById(R.id.btnCancel);
        CalendarView calendarView=dialog.findViewById(R.id.calenderView);
        date=calendarView.getDate();



        //Setting date change listener for calender view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                //Logic to get Selected date in mili seconds
                Calendar instance=Calendar.getInstance();
                instance.set(Calendar.YEAR,i);
                instance.set(Calendar.MONTH,i1);
                instance.set(Calendar.DAY_OF_MONTH,i2);
                date=instance.getTimeInMillis();
            }
        });
        View.OnClickListener onClickListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                dialog.dismiss(); //hiding dialog
                if(view!=btnCancel) //if clicked button is not cancelled button
                {
                    //go to next activity and pass data required by next screen
                    Intent n=new Intent(MainActivity.this, SearchByDateActivity.class);
                    n.putExtra("Date",date);
                    startActivity(n);
                }
            }
        };


        //setting click listeners
        btnSearch.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }



    //Method provide intial code for dialog
    public   Dialog getDialog(int layout)
    {

        //Creating dialog instance
        Dialog dialog=new Dialog(this);
        dialog.setContentView(layout); //setting layout to show on dialog

        //gettting window of dialog
        Window window = dialog.getWindow();
        //making background transparent for dialog
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //making dialog to take full width and wrap content height
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

}
