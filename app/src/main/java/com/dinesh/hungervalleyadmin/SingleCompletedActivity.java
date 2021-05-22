package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class SingleCompletedActivity extends AppCompatActivity {

    private RecyclerView res_name;

    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;

    private DatabaseReference mCartListDatabase, mUserDatabase, myDatabase;
    String userId, statusTxt, order_id, del_boy_id, payment_type_txt, delBoyId;
    TextView number, address, altNumber, total;
    ProgressBar progressbar;
    Button delete, save;
    EditText res_pay, customer_payment_type, status, profit;
    int profitAmount, intTotal;
    DatabaseReference mDeliveryBoysDatabase;
    List<String> listItems, listItems1;

    private myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_completed);


        del_boy_id = getIntent().getStringExtra("del_boy_id");
        order_id = getIntent().getStringExtra("order_id");

        userId = order_id.substring(20, 30);

        number = (TextView) findViewById(R.id.phoneNumber);
        altNumber = (TextView) findViewById(R.id.altPhoneNumber);
        address = (TextView) findViewById(R.id.address);
        progressbar = findViewById(R.id.progressbar);
        total = findViewById(R.id.total);
        res_pay = findViewById(R.id.res_pay);
        customer_payment_type = findViewById(R.id.customer_payment_type);
        status = findViewById(R.id.status);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        profit = findViewById(R.id.profit);


        listItems = new ArrayList<String>();
        listItems.add("Done");
        listItems.add("Pending");

        listItems1 = new ArrayList<String>();
        listItems1.add("Cash");
        listItems1.add("Online");


        number.setText("Phone Number : " + userId);

        res_name = (RecyclerView) findViewById(R.id.res_name);

        progressbar.setVisibility(View.VISIBLE);

        final CharSequence[] items = {"Done", "Pending"};
        final CharSequence[] items1 = {"Cash", "Online"};

        //mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(my_user_id).child("Orders");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                progressbar.setVisibility(View.VISIBLE);

                altNumber.setText("Alternate Number : " + dataSnapshot.child("Address").child("Mobile").getValue().toString());
                address.setText(dataSnapshot.child("Address").child("landmark").getValue().toString() + "\n" + dataSnapshot.child("Address").child("locality").getValue().toString() + "\n" + dataSnapshot.child("Address").child("location").getValue().toString());


                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressbar.setVisibility(View.GONE);
            }
        });

        linearLayoutManager = new LinearLayoutManager(SingleCompletedActivity.this);
        res_name.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<OrderSetGet> options =
                new FirebaseRecyclerOptions.Builder<OrderSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(del_boy_id).child("CompletedOrders").child(order_id).child("Orders"), OrderSetGet.class)
                        .build();

        adapter = new myadapter(options);
        adapter.startListening();
        res_name.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(del_boy_id).child("CompletedOrders").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                total.setText(String.valueOf(dataSnapshot.child("Total Price").getValue()));
                intTotal = Integer.parseInt(dataSnapshot.child("Total Price").getValue().toString());
                res_pay.setText(String.valueOf(dataSnapshot.child("resPayAmount").getValue()));
                status.setText(String.valueOf(dataSnapshot.child("amountStatus").getValue()));
                profit.setText(String.valueOf(dataSnapshot.child("profitAmount").getValue()));
                customer_payment_type.setText(String.valueOf(dataSnapshot.child("customerPaymentType").getValue()));

                if (dataSnapshot.child("amountStatus").getValue().equals("Pending")) {

                    status.setText(String.valueOf(dataSnapshot.child("amountStatus").getValue()));
                } else {

                    status.setText(String.valueOf(dataSnapshot.child("amountStatus").getValue()));
                    status.setTextColor(Color.parseColor("#008000"));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SingleCompletedActivity.this);
                builder.setTitle("Select");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_baseline_edit_24);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        statusTxt = items[item].toString();

                    }
                });

                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                status.setText(statusTxt);

                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SingleCompletedActivity.this)
                        .setMessage("are you sure ?")
                        .setCancelable(false)
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                if (res_pay.getText().toString().isEmpty() || Integer.parseInt(res_pay.getText().toString()) > intTotal) {

                                    progressbar.setVisibility(View.GONE);
                                    res_pay.setError("error");
                                    res_pay.requestFocus();
                                    return;

                                } else if (status.getText().toString().isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    status.setError("error");
                                    status.requestFocus();
                                    return;

                                } else {

                                    Map userMap = new HashMap();
                                    userMap.put("resPayAmount", res_pay.getText().toString());
                                    userMap.put("amountStatus", status.getText().toString());
                                    userMap.put("customerPaymentType", customer_payment_type.getText().toString());
                                    userMap.put("Status", "onWay");
                                    userMap.put("profitAmount", intTotal - Integer.parseInt(res_pay.getText().toString()));

                                    FirebaseDatabase.getInstance().getReference().child("DeliveryBoys").child(del_boy_id).child("CompletedOrders").child(order_id).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(SingleCompletedActivity.this, "SAVED.", Toast.LENGTH_LONG).show();

                                            } else {

                                                Toast.makeText(SingleCompletedActivity.this, "Try Again!.", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                }

                            }
                        }).create().show();


            }
        });
    }

    public class myadapter extends FirebaseRecyclerAdapter<OrderSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<OrderSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull OrderSetGet model) {

            progressbar.setVisibility(View.VISIBLE);

            holder.restro.setText(model.getRes());
            holder.price.setText(model.getPrice());
            holder.product_name.setText(model.getpName());
            holder.quantity.setText(model.getQuantity());

            progressbar.setVisibility(View.GONE);
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