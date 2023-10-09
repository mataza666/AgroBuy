package com.project.onlineshop.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.onlineshop.MainActivity;
import com.project.onlineshop.R;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        LinearLayout linearLayoutDataProduct = findViewById(R.id.linearLayoutDataProduct);
        LinearLayout linearLayoutDataCustomer = findViewById(R.id.linearLayoutDataCustomer);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        // Set listener click pada menu
        linearLayoutDataProduct.setOnClickListener(this);
        linearLayoutDataCustomer.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linearLayoutDataProduct) {
            // Aksi yang akan dilakukan saat menu Data Product diklik
            Toast.makeText(this, "Halaman Data Product", Toast.LENGTH_SHORT).show();
            // Navigate to Data Product page
            Intent intent = new Intent(this, DataProductActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.linearLayoutDataCustomer) {
            // Aksi yang akan dilakukan saat menu Data Customer diklik
            Toast.makeText(this, "Halaman Data Customer", Toast.LENGTH_SHORT).show();
            // Navigate to Data Customer page
            Intent intent = new Intent(this, DataCustomerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.buttonLogout) {
            // Aksi yang akan dilakukan saat tombol Logout diklik
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            // Implement logout logic here

            // Navigate back to login page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
    }
}