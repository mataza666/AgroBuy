package com.project.onlineshop.customer;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.database.DatabaseHelper;
import com.project.onlineshop.model.Cart;
import com.project.onlineshop.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Cart> cartList;
    private DatabaseHelper databaseHelper;
    private OnCartClickListener cartClickListener;

    public CartAdapter(List<Cart> cartList, DatabaseHelper databaseHelper) {
        this.cartList = cartList;
        this.databaseHelper = databaseHelper;
    }

    public void setOnCartClickListener(OnCartClickListener listener) {
        cartClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        int productId = cart.getProductId();

        // Dapatkan informasi Produk berdasarkan productId
        Product product = databaseHelper.getProductById(productId);

        if (product != null) {
            // Set data ke dalam tampilan item_cart.xml sesuai kebutuhan
            holder.textViewProductName.setText(product.getNamaProduct());
            holder.textViewProductPrice.setText(String.format("$%.2f", product.getHargaProduct()));
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewProductName;
        public TextView textViewProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Cart cart = cartList.get(position);
                if (cartClickListener != null) {
                    cartClickListener.onCartClick(cart);
                }
            }
        }
    }

    public interface OnCartClickListener {
        void onCartClick(Cart cart);
    }
}