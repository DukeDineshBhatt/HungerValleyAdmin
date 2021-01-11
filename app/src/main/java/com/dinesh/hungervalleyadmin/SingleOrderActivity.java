package com.dinesh.hungervalleyadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleOrderActivity extends AppCompatActivity {

    private RecyclerView res_name;

    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;

    private DatabaseReference mCartListDatabase, mUserDatabase, myDatabase;
    String userId;
    TextView number, name, address, altNumber, total, status;
    ProgressBar progressbar;
    Button delete,edit;

    private myadapter adapter;


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
        total = findViewById(R.id.total);
        status = findViewById(R.id.status);
        edit = findViewById(R.id.edit);

        number.setText("Phone Number : " + userId);

        res_name = (RecyclerView) findViewById(R.id.res_name);
        Log.d("DDDD", userId);

        // res_name.setHasFixedSize(true);
        // res_name.setNestedScrollingEnabled(false);


        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

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
            public void onCancelled(DatabaseError databaseError) {

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

        linearLayoutManager = new LinearLayoutManager(SingleOrderActivity.this);
        res_name.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<OrderSetGet> options =
                new FirebaseRecyclerOptions.Builder<OrderSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child("Orders"), OrderSetGet.class)
                        .build();

        adapter = new myadapter(options);
        adapter.startListening();
        res_name.setAdapter(adapter);




        /*FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder>(

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
        };*/

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialog();

            }
        });

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
                                    public void onComplete(Task<Void> task) {
                                        //progressbar.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(SingleOrderActivity.this, NewOrdersActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Toast.makeText(SingleOrderActivity.this, "This Order deleted successfully.", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(SingleOrderActivity.this, "Please try again!.", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                            }
                        }).create().show();


            }
        });
        FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status.setText(dataSnapshot.child("Status").getValue().toString());
                total.setText(String.valueOf(dataSnapshot.child("Total Price").getValue()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SingleOrderActivity.this);
        alertDialog.setTitle("Status");
        String[] items = {"java","android","Data Structures","HTML","CSS"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(SingleOrderActivity.this, "Clicked on java", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(SingleOrderActivity.this, "Clicked on android", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(SingleOrderActivity.this, "Clicked on Data Structures", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(SingleOrderActivity.this, "Clicked on HTML", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(SingleOrderActivity.this, "Clicked on CSS", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    public class myadapter extends FirebaseRecyclerAdapter<OrderSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<OrderSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull OrderSetGet model) {

            holder.restro.setText(model.getRes());
            holder.price.setText(model.getPrice());
            holder.product_name.setText(model.getpName());
            holder.quantity.setText(model.getQuantity());
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_list, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView restro, price, product_name, quantity;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                price = (TextView) itemView.findViewById(R.id.price);
                restro = (TextView) itemView.findViewById(R.id.restro);
                product_name = (TextView) itemView.findViewById(R.id.product_name);
                quantity = (TextView) itemView.findViewById(R.id.qnty);


            }
        }
    }


}
