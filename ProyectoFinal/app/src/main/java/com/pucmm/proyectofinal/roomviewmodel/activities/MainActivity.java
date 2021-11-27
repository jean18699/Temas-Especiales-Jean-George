package com.pucmm.proyectofinal.roomviewmodel.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.fragments.CategoryListFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductListFragment;
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


     /*   // Cambiando el titulo de la barra de navegacion por el de la seccion en la que estamos
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }*/

        drawerLayout.closeDrawer(GravityCompat.START); //Cerrando el side menu

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}