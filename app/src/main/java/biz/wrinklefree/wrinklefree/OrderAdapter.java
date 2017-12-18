package biz.wrinklefree.wrinklefree;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import biz.wrinklefree.wrinklefree.ResponseObjects.MyOrder;

/**
 * Created by alcanzer on 12/18/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<MyOrder> orders;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookingId;
        public TextView orderId;
        public TextView serviceId;
        public TextView serviceType;
        public TextView price;
        public TextView orderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            bookingId = itemView.findViewById(R.id.bookingID);
            orderId = itemView.findViewById(R.id.orderID);
            serviceId = itemView.findViewById(R.id.serviceID);
            serviceType = itemView.findViewById(R.id.serviceStatus);
            price = itemView.findViewById(R.id.price);
            orderDate = itemView.findViewById(R.id.orderDate);
        }
    }

    public OrderAdapter(ArrayList<MyOrder> orders) {
        this.orders = orders;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderrow, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        MyOrder order = orders.get(position);
        holder.bookingId.setText(String.valueOf(order.getBookingId()));
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.serviceId.setText(getServiceType(order.getServiceTypeId()));
        holder.serviceType.setText(getStatus(order.getOrderStatusId()));
        holder.price.setText(String.valueOf(order.getOrderPrice()));
        holder.orderDate.setText(new SimpleDateFormat("dd/MM/yyyy").
                format(new Date(order.getBookingTime())));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public String getServiceType(int i){
        switch(i){
            case 1: return "Ironing";
            case 2: return "Laundry";
            default: return "N/A";
        }
    }

    public String getStatus(int i){
        switch(i){
            case 1: return "Pickup";
            case 2: return "Ordered";
            case 3: return "Pending";
            default:
                return "N/A";
        }
    }
}

