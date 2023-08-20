package com.example.lefties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapterClass extends RecyclerView.Adapter{
//    Integer[] img;
//    String[] restname;
//    String[] item;
//    String[] itemprice;
    LayoutInflater mInflator;
    Context context;

    public CartAdapterClass(Context context, long[] cartItems, long acctId){ //,  String[] strRest, Integer[] imgData, String[] strItem, String[] strPrice){
        //img = imgData;
        this.context = context;
//        restname = strRest;
//        item = strItem;
//        itemprice = strPrice;
        mInflator = LayoutInflater.from(context);
    }

//    Integer getItem(int id){
//        return 0; //img[id];
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.recycler_cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
    }

    @Override
    public int getItemCount() {
        return 0; // img.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{
//        ImageView imageView;
//        TextView txtRestaurant;
//        TextView txtItem;
//        TextView txtItemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.imgCart);
//            txtRestaurant = itemView.findViewById(R.id.txtRest);
//            txtItem = itemView.findViewById(R.id.txtItem);
//            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //
        }
    }
}
