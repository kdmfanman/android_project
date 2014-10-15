package guillermonunez.geocoding;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import java.io.FileOutputStream;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import java.net.URL;

import org.json.*;

import java.util.*;
import java.net.*;

public class MyActivity extends Activity {
    double latitude=0.0;
    double longitude=0.0;
    String address="";
    int mapType=1;

    static final LatLng TutorialsPoint = new LatLng(30 , 98);
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // once button is pressed, it request the json from Google Maps API
    public void onClick(View view){
        EditText text = (EditText) findViewById(R.id.textView2);
        String s = "http://maps.google.com/maps/api/geocode/json?" +
                "sensor=false&address=";
        address=text.getText().toString();
        try {
            s += URLEncoder.encode(text.getText().toString(), "UTF-8");
            s += "&API=AIzaSyBYo1OgsRo_N3kBsRzna0wNntbqR-8eyaQ";


            // in order to get request, we have to create a AsyncTask, which is the class below
            new HttpAsyncTask().execute(s);



        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void onClick2(View view)
    {
        if(mapType==1) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mapType++;
        }else
            if (mapType==2){
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mapType++;
            }
        else
            if(mapType==3){
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapType=1;
            }

    }


    


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        // this function executes first and then the function below
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                // read from the URL
                Scanner scan = new Scanner(url.openStream());
                String str = new String();
                while (scan.hasNext())
                    str += scan.nextLine();
                scan.close();

                // build a JSON object
                return str;

            }catch (Exception e){

                return "Error";
            }

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.getString("status").equals("OK"))
                    return;

                // get the first result
                JSONObject res = obj.getJSONArray("results").getJSONObject(0);

                JSONObject location =
                        res.getJSONObject("geometry").getJSONObject("location");



                latitude=location.getDouble("lat");
                longitude=location.getDouble("lng");
                String ad=res.getString("formatted_address");

                String x = ad+"\n"+Double.toString(location.getDouble("lat"))+ ","+ Double.toString(location.getDouble("lng"));


                //displays the coordinates to the screen
                Toast.makeText(getBaseContext(), x, Toast.LENGTH_LONG).show();
               LatLng point= new LatLng(latitude , longitude);

                //makes a pointer on the map
                googleMap.clear();
                MarkerOptions pizza = new MarkerOptions().position(point).title(address);
                Marker TP = googleMap.addMarker(pizza);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 4));





            }catch(Exception e){
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }




}
