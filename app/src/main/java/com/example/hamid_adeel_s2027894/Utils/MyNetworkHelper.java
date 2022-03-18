package com.example.hamid_adeel_s2027894.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.hamid_adeel_s2027894.Interface.NetworkCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// Hamid_Adeel_S2027894
public class MyNetworkHelper
{


    //MyNetworkHelper takes  url and return raw string response to the calling activity

    Activity activity;
    String response="";
    boolean isSuccessfull;
    public MyNetworkHelper(Activity activity)
    {
        this.activity=activity;
        isSuccessfull=false;
        response="";
    }
    //Method to read the raw response from the url provided
    public  void  readResponse(String link, NetworkCallBack networkCallBack)
    {



        //starting new background thread to perform network work

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {


                isSuccessfull=true; //variable to check if operation is successful or not
                response=""; //varible to store response String

                try
                {

                    //response variable to store response in raw string
                    String inputLine = "";
                    URL url=new URL(link);
                    URLConnection urlConnection=url.openConnection();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    //while there is data read the data
                    while ((inputLine = bufferedReader.readLine()) != null)
                    {
                        //Add new line in previous data
                        response = response + inputLine;
                    }

                    bufferedReader.close();
                }
                catch (IOException ae)
                {
                    isSuccessfull=false;
                    ae.printStackTrace();
                }


                //Run On Main Thread
                //otherwise we will get exception
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {

                        //Returning results to the calling activity
                        networkCallBack.onResult(isSuccessfull,response);
                    }
                });

            }
        });


    }

}
