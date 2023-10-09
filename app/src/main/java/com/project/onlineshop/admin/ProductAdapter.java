package com.project.onlineshop.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context context;
    private List<Product> productList;
    private OnProductClickListener onProductClickListener; // Tambahkan properti OnProductClickListener

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewProductName.setText(product.getNamaProduct());
        holder.textViewProductPrice.setText(String.format("$%.2f", product.getHargaProduct()));

        // Load image using Picasso library
        Picasso.get()
                .load(product.getGambarProduct())
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(holder.imageViewProduct);

        // Atur klik pada elemen produk
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProductClickListener != null) {
                    onProductClickListener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.onProductClickListener = listener;
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewProductName;
        public TextView textViewProductPrice;
        public ImageView imageViewProduct;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.tvProductName);
            textViewProductPrice = itemView.findViewById(R.id.tvProductPrice);
            imageViewProduct = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
