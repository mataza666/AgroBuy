package com.project.onlineshop.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Customer;

import java.util.List;

public class DataCustomerActivity extends AppCompatActivity {
    private CustomerAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_customer);

        // Inisialisasi data customer
        List<Customer> customerList = getAllCustomers();

        // Inisialisasi RecyclerView
        RecyclerView recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(this, customerList);
        recyclerViewCustomers.setAdapter(customerAdapter);
    }

    private List<Customer> getAllCustomers() {
        List<Customer> customerList = null;
        // Fetch data from the database and populate customerList
        // Replace the following code with your actual database operations
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        customerList = databaseHelper.getAllCustomers();
        databaseHelper.close();
        return customerList;
    }
}
