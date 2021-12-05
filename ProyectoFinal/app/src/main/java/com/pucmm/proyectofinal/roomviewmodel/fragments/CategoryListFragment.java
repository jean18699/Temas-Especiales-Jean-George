package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.activities.CategoryManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.adapters.CategoryAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.viewmodel.CategoryViewModel;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.pucmm.proyectofinal.utils.OnTouchListener;
import com.pucmm.proyectofinal.utils.OptionsMenuListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A fragment representing a list of Items.
 */
public class CategoryListFragment extends Fragment implements OnTouchListener<Category> {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryListRecyclerView;
    private User user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryListFragment newInstance(int columnCount, User user) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), user, this);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatBtn_addCategory);

        categoryListRecyclerView = view.findViewById(R.id.categoryList);

        if(mColumnCount <= 1)
        {
            categoryListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            categoryListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), mColumnCount));

        }

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), user, this);
        categoryListRecyclerView.setAdapter(categoryAdapter);

        //Pasando al fragmento de registrar categoria al clickear el boton flotante
        floatingActionButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CategoryManagerActivity.class))
        );

        retrieveTasks();

        categoryAdapter.setOptionsMenuListener((OptionsMenuListener<Category>) (view1, element) -> {
            CommonUtil.popupMenu(getContext(), view1, () -> {
                Intent intent = new Intent(getContext(), CategoryManagerActivity.class);
                intent.putExtra("category",element);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }, () -> {
                CommonUtil.alertDialog(getContext(), "Confirm dialog delete!",
                        "You are about to delete record. Do you really want to proceed?",
                        () -> delete(element));
            });
        });

        return view;
    }

    private void delete(Category element) {
        final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();
        AppExecutors.getInstance().diskIO().execute(() -> {
            for (ProductWithCarousel product : appDatabase.productDao().getProducts()) {
                if (product.product.getCategory().equals(element.getName())) {
                    Snackbar.make(getView(),"There are products already registered in this category. You must delete/edit them first.",Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    //FancyToast.makeText(getActivity().getApplicationContext(), "There are products already registered in this category. You must delete/edit them first.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    return;
                }
            }

            if (element.getImage() != null && !element.getImage().isEmpty()) {
                FirebaseNetwork.obtain().delete(element.getImage(), new NetResponse<String>() {
                    @Override
                    public void onResponse(String response) {
                        function.apply(progressDialog).apply(true).accept(element);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        function.apply(progressDialog).apply(false).accept(element);
                        FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } else {
                function.apply(progressDialog).apply(true).accept(element);
            }
        });

    }

    private final Function<KProgressHUD, Function<Boolean, Consumer<Category>>> function = progress -> success -> element -> {
        if (success) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                appDatabase.categoryDao().delete(element);
                getActivity().runOnUiThread(() -> FancyToast.makeText(getContext(), "Successfully deleted!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show());
            });
        }
        progress.dismiss();
    };

    //Cargando los datos live del view model que utilizara la lista
    private void retrieveTasks(){
        CategoryViewModel categoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryViewModel(appDatabase);
            }
        }).get(CategoryViewModel.class);

        categoryViewModel.getCategoryListLiveData().observe(getActivity(), categories -> categoryAdapter.setCategories(categories));

    }

    @Override
    public void OnClick(Category element) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.content_frame, ProductListFragment.newInstance(1, element, user, null))
                .addToBackStack(null)
                .commit();
    }
}