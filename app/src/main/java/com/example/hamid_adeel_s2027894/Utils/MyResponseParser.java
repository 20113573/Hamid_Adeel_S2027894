package com.example.hamid_adeel_s2027894.Utils;

import android.util.Log;

import com.example.hamid_adeel_s2027894.Model.CurrentIncident;
import com.example.hamid_adeel_s2027894.Model.RoadWork;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MyResponseParser
{

    //MyResponseParser class  to parse raw string into useable data


    //parseRoadWorkResponse take raw response and parse the data and put the data in list
    // and returns
    public static List<RoadWork> parseRoadWorkResponse(String response)
    {

        //creating list to store roadworks
        List<RoadWork> roadWorks=new ArrayList<>();

        try
        {

            RoadWork roadWork=null;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( response ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG )
                {

                    if(xpp.getName().equals("item") )
                    {
                        roadWork=new RoadWork();
                    }

                    if(roadWork ==null)
                    {
                        eventType = xpp.next();
                        continue;
                    }

                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text

                        roadWork.setTitle(xpp.nextText());
                        roadWork.setLocation(roadWork.getTitle());

                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {

                            String description = xpp.nextText();
                            //Splitting description on '<br />' so that we can get start date , end date and work separately
                            roadWork.setDescription(description);
                            if(description!=null )
                            {
                                String[] descriptionList=description.split("<br />");


                                //Splitting description on '<br />'
                                // so that we can get start date
                                // , end date and work separately
                                for(String str:descriptionList)
                                {
                                    if (str.trim().startsWith("Start Date")) {
                                        roadWork.setStartDate(str);
                                    }
                                    else if (str.trim().startsWith("End Date")) {
                                        roadWork.setEndDate(str);
                                    }
                                    else if (str.trim().startsWith("Works")) {
                                        roadWork.setWork(str);
                                    }
                                    else if (str.trim().startsWith("TYPE ")) {
                                        roadWork.setType(str);
                                    }

                                }
                            }


                        }
                        else //checking if tag is georss
                            if (xpp.getPrefix()!=null && xpp.getPrefix().equalsIgnoreCase("georss"))
                            {
                                //putting value in model class of georss
                                roadWork.setGeorss(xpp.nextText());
                            }
                                else
                                    // check with tga name
                                    if (xpp.getName().equalsIgnoreCase("pubDate"))
                                    {
                                        roadWork.setPubDate(xpp.nextText());

                                    }
                }


                //checking if end tag and tag name is item
                // it means our model class has all data it needs title,description etc
                // now we can add that into the list
                if(eventType == XmlPullParser.END_TAG  && xpp.getName().equals("item"))
                {

                    roadWorks.add(roadWork);
                    roadWork=null;
                }
                // Get the next event
                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }


        return roadWorks;
    }



    //parseCurrentIncidentsResponse take raw response and parse the data and put the data in list
    // and returns
    public static List<CurrentIncident> parseCurrentIncidentsResponse(String response)
    {
        //creating list to store currentIncidents
        List<CurrentIncident> currentIncidents=new ArrayList<>();

        try
        {

            CurrentIncident currentIncident=null;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( response ) );
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                // Found a start tag
                if(eventType == XmlPullParser.START_TAG )
                {

                    if(xpp.getName().equals("item") )
                    {
                        currentIncident=new CurrentIncident();
                    }

                    if(currentIncident==null)
                    {
                        eventType = xpp.next();
                        continue;
                    }

                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        try
                        {


                            //splitting the title and getting other infos
                            //like location type
                            String title=xpp.nextText();
                            String[] info=title.split(" - ");
                            String location=null;
                            for(int i=0; i< info.length-1; i++)
                            {
                                if(location==null)
                                    location=info[i];
                                else
                                {
                                    location=location+" - "+ info[i];
                                }
                            }

                            String type=info[info.length-1];
                            currentIncident.setLocation(location);
                            currentIncident.setType(type);
                            currentIncident.setTitle(title);

                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {

                            String description = xpp.nextText();
                            currentIncident.setDescription(description);

                        }
                        else //checking if tag is georss
                            if (xpp.getPrefix()!=null && xpp.getPrefix().equalsIgnoreCase("georss"))
                            {
                                //putting value in model class of georss
                                currentIncident.setGeorss(xpp.nextText());
                            }

                            else
                                // check with tga name
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                {
                                    currentIncident.setPubDate(xpp.nextText());

                                }
                }

                //checking if end tag and tag name is item
                // it means our model class has all data it needs title,description etc
                // now we can add that into the list
                if(eventType == XmlPullParser.END_TAG  && xpp.getName().equals("item"))
                {

                    currentIncidents.add(currentIncident);
                    currentIncident=null;
                }
                // Get the next event
                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }


        return currentIncidents;
    }
}
