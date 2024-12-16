package com.example.onlineshoppingfp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils; // Fixed import
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    // UI Elements
    EditText productName, productPrice, productQty;
    Button uploadButton;

    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize UI elements
        productName = findViewById(R.id.uploadProduct);
        productPrice = findViewById(R.id.uploadPrice);
        productQty = findViewById(R.id.uploadQty);
        uploadButton = findViewById(R.id.uploadButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Button Click Event
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProduct();
            }
        });
    }

    private void uploadProduct() {
        // Get input from fields
        String product = productName.getText().toString().trim();
        String price = productPrice.getText().toString().trim();
        String quantity = productQty.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(product)) {
            productName.setError("Product name is required");
            return;
        }
        if (TextUtils.isEmpty(price)) {
            productPrice.setError("Product price is required");
            return;
        }
        if (TextUtils.isEmpty(quantity)) {
            productQty.setError("Product quantity is required");
            return;
        }

        // Data to be saved
        Map<String, Object> productData = new HashMap<>();
        productData.put("productName", product);
        productData.put("productPrice", price);
        productData.put("productQuantity", quantity);

        // Firestore Collection Reference
        CollectionReference collectionRef = db.collection("Products");

        // Add product data to Firestore
        collectionRef.add(productData)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UploadActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to clear input fields
    private void clearFields() {
        productName.setText("");
        productPrice.setText("");
        productQty.setText("");
    }
}
