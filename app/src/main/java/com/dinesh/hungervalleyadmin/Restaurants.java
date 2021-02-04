package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Restaurants extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private myadapter adapter;
    Toolbar toolbar;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        toolbar = findViewById(R.id.toolbar);
        progressbar = findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Restaurants");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressbar.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(Restaurants.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        FirebaseRecyclerOptions<RestaurantModel> options =
                new FirebaseRecyclerOptions.Builder<RestaurantModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Restaurants"), RestaurantModel.class)
                        .build();


        adapter = new myadapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        progressbar.setVisibility(View.GONE);
    }


    public class myadapter extends FirebaseRecyclerAdapter<RestaurantModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<RestaurantModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final myadapter.myviewholder holder, final int position, @NonNull final RestaurantModel model) {


            holder.name.setText(model.getRestaurant_name());


            holder.simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        FirebaseDatabase.getInstance().getReference().child("Restaurants").child(model.getRestaurant_name()).child("Status").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    } else {

                        FirebaseDatabase.getInstance().getReference().child("Restaurants").child(model.getRestaurant_name()).child("Status").setValue("Off").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }
                }
            });

            if (model.getStatus() != null) {

                holder.simpleSwitch.setChecked(false);
                holder.layout.setBackgroundColor(Color.parseColor("#808080"));


            } else {

                holder.simpleSwitch.setChecked(true);
                holder.layout.setBackgroundColor(Color.parseColor("#4CAF50"));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Restaurants.this, SingleRestaurant.class);
                    intent.putExtra("restaurant", model.getRestaurant_name());
                    startActivity(intent);

                }
            });


        }


        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_res_model, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView name;
            Switch simpleSwitch;
            LinearLayout layout;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                simpleSwitch = (Switch) itemView.findViewById(R.id.simpleSwitch);
                layout = itemView.findViewById(R.id.layout);

            }
        }
    }
}