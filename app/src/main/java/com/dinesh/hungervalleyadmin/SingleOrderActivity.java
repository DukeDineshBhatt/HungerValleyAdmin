package com.dinesh.hungervalleyadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleOrderActivity extends AppCompatActivity {

    private RecyclerView res_name;

    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;

    private DatabaseReference mCartListDatabase, mUserDatabase, myDatabase;
    String userId, statusTxt, delBoyNameTxt, delBoyId;
    TextView number, name, address, altNumber, total;
    ProgressBar progressbar;
    Button delete, edit, status;
    int statusItem;
    EditText delName;
    DatabaseReference mDeliveryBoysDatabase;
    List<String> listItems, listItems1;

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
        delName = findViewById(R.id.delName);

        listItems = new ArrayList<String>();
        listItems1 = new ArrayList<String>();

        number.setText("Phone Number : " + userId);

        res_name = (RecyclerView) findViewById(R.id.res_name);

        // res_name.setHasFixedSize(true);
        // res_name.setNestedScrollingEnabled(false);

        final CharSequence[] items = {"OnWay"};

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

        mDeliveryBoysDatabase = FirebaseDatabase.getInstance().getReference().child("DeliveryBoys");

        mDeliveryBoysDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    listItems.add(postSnapshot.child("name").getValue().toString());
                    listItems1.add(postSnapshot.getKey().toString());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SingleOrderActivity.this);
                builder.setTitle("Select");
                builder.setIcon(R.drawable.ic_baseline_edit_24);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        statusTxt = items[item].toString();


                    }
                });

                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child("Status").setValue(statusTxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            status.setText(statusTxt);
                                            status.setBackgroundColor(Color.parseColor("#008000"));

                                            Toast.makeText(SingleOrderActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*showAlertDialog();*/
                Intent intent = new Intent(SingleOrderActivity.this, EditOrder.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);


            }
        });

        delName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SingleOrderActivity.this);
                builder.setTitle("Select");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(listItems1.toArray(new String[listItems1.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        delBoyNameTxt = listItems1.get(item).toString();

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                progressbar.setVisibility(View.VISIBLE);

                                FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child("DelBoy").setValue(delBoyNameTxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            delName.setText(delBoyNameTxt);

                                            Map userMap = new HashMap();
                                            userMap.put("resPayAmount", "");
                                            userMap.put("amountStatus", "");
                                            userMap.put("Status", "Pending");

                                            FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(delBoyNameTxt).child("Orders").child(userId).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(SingleOrderActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                                                        progressbar.setVisibility(View.GONE);

                                                    }


                                                }
                                            });

                                        } else {
                                            Toast.makeText(SingleOrderActivity.this, "ERROR! TRY AGAIN", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

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

                if (dataSnapshot.child("Status").getValue().toString().equals("Pending")) {

                    status.setText(dataSnapshot.child("Status").getValue().toString());

                } else {

                    status.setText(dataSnapshot.child("Status").getValue().toString());
                    status.setBackgroundColor(Color.parseColor("#008000"));
                }

                if (dataSnapshot.hasChild("DelBoy")) {

                    delName.setText(dataSnapshot.child("DelBoy").getValue().toString());
                } else {

                    FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child("DelBoy").setValue("select");

                }


                total.setText(String.valueOf(dataSnapshot.child("Total Price").getValue()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
