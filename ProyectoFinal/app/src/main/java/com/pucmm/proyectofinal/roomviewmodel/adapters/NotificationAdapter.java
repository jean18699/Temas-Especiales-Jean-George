package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ItemNotificationBinding;
import com.pucmm.proyectofinal.databinding.ItemProductCartBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.MainActivity;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductDetailFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.Notification;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.utils.CommonUtil;

import java.util.List;
import java.util.Optional;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Notification> notifications;
    private Context context;
    private SharedPreferences sharedPreferences;
    private User user;



    public NotificationAdapter(Context context, User user) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("notifications_"+user.getUid(), Context.MODE_PRIVATE);
        this.user = user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding notificationBinding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(notificationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.notification = notifications.get(position);
        holder.description.setText(holder.notification.getMessage());



    }

    @Override
    public int getItemCount() {
        if(notifications == null){
            return 0;
        }
        return notifications.size();
    }


    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private Notification notification;
        public TextView description;


        public MyViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            description = binding.notificationDescription;
        }

    }
}
