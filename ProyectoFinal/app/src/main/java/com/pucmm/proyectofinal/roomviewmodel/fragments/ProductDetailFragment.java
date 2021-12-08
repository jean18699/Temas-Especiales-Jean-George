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
import com.pucmm.proyectofinal.roomviewmodel.model.Notification;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

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
    private User user;
    private TextView txtNotifQuantity;


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
    public static ProductDetailFragment newInstance(ProductWithCarousel product, User user) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            managedProduct = (ProductWithCarousel) getArguments().getSerializable("product");
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProductDetailBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //Para guardar el producto localmente en nuestro carrito
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("cart_"+user.getUid(), Context.MODE_PRIVATE);
        SharedPreferences quantityPreferences =getActivity().getSharedPreferences("quantities_"+user.getUid(), Context.MODE_PRIVATE);
        SharedPreferences notificationPreferences = getActivity().getSharedPreferences("notifications_" + user.getUid(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor2 = quantityPreferences.edit();
        SharedPreferences.Editor editorNotif = notificationPreferences.edit();

        quantity = Integer.valueOf(quantityPreferences.getString(managedProduct.product.getProductId()+"_quantity","1"));
        txtNotifQuantity = getActivity().findViewById(R.id.notification_badge);

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



            Notification notification = new Notification("Se han agregado productos al carrito");
            Gson notifGson = new Gson();
            String jsonNotif = notifGson.toJson(notification);
            editorNotif.putString(String.valueOf(notificationPreferences.getAll().size()+1), jsonNotif);
            editorNotif.commit();
            txtNotifQuantity.setText(String.valueOf(notificationPreferences.getAll().size()));


            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance(managedProduct, user))
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
