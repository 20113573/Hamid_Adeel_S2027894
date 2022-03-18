package com.example.hamid_adeel_s2027894.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hamid_adeel_s2027894.Adapters.CustomAdapter;
import com.example.hamid_adeel_s2027894.Constants;
import com.example.hamid_adeel_s2027894.Enums.ItemType;
import com.example.hamid_adeel_s2027894.Interface.NetworkCallBack;
import com.example.hamid_adeel_s2027894.Interface.OnItemClickListener;
import com.example.hamid_adeel_s2027894.Model.CurrentIncident;
import com.example.hamid_adeel_s2027894.R;
import com.example.hamid_adeel_s2027894.Utils.MyNetworkHelper;
import com.example.hamid_adeel_s2027894.Utils.MyResponseParser;

import java.util.ArrayList;
import java.util.List;

public class CurrentIncidentsActivity extends AppCompatActivity implements View.OnClickListener {


    // Hamid_Adeel_S2027894


    //Decalring Views
    LinearLayout lyt_Data,lytError;
    TextView txtEndTime;
    ProgressBar progressBar;
    MyNetworkHelper myNetworkHelper;
    List<CurrentIncident> currentIncidents;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    ImageView imgBack;
    TextView txtToolBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_incidents);



        //Method to intialize Views
        intializeViews();

        //Method to set clickListeners for views
        setClickListeners();
        //Set Adapter On RecyclerView
        setAdapter();


        //Load Data method to load data from network and set on recycerview
        loadData();
    }

    private void setAdapter() {
        customAdapter=new CustomAdapter(currentIncidents, this, ItemType.CURRENT_INCIDENT, new OnItemClickListener() {
            @Override
            public void onClick(int position) {


                Intent n=new Intent(CurrentIncidentsActivity.this,DetailsActivity.class);
                n.putExtra("Type",ItemType.CURRENT_INCIDENT.toString());
                n.putExtra("Data",currentIncidents.get(position));
                startActivity(n);
            }
        });

        recyclerView.setAdapter(customAdapter);
    }

    private void intializeViews() {

        //Intializing Views
        txtEndTime=findViewById(R.id.txtEndTime);
        txtEndTime.setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyclerView);


        lyt_Data=findViewById(R.id.lyt_Data);
        lytError=findViewById(R.id.lytError);
        progressBar=findViewById(R.id.progressBar);
        myNetworkHelper=new MyNetworkHelper(this);
        currentIncidents=new ArrayList<>();

        txtToolBarTitle=findViewById(R.id.txtToolBarTitle);
        imgBack=findViewById(R.id.imgBack);


        txtToolBarTitle.setText("Current Incidents");

    }

    private void setClickListeners() {
        imgBack.setOnClickListener(this);
    }

    private void loadData()
    {

        //Method to show progress bar on screen
        showProgressBar();


        //calling readResponse method of myNetworkHelper class to read raw response for the selected item

        myNetworkHelper.readResponse(Constants.URL_CURRENT_INCIDENTS, new NetworkCallBack() {
            @Override
            public void onResult(boolean isSuccessfull, String response) {

                if(isSuccessfull)  //if operating is successfull
                {
                    currentIncidents.clear();
                    currentIncidents.addAll( MyResponseParser.parseCurrentIncidentsResponse(response));
                    customAdapter.notifyDataSetChanged();


                    //if data is empty show empty screen
                    if(currentIncidents.isEmpty())
                    {
                        showEmptyError();
                    }
                    else 
                    {
                        showData();
                    }
                   
                }
                else
                {
                    //show internet eror
                    showError();
                }

            }
        });

    }

    //Method to show empty layout to screen
    private void showEmptyError() {


        ImageView imgError=findViewById(R.id.imgError);
        imgError.setImageResource(R.drawable.ic_empty_box);

        TextView txtError=findViewById(R.id.txtErrorMessage);

        txtError.setText("No Data Found");

        showError();


    }



    //Method to show progress bar on screen
    public  void  showProgressBar()
    {

        hideAllViews();
        progressBar.setVisibility(View.VISIBLE);
    }


    //Method to hide all views on screen
    private void hideAllViews() {
        progressBar.setVisibility(View.GONE);
        lyt_Data.setVisibility(View.GONE);
        lytError.setVisibility(View.GONE);
    }


    //Method to show error layout on screen
    public  void  showError()
    {
        hideAllViews();
        lytError.setVisibility(View.VISIBLE);
    }

    //Method to show data layout on screen
    public  void  showData()
    {
        hideAllViews();
        lyt_Data.setVisibility(View.VISIBLE);
    }


    //Method for views clicked listener
    @Override
    public void onClick(View view) {
        if(view==imgBack)
            finish();

    }
}