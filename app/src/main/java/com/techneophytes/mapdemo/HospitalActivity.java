package com.techneophytes.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class HospitalActivity extends AppCompatActivity {
    ArrayList<String> hospitalArrayList = new ArrayList<>();
    private static final String HOSPITAL_NAME_URL = "http://192.168.43.47/hospital/getHospitalInfo.php";
    private SpinnerDialog spinnerDialog;
    private Button hospital_btn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        context = HospitalActivity.this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_location));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                Intent intent = new Intent(HospitalActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        initItems();

        // create the spinner
        spinnerDialog = new SpinnerDialog(HospitalActivity.this, hospitalArrayList, "Select a hospital");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                final String hospital = hospitalArrayList.get(position);
                // Toast.makeText(HospitalActivity.this, item+" was selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HospitalDetail.class);
                intent.putExtra("hospital_name", hospital);
                context.startActivity(intent);
            }
        });

        hospital_btn = (Button)findViewById(R.id.hospital_btn);
        hospital_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });
    }
    private void initItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOSPITAL_NAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0;i<array.length();i++) {
                        JSONObject hospital = array.getJSONObject(i);
                        hospitalArrayList.add(hospital.getString("hospital_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HospitalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
