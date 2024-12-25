package com.android.virodevdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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


public class VerFacturaActivity extends AppCompatActivity {

    private String strEmail;

    //Botones
    private Button btnNueva;
    private Button btnCancelar;
    private Button btnSiguiente;
    private Button btnAnterior;

    //TextView
    private TextView tvColeccion, tvId, tvFecha, tvCliente, tvDetalle, tvSubtotal, tvIva, tvTotal;

    //Contar Subcoleciones
    private int cantidadDocumentos;
    private String strNumDocs;

    //Int de documentos
    private int numDoc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        //Instancias
        //TextView
        tvId = findViewById(R.id.textViewId);
        tvFecha = findViewById(R.id.textViewFecha);
        tvCliente = findViewById(R.id.textViewCliente);
        tvDetalle = findViewById(R.id.textViewDetalle);
        tvSubtotal = findViewById(R.id.textViewSubTotal);
        tvIva = findViewById(R.id.textViewIva);
        tvTotal = findViewById(R.id.textViewTotal);
        tvColeccion = findViewById(R.id.textViewColeccion);

        //Botones

        btnNueva = findViewById(R.id.buttonNueva);
        btnCancelar = findViewById(R.id.buttonCancelar);

        btnSiguiente =findViewById(R.id.buttonSiguiente);
        btnAnterior =findViewById(R.id.buttonAnterior);

        setup();
    }

    public void setup(){

        //Contar docs subcoleciones
        contarDocumentosSubcoleccion("/users/"+strEmail,"/facturas");

        // Llamamos a la función para obtener el documento de la subcolección
        getDoc((strEmail));  // Pasa el ID del usuario


        btnNueva.setOnClickListener(new VerFacturaActivity.listenerFacturas());
        btnCancelar.setOnClickListener(new VerFacturaActivity.listenerCancelar());
        btnSiguiente.setOnClickListener(new VerFacturaActivity.listenerSiguiente());
        btnAnterior.setOnClickListener(new VerFacturaActivity.listenerAnterior());

    }
    //Boton Siguiente Articulo
    class listenerSiguiente implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(numDoc != cantidadDocumentos-1){

                numDoc++;

                getDoc(strEmail);

            }

        }
    }

    //Boton Anterior Articulo
    class listenerAnterior implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (numDoc > 0) {

                numDoc--;

                getDoc(strEmail);

            }

        }
    }

    //Listener boton Cancelar
    class listenerCancelar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showHomeActivity();
        }
    }

    //Listener boton Facturas
    class listenerFacturas implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showFacturaActivity(strEmail);
        }
    }


    //Muestra homeActivity
    private void showHomeActivity(){
        //Crea Intents para volver homeActivity

        Intent i = new Intent(this, homeActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Muestra PerfilActivity
    private void showFacturaActivity(String strEmail){

        //Crea Intents para NuevaFacturaActivity

        Intent i = new Intent(this, FacturaActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

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
                            tvColeccion.setText("Total facturas:"+strNumDocs);

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
                .collection("/facturas")
                .orderBy("numFactura")  // Ordenar por el campo 'timestamp' (o cualquier otro campo)
                .get()  // Ejecutar la consulta
                .addOnSuccessListener(querySnapshot -> {
                    // Verificar si la consulta devolvió algún documento
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        if (querySnapshot.size() > numDoc) {
                            // Acceder al documento (según índice, ya que es base 0)
                            DocumentSnapshot thirdPostDocument = querySnapshot.getDocuments().get(numDoc);

                            // Procesar el documento (recoge campos)
                            if (thirdPostDocument != null) {
                                String docId = thirdPostDocument.getString("numFactura");
                                String docFecha = thirdPostDocument.getString("fechaFactura");
                                String docCliente = thirdPostDocument.getString("idCliente");
                                String docDetalle = thirdPostDocument.getString("detalle");
                                String docSubtotal = thirdPostDocument.getString("subtotal");
                                String docIva = thirdPostDocument.getString("ivaFactura");
                                String docTotal = thirdPostDocument.getString("totalFactura");
                                Log.d("Firestore", "Título del tercer post: " + docId);


                                //Modifica datos textview
                                tvId.setText(docId);
                                tvFecha.setText(docFecha);
                                tvCliente.setText(docCliente);
                                tvDetalle.setText(docDetalle);
                                tvSubtotal.setText("Subtotal: "+docSubtotal);
                                tvIva.setText("Iva 21%: "+docIva);
                                tvTotal.setText("Total: "+docTotal);

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
}