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
    int [] podStatus;
    Context context;
    private static LayoutInflater inflater=null;
    public PodCustomAdapter(DeviceMenuActivity mainActivity, String[] podList, String[] _cropList, int[] PodStatus) {
        // TODO Auto-generated constructor stub
        podNameList=podList;
        cropList = _cropList;
        podStatus = PodStatus;

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

    public void setPositionBackgroundColor(int Pos, int inColor) {
        View rowView = getView(Pos, null, null);
        rowView.setBackgroundColor(inColor);
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
        rowView.setBackgroundColor(podStatus[position] == 1? Color.RED : Color.GREEN);

        holder.podName.setText(podNameList[position]);
        holder.podCropName.setText(cropList[position]);

        return rowView;
    }

}