package com.example.olio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Product> cartItems;

    public CartAdapter(List<Product> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.titleTextView.setText(product.getTitle());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));
        Picasso.get().load(product.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.cart_item_image);
            titleTextView = view.findViewById(R.id.cart_item_title);
            priceTextView = view.findViewById(R.id.cart_item_price);
        }
    }
}