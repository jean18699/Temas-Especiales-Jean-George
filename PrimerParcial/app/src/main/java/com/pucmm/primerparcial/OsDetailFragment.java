package com.pucmm.primerparcial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pucmm.primerparcial.placeholder.PlaceholderContent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OsDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private PlaceholderContent.PlaceholderVersion element;

    public OsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @param element
     * @return A new instance of fragment OsDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OsDetailFragment newInstance(PlaceholderContent.PlaceholderVersion element) {
        OsDetailFragment fragment = new OsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", element);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           element = (PlaceholderContent.PlaceholderVersion) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       
        View view =  inflater.inflate(R.layout.fragment_os_detail, container, false);
        TextView txtDescription = view.findViewById(R.id.txtDescription);
        TextView txtInternalCodeName = view.findViewById(R.id.txtInternalCodeName);
        TextView txtVersionNumber = view.findViewById(R.id.txtVersionNumber);
        EditText editDate = view.findViewById(R.id.editTextDate);
        CheckBox chkSupported = view.findViewById(R.id.chkSupported);
        Button btnLink = view.findViewById(R.id.btnLink);

        txtDescription.setText(element.getDetails());
        txtInternalCodeName.setText("Internal Code:  " + element.getInternalCodeName());
        txtVersionNumber.setText("Version    " + element.getVersionNumber());
        editDate.setText(element.getReleaseDate().toString());
        editDate.setEnabled(false); //evitando que se pueda editar la fecha
        chkSupported.setChecked(element.isSupported());
        chkSupported.setEnabled(false);

        //Agregando evento de abrir link al presionar el boton
        btnLink.setOnClickListener(v -> {
            Uri link = Uri.parse(element.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, link);
            startActivity(intent);
        });

        return view;
    }
}