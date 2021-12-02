package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ItemCategoryBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.CategoryEditActivity;
import com.pucmm.proyectofinal.roomviewmodel.activities.CategoryManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.activities.LoginActivity;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.utils.CommonUtil;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private ItemCategoryBinding categoriesBinding;
    private Category category;


    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoriesBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(categoriesBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        category = categoryList.get(position);
        holder.txtCategory.setText(category.getName());
        CommonUtil.downloadImage(category.getImage(), holder.image);

        Integer id = category.getId();
        holder.editBtn.setOnClickListener(v->{
            System.out.println("mirame prro: " + id);
            Intent intent = new Intent(context, CategoryManagerActivity.class);
            intent.putExtra("categoryId",id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if(categoryList == null){
            return 0;
        }
        return categoryList.size();
    }

    public Category getCategory(int position) {
        return categoryList.get(position);
    }

    //Asignando dinamicamente a la lista
    public void setCategories(List<Category> categories) {
        categoryList = categories;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private Category category;
        private TextView txtCategory;
        private ImageView image;
        private ImageView editBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = categoriesBinding.categoryName;
            image = categoriesBinding.imgCategory;
            editBtn = categoriesBinding.configCategory;

        }
    }
}
