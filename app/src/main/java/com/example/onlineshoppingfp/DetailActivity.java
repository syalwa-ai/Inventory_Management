package com.example.onlineshoppingfp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    TextView detailPrice, detailProduct, detailQty;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = ""; // Firestore document key
    String imageUrl = ""; // URL gambar (tetap dipakai untuk Glide)

    FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize UI Components
        detailPrice = findViewById(R.id.detailPrice);
        detailImage = findViewById(R.id.detailImage);
        detailProduct = findViewById(R.id.detailProduct);
        detailQty = findViewById(R.id.detailQty);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get Data from Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailPrice.setText(bundle.getString("Price"));
            detailProduct.setText(bundle.getString("Product"));
            detailQty.setText(bundle.getString("Quantity"));
            key = bundle.getString("Key"); // Firestore document ID
            imageUrl = bundle.getString("Image");

            // Load Image using Glide
            Glide.with(this).load(imageUrl).into(detailImage);
        }

        // Delete Button: Hapus data dari Firestore
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProductFromFirestore();
            }
        });

        // Edit Button: Navigasi ke UpdateActivity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Product", detailProduct.getText().toString())
                        .putExtra("Price", detailPrice.getText().toString())
                        .putExtra("Quantity", detailQty.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key); // Document ID for Update
                startActivity(intent);
            }
        });
    }

    // Method to Delete Data from Firestore
    private void deleteProductFromFirestore() {
        if (key == null || key.isEmpty()) {
            Toast.makeText(this, "Invalid product key!", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("products")
                .document(key)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DetailActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(DetailActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
