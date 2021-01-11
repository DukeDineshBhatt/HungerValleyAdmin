package com.dinesh.hungervalleyadmin;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyActivity extends AppCompatActivity {

    Button orders, new_orders;
    TextView users_count;
    ProgressBar progressbar;
    Button status;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mOrderDatabase;
    private DatabaseReference mAdminDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        orders = (Button) findViewById(R.id.orders);
        new_orders = (Button) findViewById(R.id.new_orders);
        users_count = (TextView) findViewById(R.id.users_count);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        status = (findViewById(R.id.status));

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin");

        mOrderDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View");

        progressbar.setVisibility(View.VISIBLE);


        mOrderDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, @Nullable String s) {

                notification();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count = dataSnapshot.getChildrenCount();

                users_count.setText(String.valueOf(count));

                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressbar.setVisibility(View.GONE);

            }
        });


        mAdminDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if (dataSnapshot.child("Logout").child("status").getValue().equals("on")){

                   status.setText("Turn OFF App");
                   status.setBackgroundColor(getResources().getColor(R.color.btnColor));

                   status.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           final AlertDialog dialogBuilder = new AlertDialog.Builder(MyActivity.this).create();
                           LayoutInflater inflater = MyActivity.this.getLayoutInflater();
                           View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                           final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                           Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                           Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                           button2.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   dialogBuilder.dismiss();
                               }
                           });
                           button1.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   // DO SOMETHINGS

                                   if (editText.getText().toString().equals("")){

                                       mAdminDatabase.child("Logout").child("status").setValue("off").addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(Task<Void> task) {

                                               if (task.isSuccessful()){

                                                   status.setText("Turn On App");
                                                   status.setBackgroundColor(getResources().getColor(R.color.btnColorOff));
                                                   Toast.makeText(MyActivity.this,"App is OFF now.",Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });

                                   }
                                   else{

                                       mAdminDatabase.child("Logout").child("status").setValue("off").addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(Task<Void> task) {

                                               if (task.isSuccessful()){
                                                   mAdminDatabase.child("Logout").child("Reason").setValue(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(Task<Void> task) {

                                                           status.setText("Turn On App");
                                                           status.setBackgroundColor(getResources().getColor(R.color.btnColorOff));
                                                           Toast.makeText(MyActivity.this,"App is OFF now.",Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                               }
                                           }
                                       });

                                       Toast.makeText(MyActivity.this,"App.",Toast.LENGTH_SHORT).show();

                                   }


                                   dialogBuilder.dismiss();
                               }
                           });

                           dialogBuilder.setView(dialogView);
                           dialogBuilder.show();

                       }
                   });

               }else{

                   status.setText("Turn On App");
                   status.setBackgroundColor(getResources().getColor(R.color.btnColorOff));

                   status.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {


                           new AlertDialog.Builder(MyActivity.this)
                                   .setMessage("are you sure want to Turn On The App?")
                                   .setNegativeButton(android.R.string.no, null)
                                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                       public void onClick(final DialogInterface arg0, int arg1) {

                                           mAdminDatabase.child("Logout").child("status").setValue("on").addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(Task<Void> task) {

                                                   if (task.isSuccessful()){

                                                       status.setText("Turn OFF App");
                                                       status.setBackgroundColor(getResources().getColor(R.color.btnColor));
                                                       arg0.cancel();
                                                       Toast.makeText(MyActivity.this,"App is ON now.",Toast.LENGTH_SHORT).show();

                                                   }

                                               }
                                           });




                                       }
                                   }).create().show();


                       }
                   });

               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void notification() {


        String message = "There is a New Order";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentTitle("Hunger Valley")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(message);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }
}
