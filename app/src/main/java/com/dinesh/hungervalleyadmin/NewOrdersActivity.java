package com.dinesh.hungervalleyadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewOrdersActivity extends AppCompatActivity {


    DatabaseReference mOrdersDatabase;

    ProgressBar progressBar;
    TextView txt_no_order;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orders);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_no_order = (TextView) findViewById(R.id.txt_no_orders);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);

        linearLayoutManager = new LinearLayoutManager(NewOrdersActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar.setVisibility(View.VISIBLE);


        mOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View");

        mOrdersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    txt_no_order.setVisibility(View.GONE);


                    FirebaseRecyclerAdapter<MyDataSetGet, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<MyDataSetGet, FriendsViewHolder>(

                            MyDataSetGet.class,
                            R.layout.list_item_single,
                            FriendsViewHolder.class,
                            mOrdersDatabase

                    ) {
                        @Override
                        protected void populateViewHolder(final FriendsViewHolder viewHolder, MyDataSetGet model, int position) {

                            final String list_user_id = getRef(position).getKey();

                            mOrdersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    viewHolder.setName(list_user_id);

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        if (!dataSnapshot1.getKey().equals("Total Price")) {

                                            String status = dataSnapshot1.getValue().toString();

                                            viewHolder.setStatus(status);


                                        }

                                        if (!dataSnapshot1.getKey().equals("Status") && !dataSnapshot1.getKey().equals("Total Price")) {

                                            // name = dataSnapshot1.getKey().toString();
                                        }

                                    }

                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(NewOrdersActivity.this, SingleOrderActivity.class);

                                            intent.putExtra("user_id", list_user_id);
                                            startActivity(intent);

                                        }
                                    });

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }
                    };

                    recyclerView.setAdapter(friendsRecyclerView);


                } else {

                    txt_no_order.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public void setName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.name);
            userName.setText(name);
        }

        public void setStatus(String status) {
            TextView txt_status = (TextView) mView.findViewById(R.id.status);
            txt_status.setText(status);
        }


    }


}
