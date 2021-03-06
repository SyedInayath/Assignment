package com.company.countryinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
/*
 * ClassName    : JSONParser
 * Description  : Downloads JSON data from server.
 * 
 */
public class JSONParser {
 
    private static final String TAG = "JSONParser";
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    /* Gets JSON data from server based on url */
    public JSONObject getJSONFromUrl(String strUrl) {
        try {
            // defaultHttpClient send HTTP GET request
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(strUrl);

            /* Get response */
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
            	Log.w(TAG,"Unable to receive JSON Data ");
            	return null;
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent(); 
            Log.d(TAG,"JSON response received from server ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        /* Read buffer from input stream */
        try {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(is));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            json = result;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } finally {
        	if (is !=null ) {
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
    }
}
