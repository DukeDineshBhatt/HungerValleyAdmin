package com.dinesh.hungervalleyadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    DatabaseReference mCartListDatabase, mUserDatabase, myDatabase;
    String userId;
    TextView number, name, address, altNumber;
    ProgressBar progressbar;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        userId = getIntent().getStringExtra("user_id");

        number = (TextView) findViewById(R.id.phoneNumber);
        altNumber = (TextView) findViewById(R.id.altPhoneNumber);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        progressbar = findViewById(R.id.progressbar);
        delete = findViewById(R.id.delete);

        number.setText("Phone Number : " + userId);

        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        linearLayoutManager = new LinearLayoutManager(SingleOrderActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);



        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                progressbar.setVisibility(View.VISIBLE);

                name.setText(dataSnapshot.child("username").getValue().toString());
                altNumber.setText("Alternate Number : " + dataSnapshot.child("Address").child("Mobile").getValue().toString());
                address.setText(dataSnapshot.child("Address").child("landmark").getValue().toString() + "\n" + dataSnapshot.child("Address").child("locality").getValue().toString() + "\n" + dataSnapshot.child("Address").child("location").getValue().toString());

                altNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + dataSnapshot.child("Address").child("Mobile").getValue().toString()));
                        startActivity(callIntent);
                    }
                });

                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressbar.setVisibility(View.GONE);
            }
        });


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + userId));
                startActivity(callIntent);

            }
        });

        FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder>(

                CartDataSetGet.class,
                R.layout.list_cart_item,
                FriendsViewHolder.class,
                mCartListDatabase

        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, CartDataSetGet model, int position) {

                Log.d("masin", getRef(position).getKey());

                String restaurantName = getRef(position).getKey();


                viewHolder.setName(restaurantName);

                myDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child(getRef(position).getKey());
                FirebaseRecyclerAdapter<ItemDataSetGet, MyFriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<ItemDataSetGet, MyFriendsViewHolder>(

                        ItemDataSetGet.class,
                        R.layout.nested_list,
                        MyFriendsViewHolder.class,
                        myDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final MyFriendsViewHolder viewHolder, ItemDataSetGet model, int position) {

                        final String productName = getRef(position).getKey();

                        myDatabase.child(productName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // viewHolder.setpName(productName);

                                //viewHolder.setQuantity("2");

                                Log.d("DINESHHHHHHH", dataSnapshot.toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                };
                viewHolder.recyclerView.setAdapter(friendsRecyclerView);

                mCartListDatabase.child(restaurantName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });

            }
        };
        //recyclerView.setAdapter(friendsRecyclerView);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SingleOrderActivity.this)
                        .setMessage("are you sure want to Delete this order?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                mCartListDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       //progressbar.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()){

                                            Intent intent = new Intent(SingleOrderActivity.this, NewOrdersActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Toast.makeText(SingleOrderActivity.this,"This Order deleted successfully.",Toast.LENGTH_SHORT).show();

                                        }else
                                        {
                                            Toast.makeText(SingleOrderActivity.this,"Please try again!.",Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });



                            }
                        }).create().show();



            }
        });
    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        LinearLayout linear_layout;
        private RecyclerView recyclerView;
        private LinearLayoutManager linearLayoutManager;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.upload_list);
            linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);


        }

        public void setName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.restaurant_name);
            userName.setText(name);

        }


    }

    public static class MyFriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyFriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setpName(String pName) {
            TextView textpName = (TextView) mView.findViewById(R.id.product_name);
            textpName.setText(pName);

        }

        public void setQuantity(String quantity) {
            EditText edit_qnty = (EditText) mView.findViewById(R.id.qnty);
            edit_qnty.setText(quantity);

        }


    }


}
