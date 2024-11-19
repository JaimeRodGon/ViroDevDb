package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;

public class NuevoArticuloActivity extends AppCompatActivity {

    //Variables TextEdit

    private EditText etRefArticulo;
    private EditText etProvArticulo;
    private EditText etNomArticulo;
    private EditText etDescArticulo;
    private EditText etPrecArticulo;
    private EditText etStockArticulo;

            
    //Variables String
    private String strEmail;
    private String numArticulo = "0";
    private String refArticulo;
    private String provArticulo;
    private String nomArticulo;
    private String descArticulo;
    private String precArticulo;
    private String stockArticulo;

    private String mensaje="";


    //Variables TextView
    private TextView tvEmail;
    private TextView tvIdArticulo;

    //Variables botones
    private Button btnCancelar;
    private Button btnGuardar;

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

        //Setup
        setup();
    }

    //Setup
    private void setup(){

        //Variables editText
        etRefArticulo =findViewById(R.id.editTexArticulorRef);
        etNomArticulo =findViewById(R.id.editTexArticuloNombre);
        etProvArticulo =findViewById(R.id.editTexArticuloProv);
        etDescArticulo =findViewById(R.id.editTextArticuloDesc);
        etPrecArticulo =findViewById(R.id.editTexArticuloPrecio);
        etStockArticulo =findViewById(R.id.editTextArticuloStock);

        //Variables TextView
        tvEmail = findViewById(R.id.textViewEmail);
        tvIdArticulo = findViewById(R.id.TexViewtArticuloId);


        //Variables botones
        btnCancelar = findViewById(R.id.buttonCancelar);
        btnGuardar = findViewById(R.id.buttonGuardar);

        //Inserta datos en textViewEamil
        this.tvEmail.setText(strEmail);

        //Listener botones
        btnCancelar.setOnClickListener(new NuevoArticuloActivity.listenerCancelar());
        btnGuardar.setOnClickListener(new NuevoArticuloActivity.listenerGuardar());

    }

    private void inciarNuevoArticulo(){

        //Inicializa FireStore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail).collection
                ("articulos").document("idArticulos").collection
                ("idArticulos").document(numArticulo);

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
                        creaIdArticulo();
                        creaNuevoArticulo();
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
            provArticulo = etProvArticulo.getText().toString();
            nomArticulo = etNomArticulo.getText().toString();
            precArticulo = etPrecArticulo.getText().toString();
            descArticulo = etDescArticulo.getText().toString();
            stockArticulo = etStockArticulo.getText().toString();

            //Crea Articulo
            inciarNuevoArticulo();
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


    private void creaIdArticulo(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map idFArticulos
        Map<String, Object> idArticulos = new HashMap<>();

        // Datos de idArticulos
        idArticulos.put("idArticulos", numArticulo);

        //Inserta datos en idArticulos
        db.collection("/users").document(strEmail).collection
                ("articulos").document("idArticulos").collection
                ("idArticulos").document(numArticulo).set(idArticulos);
        //Lanza alerta
        mensaje = "ID ARTICULO CREADO!";
        showAlert();

    }

    private void creaNuevoArticulo(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map factura datosArticulo
        Map<String, Object> datosArticulo = new HashMap<>();

        // Datos de Factura
        datosArticulo.put("idArticulos", numArticulo);
        datosArticulo.put("refArticulo", refArticulo);
        datosArticulo.put("nombreArticulo", nomArticulo);
        datosArticulo.put("proveedor", provArticulo);
        datosArticulo.put("descripcion", descArticulo);
        datosArticulo.put("precio", precArticulo);
        datosArticulo.put("stock", stockArticulo);

        //Inserta datos en Factura
        db.collection("/users").document(strEmail).collection
                ("articulos").document(numArticulo).set(datosArticulo);

        //Lanza alerta
            mensaje = "ARTICULO CREADO!";
        showAlert();


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