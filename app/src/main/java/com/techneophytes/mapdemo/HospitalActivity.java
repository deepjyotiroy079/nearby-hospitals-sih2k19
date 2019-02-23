package com.techneophytes.mapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class HospitalActivity extends AppCompatActivity {
    ArrayList<String> hospitalArrayList = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    private Button hospital_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

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
                Toast.makeText(HospitalActivity.this, item+" was selected", Toast.LENGTH_SHORT).show();
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
        for(int i=0;i<50;i++) {
            hospitalArrayList.add("Hospital : "+(i+1));
        }
    }

}
