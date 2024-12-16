package com.example.onlineshoppingfp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);

        firestore = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();
        adapter = new MyAdapter(MainActivity.this, dataList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        loadDataFromFirestore(dialog);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(intent);
        });
    }

    private void loadDataFromFirestore(AlertDialog dialog) {
        firestore.collection("products").get()
                .addOnSuccessListener(querySnapshot -> {
                    dataList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String key = document.getId();
                        String product = document.getString("product");
                        String price = document.getString("price");
                        String quantity = document.getString("quantity");
                        String imageUrl = document.getString("image");

                        DataClass data = new DataClass(product, price, quantity, imageUrl);
                        data.setKey(key);
                        dataList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass data : dataList) {
            if (data.getDataProduct().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(data);
            }
        }
        adapter.searchDataList(searchList);
    }
}
