package com.project.onlineshop.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.onlineshop.MainActivity;
import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Customer;
import com.project.onlineshop.model.Orders;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView textViewName;
    private TextView textViewEmail;
    private Button buttonLogout;
    private RecyclerView recyclerViewOrderHistory;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Orders> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi tampilan
        textViewName = rootView.findViewById(R.id.textViewName);
        textViewEmail = rootView.findViewById(R.id.textViewEmail);
        buttonLogout = rootView.findViewById(R.id.buttonLogout);
        recyclerViewOrderHistory = rootView.findViewById(R.id.recyclerViewOrderHistory);

        // Mendapatkan ID customer yang login
        int customerId = getLoggedInCustomerId();

        // Mengakses database untuk mendapatkan data customer
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        Customer customer = databaseHelper.getCustomerById(customerId);
        databaseHelper.close();

        // Menampilkan data customer ke tampilan
        if (customer != null) {
            textViewName.setText(customer.getName());
            textViewEmail.setText("Email: " + customer.getEmail());
        }

        Button buttonEditProfile = rootView.findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Menampilkan daftar riwayat order
        showOrderHistory();

        return rootView;
    }

    private int getLoggedInCustomerId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mendapatkan ID customer yang login
        return sharedPreferences.getInt("customerId", -1);
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Profile");

        // Inflate layout untuk dialog edit profile
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);

        // Mendapatkan data customer yang sedang login
        int customerId = getLoggedInCustomerId();
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        Customer customer = databaseHelper.getCustomerById(customerId);
        databaseHelper.close();

        // Mengisi data customer ke dalam EditText pada dialog
        if (customer != null) {
            editTextName.setText(customer.getName());
            editTextEmail.setText(customer.getEmail());
            editTextUsername.setText(customer.getUsername());
            editTextPassword.setText(customer.getPassword());
        }

        builder.setView(view);

        // Menambahkan tombol Save pada dialog
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mengambil data yang telah diubah pada dialog
                String newName = editTextName.getText().toString().trim();
                String newEmail = editTextEmail.getText().toString().trim();
                String newUsername = editTextUsername.getText().toString().trim();
                String newPassword = editTextPassword.getText().toString().trim();

                // Mengupdate data customer di database
                databaseHelper.updateCustomer(customerId, newName, newEmail, newUsername, newPassword);
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                // Menampilkan data customer yang telah diupdate pada tampilan
                textViewName.setText(newName);
                textViewEmail.setText("Email: " + newEmail);
            }
        });

        // Menambahkan tombol Cancel pada dialog
        builder.setNegativeButton("Cancel", null);

        // Membuat dan menampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void logout() {
        // Menghapus data login yang disimpan dalam SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }

    private void showOrderHistory() {
        // Mendapatkan ID customer yang login
        int customerId = getLoggedInCustomerId();

        // Mengakses database untuk mendapatkan daftar order
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<Orders> orderList = databaseHelper.getOrderHistory(customerId);
        databaseHelper.close();

        // Mengatur LayoutManager dan Adapter untuk RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerViewOrderHistory.setLayoutManager(layoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(requireContext(), orderList, databaseHelper);

        recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);
    }
}
