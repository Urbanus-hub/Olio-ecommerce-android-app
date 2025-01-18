package com.example.olio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Order> orders = new ArrayList<>(); // You'll need to create an Order class

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderIdText.setText("Order #" + order.getOrderId());
        holder.orderDateText.setText(order.getOrderDate());
        holder.orderAmountText.setText(String.format("$%.2f", order.getTotalAmount()));
        holder.orderStatusText.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText;
        TextView orderDateText;
        TextView orderAmountText;
        TextView orderStatusText;

        ViewHolder(View view) {
            super(view);
            orderIdText = view.findViewById(R.id.order_id);
            orderDateText = view.findViewById(R.id.order_date);
            orderAmountText = view.findViewById(R.id.order_amount);
            orderStatusText = view.findViewById(R.id.order_status);
        }
    }
}