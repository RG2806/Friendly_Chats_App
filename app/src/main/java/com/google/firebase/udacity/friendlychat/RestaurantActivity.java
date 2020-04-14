package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class RestaurantActivity  extends AppCompatActivity {
   private String restaurant_name;
    private String username;
    private List<String> images = new ArrayList<>();
    private List<Pair<String, String>> reviews;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_review);
        Intent intent=getIntent();
        restaurant_name=intent.getExtras().getString("restaurant_name");
        username=intent.getExtras().getString("username");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query ref = database.getReference("messages").orderByChild("name").equalTo(restaurant_name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                images=friendlyMessage.getPhotoUrl();
                reviews=friendlyMessage.getmReviews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ListView list=findViewById(R.id.list_review);
        reviewAdapter mAapter= new reviewAdapter(this, R.id.review, reviews);
        list.setAdapter(mAapter);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantActivity.this, ReviewActivity.class);
                intent.putExtra("restaurant_name", restaurant_name);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }
}
