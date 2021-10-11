package com.pucmm.primerparcial;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.primerparcial.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment implements OnTouchListener<PlaceholderContent.PlaceholderVersion> {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<String> perms = new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS, this));
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void OnClick(PlaceholderContent.PlaceholderVersion element) {

        int orientacion = getResources().getConfiguration().orientation;
        int columnas;

        //Al clickear el menu comenzamos a preguntar por los permisos
        perms.add("android.permission.READ_EXTERNAL_STORAGE");
        perms.add("android.permission.WRITE_EXTERNAL_STORAGE");
        perms.add("android.permission.CAMERA");
        getActivity().requestPermissions(perms.toArray(new String[0]),200);

        if(checkPermision("android.permission.READ_EXTERNAL_STORAGE") && checkPermision("android.permission.WRITE_EXTERNAL_STORAGE") && checkPermision("android.permission.CAMERA")){
            if(orientacion == Configuration.ORIENTATION_PORTRAIT){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_parent, OsDetailFragment.newInstance(element))
                        .addToBackStack(null)
                        .commit();
            }else
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_details, OsDetailFragment.newInstance(element))
                        .addToBackStack(null)
                        .commit();
            }

        }else
        {
            Snackbar.make(getView(), "Faltan permisos", Snackbar.LENGTH_LONG).show();
        }

    }


    //Funcion para determinar si la aplicacion obtuvo los permisos de aquel elemento en el permissionString
    private boolean checkPermision(String permissionString)
    {
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissionString) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}