package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditCategory extends AppCompatActivity {

    Toolbar toolbar;
    Button add_btn;
    int no;
    ProgressBar progressbar;
    String poss, cat_name;
    EditText name, image;
    DatabaseReference mCategoryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        toolbar = findViewById(R.id.toolbar);
        add_btn = findViewById(R.id.add_btn);
        progressbar = findViewById(R.id.progressbar);
        name = findViewById(R.id.name);
        image = findViewById(R.id.image);

        Intent intent = getIntent();
        cat_name = intent.getStringExtra("name");
        poss = intent.getStringExtra("poss");

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setTitle("Edit");


        name.setText(cat_name.toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

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

                                Toast.makeText(EditCategory.this, "Edited Successfully.", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);
                                Intent intent = new Intent(EditCategory.this, CategoriesActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(EditCategory.this, "Failed.", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);


                            }
                        }
                    });


                }


            }
        });
    }
}