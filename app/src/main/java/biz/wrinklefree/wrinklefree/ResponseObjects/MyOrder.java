package biz.wrinklefree.wrinklefree.ResponseObjects;

/**
 * Created by alcanzer on 12/18/17.
 */

public class MyOrder {

    int orderId;
    int bookingId;
    int serviceTypeId;
    int orderStatusId;
    int orderPrice;
    long orderTimestamp;
    int userId;
    long bookingTime;

    public int getOrderId() {
        return orderId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public long getOrderTimestamp() {
        return orderTimestamp;
    }

    public int getUserId() {
        return userId;
    }

    public long getBookingTime() {
        return bookingTime;
    }
}
