package com.astrogro.astrogro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

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
        final String[] podsList = { "Pod1", "Pod2", "Pod3",
                             "Pod4", "Pod5", "Pod5",
                             "Pod6", "Pod7", "Pod8" };
        final String[] cropList = { "tomatoes", "empty", "basil",
                                    "cauliflower", "wheat", "strawberries",
                                    "empty", "empty", "empty"};
        podListView.setAdapter(new PodCustomAdapter(this, podsList, cropList));
        podListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                Toast.makeText(getBaseContext(), podsList[arg2],
                        Toast.LENGTH_SHORT).show();
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
