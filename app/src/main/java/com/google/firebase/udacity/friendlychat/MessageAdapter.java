package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.restaurant_image);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.restaurant_name);

        FriendlyMessage message = getItem(position);

        photoImageView.setVisibility(View.VISIBLE);
        Glide.with(photoImageView.getContext()).load(message.getPhotoUrl().get(0)).into(photoImageView);
        authorTextView.setText(message.getName());

        return convertView;
    }
}
