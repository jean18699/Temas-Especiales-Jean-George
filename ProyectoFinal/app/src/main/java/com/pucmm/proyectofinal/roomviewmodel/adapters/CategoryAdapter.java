package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.FragmentCategoryBinding;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private FragmentCategoryBinding categoriesBinding;


    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoriesBinding = FragmentCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(categoriesBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Category category = categoryList.get(position);
        holder.category.setText(category.getName());

    }

    @Override
    public int getItemCount() {
        if(categoryList == null){
            return 0;
        }
        return categoryList.size();
    }

    //Asignando dinamicamente a la lista
    public void setCategories(List<Category> categories) {
        categoryList = categories;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView category;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category = categoriesBinding.categoryName;
        }
    }
}
