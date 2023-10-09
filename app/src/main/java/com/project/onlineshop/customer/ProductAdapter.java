package com.project.onlineshop.customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.onlineshop.R;
import com.project.onlineshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context context;
    private List<Product> productList;
    private List<Product> filteredList;
    private OnProductClickListener productClickListener;
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredList.get(position);

        holder.textViewProductName.setText(product.getNamaProduct());
        holder.textViewProductPrice.setText(String.format("$%.2f", product.getHargaProduct()));

        // Load image using Picasso or any other image loading library
        Picasso.get()
                .load(product.getGambarProduct())
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(holder.imageViewProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productClickListener != null) {
                    productClickListener.onProductClick(product);
                }
            }
        });
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
    public void setOnProductClickListener(OnProductClickListener listener) {
        this.productClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        this.filteredList = new ArrayList<>(productList);
        notifyDataSetChanged();
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

    public void filterList(String keyword) {
        filteredList.clear();
        if (TextUtils.isEmpty(keyword)) {
            filteredList.addAll(productList);
        } else {
            keyword = keyword.toLowerCase().trim();
            for (Product product : productList) {
                if (product.getNamaProduct().toLowerCase().contains(keyword)) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}

