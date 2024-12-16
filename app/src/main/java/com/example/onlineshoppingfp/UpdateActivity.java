package com.example.onlineshoppingfp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updatePrice, updateProduct, updateQuantity;

    String product, price, quantity, key, imageUrl;
    FirebaseFirestore firestore;

    AlertDialog dialog; // Progress Dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize UI Components
        updateButton = findViewById(R.id.updateButton);
        updatePrice = findViewById(R.id.updatePrice);
        updateImage = findViewById(R.id.updateImage);
        updateQuantity = findViewById(R.id.updateQty);
        updateProduct = findViewById(R.id.updateProduct);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get Data from Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Load data to views
            imageUrl = bundle.getString("Image");
            Glide.with(UpdateActivity.this).load(imageUrl).into(updateImage);
            updateProduct.setText(bundle.getString("Product"));
            updatePrice.setText(bundle.getString("Price"));
            updateQuantity.setText(bundle.getString("Quantity"));
            key = bundle.getString("Key"); // Firestore document ID
        }

        // Update Button Click Listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(); // Update Firestore Data
            }
        });
    }

    // Update Data to Firestore
    private void updateData() {
        product = updateProduct.getText().toString().trim();
        price = updatePrice.getText().toString().trim();
        quantity = updateQuantity.getText().toString().trim();

        // Validate input fields
        if (product.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Progress Dialog
        showProgressDialog();

        // Create a Map to store updated data
        Map<String, Object> productData = new HashMap<>();
        productData.put("product", product);
        productData.put("price", price);
        productData.put("quantity", quantity);
        productData.put("image", imageUrl); // Keep existing image URL

        // Reference to Firestore document
        DocumentReference documentReference = firestore.collection("products").document(key);

        // Update Firestore Data
        documentReference.update(productData)
                .addOnSuccessListener(aVoid -> {
                    hideProgressDialog();
                    Toast.makeText(UpdateActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close Activity
                })
                .addOnFailureListener(e -> {
                    hideProgressDialog();
                    Toast.makeText(UpdateActivity.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Show Progress Dialog
    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();
        dialog.show();
    }

    // Hide Progress Dialog
    private void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
