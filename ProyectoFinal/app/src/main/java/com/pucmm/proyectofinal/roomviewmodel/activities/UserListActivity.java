package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentUserBinding;
import com.pucmm.proyectofinal.databinding.FragmentUserListBinding;
import com.pucmm.proyectofinal.roomviewmodel.adapters.UserAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.viewmodel.UserViewModel;

import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private FragmentUserListBinding binding;
    private RecyclerView userListRecyclerView;
    private UserAdapter userAdapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Conectandome a la base de datos
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        binding = FragmentUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userListRecyclerView = binding.userList;
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        userAdapter = new UserAdapter(this);
        userListRecyclerView.setAdapter(userAdapter);

        retrieveTasks();
    }

    //Cargando los datos live del view model que utilizara la lista
    private void retrieveTasks(){
        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserViewModel(appDatabase);
            }
        }).get(UserViewModel.class);

        userViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUsers(users);
            }
        });

    }

}