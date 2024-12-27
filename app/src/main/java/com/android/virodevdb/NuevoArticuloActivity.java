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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class NuevoArticuloActivity extends AppCompatActivity {

    //Variables TextEdit

    private EditText etRefArticulo;
    private EditText etNomArticulo;
    private EditText etPrecArticulo;


    //Variables String
    private String strEmail;
    private String numArticulo = "0";
    private String refArticulo;
    private String nomArticulo;
    private String precArticulo;


    private String mensaje="";


    //Variables TextView
    private TextView tvEmail;
    private TextView tvIdArticulo;

    //Variables botones
    private Button btnCancelar;
    private Button btnGuardar;

    //Double format
    private DecimalFormat formatoDbl = new DecimalFormat("#.00");
    private Double precDbl;

    private String precDbl2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuevo_articulo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        //Variables editText
        etRefArticulo =findViewById(R.id.editTexArticulorRef);
        etNomArticulo =findViewById(R.id.editTexArticuloNombre);
        etPrecArticulo =findViewById(R.id.editTexArticuloPrecio);

        //Variables TextView
        tvEmail = findViewById(R.id.textViewTitulo);
        tvIdArticulo = findViewById(R.id.TexViewtArticuloId);


        //Variables botones
        btnCancelar = findViewById(R.id.buttonAtras);
        btnGuardar = findViewById(R.id.buttonGuardar);

        //Inserta datos en textViewEamil
        this.tvEmail.setText(strEmail);

        //Setup
        setup();
    }

    //Setup
    private void setup(){

        //Listener botones
        btnCancelar.setOnClickListener(new NuevoArticuloActivity.listenerCancelar());
        btnGuardar.setOnClickListener(new NuevoArticuloActivity.listenerGuardar());

    }

    private void inciarNuevoArticulo(){

        //Inicializa FireStore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail).collection
                ("articulos").document(numArticulo);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //Si el Doc existe
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Pide que se asigne nuevo Id a articulo
                        numArticulo = devuelveId(numArticulo);

                        //Prueba de generar nuevo articulo
                        inciarNuevoArticulo();

                        //Si no existe
                    } else {
                        Log.d(TAG, "No such document");
                        showdialog();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //Boton Cerrar

    class listenerGuardar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            //Recoge datos de TextViews
            refArticulo = etRefArticulo.getText().toString();
            nomArticulo = etNomArticulo.getText().toString();
            precArticulo = etPrecArticulo.getText().toString();

            //comprueba validez double
            try {

                //usa nueva Var para guardar importe double con formato
                precDbl = Double.parseDouble(etPrecArticulo.getText().toString());

                //Aplica formato
                precDbl2 = formatoDbl.format(precDbl);

                //Modifica precio articulo
                precArticulo =(String.valueOf(precDbl2.replaceAll(",",".")));

                //Inserta artículo
                inciarNuevoArticulo();

            } catch (NumberFormatException e) {
                e.printStackTrace();
                mensaje= "Precio artículo incorrecoto (ejemplo 35.27)";
                showAlert();

            }
            //Crea Articulo

        }
    }

    //Boton Cerrar

    class listenerCancelar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showVerArticuloActivity();
        }
    }

    //Calculo nuevos IdFra
    private String devuelveId (String strNumArticulo){

        int numIdArt;
        String strIdArticulo="";
        try{
            //Parse a Int strNumArticulo
            numIdArt= Integer.valueOf(strNumArticulo);
            numIdArt++;

            //Parse a String numId
            strIdArticulo = Integer.toString(numIdArt);

        }
        catch (NumberFormatException e){
            System.out.println("Error");

        }

        //Devuelve String
        return strIdArticulo;
    }


    private void creaNuevoArticulo(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map factura datosArticulo
        Map<String, Object> datosArticulo = new HashMap<>();

        // Datos de Factura
        datosArticulo.put("idArticulos", numArticulo);
        datosArticulo.put("refArticulo", refArticulo);
        datosArticulo.put("nombreArticulo", nomArticulo);
        datosArticulo.put("precio", precArticulo);


        //Inserta datos en Factura
        db.collection("/users").document(strEmail).collection
                ("articulos").document(numArticulo).set(datosArticulo);

        //Lanza alerta
            mensaje = "ARTICULO CREADO!";
        showAlert();
        showVerArticuloActivity();


    }
    //Mustra dialogo
    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// Configura el titulo.
        alertDialogBuilder.setTitle("CONFIRMAR");

// Configura el mensaje.
        alertDialogBuilder
                .setMessage("Quieres guardar el nuevo artículo?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                        creaNuevoArticulo();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();


    }


    //Muestra verArticulo
    private void showVerArticuloActivity(){
        //Crea Intents para volver verArticulo

        Intent i = new Intent(this, VerArticuloActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }


}