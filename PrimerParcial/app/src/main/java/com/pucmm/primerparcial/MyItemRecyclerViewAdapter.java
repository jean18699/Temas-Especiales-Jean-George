package com.pucmm.primerparcial;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pucmm.primerparcial.placeholder.PlaceholderContent.PlaceholderVersion;
import com.pucmm.primerparcial.databinding.FragmentItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderVersion}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderVersion> mValues;
    private final OnTouchListener<PlaceholderVersion> mListener; //Agregado para que la lista responda a los eventos de click


    public MyItemRecyclerViewAdapter(List<PlaceholderVersion> items, OnTouchListener<PlaceholderVersion> mListener) {
        mValues = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentItemBinding binding= FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false); //Cargando la vista
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getVersionNumber());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderVersion mItem;
        OnTouchListener<PlaceholderVersion> mListener;

        public ViewHolder(FragmentItemBinding binding, OnTouchListener<PlaceholderVersion> listener) {
            super(binding.getRoot());
            mIdView = binding.versionTitle;
            mContentView = binding.versionNumber;
            mListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(mItem);
        }
    }
}