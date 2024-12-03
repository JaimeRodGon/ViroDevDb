package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArticulosFraActivity extends AppCompatActivity {

    String strEmail;
    private String idArticulo="0";
    private String refArticulo;
    private String provArticulo;
    private String nomArticulo;
    private String descArticulo;
    private String precArticulo;
    private String stockArticulo;

    private int numIdArt;

    private ListView lvArticulos;

    private Button btnAñadir;

    private Spinner spClientes;

    //ArrayList
    private ArrayList<String> alArticulos = new ArrayList<String>();
    private ArrayList<String> alArticulosSelect = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_articulos_fra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });
        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        //Instancia listViewArticulos
        lvArticulos = findViewById(R.id.listViewArticulos);

        //Instancias botones
        btnAñadir = findViewById(R.id.buttonAñadir);

        spClientes = (Spinner) findViewById(R.id.spinnerArticulos);

        //Listeners Botones
        btnAñadir.setOnClickListener(new ArticulosFraActivity.listenerAñadir());



        insertarArticulosSpinner();
    }
    private void insertarArticulosSpinner(){
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

                        numIdArt= Integer.valueOf(idArticulo);

                            alArticulos.add(numIdArt, "Ref: "+ refArticulo + " | " + nomArticulo + " | " + precArticulo + "€" );

                            numIdArt++;

                            idArticulo = Integer.toString(numIdArt);

                            insertarArticulosSpinner();


                    } else {
                        Log.d(TAG, "No such document");
                        añadirElementosSpinner();
                        //tvNomArticulo.setText("No existe el doc");
                    }
                }
            }
        });

    }

    //Listener boton añadir articulo
    class listenerAñadir implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            añadirArticulo();
        }
    }
    //Añadir articulo
    private void añadirArticulo(){
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alArticulosSelect);
        adapter2.add(spClientes.getSelectedItem().toString());

        lvArticulos.setAdapter(adapter2);

    }

    //Añadir elementos al spiner
    private void añadirElementosSpinner(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alArticulos);

        spClientes.setAdapter(adapter);


    }

}