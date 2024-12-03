package com.android.virodevdb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        setup();

        }

        private void setup(){

            //Instancias
            tvFechaFra= findViewById(R.id.textViewFechaFra);
            btnModificar = findViewById(R.id.buttonModificar);
            btnSiguiente = findViewById(R.id.buttonSiguiente);
            btnCancelar = findViewById(R.id.buttonCancelar);

            //Listeners Botones
            btnModificar.setOnClickListener(new listenerModificar());
            btnSiguiente.setOnClickListener(new listenerSiguiente());
            btnCancelar.setOnClickListener(new listenerCancelar());

            mostrarFechaActual();

            spinnerCliente();

        }

        private void spinnerCliente(){

            Spinner spClientes = (Spinner) findViewById(R.id.spinnerArticulos);

            String[] datos = new String[] {"DataSystemSL", "PCshop", "Antonio Garcia", "Bulevard Cash", "Laptop Nitza"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, datos);

            spClientes.setAdapter(adapter);
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

                showClienteFra();
            }
        }

        //Muestra homeActivity
        private void showClienteFra(){
            //Crea Intents para  clienteFra

            Intent i2 = new Intent(this, ArticulosFraActivity.class);

            i2.putExtra("DatosEmail", strEmail);

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




