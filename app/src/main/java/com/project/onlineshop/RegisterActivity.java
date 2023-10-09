package com.project.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Customer;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi elemen tampilan
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        Button buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        // Menangani klik tombol Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Menangani klik tombol Kembali ke Login
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
    }

    private void registerUser() {
        // Mendapatkan input dari pengguna
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        // Validasi input
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Membuat instance DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Membuat objek Customer baru
        Customer customer = new Customer(0, username, password, email, name);

        // Menyimpan data customer ke database
        long customerId = databaseHelper.addCustomer(customer);

        if (customerId != -1) {
            // Registrasi berhasil
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

            // Kembali ke halaman login setelah registrasi
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Menutup activity register
        } else {
            // Registrasi gagal
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void backToLogin() {
        // Kembali ke halaman login
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra("role", "customer");
        startActivity(intent);
    }
}
