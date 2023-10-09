package com.project.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCustomer = findViewById(R.id.buttonCustomer);
        Button buttonAdmin = findViewById(R.id.buttonAdmin);

        buttonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka halaman login untuk role customer
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("role", "customer");
                startActivity(intent);
            }
        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka halaman login untuk role admin
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("role", "admin");
                startActivity(intent);
            }
        });
    }
}
