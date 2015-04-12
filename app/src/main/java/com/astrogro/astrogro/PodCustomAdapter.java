package com.astrogro.astrogro;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by swinston1 on 4/12/15.
 */
public class PodCustomAdapter extends BaseAdapter {

    String [] podNameList;
    String [] cropList;
    Context context;
    private static LayoutInflater inflater=null;
    public PodCustomAdapter(DeviceMenuActivity mainActivity, String[] podList, String[] _cropList) {
        // TODO Auto-generated constructor stub
        podNameList=podList;
        cropList = _cropList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return podNameList.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setPositionBackgroundColor(int Pos, Color inColor) {

    }

    public class Holder
    {
        TextView podName;
        TextView podCropName;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.row, null);
        holder.podName=(TextView) rowView.findViewById(R.id.podName);
        holder.podCropName = (TextView) rowView.findViewById(R.id.cropName);

        holder.podName.setText(podNameList[position]);
        holder.podCropName.setText(cropList[position]);

//        rowView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
//            }
//        });

        return rowView;
    }

}