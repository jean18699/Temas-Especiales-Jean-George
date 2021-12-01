package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentProductDetailBinding;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding binding;
    private ProductWithCarousel managedProduct;
    private Integer quantity;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductDetailFragment.
     * @param product
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(ProductWithCarousel product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            managedProduct = (ProductWithCarousel) getArguments().getSerializable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProductDetailBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        /* TextView txtDescription = view.findViewById(R.id.txtDetailDescription);
        TextView txtPrice = view.findViewById(R.id.txtPriceDetail);
        TextView txtQuantity = view.findViewById(R.id.txtQuantity);
        Button btnRemoveQuantity = view.findViewById(R.id.btnRemoveQuantity);
        Button btnAddQuantity = view.findViewById(R.id.btnAddQuantity);
        Button btnAddToCart = view.findViewById(R.id.btnAddCar);*/

        //Para guardar el producto localmente en nuestro carrito
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences quantityPreferences =getActivity().getSharedPreferences("quantities", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor2 = quantityPreferences.edit();

        quantity = Integer.valueOf(quantityPreferences.getString(managedProduct.product.getProductId()+"_quantity","1"));


        if (managedProduct.carousels != null && !managedProduct.carousels.isEmpty()) {
            final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showDownload();
            FirebaseNetwork.obtain().downloads(managedProduct.carousels, new NetResponse<List<Bitmap>>() {
                @Override
                public void onResponse(List<Bitmap> response) {
                    ArrayList<Drawable> drawables = new ArrayList<>();
                    for (Bitmap bitmap : response) {
                        drawables.add(new BitmapDrawable(getContext().getResources(), bitmap));
                    }
                    carouselView.accept(drawables);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressDialog.dismiss();
                }
            });
        }

       // sharedPreferences.edit().clear().commit();
        //quantityPreferences.edit().clear().commit();

        binding.txtDetailDescription.setText(managedProduct.product.getDescription());
        binding.txtPriceDetail.setText(managedProduct.product.getPrice().toString());
        binding.txtQuantity.setText(String.valueOf(quantity));


        //Eventos
        binding.btnRemoveQuantity.setOnClickListener(v->{
            if(quantity > 1){
                quantity--;
                binding.txtQuantity.setText(String.valueOf(quantity));
            }

        });

        binding.btnAddQuantity.setOnClickListener(v->{
            quantity++;
            binding.txtQuantity.setText(String.valueOf(quantity));
        });

        binding.btnAddCar.setOnClickListener(v->{

            //Convertimos el producto a json para guardarlo
            Gson gson = new Gson();
            String jsonProduct = gson.toJson(managedProduct);
            editor.putString(managedProduct.product.getProductId(), jsonProduct);
            editor.commit();

            editor2.putString(managedProduct.product.getProductId()+"_quantity", binding.txtQuantity.getText().toString());
            editor2.commit();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance(managedProduct))
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private final Consumer<ArrayList<Drawable>> carouselView = new Consumer<ArrayList<Drawable>>() {
        @Override
        public void accept(ArrayList<Drawable> drawables) {
            binding.carouselView.setSize(drawables.size());
            binding.carouselView.setCarouselViewListener((view1, position) -> {
                ImageView imageView = view1.findViewById(R.id.imageView);
                imageView.setImageDrawable(drawables.get(position));
            });
            binding.carouselView.show();
        }
    };

}
