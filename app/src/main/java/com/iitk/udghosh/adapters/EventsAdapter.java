package com.iitk.udghosh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iitk.udghosh.EventPageActivity;
import com.iitk.udghosh.R;
import com.iitk.udghosh.models.Event;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter {

    ArrayList<Event> eventsList = new ArrayList<>();

    public EventsAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        eventsList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        final String EventName = eventsList.get(position).getEventName();
        final String className = eventsList.get(position).getClassName();
        TextView textView = v.findViewById(R.id.textView);
        ImageView imageView =  v.findViewById(R.id.imageView);
        textView.setText(EventName);
        imageView.setImageResource(eventsList.get(position).getEventImage());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent redirect = new Intent(context, EventPageActivity.class);
                // Pass Event Object from here as Intent.putExtra to the EventPageActivity
                redirect.putExtra("data", EventName);
                redirect.putExtra("linkN", className);
                context.startActivity(redirect);

            }
        });

        return v;

    }

}