package com.dinesh.hungervalleyadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText name, password;
    Button button_login;
    String txt_username, txt_password;
    private DatabaseReference mCartListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.button_login);


        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Login");


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_username = name.getText().toString().trim();

                txt_password = password.getText().toString().trim();

                if (txt_username.isEmpty()) {

                    name.setError("Enter username");
                    name.requestFocus();
                    return;

                } else if (txt_password.isEmpty() || txt_password.length() < 6) {

                    password.setError("Enter your 6 digit password");
                    password.requestFocus();

                } else {


                    mCartListDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.child("User").getValue().equals(txt_username) && dataSnapshot.child("Password").getValue().equals(txt_password)) {

                                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(MainActivity.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
        });


    }
}
