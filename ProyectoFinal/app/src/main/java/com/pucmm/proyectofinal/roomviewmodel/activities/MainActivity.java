package com.pucmm.proyectofinal.roomviewmodel.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.MainActivityBinding;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.fragments.CategoryListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.HomeFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.NotificationFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ShoppingCartFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.UserManagerFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int columnsCategory = 2;
    private int columnsProducts = 1;
    private User user;
    private FirebaseAuth mAuth;
    private SearchView searchView;
    private MenuItem itemSearch;
    private MenuItem itemCart;
    private MenuItem itemNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        mAuth = FirebaseAuth.getInstance();
        user = (User) getIntent().getSerializableExtra("user");

        signInAnonymously();


        //REINICIAR LA BASE DE DATOS
        //getApplicationContext().deleteDatabase("e-commerce");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        CircularImageView profilePicture = headerLayout.findViewById(R.id.profilePicture);
        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
            FirebaseNetwork.obtain().download(user.getPhoto(), new NetResponse<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    profilePicture.setImageBitmap(response);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.cart_badge){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance(user))
                    .addToBackStack(null)
                    .commit();

            if(!searchView.isIconified()){
                searchView.setIconified(true);
            }
            return true;
        }


        if(item.getItemId() == R.id.action_search){
           searchView.setQueryHint("Search...");
       }

        return super.onOptionsItemSelected(item);
    }




    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> {

        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("signInAnonymously:FAILURE");
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        itemSearch = menu.findItem(R.id.action_search);
        itemCart = menu.findItem(R.id.cart);
        itemNotification = menu.findItem(R.id.notification);

        searchView = (SearchView) itemSearch.getActionView();

        itemCart.setActionView(R.layout.menu_shopping_cart_symbol);
        itemNotification.setActionView(R.layout.menu_notification_symbol);

        /**Colocando la cantidad en el carrito al simbolo del carrito**/
        FrameLayout cartBadgeLayout =  (FrameLayout) menu.findItem(R.id.cart).getActionView();
        TextView cartQuantity = (TextView) cartBadgeLayout.findViewById(R.id.cart_badge);
        SharedPreferences sharedPreferences = getSharedPreferences("cart_"+user.getUid(), Context.MODE_PRIVATE);
        cartQuantity.setText(String.valueOf(sharedPreferences.getAll().size()));

        /**Colocando la cantidad de notificaciones al simbolo de notificaciones**/
        FrameLayout notificationBadgeLayout =  (FrameLayout) menu.findItem(R.id.notification).getActionView();
        TextView notificationQuantity = (TextView) notificationBadgeLayout.findViewById(R.id.notification_badge);
        SharedPreferences notificationPreferences = getSharedPreferences("notifications_" + user.getUid(), Context.MODE_PRIVATE);
        notificationQuantity.setText(String.valueOf(notificationPreferences.getAll().size()));


        cartQuantity.setOnClickListener(v->{
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance(user))
                    .addToBackStack(null)
                    .commit();

            if(!searchView.isIconified()){
                searchView.setIconified(true);
            }
        });

        notificationQuantity.setOnClickListener(v->{
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, NotificationFragment.newInstance(user))
                    .addToBackStack(null)
                    .commit();

            if(!searchView.isIconified()){
                searchView.setIconified(true);
            }
        });


        /**Buscador de productos**/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.content_frame, ProductListFragment.newInstance(1,null,user,newText))
                        .commit();
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //Eventos al presionar un elemento del menu. Aqui alternaremos de fragmentos
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;
        switch (id){

            case R.id.menuHome:
                fragment = HomeFragment.newInstance();
                break;

            case R.id.menuCategory:
                fragment = CategoryListFragment.newInstance(columnsCategory, user);
                break;

            case R.id.menuProduct:
            fragment = ProductListFragment.newInstance(columnsProducts,null, user, null);
            break;

            case R.id.menuProfile:
                fragment = UserManagerFragment.newInstance(user);
                break;

            case R.id.menuLogout:
                finish();
                break;

        }

        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }


        drawerLayout.closeDrawer(GravityCompat.START); //Cerrando el side menu

        return true;
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            columnsCategory = 2;
            System.out.println("configuracion nueva: " + newConfig.orientation);
        }else
        {
           columnsCategory = 1;
        }
    }

}