package com.android.virodevdb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class FacturaActivity extends AppCompatActivity {

    private int anio;
    private int mes;
    private int dia;

    private TextView tvFechaFra, tvClienteId,tvClienteNombre, tvClienteNif, tvColeccion;

    private Button btnModificar;
    private Button btnSiguiente;
    private Button btnCancelar;
    private Button btnSiguienteCl;
    private Button btnAnteriorCl;

    private String strEmail;

    private Date fechaActual;

    private String fecha, mensaje ="", docId, docNombre, docNif;


    //Contar Subcoleciones
    private int cantidadDocumentos;
    private String strNumDocs;

    //Int de documentos
    private int numDoc=0;



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

        //Instancias
        tvFechaFra= findViewById(R.id.textViewFechaFra);

        //Instancias Cliente
        tvClienteId = findViewById(R.id.textViewClienteId);
        tvClienteNombre = findViewById(R.id.textViewClienteNombre);
        tvClienteNif = findViewById(R.id.textViewClienteNif);
        tvColeccion = findViewById(R.id.textViewColeccion);

        btnSiguienteCl = findViewById(R.id.buttonSiguiente);
        btnAnteriorCl = findViewById(R.id.buttonAnterior);
        btnModificar = findViewById(R.id.buttonModificar);
        btnSiguiente = findViewById(R.id.buttonContinuar);
        btnCancelar = findViewById(R.id.buttonCancelar);

        setup();

        }

        private void setup(){

            //Contar docs subcoleciones
            contarDocumentosSubcoleccion("/users/"+strEmail,"/clientes");

            // Llamamos a la función para obtener el documento de la subcolección
            getDoc((strEmail));  // Pasa el ID del usuario


            //Listeners Botones
            btnModificar.setOnClickListener(new listenerModificar());
            btnSiguiente.setOnClickListener(new listenerSiguiente());
            btnCancelar.setOnClickListener(new listenerCancelar());
            btnSiguienteCl.setOnClickListener(new listenerSiguienteCl());
            btnAnteriorCl.setOnClickListener(new listenerAnteriorCl());

            //Fecha
            mostrarFechaActual();


        }
    //Contar documentos subcolecciones

    private void contarDocumentosSubcoleccion(String documentId, String subcollectionName) {

        //Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Referencia al documento específico
        DocumentReference documentRef = db.collection("/users").document(strEmail);

        // Referencia a la subcolección dentro de ese documento
        CollectionReference subcollectionRef = documentRef.collection(subcollectionName);

        // Obtener todos los documentos de la subcolección
        subcollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Contar la cantidad de documentos en la subcolección
                            QuerySnapshot querySnapshot = task.getResult();
                            cantidadDocumentos = querySnapshot.size();

                            strNumDocs= Integer.toString(cantidadDocumentos);
                            tvColeccion.setText("Total clientes:"+strNumDocs);

                            // Mostrar el resultado
                            Log.d("Firestore", "Cantidad de documentos en la subcolección: " + cantidadDocumentos);
                        } else {
                            Log.e("Firestore", "Error obteniendo los documentos de la subcolección: ", task.getException());
                        }
                    }
                });
    }

    //Recoge datos del documento
    private void getDoc(String userId) {

        //Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Referencia a la subcolección 'posts' del usuario especificado
        db.collection("/users")
                .document(userId)
                .collection("/clientes")
                .orderBy("idCliente")  // Ordenar por el campo 'timestamp' (o cualquier otro campo)
                .get()  // Ejecutar la consulta
                .addOnSuccessListener(querySnapshot -> {
                    // Verificar si la consulta devolvió algún documento
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        if (querySnapshot.size() > numDoc) {
                            // Acceder al documento (según índice, ya que es base 0)
                            DocumentSnapshot thirdPostDocument = querySnapshot.getDocuments().get(numDoc);

                            // Procesar el documento (recoge campos)
                            if (thirdPostDocument != null) {
                                docId = thirdPostDocument.getString("idCliente");
                                docNombre = thirdPostDocument.getString("nombreEmpresa");
                                docNif = thirdPostDocument.getString("NIF");

                                Log.d("Firestore", "Título del tercer post: " + docNombre);


                                //Modifica datos textview
                                tvClienteId.setText(docId);
                                tvClienteNombre.setText(docNombre);
                                tvClienteNif.setText(docNif);

                            }
                        } else {
                            Log.d("Firestore", "No hay suficientes documentos en la subcolección.");
                        }
                    } else {
                        Log.d("Firestore", "No se encontraron documentos en la subcolección.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener los documentos: ", e);
                });
    }
    //Boton Siguiente
    class listenerSiguienteCl implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(numDoc != cantidadDocumentos-1){

                numDoc++;

                getDoc(strEmail);

            }

        }
    }

    //Boton Anterior
    class listenerAnteriorCl implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (numDoc > 0) {

                numDoc--;

                getDoc(strEmail);


            }

        }
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


        //Muestra homeActivity
        private void showArticulosFra(){
            //Crea Intents para  ArticulosFra

            Intent i2 = new Intent(this, AnadirArticulosFraActivity.class);
            i2.putExtra("DatosEmail", strEmail);
            //El cliente seleccionado en spinner
            i2.putExtra("fechaFactura", tvFechaFra.getText().toString());
            i2.putExtra("idCliente", docId);
            i2.putExtra("nombreCliente", docNombre);
            i2.putExtra("nifCliente", docNif);

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




