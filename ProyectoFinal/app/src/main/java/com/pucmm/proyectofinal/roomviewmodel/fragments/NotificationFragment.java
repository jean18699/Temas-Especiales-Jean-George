package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.adapters.NotificationAdapter;
import com.pucmm.proyectofinal.roomviewmodel.adapters.ProductCartAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Notification;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {


    private SharedPreferences sharedPreferences;

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private int mColumnCount = 1;
    private User user;
    private List<Notification> notifications = new ArrayList<>();
    private TextView txtNotificationQuantity;


    public NotificationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(User user) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable("user");
        sharedPreferences = getActivity().getSharedPreferences("notifications_"+user.getUid(), Context.MODE_PRIVATE);
        //AppExecutors.getInstance().diskIO().execute(() -> { updateCart();});

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        //sharedPreferences.edit().clear().commit(); //limpiar shared preferences
        notificationRecyclerView = view.findViewById(R.id.notificationList);
        txtNotificationQuantity = getActivity().findViewById(R.id.notification_badge);

        Button btnClear = view.findViewById(R.id.btnClearNotifications);


        if(mColumnCount <= 1)
        {
            notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            notificationRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));

        }
        loadNotifications();
        notificationRecyclerView.setAdapter(notificationAdapter);

        btnClear.setOnClickListener(v->{
            sharedPreferences.edit().clear().commit();
            notifications.clear();
            notificationAdapter.notifyDataSetChanged();
            txtNotificationQuantity.setText(String.valueOf(0));
        });


        return view;

    }



    public void loadNotifications() {
        //Accediendo a los datos guardados localmente

        notifications.clear();
        Gson gson = new Gson();
        String json;
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            System.out.println(sharedPreferences.getString(entry.getKey(),"null"));
            json = sharedPreferences.getString(entry.getKey(),"");
            Notification notification = gson.fromJson(json,Notification.class);
            notifications.add(notification);
        }
        notificationAdapter = new NotificationAdapter(getActivity(), user);
        notificationAdapter.setNotifications(notifications);
        txtNotificationQuantity.setText(String.valueOf(notifications.size()));
    }

}