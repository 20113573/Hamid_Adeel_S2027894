package com.example.hamid_adeel_s2027894.Activities;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.example.hamid_adeel_s2027894.Enums.ItemType;
import com.example.hamid_adeel_s2027894.Model.CurrentIncident;
import com.example.hamid_adeel_s2027894.Model.RoadWork;
import com.example.hamid_adeel_s2027894.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends FragmentActivity implements View.OnClickListener {

    // Hamid_Adeel_S2027894

    //Declaring Views
    TextView txtTitle,txtToolBarTitle,txtLocation,txtDate,txtStartTime,txtEndTime,txtType,txtdescription;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        // //Method to intialize Views
        intializeViews();

        //Method to setvals on the views
        setVals();


    }


    //Method to setvals on the views
    private void setVals()
    {

        //Getting values passed by previous activity
        Bundle extras=getIntent().getExtras();
        ItemType itemType=ItemType.valueOf(extras.getString("Type",""));


        //checking which itemtype is and casting
        //the data accoring to that
        //then calling showVals() methos with paramters
        if(itemType==ItemType.CURRENT_INCIDENT)
        {
            CurrentIncident currentIncident=(CurrentIncident) extras.get("Data");

            showVals(currentIncident.getTitle(),currentIncident.getLocation(),currentIncident.getPubDate(),currentIncident.getStartDate()," - - - ",currentIncident.getType(),currentIncident.getDescription(),currentIncident.getGeorss());
        }
        else
        if(itemType==ItemType.PLANNED_ROAD_WORK)
        {
            RoadWork roadWork=(RoadWork) extras.get("Data");
            showVals(roadWork.getTitle(),roadWork.getLocation(),roadWork.getPubDate(),roadWork.getStartDate(),roadWork.getEndDate(),roadWork.getWork(),roadWork.getDescription(),roadWork.getGeorss());
        }
        else
        if(itemType==ItemType.ROAD_WORK)
        {
            RoadWork roadWork=(RoadWork) extras.get("Data");

            showVals(roadWork.getTitle(),roadWork.getLocation(),roadWork.getPubDate(),roadWork.getStartDate(),roadWork.getEndDate(),roadWork.getRoadWorkType(),roadWork.getDescription(),roadWork.getGeorss());
        }

    }

    public void showVals(String title,String location,String date,String startTime,String endTime,String type,String description,String georss)
    {

        //showing values on the views
        txtTitle.setText(title);
        txtLocation.setText(location);
        txtDate.setText(date);
        txtStartTime.setText(startTime);
        txtEndTime.setText(endTime);
        txtType.setText(type);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtdescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtdescription.setText(Html.fromHtml(description));
        }



        //Creating Map Frament
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                //when map is ready showing the location of incident,road work on map

                LatLng sydney=new LatLng(Double.parseDouble(georss.split(" ")[0]), Double.parseDouble(georss.split(" ")[1]));
                googleMap.addMarker(new MarkerOptions().position(sydney).title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
            }
        });
    }


    // //Method to intialize Views
    private void intializeViews() {


        //Intializing Views

        txtToolBarTitle=findViewById(R.id.txtToolBarTitle);
        imgBack=findViewById(R.id.imgBack);
        txtTitle=findViewById(R.id.txtTitle);
        txtLocation=findViewById(R.id.txtLocation);
        txtDate=findViewById(R.id.txtDate);
        txtStartTime=findViewById(R.id.txtStartTime);
        txtEndTime=findViewById(R.id.txtEndTime);
        txtType=findViewById(R.id.txtType);
        txtdescription=findViewById(R.id.txtdescription);

        txtToolBarTitle.setText("Details");
        imgBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==imgBack)
        {
            finish();
        }
    }
}