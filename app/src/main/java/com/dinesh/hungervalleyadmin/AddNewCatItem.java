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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddNewCatItem extends AppCompatActivity {

    Toolbar toolbar;
    Button add_btn;
    ProgressBar progressbar;
    String restaurant,name,poss;
    EditText food_name, price, res;
    DatabaseReference mCategoryDatabase;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_cat_item);

        toolbar = findViewById(R.id.toolbar);
        add_btn = findViewById(R.id.add_btn);
        progressbar = findViewById(R.id.progressbar);
        food_name = findViewById(R.id.food_name);
        price = findViewById(R.id.price);
        res = findViewById(R.id.res);
        radioGroup = findViewById(R.id.radioGender);

        Intent intent = getIntent();
        poss = intent.getStringExtra("poss");
        name = intent.getStringExtra("name");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setTitle(name);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressbar.setVisibility(View.VISIBLE);

                String food = food_name.getText().toString().trim();
                String pricetxt = price.getText().toString().trim();
                String restext = res.getText().toString().trim();
                int selectedID = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(selectedID);

                //Toast.makeText(AddItem.this, radioButton.getText(),Toast.LENGTH_SHORT).show();

                if (food.isEmpty()) {
                    progressbar.setVisibility(View.GONE);

                    food_name.setError("Enter FoodName");
                    food_name.requestFocus();
                    return;

                } else if (pricetxt.isEmpty()) {
                    progressbar.setVisibility(View.GONE);

                    price.setError("Enter Price");
                    price.requestFocus();
                    return;

                } else if (restext.isEmpty()) {
                    progressbar.setVisibility(View.GONE);

                    res.setError("Enter Res");
                    res.requestFocus();
                    return;

                } else if (selectedID < 0) {

                    progressbar.setVisibility(View.GONE);

                    price.setError("select Type");
                    price.requestFocus();
                    return;
                } else {


                    mCategoryDatabase = FirebaseDatabase.getInstance().getReference().child("Categories").child(poss).child("Menu");

                    HashMap<String, Object> map = new HashMap<>();

                    map.put("FoodName", food);
                    map.put("Price", Integer.parseInt(pricetxt));
                    map.put("Res", restext);
                    map.put("Type", radioButton.getText().toString());

                    mCategoryDatabase.push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                food_name.setText("");
                                price.setText("");
                                res.setText("");

                                Toast.makeText(AddNewCatItem.this, "Added Successfully.", Toast.LENGTH_SHORT).show();


                                progressbar.setVisibility(View.GONE);

                            } else {

                                Toast.makeText(AddNewCatItem.this, "Failed.", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);


                            }
                        }
                    });


                }


            }
        });
    }
}