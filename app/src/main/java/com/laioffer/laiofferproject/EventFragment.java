package com.laioffer.laiofferproject;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    ListView listView;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false); // 得到view的instance
        listView = (ListView) view.findViewById(R.id.event_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getRestaurantName());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                aCallBack.onItemSelected(i);
            }
        });

        return view;
    }

    private String[] getRestaurantName() {
        String[] name = {
                "Restaurant1", "Restaurant2", "Restaurant3",
                "Restaurant4", "Restaurant5", "Restaurant6",
                "Restaurant7", "Restaurant8", "Restaurant9",
                "Restaurant10", "Restaurant11", "Restaurant12"
        };
        return name;
    }

    OnItemSelectListener aCallBack;

    public interface OnItemSelectListener {
        public void onItemSelected(int position);
    }

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            aCallBack = (OnItemSelectListener) context;
        } catch (ClassCastException e) {

        }
    }

    public void onItemClicked(int position) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            if (position == i) {
                listView.getChildAt(i).setBackgroundColor(Color.BLUE);
            } else {
                listView.getChildAt(i).setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
        }
    }

}
