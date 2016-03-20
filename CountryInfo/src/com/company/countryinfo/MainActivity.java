package com.company.countryinfo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    private ArrayList<JsonItem>  listData = new ArrayList<JsonItem>();
    //URL to get JSON Array
    private static String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    ListView list;
    LazyImageLoadAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list=(ListView)findViewById(R.id.list);
        // Create custom adapter for listview
        adapter=new LazyImageLoadAdapter(this, listData);

        //Set adapter to listview
        list.setAdapter(adapter);

        Button b=(Button)findViewById(R.id.button1);
        b.setOnClickListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Start Task
        new JSONParse().execute();
    }



    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        list.setAdapter(null);
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
}
