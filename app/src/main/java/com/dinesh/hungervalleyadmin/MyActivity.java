package com.dinesh.hungervalleyadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyActivity extends AppCompatActivity {

    Button orders,new_orders;
    TextView users_count;
    ProgressBar progressbar;

    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        orders = (Button) findViewById(R.id.orders);
        new_orders = (Button) findViewById(R.id.new_orders);
        users_count = (TextView) findViewById(R.id.users_count);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        progressbar.setVisibility(View.VISIBLE);


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count = dataSnapshot.getChildrenCount();

                users_count.setText(String.valueOf(count));

                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressbar.setVisibility(View.GONE);

            }
        });


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyActivity.this, OrdersActivity.class);

                startActivity(intent);

            }
        });

        new_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyActivity.this, NewOrdersActivity.class);

                startActivity(intent);

            }
        });



    }
}
