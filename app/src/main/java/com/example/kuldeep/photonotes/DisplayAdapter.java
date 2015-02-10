package com.example.kuldeep.photonotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kuldeep on 2/8/2015.
 */
public class DisplayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> path;
    private ArrayList<String> caption;

    public DisplayAdapter(Context c, ArrayList<String> path, ArrayList<String> caption){
        this.context = c;
        this.path = path;
        this.caption = caption;
    }

    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;

        if(child == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.item_layout,null);
            mHolder = new Holder();
            mHolder.cap = (TextView) child.findViewById(R.id.description);
            child.setTag(mHolder);
        }
        else
        {
            mHolder = (Holder) child.getTag();
        }
        mHolder.cap.setText(path.get(position));
        return child;
    }
    public class Holder{
        TextView cap;
    }
}
