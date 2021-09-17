package com.pucmm.practica1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener listener;
    ArrayList<String> lenguajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Puente entre actividades
        Intent summary = new Intent(MainActivity.this,SummaryActivity.class);



        Button btnEnviar = findViewById(R.id.btnEnviar);
        Button btnLimpiar = findViewById(R.id.btnLimpiar);
        EditText editNombre = findViewById(R.id.editNombre);
        EditText editApellido = findViewById(R.id.editApellido);
        EditText editNacimiento = findViewById(R.id.editTextDate);
        Spinner spnGenero =  findViewById(R.id.spnGenero);
        RadioGroup radGroupProgramar = findViewById(R.id.radGroupProgramar);
        RadioButton radProgramarSi = findViewById(R.id.radProgramarSi);
        RadioButton radProgramarNo = findViewById(R.id.radProgramarNo);
        CheckBox chkJava = findViewById(R.id.chkJava);
        CheckBox chkCpp = findViewById(R.id.chkCPP);
        CheckBox chkPython = findViewById(R.id.chkPython);
        CheckBox chkJs = findViewById(R.id.chkJS);
        CheckBox chkCSharp = findViewById(R.id.chkCSharp);
        CheckBox chkGolang = findViewById(R.id.chkGolang);
        Toast toastNombre = Toast.makeText(getApplicationContext(), "Necesita escribir un nombre", Toast.LENGTH_SHORT);
        Toast toastApellido = Toast.makeText(getApplicationContext(), "Necesita escribir un apellido", Toast.LENGTH_SHORT);
        Toast toastLenguaje = Toast.makeText(getApplicationContext(), "Si le gusta programar, necesita indicar al menos un lenguaje", Toast.LENGTH_SHORT);




        //Para las fechas
        Calendar cal = Calendar.getInstance();
        int agno = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);



        //Valores para el dropdown de genero
        List<String> generos = new ArrayList<>();
        generos.add("Masculino");
        generos.add("Femenino");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,generos);
        spnGenero.setAdapter(adapter);

        //Al clickear el edit de fecha se creara un dialog para seleccionar nuestra fecha
        editNacimiento.setOnClickListener(v->{
            DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,agno,mes,dia);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });



        //Cuando se seleccionen los valores de la fecha en el dialog se envia al edit
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String fechaNacimiento = dayOfMonth+ "/" + month + "/" + year;
                editNacimiento.setText(fechaNacimiento);
            }
        };

        //Evento para cuando se escoja si nos gusta programar
        radGroupProgramar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton gustoProgramar = findViewById(checkedId);
                if(gustoProgramar.getText().toString().equalsIgnoreCase("No")
                && gustoProgramar.isChecked())
                {
                    chkCpp.setEnabled(false);
                    chkJava.setEnabled(false);
                    chkCSharp.setEnabled(false);
                    chkJs.setEnabled(false);
                    chkGolang.setEnabled(false);
                    chkPython.setEnabled(false);
                }else
                {
                    chkCpp.setEnabled(true);
                    chkJava.setEnabled(true);
                    chkCSharp.setEnabled(true);
                    chkJs.setEnabled(true);
                    chkGolang.setEnabled(true);
                    chkPython.setEnabled(true);
                }
            }
        });

        //Evento para validar el formulario y enviar la informacion a la actividad summary
        btnEnviar.setOnClickListener(v -> {

            if(editNombre.getText().toString().equalsIgnoreCase(""))
            {
                toastNombre.show();
            }
            else if(editApellido.getText().toString().equalsIgnoreCase(""))
            {
                toastApellido.show();
            }else
            {
                summary.putExtra("nombre",editNombre.getText().toString());
                summary.putExtra("apellido",editApellido.getText().toString());
                summary.putExtra("genero",spnGenero.getSelectedItem().toString());
                summary.putExtra("fecha",editNacimiento.getText().toString());

                RadioButton radioButton = findViewById(radGroupProgramar.getCheckedRadioButtonId());
                summary.putExtra("gustar", radioButton.getText().toString());

                lenguajes = new ArrayList<>();

                if(radioButton.getText().toString().equalsIgnoreCase("Si"))
                {
                    if(chkJava.isChecked())
                    {
                        lenguajes.add(chkJava.getText().toString());
                    }
                    if(chkCpp.isChecked())
                    {
                        lenguajes.add(chkCpp.getText().toString());
                    }
                    if(chkGolang.isChecked())
                    {
                        lenguajes.add(chkGolang.getText().toString());
                    }
                    if(chkCSharp.isChecked())
                    {
                        lenguajes.add(chkCSharp.getText().toString());
                    }
                    if(chkJs.isChecked())
                    {
                        lenguajes.add(chkJs.getText().toString());
                    }
                    if(chkPython.isChecked())
                    {
                        lenguajes.add(chkPython.getText().toString());
                    }

                    summary.putStringArrayListExtra("lenguajes",lenguajes);

                }else
                {
                    summary.putStringArrayListExtra("lenguajes",null);
                }

                if(lenguajes.isEmpty() && radioButton.getText().toString().equalsIgnoreCase("Si"))
                {
                    toastLenguaje.show();
                }else
                {
                    startActivity(summary);
                }


            }


        });

        //Reiniciando los campos
        btnLimpiar.setOnClickListener(v ->{

            editNombre.setText(null);
            editApellido.setText(null);
            editNacimiento.setText(null);
            spnGenero.setSelection(0);
            radGroupProgramar.check(R.id.radProgramarSi);

            chkCpp.setChecked(false);
            chkJava.setChecked(false);
            chkCSharp.setChecked(false);
            chkJs.setChecked(false);
            chkGolang.setChecked(false);
            chkPython.setChecked(false);

            chkCpp.setEnabled(true);
            chkJava.setEnabled(true);
            chkCSharp.setEnabled(true);
            chkJs.setEnabled(true);
            chkGolang.setEnabled(true);
            chkPython.setEnabled(true);

        });

    }





}