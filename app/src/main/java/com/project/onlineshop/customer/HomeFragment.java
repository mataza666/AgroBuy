package com.project.onlineshop.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.onlineshop.LoginActivity;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.databinding.FragmentHomeBinding;
import com.project.onlineshop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {
    private FragmentHomeBinding binding;
    private int customerId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false); // Gunakan binding untuk menggantikan inflate
        View rootView = binding.getRoot();

        // Mendapatkan ID pelanggan yang login dari SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customerId",-1);

        // Inisialisasi data produk
        List<Product> productList = getAllProducts();

        // Inisialisasi RecyclerView
        RecyclerView recyclerViewProducts = binding.recyclerViewProducts;
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Buat instance dari adapter produk
        ProductAdapter productAdapter = new ProductAdapter(requireContext(), productList);
        recyclerViewProducts.setAdapter(productAdapter);

        setupSearch(productAdapter);
        productAdapter.setOnProductClickListener(this);

        return rootView;
    }

    private List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // Fetch data from the database and populate productList
        // Replace the following code with your actual database operations
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        productList = databaseHelper.getAllProducts();
        databaseHelper.close();

        return productList;
    }

    private void setupSearch(ProductAdapter productAdapter) {
        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString().trim();
                productAdapter.filterList(keyword);
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Hapus referensi binding saat view dihancurkan
    }

    @Override
    public void onProductClick(Product product) {
        showAddToCartDialog(product);
    }

    private void showAddToCartDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add to Cart");
        builder.setMessage("Are you sure you want to add this product to your cart?");
        builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addToCart(product);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addToCart(Product product) {
        // Assume you have an instance of DatabaseHelper to access the database
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        if (customerId != -1) {
            boolean success = databaseHelper.addToCart(product, customerId);

            if (success) {
                Toast.makeText(requireContext(), "Product successfully added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to add product to cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Invalid customer ID", Toast.LENGTH_SHORT).show();
        }
    }

}
