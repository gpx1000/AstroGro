package com.astrogro.astrogro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by swinston1 on 4/11/15.
 */
public class DeviceMenuActivity extends Activity {
    private GridView podListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_menu);
        // Find the GridView resource.
        podListView = (GridView) findViewById(R.id.gridView);
        if(podListView == null)
            return;
        final String[] podsList = { "Pod", "Pod2", "Pod3",
                             "Pod4", "Pod5", "Pod5",
                             "Pod6", "Pod7", "Pod8" };
        String[] cropList = { "tomatoes", "lettuce", "basil",
                                    "cauliflower", "wheat", "strawberries",
                                    "empty", "empty", "empty"};
        int[] PodStatus = { 0, 0, 0,
                            0, 1, 0,
                            0, 0, 1};

        podListView.setAdapter(new PodCustomAdapter(this, podsList, cropList, PodStatus));
        podListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                startActivity(new Intent(getApplicationContext(), PodInfo.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

}
