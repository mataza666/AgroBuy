package com.project.onlineshop.database;

public class DatabaseContract {
    private DatabaseContract() {}

    // Konstanta untuk nama tabel dan kolom dalam tabel Customer
    public static final class CustomerEntry {
        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_NAME = "name";

        // Pernyataan SQL untuk membuat tabel Customer
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT ," +
                        COLUMN_PASSWORD + " TEXT," +
                        COLUMN_EMAIL + " TEXT, "+
                        COLUMN_NAME + " TEXT )";

        // Pernyataan SQL untuk menghapus tabel Customer
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Konstanta untuk nama tabel dan kolom dalam tabel Admin
    public static final class AdminEntry {
        public static final String TABLE_NAME = "admin";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

        // Pernyataan SQL untuk membuat tabel Admin
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT ," +
                        COLUMN_PASSWORD + " TEXT)";

        // Pernyataan SQL untuk menghapus tabel Admin
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Konstanta untuk nama tabel dan kolom dalam tabel Product
    public static final class ProductEntry {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_ID = "id_product";
        public static final String COLUMN_NAME = "nama_product";
        public static final String COLUMN_PRICE = "harga_product";
        public static final String COLUMN_IMAGE = "gambar_product";

        // Pernyataan SQL untuk membuat tabel Product
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT," +
                        COLUMN_PRICE + " REAL," +
                        COLUMN_IMAGE + " TEXT)";

        // Pernyataan SQL untuk menghapus tabel Product
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CartEntry {
        public static final String TABLE_NAME = "cart";
        public static final String COLUMN_ID = "id_cart";
        public static final String COLUMN_ID_CUSTOMER = "id_customer";
        public static final String COLUMN_ID_PRODUCT = "id_product";

        // Pernyataan SQL untuk membuat tabel Cart
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_ID_CUSTOMER + " INTEGER," +
                        COLUMN_ID_PRODUCT + " INTEGER," +
                        "FOREIGN KEY (" + COLUMN_ID_CUSTOMER + ") REFERENCES " +
                        CustomerEntry.TABLE_NAME + "(" + CustomerEntry.COLUMN_ID + ")," +
                        "FOREIGN KEY (" + COLUMN_ID_PRODUCT + ") REFERENCES " +
                        ProductEntry.TABLE_NAME + "(" + ProductEntry.COLUMN_ID + "))";

        // Pernyataan SQL untuk menghapus tabel Product
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Konstanta untuk nama tabel dan kolom dalam tabel Order
    public static final class OrderEntry {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_ID = "id_order";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_TOTAL_PRICE = "total_price";
        public static final String COLUMN_ORDER_DATE = "order_date";
        public static final String COLUMN_ADDRESS = "address";

        // Pernyataan SQL untuk membuat tabel Order
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_CUSTOMER_ID + " INTEGER," +
                        COLUMN_TOTAL_PRICE + " REAL," +
                        COLUMN_ORDER_DATE + " TEXT," +
                        COLUMN_ADDRESS + " TEXT," +
                        "FOREIGN KEY (" + COLUMN_CUSTOMER_ID + ") REFERENCES " +
                        CustomerEntry.TABLE_NAME + "(" + CustomerEntry.COLUMN_ID + "))";

        // Pernyataan SQL untuk menghapus tabel Order
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class OrderItemEntry {
        public static final String TABLE_NAME = "order_item";
        public static final String COLUMN_ID = "id_order_item";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";

        // Pernyataan SQL untuk membuat tabel OrderItem
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_ORDER_ID + " INTEGER," +
                        COLUMN_PRODUCT_ID + " INTEGER," +
                        "FOREIGN KEY (" + COLUMN_ORDER_ID + ") REFERENCES " +
                        OrderEntry.TABLE_NAME + "(" + OrderEntry.COLUMN_ID + ")," +
                        "FOREIGN KEY (" + COLUMN_PRODUCT_ID + ") REFERENCES " +
                        ProductEntry.TABLE_NAME + "(" + ProductEntry.COLUMN_ID + "))";

        // Pernyataan SQL untuk menghapus tabel OrderItem
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}