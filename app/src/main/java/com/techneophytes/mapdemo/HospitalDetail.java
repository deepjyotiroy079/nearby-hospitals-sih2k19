package com.techneophytes.mapdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HospitalDetail extends AppCompatActivity {
    private static final String TAG = "HospitalDetail";

    private Context context;
    private Bundle extras;
    private TextView hospital_name, Total_Beds, Available_Beds;
    // private RecyclerView recyclerView;
    private static String GET_BEDS_URL = "http://192.168.43.47/hospital/getHospitalInfoByName.php?hospital_name=";
    // private List<list_data> l_data;
    private String h_name, temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        Log.d(TAG, "onCreate: called");

        hospital_name = (TextView) findViewById(R.id.hospital_name);
        Total_Beds = (TextView) findViewById(R.id.Total_Beds);
        Available_Beds = (TextView) findViewById(R.id.Available_Beds);
        // recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        extras = getIntent().getExtras();
        String lol;
        if(extras!=null) {
            h_name = extras.getString("hospital_name");
            hospital_name.setText(h_name);
            temp = h_name.replace(" ","+");
        }

        getBeds();
    }
    private void getBeds() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_BEDS_URL+temp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject ob = new JSONObject(response);
                    JSONArray array = ob.getJSONArray("beds");
                    JSONObject data = array.getJSONObject(0);
                    int t_beds = Integer.parseInt(data.getString("Total_beds"));
                    int a_beds = Integer.parseInt(data.getString("Total_beds_vacant"));

                    Total_Beds.setText("Total Beds : "+t_beds);
                    Available_Beds.setText("Available Beds : "+a_beds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
