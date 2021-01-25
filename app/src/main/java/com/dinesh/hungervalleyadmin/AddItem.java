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

public class AddItem extends AppCompatActivity {

    Toolbar toolbar;
    Button add_btn;
    ProgressBar progressbar;
    String restaurant;
    EditText food_name, price;
    DatabaseReference mRestaurantDatabase;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = findViewById(R.id.toolbar);
        add_btn = findViewById(R.id.add_btn);
        progressbar = findViewById(R.id.progressbar);
        food_name = findViewById(R.id.food_name);
        price = findViewById(R.id.price);

        Intent intent = getIntent();
        restaurant = intent.getStringExtra("restaurant");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(restaurant);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        radioGroup = findViewById(R.id.radioGender);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressbar.setVisibility(View.VISIBLE);

                String food = food_name.getText().toString().trim();
                String pricetxt = price.getText().toString().trim();

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

                } else if (selectedID < 0) {

                    progressbar.setVisibility(View.GONE);

                    price.setError("select Type");
                    price.requestFocus();
                    return;
                } else {


                    mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restaurant).child("Menu");

                    HashMap<String, Object> map = new HashMap<>();

                    map.put("FoodName", food);
                    map.put("Price", Integer.parseInt(pricetxt));
                    map.put("Type", radioButton.getText().toString());

                    mRestaurantDatabase.child(food).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                food_name.setText("");
                                price.setText("");

                                Toast.makeText(AddItem.this, "Added Successfully.", Toast.LENGTH_SHORT).show();


                                progressbar.setVisibility(View.GONE);

                            } else {

                                Toast.makeText(AddItem.this, "Failed.", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);


                            }
                        }
                    });


                }


            }
        });

    }
}