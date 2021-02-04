package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddNewCategory extends AppCompatActivity {

    Toolbar toolbar;
    Button add_btn;
    int no;
    ProgressBar progressbar;
    String  nametxt, poss;
    EditText name, image;
    DatabaseReference mCategoryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        toolbar = findViewById(R.id.toolbar);
        add_btn = findViewById(R.id.add_btn);
        progressbar = findViewById(R.id.progressbar);
        name = findViewById(R.id.name);
        image = findViewById(R.id.image);

        Intent intent = getIntent();

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setTitle("Add");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FirebaseDatabase.getInstance().getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                no = (int)dataSnapshot.getChildrenCount();

                int a = ++no;
                poss = "Cat" + a;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressbar.setVisibility(View.VISIBLE);

                String cat_name = name.getText().toString().trim();
                String txtimage = image.getText().toString().trim();


                //Toast.makeText(AddItem.this, radioButton.getText(),Toast.LENGTH_SHORT).show();

                if (cat_name.isEmpty()) {
                    progressbar.setVisibility(View.GONE);

                    name.setError("Enter name");
                    name.requestFocus();
                    return;

                } else if (txtimage.isEmpty()) {
                    progressbar.setVisibility(View.GONE);

                    image.setError("Enter url");
                    image.requestFocus();
                    return;

                } else {

                    mCategoryDatabase = FirebaseDatabase.getInstance().getReference().child("Categories").child(poss);

                    HashMap<String, Object> map = new HashMap<>();

                    map.put("Name", cat_name);
                    map.put("Image", txtimage);

                    mCategoryDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                name.setText("");
                                image.setText("");

                                Toast.makeText(AddNewCategory.this, "Added Successfully.", Toast.LENGTH_SHORT).show();


                                progressbar.setVisibility(View.GONE);

                            } else {

                                Toast.makeText(AddNewCategory.this, "Failed.", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);


                            }
                        }
                    });


                }


            }
        });

    }
}