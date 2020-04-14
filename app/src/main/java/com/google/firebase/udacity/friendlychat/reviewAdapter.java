package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.util.List;

public class reviewAdapter extends ArrayAdapter<Pair<String,String>> {
    List<Pair<String, String>> reviews;
    public reviewAdapter(@NonNull Context context, int resource, List<Pair<String, String>> objects) {
        super(context, resource, objects);
        reviews=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView reviewTextView = (TextView) convertView.findViewById(R.id.review);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.user_name);

        authorTextView.setText(reviews.get(position).first);
        reviewTextView.setText(reviews.get(position).second);

        return convertView;
    }
}
