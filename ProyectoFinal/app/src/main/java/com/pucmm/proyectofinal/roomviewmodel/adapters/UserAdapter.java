package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.ActivityLoginBinding;
import com.pucmm.proyectofinal.databinding.FragmentUserBinding;
import com.pucmm.proyectofinal.databinding.FragmentUserListBinding;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;
    private Context context;
    private FragmentUserBinding usersBinding;


    public UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        usersBinding = FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(usersBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user = userList.get(position);
      //  holder.username.setText(user.getUsername());

    }

    @Override
    public int getItemCount() {
        if(userList == null){
            return 0;
        }
        return userList.size();
    }

    //Asignando dinamicamente a la lista
    public void setUsers(List<User> users) {
        userList = users;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = usersBinding.username;
        }
    }
}
