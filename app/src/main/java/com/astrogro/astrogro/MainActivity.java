package com.astrogro.astrogro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    private Intent DeviceMenuIntent;
    private Intent MenuIntent;
    private Intent ScheduleIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DeviceMenuIntent = new Intent(getApplicationContext(), DeviceMenuActivity.class);
        MenuIntent = new Intent(getApplicationContext(), MenuActivity.class);
        ScheduleIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
        Button menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MenuIntent);
            }
        });
        Button podButton = (Button) findViewById(R.id.podButton);
        podButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DeviceMenuIntent);
            }
        });
        Button schedButton = (Button) findViewById(R.id.scheduleButton);
        schedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ScheduleIntent);
            }
        });
    }
}
