package com.example.todolist;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ArrayListAdapter extends BaseAdapter {


    private ArrayList<Tasks> listTask;
    private Context mContext;

    public ArrayListAdapter(Context mContext,ArrayList<Tasks> list)
    {
        this.listTask=list;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return listTask.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myLayout = inflater.inflate(R.layout.row_layout,parent, false);
        //myLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorAccent));
        if(this.listTask.get(position).isDone())
            myLayout.setBackgroundColor(Color.GREEN);
        else {
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date d = sdf.parse(this.listTask.get(position).getTaskDate()+" "+this.listTask.get(position).getTaskTime());
                //Date d = sdf.parse("20/12/2011");
                String s= sdf.format(new Date());
                Date d2=sdf.parse(s);
                if(d.before(d2))
                    myLayout.setBackgroundColor(Color.RED);
                else
                    myLayout.setBackgroundColor(Color.GRAY);
            }catch(Exception ex){
                myLayout.setBackgroundColor(Color.YELLOW);
                Toast.makeText(mContext,this.listTask.get(position).getTaskDate(),
                        Toast.LENGTH_SHORT).show();
            }
        }
        TextView textView = (TextView) myLayout.findViewById(R.id.rowView);
        textView.setTextColor(Color.BLACK);
        textView.setText(this.listTask.get(position).toString());
        return myLayout;
    }
}
