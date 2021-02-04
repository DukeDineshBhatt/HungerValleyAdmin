package com.dinesh.hungervalleyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CategoriesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressbar;
    private RecyclerView recyclerView;
    Button add;
    private LinearLayoutManager linearLayoutManager;
    private myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        progressbar = findViewById(R.id.progressbar);
        add = findViewById(R.id.add);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressbar.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(CategoriesActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        FirebaseRecyclerOptions<CatSetGet> options =
                new FirebaseRecyclerOptions.Builder<CatSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories"), CatSetGet.class)
                        .build();


        adapter = new myadapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        progressbar.setVisibility(View.GONE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesActivity.this, AddNewCategory.class);
                startActivity(intent);

            }
        });

    }

    public class myadapter extends FirebaseRecyclerAdapter<CatSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<CatSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull final CatSetGet model) {

            holder.name.setText(model.getName());

            Glide.with(holder.img.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.placeholder_restaurant)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(42)))
                    .into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CategoriesActivity.this, SubCategory.class);
                    intent.putExtra("pos", position);
                    intent.putExtra("cat_id", model.getName());
                    intent.putExtra("image", model.getImage());
                    startActivity(intent);

                }
            });

        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_model2, parent, false);
            return new myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            ImageView img;
            TextView name;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.textView18);
                img = (ImageView) itemView.findViewById(R.id.imageView5);

            }
        }
    }

}