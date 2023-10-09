package com.project.onlineshop.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.OrderItem;
import com.project.onlineshop.model.Orders;
import com.project.onlineshop.model.Product;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Orders> orderList;
    private DatabaseHelper databaseHelper;
    public OrderHistoryAdapter(Context context, List<Orders> orderList,DatabaseHelper databaseHelper) {
        this.context = context;
        this.orderList = orderList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders orders = orderList.get(position);

        holder.textViewOrderId.setText("Order : ");
        holder.textViewTotalPrice.setText("Total Price: $" + orders.getTotalPrice());
        holder.textViewOrderDate.setText("Order Date: " + orders.getOrderDate());

        List<OrderItem> orderItemList = orders.getOrderItemList();

        StringBuilder productsText = new StringBuilder();
        for (OrderItem orderItem : orderItemList) {
            int productId = orderItem.getProductId();

            // Mendapatkan informasi produk berdasarkan ID produk
            Product product = databaseHelper.getProductById(productId);

            if (product != null) {
                String productName = product.getNamaProduct();

                productsText.append("- ").append(productName).append("\n");
            }
        }

        holder.textViewProductList.setText("Daftar Barang yang dipesan : \n" +productsText.toString());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderId;
        public TextView textViewTotalPrice;
        public TextView textViewOrderDate;
        public TextView textViewProductList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewProductList= itemView.findViewById(R.id.textViewProductList);
        }
    }


}
