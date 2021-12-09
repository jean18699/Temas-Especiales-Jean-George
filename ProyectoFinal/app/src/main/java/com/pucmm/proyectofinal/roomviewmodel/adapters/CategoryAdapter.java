package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.ItemCategoryBinding;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.OnTouchListener;
import com.pucmm.proyectofinal.utils.OptionsMenuListener;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private ItemCategoryBinding categoriesBinding;
    private final OnTouchListener<Category> mListener;
    private OptionsMenuListener optionsMenuListener;
    private User user;



    public CategoryAdapter(Context context, User user, OnTouchListener<Category> mListener) {
        this.context = context;
        this.user = user;
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

        holder.editBtn.setOnClickListener(v->{
            if (optionsMenuListener != null) {
                optionsMenuListener.onCreateOptionsMenu(holder.editBtn, holder.category);
            }
        });

        CommonUtil.downloadImage(holder.category.getImage(), holder.image);


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

    public void setOptionsMenuListener(OptionsMenuListener optionsMenuListener) {
        this.optionsMenuListener = optionsMenuListener;
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

            if(user.getRol().equals(User.ROL.CUSTOMER)){
                binding.configCategory.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(category);
        }
    }
}
