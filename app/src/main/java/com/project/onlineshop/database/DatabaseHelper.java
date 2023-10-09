package com.project.onlineshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.onlineshop.model.Cart;
import com.project.onlineshop.model.Customer;
import com.project.onlineshop.model.OrderItem;
import com.project.onlineshop.model.Orders;
import com.project.onlineshop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "online_shop.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.CustomerEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.AdminEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.ProductEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.CartEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.OrderEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.OrderItemEntry.SQL_CREATE_TABLE);
        // Tambahkan pernyataan CREATE TABLE untuk tabel lainnya jika diperlukan

        // Menambahkan data admin default
        db.execSQL("INSERT INTO " + DatabaseContract.AdminEntry.TABLE_NAME +
                " (" + DatabaseContract.AdminEntry.COLUMN_USERNAME + ", " +
                DatabaseContract.AdminEntry.COLUMN_PASSWORD + ") VALUES ('admin', 'admin')");

        // Menambahkan data customer default
        db.execSQL("INSERT INTO " + DatabaseContract.CustomerEntry.TABLE_NAME +
                " (" + DatabaseContract.CustomerEntry.COLUMN_USERNAME + ", " +
                DatabaseContract.CustomerEntry.COLUMN_PASSWORD + ") VALUES ('user1', 'user1')");

        // Menambahkan data product default
        db.execSQL("INSERT INTO " + DatabaseContract.ProductEntry.TABLE_NAME +
                " (" + DatabaseContract.ProductEntry.COLUMN_NAME + ", " +
                DatabaseContract.ProductEntry.COLUMN_PRICE + ", " +  DatabaseContract.ProductEntry.COLUMN_IMAGE + ") VALUES ('Barang 1', '10','barang1.jpg')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.CustomerEntry.SQL_DROP_TABLE);
        db.execSQL(DatabaseContract.AdminEntry.SQL_DROP_TABLE);
        db.execSQL(DatabaseContract.ProductEntry.SQL_DROP_TABLE);
        db.execSQL(DatabaseContract.CartEntry.SQL_DROP_TABLE);
        db.execSQL(DatabaseContract.OrderEntry.SQL_DROP_TABLE);
        db.execSQL(DatabaseContract.OrderItemEntry.SQL_DROP_TABLE);
        // Tambahkan pernyataan DROP TABLE untuk tabel lainnya jika diperlukan

        onCreate(db);
    }

    public Customer getCustomerById(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseContract.CustomerEntry.COLUMN_ID,
                DatabaseContract.CustomerEntry.COLUMN_USERNAME,
                DatabaseContract.CustomerEntry.COLUMN_PASSWORD,
                DatabaseContract.CustomerEntry.COLUMN_EMAIL,
                DatabaseContract.CustomerEntry.COLUMN_NAME
        };

        String selection = DatabaseContract.CustomerEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(customerId)};

        Cursor cursor = db.query(
                DatabaseContract.CustomerEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Customer customer = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_ID));
            String username = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_EMAIL));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_NAME));

            customer = new Customer(id, username, password, email, name);
            cursor.close();
        }

        db.close();

        return customer;
    }

    public long addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CustomerEntry.COLUMN_USERNAME, customer.getUsername());
        values.put(DatabaseContract.CustomerEntry.COLUMN_PASSWORD, customer.getPassword());
        values.put(DatabaseContract.CustomerEntry.COLUMN_EMAIL, customer.getEmail());
        values.put(DatabaseContract.CustomerEntry.COLUMN_NAME, customer.getName());

        long result = db.insert(DatabaseContract.CustomerEntry.TABLE_NAME, null, values);
        db.close();

        return result;
    }

    public boolean updateCustomer(int customerId,  String newName, String newEmail,String newUsername, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CustomerEntry.COLUMN_USERNAME, newUsername);
        values.put(DatabaseContract.CustomerEntry.COLUMN_PASSWORD, newPassword);
        values.put(DatabaseContract.CustomerEntry.COLUMN_NAME, newName);
        values.put(DatabaseContract.CustomerEntry.COLUMN_EMAIL, newEmail);

        int result = db.update(DatabaseContract.CustomerEntry.TABLE_NAME, values,
                DatabaseContract.CustomerEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(customerId)});

        db.close();
        return result != -1;
    }


    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DatabaseContract.CustomerEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_PASSWORD));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_EMAIL));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerEntry.COLUMN_NAME));

                Customer customer = new Customer(id, username, password, email, name);
                customerList.add(customer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return customerList;
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProductEntry.COLUMN_NAME, product.getNamaProduct());
        values.put(DatabaseContract.ProductEntry.COLUMN_PRICE, product.getHargaProduct());
        values.put(DatabaseContract.ProductEntry.COLUMN_IMAGE, product.getGambarProduct());

        long result = db.insert(DatabaseContract.ProductEntry.TABLE_NAME, null, values);
        db.close();

        return result;
    }


    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DatabaseContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE));
                String image = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE));

                Product product = new Product(id, name, price, image);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }

    public int deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(DatabaseContract.ProductEntry.TABLE_NAME,
                DatabaseContract.ProductEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(productId)});
        db.close();
        return result;
    }

    public boolean addToCart(Product product, int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CartEntry.COLUMN_ID_CUSTOMER, customerId);
        values.put(DatabaseContract.CartEntry.COLUMN_ID_PRODUCT, product.getIdProduct());

        long result = db.insert(DatabaseContract.CartEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public List<Cart> getCartItems(int customerId) {
        List<Cart> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseContract.CartEntry.COLUMN_ID,
                DatabaseContract.CartEntry.COLUMN_ID_PRODUCT
        };

        String selection = DatabaseContract.CartEntry.COLUMN_ID_CUSTOMER + " = ?";
        String[] selectionArgs = {String.valueOf(customerId)};

        Cursor cursor = db.query(
                DatabaseContract.CartEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.CartEntry.COLUMN_ID));
                int productId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.CartEntry.COLUMN_ID_PRODUCT));

                // Mendapatkan informasi produk berdasarkan ID produk
                Product product = getProductById(productId);

                if (product != null) {
                    Cart cartItem = new Cart(id, customerId, productId);
                    cartItems.add(cartItem);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cartItems;
    }



    public boolean deleteCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id_cart = ?";
        String[] whereArgs = { String.valueOf(cartId) };
        int result = db.delete("cart", whereClause, whereArgs);
        db.close();
        return result != -1;
    }

    public long addOrder(int customerId, double totalPrice, String orderDate, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID, customerId);
        values.put(DatabaseContract.OrderEntry.COLUMN_TOTAL_PRICE, totalPrice);
        values.put(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE, orderDate);
        values.put(DatabaseContract.OrderEntry.COLUMN_ADDRESS, address);

        long result = db.insert(DatabaseContract.OrderEntry.TABLE_NAME, null, values);
        db.close();

        return result;
    }

    public boolean addOrderItem(int orderId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID, orderId);
        values.put(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID, productId);

        long result = db.insert(DatabaseContract.OrderItemEntry.TABLE_NAME, null, values);
        db.close();

        return result != -1;
    }


    public boolean deleteCartItems(int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(DatabaseContract.CartEntry.TABLE_NAME,
                DatabaseContract.CartEntry.COLUMN_ID_CUSTOMER + " = ?",
                new String[]{String.valueOf(customerId)});
        db.close();
        return result != -1;
    }

    // Metode untuk mendapatkan daftar riwayat order berdasarkan ID customer
    public List<Orders> getOrderHistory(int customerId) {
        List<Orders> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseContract.OrderEntry.COLUMN_ID,
                DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID,
                DatabaseContract.OrderEntry.COLUMN_TOTAL_PRICE,
                DatabaseContract.OrderEntry.COLUMN_ORDER_DATE,
                DatabaseContract.OrderEntry.COLUMN_ADDRESS
        };

        String selection = DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(customerId)};

        Cursor cursor = db.query(
                DatabaseContract.OrderEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ID));
                int customer_id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_CUSTOMER_ID));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_TOTAL_PRICE));
                String orderDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE));
                String address = cursor.getString(cursor.getColumnIndex(DatabaseContract.OrderEntry.COLUMN_ADDRESS));

                // Mendapatkan daftar OrderItem berdasarkan ID pesanan
                List<OrderItem> orderItemList = getOrderItems(db, orderId);

                // Membuat objek Orders dan menambahkannya ke daftar orderList
                Orders orders = new Orders(orderId, customer_id, totalPrice, orderDate, address, orderItemList);
                orderList.add(orders);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return orderList;
    }

    private List<OrderItem> getOrderItems(SQLiteDatabase db, int orderId) {
        List<OrderItem> orderItemList = new ArrayList<>();

        String[] projection = {
                DatabaseContract.OrderItemEntry.COLUMN_ID,
                DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID,
                DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID,
        };

        String selection = DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        Cursor cursor = db.query(
                DatabaseContract.OrderItemEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderItemId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_ID));
                int productId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID));

                // Buat objek OrderItem dan tambahkan ke dalam daftar
                OrderItem orderItem = new OrderItem(orderItemId, orderId, productId);
                orderItemList.add(orderItem);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return orderItemList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseContract.ProductEntry.COLUMN_ID,
                DatabaseContract.ProductEntry.COLUMN_NAME,
                DatabaseContract.ProductEntry.COLUMN_PRICE,
                DatabaseContract.ProductEntry.COLUMN_IMAGE
        };

        String selection = DatabaseContract.ProductEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(
                DatabaseContract.ProductEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            String productName = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_NAME));
            double productPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRICE));
            String productImage = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_IMAGE));
            product = new Product(productId, productName, productPrice, productImage);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return product;
    }

}