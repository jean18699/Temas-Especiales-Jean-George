package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.ItemCategoryBinding;
import com.pucmm.proyectofinal.databinding.ItemProductBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.CategoryManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.OnTouchListener;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private ItemCategoryBinding categoriesBinding;
    private final OnTouchListener<Category> mListener;



    public CategoryAdapter(Context context, OnTouchListener<Category> mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoriesBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(categoriesBinding,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category = categoryList.get(position);
        holder.txtCategory.setText(holder.category.getName());
        CommonUtil.downloadImage(holder.category.getImage(), holder.image);

        holder.editBtn.setOnClickListener(v->{
            Intent intent = new Intent(context, CategoryManagerActivity.class);
            intent.putExtra("category",holder.category);
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

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

        private TextView txtCategory;
        private ImageView image;
        private ImageView editBtn;
        public OnTouchListener<Category> mListener;
        public Category category;



        public MyViewHolder(ItemCategoryBinding binding, OnTouchListener<Category> listener) {
            super(binding.getRoot());
            txtCategory = categoriesBinding.categoryName;
            image = categoriesBinding.imgCategory;
            editBtn = categoriesBinding.configCategory;
            mListener = listener;
            binding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(category);
        }
    }
}
