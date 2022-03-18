package com.example.hamid_adeel_s2027894.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hamid_adeel_s2027894.Enums.ItemType;
import com.example.hamid_adeel_s2027894.Interface.OnItemClickListener;
import com.example.hamid_adeel_s2027894.Model.CurrentIncident;
import com.example.hamid_adeel_s2027894.Model.RoadWork;
import com.example.hamid_adeel_s2027894.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

// Hamid_Adeel_S2027894
    //CustomAdapter for recyclerView Used In Activities


    //Declaring listData which can take any type of values RoadWork , Current Incident etc
    private List<?>listData;
    Context context;

    //OnItemClickListener to tell the activity an item is clicked with its position
    OnItemClickListener onItemClickListener;

    //ItemType to check for which type of data this recyclerview adapter is being used
    // it can be Road Work , Current Incident or Planned Raod Work
    ItemType itemType;


    //Constuctor to take values and assign
    public CustomAdapter(List<?> listData, Context context, ItemType itemType, OnItemClickListener onItemClickListener)
    {
        this.listData = listData;
        this.context=context;
        this.onItemClickListener=onItemClickListener;
        this.itemType=itemType;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       //Inflating our custom layout for items
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override  //onBindViewHolder used to set data on the layout
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {



        //Checking which  type of data is passed
        if(itemType==ItemType.CURRENT_INCIDENT)
        {


            //Get date for CurrentIncident at  position
            CurrentIncident currentIncident=(CurrentIncident) listData.get(i);

            //set data on views
            holder.txtDate.setText(currentIncident.getPubDate());
            holder.txtStartTime.setText(currentIncident.getStartDate());
            holder.txtEndTime.setVisibility(View.GONE);

            holder.txtLocation.setText(currentIncident.getLocation());
            holder.txtType.setText(currentIncident.getType());
        }
        else
        if(itemType==ItemType.PLANNED_ROAD_WORK || itemType==ItemType.ROAD_WORK)
        {
            //Get date for RoadWork at  position

            RoadWork roadWork=(RoadWork) listData.get(i);

            //set data on views
            holder.txtDate.setText(roadWork.getPubDate());
            holder.txtStartTime.setText(roadWork.getFormattedStartDate());
            holder.txtEndTime.setVisibility(View.VISIBLE);
            holder.txtEndTime.setText(roadWork.getFormattedEndDate());


            holder.txtLocation.setText(roadWork.getLocation());

            if(itemType==ItemType.ROAD_WORK)
                 holder.txtType.setText(roadWork.getRoadWorkType());
            else if(itemType==ItemType.PLANNED_ROAD_WORK)
                holder.txtType.setText(roadWork.getWork());


        }


        //changing background color of item layout
        if(i%2==0)
            holder.lytParent.setBackgroundResource(R.color.back_color1);
        else
            holder.lytParent.setBackgroundResource(R.color.back_color_2);



        //setting ClickListener For ItemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //pass the clicked item position back to activity
                onItemClickListener.onClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    //ViewHolder to hold views used for our recyclerview item
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        //Declaring views
        private TextView txtDate,txtStartTime,txtEndTime,txtLocation,txtType;
        LinearLayout lytParent;
        public ViewHolder(View itemView)
        {
            super(itemView);


            //Intializing Views
            lytParent=itemView.findViewById(R.id.lytParent);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtStartTime=itemView.findViewById(R.id.txtStartTime);
            txtEndTime=itemView.findViewById(R.id.txtEndTime);
            txtLocation=itemView.findViewById(R.id.txtLocation);
            txtType=itemView.findViewById(R.id.txtType);


        }
    }
}
