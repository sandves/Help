package com.sandves.help;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FirstAidFragment extends ListFragment {

	String[] mSubjects;
	TypedArray mIcons;

	public FirstAidFragment() {
		
	}

	@SuppressLint("Recycle")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		mSubjects = getActivity().getResources().getStringArray(R.array.first_aid_content);
		mIcons = getActivity().getResources().obtainTypedArray(R.array.firstaid_icons);
		/** Creating an array adapter to store the list of countries **/

		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.firstaid, subjects);
		FirstAidAdapter adapter = new FirstAidAdapter(getActivity(), R.layout.firstaid, mSubjects, mIcons);
		/** Setting the list adapter for the ListFragment */
		setListAdapter(adapter);


		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// selected item

				// Launching new Activity on selecting single List Item
				Intent i = new Intent(getActivity(), FirstAidContent.class);
				// sending data to new activity
				i.putExtra("position", position);
				startActivity(i);
			}
		});

		registerForContextMenu(getListView());
		
		//int[] colors = {0xCCCC0000, 0xCCCC0000, 0}; // red for the example
		getListView().setDivider(getActivity().getResources().getDrawable(R.drawable.divider));
		getListView().setDividerHeight(1);
		//getListView().setBackgroundResource(android.R.color.white);
	}
}
