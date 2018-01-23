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

import static biz.wrinklefree.wrinklefree.Homepage.config;

/**
 * Created by alcanzer on 12/18/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<MyOrder> orders;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookingId;
        public TextView orderId;
        public TextView serviceId;
        public TextView orderplaced;
        public TextView price;
        public TextView items;
        public TextView orderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            bookingId = itemView.findViewById(R.id.bookingtext);
            orderId = itemView.findViewById(R.id.ordertext);
            serviceId = itemView.findViewById(R.id.servicetypetext);
            orderplaced = itemView.findViewById(R.id.orderplacedtext);
            price = itemView.findViewById(R.id.pricetext);
            items = itemView.findViewById(R.id.itemtext);
            orderDate = itemView.findViewById(R.id.deliverytext);
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
        holder.bookingId.setText("Booking ID: " + String.valueOf(order.getBookingId()));
        holder.orderId.setText("Order ID: " + String.valueOf(order.getOrderId()));
        holder.serviceId.setText(getServiceType(order.getServiceTypeId()));
        holder.orderplaced.setText("Order placed on: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date(order.getBookingTime())));
        holder.price.setText(String.valueOf(order.getOrderPrice()));
        holder.items.setText("Items: ");
        holder.orderDate.setText(new SimpleDateFormat("dd/MM/yyyy").
                format(new Date(order.getBookingTime())));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public String getServiceType(int i){

        return config.getServiceTypeList()[i-1].getServiceName();

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

