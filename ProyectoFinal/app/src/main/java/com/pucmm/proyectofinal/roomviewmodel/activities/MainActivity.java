package com.pucmm.proyectofinal.roomviewmodel.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.fragments.CategoryListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ShoppingCartFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.UserEditFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.User;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int columnsCategory = 1;
    private int columnsProducts = 1;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        user = (User) getIntent().getSerializableExtra("user");

        //loadUser();

        //REINICIAR LA BASE DE DATOS
       // getApplicationContext().deleteDatabase("e-commerce");

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        MenuItem itemCart = menu.findItem(R.id.cart);

        SearchView searchView = (SearchView) itemSearch.getActionView();

        searchView.setQueryHint("Search a product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        itemCart.setOnMenuItemClickListener(item -> {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance())
                    .commit();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }



    //Eventos al presionar un elemento del menu. Aqui alternaremos de fragmentos
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;
        switch (id){
            case R.id.menuCategory:
                fragment = CategoryListFragment.newInstance(columnsCategory);
                break;

            case R.id.menuProduct:
            fragment = ProductListFragment.newInstance(columnsProducts);
            break;

            case R.id.menuProfile:
                fragment = UserEditFragment.newInstance(user);
                break;
        }

        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }


        drawerLayout.closeDrawer(GravityCompat.START); //Cerrando el side menu

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}