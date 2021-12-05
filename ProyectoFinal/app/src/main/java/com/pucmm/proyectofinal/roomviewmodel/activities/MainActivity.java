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
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.fragments.CategoryListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.HomeFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ShoppingCartFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.UserEditFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.UserManagerFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.model.UserChangePassword;
import com.pucmm.proyectofinal.roomviewmodel.services.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int columnsCategory = 1;
    private int columnsProducts = 1;
    private User user;
    private FirebaseAuth mAuth;
    private SearchView searchView;
    private MenuItem itemSearch;
    private MenuItem itemCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        mAuth = FirebaseAuth.getInstance();
        user = (User) getIntent().getSerializableExtra("user");
        FirebaseUser user = mAuth.getCurrentUser();
        signInAnonymously();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }


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

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if(item.getItemId() == R.id.cart){
           getSupportFragmentManager().beginTransaction()
                   .setReorderingAllowed(true)
                   .replace(R.id.content_frame, ShoppingCartFragment.newInstance(user))
                   .addToBackStack(null)
                   .commit();
       }

       if(item.getItemId() == R.id.action_search){
           searchView.setQueryHint("Search a product");
       }

        return super.onOptionsItemSelected(item);
    }




    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
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

        searchView = (SearchView) itemSearch.getActionView();

       /* searchView.setQueryHint("Search a product");*/
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}