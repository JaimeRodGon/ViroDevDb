package com.android.virodevdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;


public class VerArticuloActivity extends AppCompatActivity {

    //Variables String
    private String strEmail;

    private String mensaje="";

    //Variables TextView

    private TextView tvIdArticulo;
    private TextView tvRefArticulo;
    private TextView tvNomArticulo;
    private TextView tvTitulo;

    private TextView tvPrecArticulo;

    private TextView tvColeccion;

    //Variables button
    private Button btnActualizar;
    private Button btnEliminar;
    private Button btnCancelar;
    private Button btnNuevo;
    private Button btnSiguiente;
    private Button btnAnterior;

    //Contar Subcoleciones
    private int cantidadDocumentos;
    private String strNumDocs;
    private String docId;
    private String docRef;
    private String docNombre;
    private String docPrecio;


    //Int de documentos
    private int numDoc=0;

    //Double format
    DecimalFormat formatoDbl = new DecimalFormat("#.00");
    private Double precDbl;


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

        //Instancias Btn
        btnCancelar =findViewById(R.id.buttonAtras);
        btnNuevo =findViewById(R.id.buttonNuevo);
        btnSiguiente =findViewById(R.id.buttonSiguiente);
        btnAnterior =findViewById(R.id.buttonAnterior);
        btnEliminar =findViewById(R.id.buttonEliminar);
        btnActualizar =findViewById(R.id.buttonActualizar);


        //Instancias TextView
        tvIdArticulo = findViewById(R.id.textViewIdArticulo);
        tvRefArticulo = findViewById(R.id.textViewRefArticulo);
        tvNomArticulo = findViewById(R.id.textViewNomArticulo);
        tvPrecArticulo = findViewById(R.id.textViewPrecArticlo);

        tvColeccion = findViewById(R.id.textViewColeccion);

        setup();
    }

    //Setup
    private void setup(){

        //Contar docs subcoleciones
        contarDocumentosSubcoleccion("/users/"+strEmail,"/articulos");

        // Llamamos a la función para obtener el documento de la subcolección
        getDoc((strEmail));  // Pasa el ID del usuario

        //Listener botones
        btnNuevo.setOnClickListener(new VerArticuloActivity.listenerNuevo());
        btnCancelar.setOnClickListener(new VerArticuloActivity.listenerCancelar());
        btnSiguiente.setOnClickListener(new VerArticuloActivity.listenerSiguienteArt());
        btnAnterior.setOnClickListener(new VerArticuloActivity.listenerAnteriorArt());
        btnEliminar.setOnClickListener(new VerArticuloActivity.listenerEliminar());
        btnActualizar.setOnClickListener(new VerArticuloActivity.listenerActualizar());


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
                            tvColeccion.setText("Total articulos:"+strNumDocs);

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
                .collection("/articulos")
                .orderBy("idArticulos")  // Ordenar por el campo 'timestamp' (o cualquier otro campo)
                .get()  // Ejecutar la consulta
                .addOnSuccessListener(querySnapshot -> {
                    // Verificar si la consulta devolvió algún documento
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        if (querySnapshot.size() > numDoc) {
                            // Acceder al documento (según índice, ya que es base 0)
                            DocumentSnapshot thirdPostDocument = querySnapshot.getDocuments().get(numDoc);

                            // Procesar el documento (recoge campos)
                            if (thirdPostDocument != null) {
                                docId = thirdPostDocument.getString("idArticulos");
                                docRef = thirdPostDocument.getString("refArticulo");
                                docNombre = thirdPostDocument.getString("nombreArticulo");
                                docPrecio = thirdPostDocument.getString("precio");
                                Log.d("Firestore", "Título del tercer post: " + docNombre);


                                //Modifica datos textview
                                tvIdArticulo.setText(docId);
                                tvRefArticulo.setText(docRef);
                                tvNomArticulo.setText(docNombre);
                                tvPrecArticulo.setText(docPrecio);
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

    //Boton Eliminar Articulo
    class listenerEliminar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showdialog();


        }
    }

    //Boton Eliminar Articulo
    class listenerActualizar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showActualizaArticulo();


        }
    }

    //Muestra
    private void showActualizaArticulo(){
        //Crea Intents para actualizar

        Intent i4 = new Intent(this, ActializaArticuloActivity.class);

        i4.putExtra("DatosEmail", strEmail);
        i4.putExtra("numArticulo", docId);
        i4.putExtra("refArticulo", docRef);
        i4.putExtra("nomArticulo", docNombre);
        i4.putExtra("precArticulo", docPrecio);

        startActivity(i4);

    }

    //Eliminar artículo
    public void eliminarArticulo(){

        //Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("/users") // Colección principal
                .document(strEmail) // Documento principal
                .collection("/articulos") // Subcolección
                .document(docId) // Documento a eliminar en la subcolección
                .delete() // Eliminar documento de la subcolección
                .addOnSuccessListener(aVoid -> {

                    // Acción si la eliminación fue exitosa
                    mensaje="El artículo ha sido elimnado";
                    showAlert();

                    //vuelve numDoc a posición 0
                    numDoc = 0;
                    //Ejecuta setup
                    setup();

                    Log.d("Firestore", "Documento de la subcolección eliminado exitosamente");
                })
                .addOnFailureListener(e -> {
                    // Acción si hubo un error
                    Log.e("Firestore", "Error al eliminar el documento de la subcolección", e);
                });


    }

    //Eliminar Articulo
    //Mustra dialogo
    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// Configura el titulo.
        alertDialogBuilder.setTitle("CONFIRMAR");

// Configura el mensaje.
        alertDialogBuilder
                .setMessage("Quieres eliminar el artículo?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                        eliminarArticulo();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }


    //Boton Siguiente Articulo
    class listenerSiguienteArt implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(numDoc != cantidadDocumentos-1){

                numDoc++;

                getDoc(strEmail);

            }

        }
    }

    //Boton Anterior Articulo
    class listenerAnteriorArt implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (numDoc > 0) {

                numDoc--;

                getDoc(strEmail);


            }

        }
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

    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();

    }
}