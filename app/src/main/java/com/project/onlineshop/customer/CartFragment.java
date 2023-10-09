package com.project.onlineshop.customer;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Cart;
import com.project.onlineshop.model.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartClickListener {
    private List<Cart> cartList;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        // Inisialisasi RecyclerView
        RecyclerView recyclerViewCart = rootView.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Ambil daftar cart berdasarkan ID customer yang login
        cartList = getCartItems();

        // Buat instance adapter untuk daftar cart
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        cartAdapter = new CartAdapter(cartList, databaseHelper);
        cartAdapter.setOnCartClickListener(this);
        recyclerViewCart.setAdapter(cartAdapter);

        // Tambahkan aksi klik pada tombol "Place Order"
        Button buttonPlaceOrder = rootView.findViewById(R.id.buttonPlaceOrder);
        if (cartList.isEmpty()) {
            buttonPlaceOrder.setVisibility(View.GONE); // Sembunyikan tombol jika daftar cart kosong
        } else {
            buttonPlaceOrder.setVisibility(View.VISIBLE); // Tampilkan tombol jika daftar cart tidak kosong

            buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderConfirmationDialog();
                }
            });
        }

        return rootView;
    }

    private List<Cart> getCartItems() {
        // Asumsikan Anda memiliki instance DatabaseHelper untuk mengakses database
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        // Ganti customerId dengan ID customer yang login
        int customerId = getLoggedInCustomerId();

        // Ambil daftar cart berdasarkan ID customer
        List<Cart> cartList = databaseHelper.getCartItems(customerId);

        return cartList;
    }

    private int getLoggedInCustomerId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mendapatkan ID customer yang login
        int customerId = sharedPreferences.getInt("customerId", -1);

        return customerId;
    }

    @Override
    public void onCartClick(Cart cart) {
        showDeleteConfirmationDialog(cart);
    }

    private void showDeleteConfirmationDialog(Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Cart Item");
        builder.setMessage("Are you sure you want to delete this item from your cart?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCartItem(cart.getId());
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCartItem(int cartId) {
        // Asumsikan Anda memiliki instance DatabaseHelper untuk mengakses database
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        boolean success = databaseHelper.deleteCartItem(cartId);

        if (success) {
            Toast.makeText(requireContext(), "Item deleted from cart", Toast.LENGTH_SHORT).show();
            cartList = getCartItems(); // Dapatkan ulang daftar cart setelah menghapus item
            cartAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireContext(), "Failed to delete item from cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Place Order");

        // Mengambil total harga produk yang dipesan berdasarkan cart
        double totalHarga = calculateTotalPrice();
        String message = "Total Price: $" + totalHarga + "\n\n";

        // Menambahkan input alamat
        final EditText editTextAddress = new EditText(requireContext());
        editTextAddress.setHint("Enter Address");
        builder.setView(editTextAddress);

        builder.setMessage(message + "Proceed with placing the order?");
        builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mengambil alamat dari input
                String address = editTextAddress.getText().toString().trim();

                // Mengambil tanggal pesanan saat ini
                String orderDate = getCurrentDate();

                // Menempatkan pesanan
                placeOrder(address, orderDate);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;

        for (Cart cart : cartList) {
            double productPrice = getProductPrice(cart.getProductId());
            totalPrice += productPrice;
        }

        return totalPrice;
    }

    private double getProductPrice(int productId) {
        // Asumsikan Anda memiliki instance DatabaseHelper untuk mengakses database
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        // Mengambil objek Product berdasarkan ID produk
        Product product = databaseHelper.getProductById(productId);

        double productPrice = 0;
        if (product != null) {
            productPrice = product.getHargaProduct();
        }

        // Tutup koneksi ke database
        databaseHelper.close();

        return productPrice;
    }

    private String getCurrentDate() {
        // Mengambil tanggal pesanan saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void placeOrder(String address, String orderDate) {
        // Mendapatkan ID pelanggan yang sedang login
        int customerId = getLoggedInCustomerId();

        // Melakukan proses pemesanan, misalnya mengirim data ke server atau tindakan yang sesuai
        // ...

        // Simpan pesanan ke tabel "order"
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        long orderId = databaseHelper.addOrder(customerId, calculateTotalPrice(), orderDate, address);

        if (orderId != -1) {
            // Pesanan berhasil disimpan, lanjutkan dengan menyimpan daftar item pesanan ke tabel "order_item"
            boolean orderItemSaved = saveOrderItems(orderId);
            if (orderItemSaved) {
                // Item pesanan berhasil disimpan, kosongkan cart
                clearCart();
                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Gagal menyimpan item pesanan
                Toast.makeText(requireContext(), "Failed to save order items", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Gagal menyimpan pesanan
            Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean saveOrderItems(long orderId) {
        boolean success = true;

        // Mendapatkan ID pelanggan yang sedang login
        int customerId = getLoggedInCustomerId();

        // Mendapatkan daftar item cart dari database berdasarkan ID pelanggan
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<Cart> cartItems = databaseHelper.getCartItems(customerId);

        // Menyimpan setiap item cart sebagai item pesanan dalam tabel "order_item"
        for (Cart cart : cartItems) {
            int cartId = cart.getId();

            boolean itemSaved = databaseHelper.addOrderItem((int) orderId, cart.getProductId());
            if (!itemSaved) {
                success = false;
                break;
            }
        }

        return success;
    }

    private void clearCart() {
        // Membersihkan data cart
        // Misalnya, menghapus semua item dari database atau mengatur ulang variabel cartList ke list kosong
        // ...

        // Contoh tindakan: menghapus semua item dari database berdasarkan ID pelanggan
        int customerId = getLoggedInCustomerId();
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        databaseHelper.deleteCartItems(customerId);
        cartList.clear();
        cartAdapter.notifyDataSetChanged();
    }
}