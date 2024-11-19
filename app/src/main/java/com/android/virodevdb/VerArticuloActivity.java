package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class VerArticuloActivity extends AppCompatActivity {

    //Variables String
    private String strEmail;
    private String idArticulo="0";
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
    private TextView tvRefArticulo;
    private TextView tvNomArticulo;
    private TextView tvProvArticulo;
    private TextView tvDescArticulo;
    private TextView tvPrecArticulo;
    private TextView tvStockArticulo;

    //Variables button
    private Button btnActualizar;
    private Button btnEliminar;
    private Button btnCancelar;
    private Button btnNuevo;
    private Button btnSiguiente;
    private Button btnAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_articulo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recibe datosEmail datosPass
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        setup();
    }

    //Setup
    private void setup(){

        btnCancelar =findViewById(R.id.buttonCancelar);
        btnNuevo =findViewById(R.id.buttonNuevo);
        btnSiguiente =findViewById(R.id.buttonSiguiente);
        btnAnterior =findViewById(R.id.buttonAnterior);

        tvEmail = findViewById(R.id.textViewEmail);
        tvIdArticulo = findViewById(R.id.textViewIdArticulo);
        tvRefArticulo = findViewById(R.id.textViewRefArticulo);
        tvNomArticulo = findViewById(R.id.textViewNomArticulo);
        tvProvArticulo = findViewById(R.id.textViewProv);
        tvDescArticulo = findViewById(R.id.textViewDescArticulo);
        tvPrecArticulo = findViewById(R.id.textViewPrecArticlo);
        tvStockArticulo = findViewById(R.id.textViewStock);


        //Recibe datos variables AuthActivity
        this.tvEmail.setText(strEmail);

        mostrarArticulo();


        //Listener botones
        btnNuevo.setOnClickListener(new VerArticuloActivity.listenerNuevo());
        btnCancelar.setOnClickListener(new VerArticuloActivity.listenerCancelar());
        btnSiguiente.setOnClickListener(new VerArticuloActivity.listenerSiguienteArt());
        btnAnterior.setOnClickListener(new VerArticuloActivity.listenerAnteriorArt());



    }
    private void mostrarArticulo(){
        //Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Busca el documento 0 de articulos (modificar para ir buscando)
        DocumentReference docRef = db.collection("/users").document(strEmail).collection("articulos")
                .document(idArticulo);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //Si el Doc existe
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Crea hashMap para almacenar document
                        Map<String, Object> mapArticulos = new HashMap<>();
                        mapArticulos = document.getData();

                        //Recogemos los datos del HashMap en Strings
                        idArticulo = mapArticulos.get("idArticulos").toString();
                        refArticulo = mapArticulos.get("refArticulo").toString();
                        nomArticulo = mapArticulos.get("nombreArticulo").toString();
                        provArticulo = mapArticulos.get("proveedor").toString();
                        descArticulo = mapArticulos.get("descripcion").toString();
                        precArticulo = mapArticulos.get("precio").toString();
                        stockArticulo = mapArticulos.get("stock").toString();


                        //Llama a función para insertar datos en textViews
                        insertaDatosTextViews();

                    } else {
                        Log.d(TAG, "No such document");
                        tvNomArticulo.setText("No existe el doc");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    tvNomArticulo.setText("Fallo en get");
                }
            }
        });

    }

    //Inserta los datos en TextViews
    private void insertaDatosTextViews(){
        tvIdArticulo.setText("Id:"+idArticulo);
        tvRefArticulo.setText("Ref.Articulo:"+refArticulo);
        tvNomArticulo.setText("Nombre:"+nomArticulo);
        tvProvArticulo.setText("Proveedor:"+provArticulo);
        tvDescArticulo.setText("Descripción:"+descArticulo);
        tvPrecArticulo.setText("Precio:"+precArticulo);
        tvStockArticulo.setText("Stock:"+stockArticulo);

    }

    //Boton Siguiente Articulo

    class listenerSiguienteArt implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            siguienteArticulo();
        }
    }

    //Siguiente Articulo
    private void siguienteArticulo(){

        int numIdArt;

        try{
            //Parse a Int strNumArticulo
            numIdArt= Integer.valueOf(idArticulo);
            numIdArt++;

            //Parse a String numId
            idArticulo = Integer.toString(numIdArt);

        }
        catch (NumberFormatException e){
            System.out.println("Error");
            idArticulo="0";

        }

        mostrarArticulo();

    }

    //Boton Anterior Articulo

    class listenerAnteriorArt implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            anteriorArticulo();
        }
    }

    //Anterior Articulo
    private void anteriorArticulo(){

        int numIdArt;

        try{
            //Parse a Int strNumArticulo
            numIdArt= Integer.valueOf(idArticulo);
            if(numIdArt > 0){
                numIdArt--;
                //Parse a String numId
                idArticulo = Integer.toString(numIdArt);
            }
            else{

            }

        }
        catch (NumberFormatException e){
            System.out.println("Error");
            idArticulo="0";

        }

        mostrarArticulo();

    }


    //Muestra ArticulosActivity
    private void showNuevoArticuloActivity(String strEmail){

        //Crea Intents para NuevaFacturaActivity

        Intent i = new Intent(this, NuevoArticuloActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Boton Nuevo

    class listenerNuevo implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showNuevoArticuloActivity(strEmail);
        }
    }

    //Boton Cerrar

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