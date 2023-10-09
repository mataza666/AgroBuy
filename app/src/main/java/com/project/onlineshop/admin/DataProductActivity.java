package com.project.onlineshop.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedImagePath;
    private ProductAdapter productAdapter;
    private ImageView imageViewProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_product);

        // Inisialisasi data produk
        List<Product> productList = getAllProducts();

        // Inisialisasi RecyclerView
        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Tambahkan aksi klik pada tombol "Add Product"
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memunculkan dialog form input produk
                showAddProductDialog();
            }
        });

        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                showDeleteProductDialog(product);
            }
        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        Button buttonSelectImage = dialogView.findViewById(R.id.buttonSelectImage);
        imageViewProduct = dialogView.findViewById(R.id.imageViewProduct);

        // Set click listener for the image selection button
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the image gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered product name and price
                String productName = editTextProductName.getText().toString();
                double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());

                // Create a new Product object with the entered data
                Product product = new Product(0, productName, productPrice, selectedImagePath);

                // Assuming you have a DatabaseHelper instance called "databaseHelper"
                DatabaseHelper databaseHelper = new DatabaseHelper(DataProductActivity.this);
                long result = databaseHelper.addProduct(product);
                if (result != -1) {
                    Toast.makeText(DataProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the product list in the RecyclerView
                    productAdapter.setProductList(databaseHelper.getAllProducts());
                } else {
                    Toast.makeText(DataProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Assuming you have a DatabaseHelper instance called "databaseHelper"
                DatabaseHelper databaseHelper = new DatabaseHelper(DataProductActivity.this);
                int result = databaseHelper.deleteProduct(product.getIdProduct());
                if (result > 0) {
                    Toast.makeText(DataProductActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the product list in the RecyclerView
                    productAdapter.setProductList(databaseHelper.getAllProducts());
                } else {
                    Toast.makeText(DataProductActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
                databaseHelper.close();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // Fetch data from the database and populate productList
        // Replace the following code with your actual database operations
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        productList = databaseHelper.getAllProducts();
        databaseHelper.close();

        return productList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();

                    // Load the selected image into the imageViewProduct using Picasso
                    Picasso.get().load(selectedImagePath).into(imageViewProduct);
                }
            }
        }
    }


}
