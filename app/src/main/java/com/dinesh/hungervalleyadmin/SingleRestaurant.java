package com.dinesh.hungervalleyadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SingleRestaurant extends AppCompatActivity {

    Toolbar toolbar;
    String restaurant;
    Button add_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        toolbar = findViewById(R.id.toolbar);
        add_item = findViewById(R.id.add_item);

        Intent intent = getIntent();
        restaurant = intent.getStringExtra("restaurant");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(restaurant);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleRestaurant.this, AddItem.class);
                intent.putExtra("restaurant", restaurant);
                startActivity(intent);

            }
        });


    }
}