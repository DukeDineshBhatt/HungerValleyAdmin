package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SubCategory extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    String cat_id, imagetxt, poss;
    int pos;
    Button add, delete,edit;
    RecyclerView subCat;
    myadapter adapter;
    ImageView image;
    ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    DatabaseReference mCatDatabase, mAdminDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subzcat);


        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        image = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        edit = (Button) findViewById(R.id.edit);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        imagetxt = intent.getStringExtra("image");
        pos = intent.getIntExtra("pos", 0);
        getSupportActionBar().setTitle(cat_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        subCat = (RecyclerView) findViewById(R.id.subCat);

        int a = pos++;
        poss = "Cat" + pos;


        Log.d("SSSS", poss);

        linearLayoutManager = new LinearLayoutManager(this);

        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Logout");
        mCatDatabase = FirebaseDatabase.getInstance().getReference().child("Categories").child(poss).child("Menu");

        subCat.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<SubModel> options =
                new FirebaseRecyclerOptions.Builder<SubModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories").child(poss).child("Menu"), SubModel.class)
                        .build();


        adapter = new myadapter(options);
        subCat.setAdapter(adapter);

        Glide.with(image.getContext()).load(imagetxt).into(image);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SubCategory.this)
                        .setMessage("are you sure want to Delete this Category?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                FirebaseDatabase.getInstance().getReference().child("Categories").child(poss).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        //progressbar.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(SubCategory.this, CategoriesActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Toast.makeText(SubCategory.this, "This Category deleted successfully.", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(SubCategory.this, "Please try again!.", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                            }
                        }).create().show();


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SubCategory.this, AddNewCatItem.class);
                intent.putExtra("poss", poss);
                intent.putExtra("name", cat_id);
                startActivity(intent);

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SubCategory.this, EditCategory.class);
                intent.putExtra("poss", poss);
                intent.putExtra("name", cat_id);
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }


    public class myadapter extends FirebaseRecyclerAdapter<SubModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<SubModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull final SubModel model) {

            holder.setIsRecyclable(false);

            holder.res_name.setText(model.getRes());
            holder.price.setText(Integer.toString(model.getPrice()));

            String type = model.getType();

            holder.food_name.setText(model.getFoodName());

            if (!type.equals("Non-Veg")) {

                Glide.with(holder.type_image.getContext()).load(R.drawable.veg).into(holder.type_image);

            } else {

                Glide.with(holder.type_image.getContext()).load(R.drawable.non_veg).into(holder.type_image);

            }
            Log.d("DDDDD", getRef(position).getKey().toString());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(SubCategory.this)
                            .setMessage("are you sure want to Delete this Item?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    mCatDatabase.child(getRef(position).getKey().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            //progressbar.setVisibility(View.VISIBLE);

                                            if (task.isSuccessful()) {

                                                Intent intent = new Intent(SubCategory.this, CategoriesActivity.class);
                                                startActivity(intent);
                                                finish();

                                                Toast.makeText(SubCategory.this, "This Order deleted successfully.", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(SubCategory.this, "Please try again!.", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });


                                }
                            }).create().show();


                }
            });

        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subcat, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            ImageView img, type_image, delete;
            TextView res_name, food_name, price, textCount;
            LinearLayout main_view;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.imageView5);
                res_name = (TextView) itemView.findViewById(R.id.res_name);
                food_name = (TextView) itemView.findViewById(R.id.food_name);
                price = (TextView) itemView.findViewById(R.id.price);
                type_image = (ImageView) itemView.findViewById(R.id.type_image);
                delete = (ImageView) itemView.findViewById(R.id.delete);

                textCount = (TextView) itemView.findViewById(R.id.text);
                main_view = itemView.findViewById(R.id.main_view);
            }
        }

    }
}