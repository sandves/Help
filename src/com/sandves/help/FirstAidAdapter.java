package com.sandves.help;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstAidAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    
    private String[] mStrings;
    private TypedArray mIcons;
    
    private int mViewResourceId;
    
    public FirstAidAdapter(Context ctx, int viewResourceId, String[] strings, TypedArray icons) {
        super(ctx, viewResourceId, strings);
        
        mInflater = (LayoutInflater)ctx.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        
        mStrings = strings;
        mIcons = icons;
        
        mViewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        return mStrings.length;
    }

    @Override
    public String getItem(int position) {
        return mStrings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	
    	if(convertView==null) {
    		holder = new ViewHolder();
    		convertView = mInflater.inflate(mViewResourceId, null);
    		holder.icon = (ImageView) convertView.findViewById(R.id.spinnerItemIcon);
    		holder.text = (TextView) convertView.findViewById(R.id.spinnerItemText);
    		convertView.setTag(holder);
    	}
    	else {
    		holder = (ViewHolder)convertView.getTag();
    	}     
        holder.icon.setImageDrawable(mIcons.getDrawable(position));
        holder.text.setText(mStrings[position]);
        
        return convertView;
    }
    
    public static class ViewHolder {
    	ImageView icon;
    	TextView text;
    }
}
