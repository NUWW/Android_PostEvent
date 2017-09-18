package com.laioffer.laiofferproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by weiguang on 8/27/17.
 */

public class EventAdapter extends BaseAdapter {
    Context context;
    List<Event> eventData;
    List<String> imgSet;
    public EventAdapter(Context context) {
        this.context = context;
        //eventData = DataService.getEventData();
    }

    @Override
    public int getCount() {
        return eventData.size();
    }

    @Override
    public Event getItem(int position) {
        return eventData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
        }

        TextView eventTitle = (TextView) convertView.findViewById(R.id.event_title);
        TextView eventAddress = (TextView) convertView.findViewById(R.id.event_address);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.event_description);
        ImageView eventThumbnail = (ImageView) convertView.findViewById(R.id.event_thumbnail);

        Event r = eventData.get(position);
        //eventTitle.setText(r.getTitle());
        //eventAddress.setText(r.getAddress());
        eventDescription.setText(r.getDescription());
        if (position <= 3) {
            eventThumbnail.setImageResource(R.drawable.event_thumbnail);
        } else if (position >= 4 && position <=5) {
            eventThumbnail.setImageResource(R.drawable.mongo);
        } else {
            eventThumbnail.setImageResource(R.drawable.timg);
        }
        return convertView;
    }
}
