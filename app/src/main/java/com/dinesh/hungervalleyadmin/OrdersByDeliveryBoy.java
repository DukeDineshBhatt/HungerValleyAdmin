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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdersByDeliveryBoy extends AppCompatActivity {

    private DatabaseReference mOrdersDatabase;
    ProgressBar progressBar;
    TextView txt_no_order;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private myadapter adapter;
    Toolbar toolbar;
    String del_boy_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersbydelivery_boy);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_no_order = (TextView) findViewById(R.id.txt_no_orders);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);

        del_boy_id = getIntent().getStringExtra("del_boy_id");

        getSupportActionBar().setTitle(del_boy_id);

        progressBar.setVisibility(View.VISIBLE);

        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(del_boy_id).child("CompletedOrders");

        mOrdersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    txt_no_order.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(OrdersByDeliveryBoy.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    FirebaseRecyclerOptions<ListSetGet1> options =
                            new FirebaseRecyclerOptions.Builder<ListSetGet1>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(del_boy_id).child("CompletedOrders"), ListSetGet1.class)
                                    .build();


                    adapter = new myadapter(options);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);


                    progressBar.setVisibility(View.GONE);

                } else {

                    txt_no_order.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public class myadapter extends FirebaseRecyclerAdapter<ListSetGet1, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<ListSetGet1> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull ListSetGet1 model) {

            final String Id = getRef(position).getKey();
            holder.name.setText(position+1+" .  "+ Id);

            if (model.getAmountStatus().equals("Pending")) {

                holder.status.setText(model.getAmountStatus());
            } else {

                holder.status.setText(model.getAmountStatus());
                holder.status.setTextColor(Color.parseColor("#008000"));

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(OrdersByDeliveryBoy.this, SingleCompletedActivity.class);

                    intent.putExtra("del_boy_id", del_boy_id);
                    intent.putExtra("order_id", getRef(position).getKey());
                    startActivity(intent);

                }
            });
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single1, parent, false);
            return new myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView name, status;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                status = (TextView) itemView.findViewById(R.id.amountStatus);

            }
        }
    }
}