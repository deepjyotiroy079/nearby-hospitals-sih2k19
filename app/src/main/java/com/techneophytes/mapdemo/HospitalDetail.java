package com.techneophytes.mapdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HospitalDetail extends AppCompatActivity {
    private static final String TAG = "HospitalDetail";

    private Context context;
    private Bundle extras;
    private TextView hospital_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        Log.d(TAG, "onCreate: called");

        hospital_name = (TextView) findViewById(R.id.hospital_name);

        extras = getIntent().getExtras();

        if(extras!=null) {
            String h_name = extras.getString("hospital_name");
            hospital_name.setText(h_name);
        }
    }
}
