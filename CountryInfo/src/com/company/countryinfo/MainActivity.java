package com.company.countryinfo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ArrayList<JsonItem>  listData = new ArrayList<JsonItem>();
    //URL to get JSON Array
    private static String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    ListView list;
    LazyImageLoadAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (isOnline()) {

            list=(ListView)findViewById(R.id.list);
            // Create custom adapter for listview
            adapter=new LazyImageLoadAdapter(this, listData);

            //Set adapter to listview
            list.setAdapter(adapter);

            Button b=(Button)findViewById(R.id.button1);
            b.setOnClickListener(listener);
            //Start Task
            new JSONParse().execute();
        }  else  {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                   }
                 })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        finish();
                   }
                });
                 AlertDialog alert = builder.create();
                 alert.show();
                }
                catch(Exception e) {
                    Log.d(TAG, "Show Dialog: "+e.getMessage());
                }
            }
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
    	if (list != null) {
            list.setAdapter(null);
        }
        super.onDestroy();
    }

    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {

            //Refresh cache directory downloaded images
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };

    public void onItemClick(int position) {
        //TODO later
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(MainActivity.this);
           pDialog.setMessage("Getting Data ...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

       @Override
       protected JSONObject doInBackground(String... args) {
           JSONParser jParser = new JSONParser();

           // Getting JSON from URL
           JSONObject json = jParser.getJSONFromUrl(url);
           return json;
       }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                   // Getting JSON Array
                   String title = json.getString("title");
                   JSONArray rows = json.getJSONArray("rows");

                   for (int index = 0; index < rows.length(); index ++) {
                      JSONObject row = rows.getJSONObject(index);
                      JsonItem dataItem = new JsonItem(row.getString("title"), row.getString("description"),row.getString("imageHref")); 
                      listData.add(dataItem);
                   }
                   adapter.notifyDataSetChanged();
                   ActionBar actionBar = getActionBar();
                   actionBar.setTitle(title);

           } catch (JSONException e) {
               e.printStackTrace();
           }
        }
   }

    private boolean isOnline() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
