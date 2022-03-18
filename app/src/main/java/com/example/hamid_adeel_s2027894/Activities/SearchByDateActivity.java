package com.example.hamid_adeel_s2027894.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.hamid_adeel_s2027894.Model.RoadWork;
import com.example.hamid_adeel_s2027894.R;
import com.example.hamid_adeel_s2027894.Utils.MyNetworkHelper;
import com.example.hamid_adeel_s2027894.Utils.MyResponseParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
public class SearchByDateActivity extends AppCompatActivity implements View.OnClickListener {



    //Decalring Views
    LinearLayout lyt_Data,lytError;
    TextView txtEndTime;
    ProgressBar progressBar;
    MyNetworkHelper myNetworkHelper;
    List<?> listItems;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    ImageView imgBack;
    TextView txtToolBarTitle;

    Button btnCurrentIncidents,btnPlannedRoadWorks,btnRoadWorks;

    Button selectedBtn;

    Date userSelectedDate; //used to store user Selected Date taken in mainactivity
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_date);


        //Getting data passed by previous activity
        userSelectedDate=new Date(getIntent().getExtras().getLong("Date"));
        //Method to intialize Views
        intializeViews();
        //setting click listener for views
        setClickListeners();



        //By default we are auto clicking btnCurrentIncidents
        //so that it can get selected when this screen opens
        btnCurrentIncidents.performClick();

    }

    private void intializeViews() {



        //Intializing Views
        btnCurrentIncidents=findViewById(R.id.btnCurrentIncidents);
        btnPlannedRoadWorks=findViewById(R.id.btnPlannedRoadWorks);
        btnRoadWorks=findViewById(R.id.btnRoadWorks);

        txtEndTime=findViewById(R.id.txtEndTime);

        recyclerView=findViewById(R.id.recyclerView);


        lyt_Data=findViewById(R.id.lyt_Data);
        lytError=findViewById(R.id.lytError);
        progressBar=findViewById(R.id.progressBar);


        txtToolBarTitle=findViewById(R.id.txtToolBarTitle);
        imgBack=findViewById(R.id.imgBack);

        myNetworkHelper=new MyNetworkHelper(this);
        listItems=new ArrayList<>();



    }


    //Method to update text on toolbar
    private void updateToolBarText( String s) {
        txtToolBarTitle.setText(s);
    }


    //setting click listener for views
    private void setClickListeners() {
        imgBack.setOnClickListener(this);
        btnCurrentIncidents.setOnClickListener(this);
        btnPlannedRoadWorks.setOnClickListener(this);
        btnRoadWorks.setOnClickListener(this);
    }


    //Load Data method to load data from network and set on recycerview
    private void loadData(ItemType itemType)
    {


        //Method to show progress bar on screen
        showProgressBar();

        String url=null;



        //giving url variable a link according to the provided item type
        if(itemType==ItemType.CURRENT_INCIDENT) //if item type is current icidents
            url=Constants.URL_CURRENT_INCIDENTS; //assign current incident link to url

        if(itemType==ItemType.ROAD_WORK) //if item type is Road Works
            url=Constants.URL_ROAD_WORKS; //assign Road Works  link to url

        if(itemType==ItemType.PLANNED_ROAD_WORK) //if item type is Planned Road Works
            url=Constants.URL_PLANNED_ROAD_WORKS; //assign Planned Road Works  link to url


        //calling readResponse method of myNetworkHelper class to read raw response for the selected item

        myNetworkHelper.readResponse(url, new NetworkCallBack() {
            @Override
            public void onResult(boolean isSuccessfull, String response) {
                listItems=new ArrayList<>(); //list to contin data
                if(isSuccessfull) //if operating is successfull
                {

                    //parsing the data according to the item type
                    if(itemType==ItemType.CURRENT_INCIDENT)
                    {
                        txtEndTime.setVisibility(View.GONE);
                        listItems=filterCurrentIncidents(MyResponseParser.parseCurrentIncidentsResponse(response));
                    }
                    else
                    if(itemType==ItemType.PLANNED_ROAD_WORK || itemType==ItemType.ROAD_WORK)
                    {
                        txtEndTime.setVisibility(View.VISIBLE);
                        listItems=filterRoadWorks(MyResponseParser.parseRoadWorkResponse(response));

                    }



                    //if data is empty show empty screen
                    if(listItems.isEmpty())
                    {
                        showEmptyError();
                    }
                    else
                    {

                        //Set Adapter On RecyclerView
                        setAdapter(itemType);
                        showData();
                    }

                }
                else
                {

                    //show internet eror
                    showConnectionError();
                }

            }
        });

    }

    //method to filter Road Works   with the matching user provided query
    private List<RoadWork> filterRoadWorks(List<RoadWork> roadWorks)
    {
        List<RoadWork> filteredRoadWorks=new ArrayList<>();

        for(RoadWork roadWork : roadWorks)
        {
            Date date1=new Date(roadWork.getStartDate());
            Date date2=new Date(roadWork.getEndDate());
            if(betweenDate(date1,date2,userSelectedDate))
            {
                filteredRoadWorks.add(roadWork);
            }
        }

        return filteredRoadWorks;

    }


    //Method to check if user date is between the roadwork start and end date
    private boolean betweenDate(Date date1, Date date2, Date userSelectedDate) {

        if(onSameDay(date1,userSelectedDate) || onSameDay(date2,userSelectedDate))
            return true;
        else
            if(userSelectedDate.after(date1) && userSelectedDate.before(date2))
                return true;


            return false;
    }


    //Method to set adapter on recyclerView
    private void setAdapter(ItemType itemType)
    {
        customAdapter=new CustomAdapter(listItems, SearchByDateActivity.this, itemType, new OnItemClickListener() {
            @Override
            public void onClick(int position)
            {



                //go to next activity and pass data required by next screen
                Intent n=new Intent(SearchByDateActivity.this,DetailsActivity.class);
                n.putExtra("Type",itemType.toString());

                if(itemType ==ItemType.CURRENT_INCIDENT)
                {
                    n.putExtra("Data",(CurrentIncident)listItems.get(position));
                }
                if(itemType ==ItemType.PLANNED_ROAD_WORK || itemType ==ItemType.ROAD_WORK)
                {
                    n.putExtra("Data",(RoadWork)listItems.get(position));
                }

                startActivity(n);
            }
        });

        recyclerView.setAdapter(customAdapter);
    }


    //method to filter current incidents with the matching user provided query
    private List<CurrentIncident> filterCurrentIncidents(List<CurrentIncident> currentIncidents)
    {
        List<CurrentIncident> filteredCurrentIncidents=new ArrayList<>();

        for(CurrentIncident currentIncident:currentIncidents)
        {
            Date date1=new Date(currentIncident.getPubDate());
            if(onSameDay(date1,userSelectedDate))
            {
                filteredCurrentIncidents.add(currentIncident);
            }
        }

        return filteredCurrentIncidents;
    }


    //Method to check if User selected and road work/current incident date is on same day
    private boolean onSameDay(Date date1, Date userSelectedDate) {

        System.out.println("DATE 1 : "+date1.getTime()+" DATE2: "+userSelectedDate.getDate());
        return   userSelectedDate.getDay()==date1.getDay() && userSelectedDate.getMonth()==date1.getMonth() && userSelectedDate.getYear()==date1.getYear();

    }


    //Method to show empty layout to screen
    private void showEmptyError() {


        ImageView imgError=findViewById(R.id.imgError);
        imgError.setImageResource(R.drawable.ic_empty_box);

        TextView txtError=findViewById(R.id.txtErrorMessage);

        txtError.setText("No Data Found");

        showError();


    }

    //Method to show no internet error
    private void showConnectionError() {


        ImageView imgError=findViewById(R.id.imgError);
        imgError.setImageResource(R.drawable.ic_no_internet);

        TextView txtError=findViewById(R.id.txtErrorMessage);

        txtError.setText("Check your internet connection");

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

        if(view==btnCurrentIncidents)
        {
            updateToolBarText("Current Incidents");
            unselectPrevBtn();
            selectButton(btnCurrentIncidents);

            loadData(ItemType.CURRENT_INCIDENT);
        }
        if(view==btnPlannedRoadWorks)
        {
            updateToolBarText("Planned Road Works");
            unselectPrevBtn();
            selectButton(btnPlannedRoadWorks);
            loadData(ItemType.PLANNED_ROAD_WORK);
        }
        if(view==btnRoadWorks)
        {
            updateToolBarText("Road Works");
            unselectPrevBtn();
            selectButton(btnRoadWorks);
            loadData(ItemType.ROAD_WORK);
        }
    }



    //method to change backgroud  previously selcted button color to gray and text color of clicked button black
    private void unselectPrevBtn() {
        if(selectedBtn!=null)
        {
            selectedBtn.setBackgroundColor(getColor(R.color.back_color1));
            selectedBtn.setTextColor(getColor(R.color.black));
        }
    }


    //method to change backgroud  color to red and text color of clicked button white
    private void selectButton(Button btnClicked) {

        btnClicked.setBackgroundColor(getColor(R.color.red));
        btnClicked.setTextColor(getColor(R.color.white));
        selectedBtn=btnClicked;
    }
}