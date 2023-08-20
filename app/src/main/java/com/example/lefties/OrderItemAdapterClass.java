package com.example.lefties;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItemAdapterClass extends RecyclerView.Adapter {
    LayoutInflater layoutInflater;
    Context context;
    long acctId;
    String contextClass;
    ArrayList<HashMap> orderDetails;
    DBHelper dbh;
    Boolean isRestaurant = false;

    public OrderItemAdapterClass(@NonNull Context context, ArrayList<HashMap> orderDetails, Boolean isRestaurant){
        this.context = context;
        this.orderDetails = orderDetails;
        layoutInflater = LayoutInflater.from(context);
        this.isRestaurant = isRestaurant;
        //Log.i("acct id long is ", ""+acctId);
        dbh = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView orderDate;
        TextView orderStatus;
        TextView restName;
        TextView orderTotal;
        TextView accountName;
        TextView accountAddress;
        TextView orderId;
        Button btnViewOrderItems;
        Button btnOrderRemind;
        Button btnOrderComplete;
        Button btnOrderCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.textOrderDateTime);
            orderStatus = itemView.findViewById(R.id.txtorderStatus);
            restName = itemView.findViewById(R.id.textOrderRestaurantName);
            orderId = itemView.findViewById(R.id.textBrandName);
            orderTotal = itemView.findViewById(R.id.textOrderTotal);
            accountAddress = itemView.findViewById(R.id.textOrderAddress);
            accountName = itemView.findViewById(R.id.textOrderCustomerName);
            btnOrderCancel = itemView.findViewById(R.id.btnOrderCancel);
            btnOrderComplete = itemView.findViewById(R.id.btnOrderComplete);
            btnOrderRemind = itemView.findViewById(R.id.btnOrderRemind);
            btnViewOrderItems = itemView.findViewById(R.id.btnViewOrderDetails);
            return;
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  layoutInflater.inflate(R.layout.recycler_order_details, parent, false);
        OrderItemAdapterClass.ViewHolder viewHolder = new OrderItemAdapterClass.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> orders = orderDetails.get(position);
        String restaurantNameString = orders.get("restaurant_name");
        String orderIdData = orders.get("order_id");
        String odateData = orders.get("order_date");
        String ostatusData = orders.get("order_status");
        String customerNameData = orders.get("customer_name");
        String customerAddressData= orders.get("customer_address");
        String orderTotalData = orders.get("order_total");
        String restName = orders.get("restaurant_name");
//
        ((ViewHolder)holder).orderId.setText("#00"+orderIdData);
        ((ViewHolder)holder).orderDate.setText(odateData);
        ((ViewHolder)holder).orderStatus.setText(ostatusData);
        ((ViewHolder)holder).orderTotal.setText("$ " + orderTotalData);
//
        ((ViewHolder)holder).restName.setText(restName);
        ((ViewHolder)holder).accountAddress.setText(customerAddressData);
        ((ViewHolder)holder).accountName.setText(customerNameData);

        showCorrectBtns(holder);

    }

    //to display "view order items" only in customer;
    //"cancel, complete, remind order" only in restaurant
    public void showCorrectBtns(RecyclerView.ViewHolder holder){
        if(!isRestaurant){
            ((ViewHolder)holder).btnOrderCancel.setVisibility(GONE);
            ((ViewHolder)holder).btnOrderRemind.setVisibility(GONE);
            ((ViewHolder)holder).btnOrderComplete.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
       //return 2;
       return orderDetails.size();
    }
}