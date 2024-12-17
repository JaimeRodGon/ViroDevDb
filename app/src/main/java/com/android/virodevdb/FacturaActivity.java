package com.android.virodevdb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacturaActivity extends AppCompatActivity {

    private int anio;
    private int mes;
    private int dia;

    private TextView tvFechaFra;

    private Button btnModificar;
    private Button btnSiguiente;
    private Button btnCancelar;

    private String strEmail;

    private Date fechaActual;

    private String fecha;
    private String mensaje="";
    private String ClienteSelect;

    //Variable arrayList
    private ArrayList<clasePerfil> alPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;




        });

        //creamos un arraylist para almacenar perfil
        alPerfil = new ArrayList<clasePerfil>();

        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");
        //Recibe arrayList articulos
        ArrayList<clasePerfil> alPerfil = (ArrayList<clasePerfil>) getIntent().getSerializableExtra("arrayPerfil");

        //Instancias
        tvFechaFra= findViewById(R.id.textViewFechaFra);
        btnModificar = findViewById(R.id.buttonModificar);
        btnSiguiente = findViewById(R.id.buttonContinuar);
        btnCancelar = findViewById(R.id.buttonCancelar);



        setup();

        }

        private void setup(){


            //Listeners Botones
            btnModificar.setOnClickListener(new listenerModificar());
            btnSiguiente.setOnClickListener(new listenerSiguiente());
            btnCancelar.setOnClickListener(new listenerCancelar());


            //Spinner Clientes
            Spinner spClientes = (Spinner) findViewById(R.id.spinnerArticulos);

            String[] datos = new String[] {"DataSystemSL", "PCshop", "Antonio Garcia", "Bulevard Cash", "Laptop Nitza"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, datos);

            spClientes.setAdapter(adapter);


            //Fecha
            mostrarFechaActual();

            spClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ClienteSelect = parent.getSelectedItem().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


         private void modificarFecha(){

             Calendar cal = Calendar.getInstance();

             anio = cal.get(Calendar.YEAR);
             mes = cal.get(Calendar.MONTH);
             dia = cal.get(Calendar.DAY_OF_MONTH);

             DatePickerDialog dpd = new DatePickerDialog(FacturaActivity.this, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                     fecha = dayOfMonth + "/" + (month+1) + "/" + year;

                     tvFechaFra.setText(fecha);

                 }
             }, anio, mes, dia);

             dpd.show();

         }

         private void mostrarFechaActual(){

             Calendar cal = Calendar.getInstance();

             fechaActual = cal.getTime();

             anio = cal.get(Calendar.YEAR);
             mes = cal.get(Calendar.MONTH);
             dia = cal.get(Calendar.DAY_OF_MONTH);

             fecha = dia+"/"+(mes+1)+"/"+anio;

             tvFechaFra.setText(fecha);


         }


        //Listener boton Modificar
        class listenerModificar implements View.OnClickListener{

            @Override
            public void onClick(View v) {

                modificarFecha();
            }
        }

        //Listener boton Siguiente
        class listenerSiguiente implements View.OnClickListener{

            @Override
            public void onClick(View v) {

                showArticulosFra();

            }
        }

    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();

    }


        //Muestra homeActivity
        private void showArticulosFra(){
            //Crea Intents para  ArticulosFra

            Intent i2 = new Intent(this, AnadirArticulosFraActivity.class);

            i2.putExtra("DatosEmail", strEmail);
            //El cliente seleccionado en spinner
            i2.putExtra("clienteFactura", ClienteSelect);
            i2.putExtra("fechaFactura", tvFechaFra.getText().toString());
            //Enviamos arrayPerfil
            i2.putExtra("arrayPerfil", alPerfil);


            startActivity(i2);

        }

        //Listener boton Cancelar
        class listenerCancelar implements View.OnClickListener{

            @Override
            public void onClick(View v) {

                showHomeActivity();
            }
        }

        //Muestra homeActivity
        private void showHomeActivity(){
            //Crea Intents para volver homeActivity

            Intent i = new Intent(this, homeActivity.class);

            i.putExtra("DatosEmail", strEmail);

            startActivity(i);

        }



}




