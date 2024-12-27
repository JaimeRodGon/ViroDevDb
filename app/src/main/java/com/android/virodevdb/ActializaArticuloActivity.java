package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ActializaArticuloActivity extends AppCompatActivity {


    private String mensaje, strEmail, strNumArticulo, strRefArticulo, strNomArticulo,strPrecArticulo;

    private TextView tvNumArticulo;
    private EditText etRefArticulo;
    private EditText etNomArticulo;
    private EditText etPrecArticulo;

    private Button btnGuardar;
    private Button btnCancelar;

    //Double format
    DecimalFormat formatoDbl = new DecimalFormat("#.00");
    private Double precDbl;
    private String precDbl2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actializa_articulo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Instancias
        //textview
        tvNumArticulo = findViewById(R.id.textViewNumArt);

        //edittext
        etRefArticulo = findViewById(R.id.editTextRef);
        etNomArticulo = findViewById(R.id.editTextNom);
        etPrecArticulo = findViewById(R.id.editTextPrec);

        //botones
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);


        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");
        strNumArticulo = recibir.getStringExtra("numArticulo");
        strRefArticulo = recibir.getStringExtra("refArticulo");
        strNomArticulo = recibir.getStringExtra("nomArticulo");
        strPrecArticulo = recibir.getStringExtra("precArticulo");


        //Insertamos datos en textView
        tvNumArticulo.setText(strNumArticulo);
        etRefArticulo.setText(strRefArticulo);
        etNomArticulo.setText(strNomArticulo);
        etPrecArticulo.setText(strPrecArticulo);

        setup();
    }


    public void setup(){

        //Listeners Botones
        btnGuardar.setOnClickListener(new ActializaArticuloActivity.listenerGuardar());
        btnCancelar.setOnClickListener(new ActializaArticuloActivity.listenerCancelar());

    }

    //Mustra dialogo
    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// Configura el titulo.
        alertDialogBuilder.setTitle("CONFIRMAR");

// Configura el mensaje.
        alertDialogBuilder
                .setMessage("Quieres actualizar el artículo?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                        ActualizaArticulo();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }


    //Listener boton Cancelar
    class listenerCancelar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showVerArticulos();
        }
    }


    //Muestra verArticulos
    private void showVerArticulos(){
        //Crea Intents para volver VerArticulo

        Intent i = new Intent(this, VerArticuloActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Boton Cerrar

    class listenerGuardar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            //Recoge datos de TextViews
            strRefArticulo = etRefArticulo.getText().toString();
            strNomArticulo = etNomArticulo.getText().toString();
            strPrecArticulo = etPrecArticulo.getText().toString();

            //comprueba validez double
            try {

                //usa nueva Var para guardar importe double con formato
                precDbl = Double.parseDouble(strPrecArticulo);

                //Aplica formato
                precDbl2 = formatoDbl.format(precDbl);

                //Modifica precio articulo
                strPrecArticulo =(String.valueOf(precDbl2.replaceAll(",",".")));

                //Muestra dialogo
                showdialog();

            } catch (NumberFormatException e) {
                e.printStackTrace();
                mensaje= "Precio artículo incorrecoto (ejemplo 35.27)";
                showAlert();

            }
            //Crea Articulo

        }
    }

    private void ActualizaArticulo(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map factura datosArticulo
        Map<String, Object> datosArticulo = new HashMap<>();

        // Datos de Factura
        datosArticulo.put("idArticulos", strNumArticulo);
        datosArticulo.put("refArticulo", strRefArticulo);
        datosArticulo.put("nombreArticulo", strNomArticulo);
        datosArticulo.put("precio", strPrecArticulo);


        //Inserta datos en Factura
        db.collection("/users").document(strEmail).collection
                ("articulos").document(strNumArticulo).set(datosArticulo);

        //Lanza alerta
        mensaje = "ARTICULO ACTUALIZADO!";
        showAlert();
        showVerArticulos();


    }
    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();

    }




}