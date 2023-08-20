package com.example.lefties;

import static android.view.View.GONE;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;



public class FoodItemAdapterClass extends RecyclerView.Adapter {
    LayoutInflater layoutInflater;
    Context context;
    String contextClass;
    String[] strArr;
    ArrayList<HashMap> foods;
    DBHelper dbh;
    long acctId;


    public FoodItemAdapterClass(@NonNull Context context, ArrayList<HashMap> foods, long acctId) {
        this.context = context;
        this.foods = foods;
        this.acctId = acctId;
        Log.i("acct id long is ", ""+acctId);
        layoutInflater = LayoutInflater.from(context);
        dbh = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgCompanyLogo;
        TextView foodName;
        TextView restaurantName;
        TextView discountedPrice;
        TextView regularPrice;
        ImageView foodImg;
        Button btnEdit;
        Button btnDelete;
        Button btnAddToCart;
        Button btnGoToRestaurant;



        // THIS MAPS ATTRIBUTES PER ITEM
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.itemFoodName);
            discountedPrice = itemView.findViewById(R.id.itemDiscountedPrice);
            regularPrice = itemView.findViewById(R.id.itemRegularPrice);
            regularPrice.setPaintFlags(regularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            foodImg = itemView.findViewById(R.id.imgFood);
            restaurantName = itemView.findViewById(R.id.itemRestaurantName);
            btnDelete = itemView.findViewById(R.id.itemBtnDelete);
            btnEdit= itemView.findViewById(R.id.itemBtnEdit);
            btnGoToRestaurant = itemView.findViewById(R.id.itemBtnGoToRestaurant);
            btnAddToCart = itemView.findViewById(R.id.btnPlaceOrder);
            return;
        }


        @Override
        public void onClick(View v) {
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view =  layoutInflater.inflate(R.layout.recyler_food_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view); // viewHolder holds the layoutInflater
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HashMap<String, String> foodItem = foods.get(position);
        String foodIdString = foodItem.get("food_id");
        long restaurantId = Long.parseLong(foodItem.get("account_id"));
        int foodId = Integer.parseInt(foodIdString);
        String name = foodItem.get("food_name");
        String regularPrice = foodItem.get("food_regular_price");
        String discountPrice = foodItem.get("food_discounted_price");
        String restaurantNameString = foodItem.get("restaurant_name");

        ((ViewHolder)holder).foodName.setText(name);
        ((ViewHolder)holder).restaurantName.setText(restaurantNameString);

        ((ViewHolder)holder).discountedPrice.setText("$ "+discountPrice);
        ((ViewHolder)holder).regularPrice.setText("$ "+regularPrice);

        showCorrectBtns(holder);

        // get image from drawable with id name
        int resID = context.getResources().getIdentifier("food_" + foodIdString , "drawable", context.getPackageName());
        ((ViewHolder)holder).foodImg.setImageResource(resID);

        ((ViewHolder)holder).btnGoToRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRestaurant(restaurantId, restaurantNameString);
            }
        });

        ((ViewHolder) holder).btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(foodId, restaurantId, restaurantNameString);

                String channelId = "cart_notification";
                String channelName = "Cart Notification";
                String contentTitle = "New Item Added to Cart";
                String contentText = "A new item has been added to your cart.";

                // Create the notification channel
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);
                }

                // Set the notification properties
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.bg_blob)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        });

//        ((ViewHolder) holder).btnAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToCart(foodId, restaurantId, restaurantNameString);
//
//                String channelId = "cart_notification";
//                String channelName = "Cart Notification";
//                String contentTitle = "New Item Added to Cart";
//                String contentText = "A new item has been added to your cart.";
//
//                // Create the notification channel
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.createNotificationChannel(channel);
//                }
//
//                // Set the notification properties
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
//                        .setSmallIcon(R.drawable.bg_blob)
//                        .setContentTitle(contentTitle)
//                        .setContentText(contentText)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                // Create the intent for launching the app when notification is clicked
//                Intent intent = new Intent(context, MainActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//
//                // Show the notification
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                notificationManager.notify(1, builder.build());
//            }
//        });


        ((ViewHolder)holder).btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("delete me");
                Boolean isDeleted = dbh.deleteRecFood(foodId);
                System.out.println(isDeleted);
                Intent i = new Intent(context, RestaurantHomeActivity.class);

                i.putExtra("acctId", acctId);
                i.putExtra("acctName", restaurantNameString);
                context.startActivity(i);
            }
        });

    }

    public void showCorrectBtns(RecyclerView.ViewHolder holder){
        contextClass = context.getClass().getSimpleName();
        if(contextClass.equals("RestaurantHomeActivity")){
            ((ViewHolder)holder).btnAddToCart.setVisibility(GONE);
            ((ViewHolder)holder).btnGoToRestaurant.setVisibility(GONE);

        }else{
            ((ViewHolder)holder).btnEdit.setVisibility(GONE);
            ((ViewHolder)holder).btnDelete.setVisibility(GONE);
            if(contextClass.equals("CustomerHomeActivity")){
                ((ViewHolder)holder).btnAddToCart.setVisibility(GONE);
            }else{
                ((ViewHolder)holder).btnGoToRestaurant.setVisibility(GONE);
            }
        }
    }


    public void createOrder(){
        // find all orders related to user Id
    }
    public void addToCart(int foodId, long restaurantId, String restaurantNameString){
        Log.i("add to cart acct id", "is "+acctId);
        dbh.addFoodToTempCart(foodId, acctId);

        Intent i = new Intent(context, CartActivity.class);
        i.putExtra("acctId", acctId);
        i.putExtra("restaurantId", restaurantId);
        i.putExtra("restaurantName", restaurantNameString);
        context.startActivity(i);
    }

    public void goToRestaurant(long restaurantId, String restaurantNameString){
        Intent intent = new Intent(context, CustomerDisplayRestaurantActivity.class);
        intent.putExtra("acctId", acctId);
        intent.putExtra("restaurantId", restaurantId);
        intent.putExtra("restaurantName", restaurantNameString);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return foods.size() ;
    }
}
