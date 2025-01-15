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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AnadirArticulosFraActivity extends AppCompatActivity {

    //Variables String
    private String strEmail;
    private String idArticulo="0";
    private String refArticulo;
    private String nomArticulo;

    private String strFechaFra;

    private String precArticulo;

    private String mensaje="";

    private String docId;

    private String strClienteFra;
    private String strClienteNombre;
    private String strClienteNif;

    //Variables TextView
    private TextView tvTitulo;
    private TextView tvIdArticulo;
    private TextView tvRefArticulo;
    private TextView tvNomArticulo;

    private TextView tvPrecArticulo;
    private TextView tvFechaFra;
    private TextView tvClienteFra;

    //Variables button

    private Button btnAnadir;
    private Button btnSiguiente;
    private Button btnAnterior;
    private Button btnCancelar;
    private Button btnGenerar;

    //Variable listView
    private ListView lvArticulos;

    //Variable arrayList
    private ArrayList<claseArticulo> alArticulos;
    private ArrayAdapter<claseArticulo> adaptador1;


    //Variables subTotal
    private String strSubTotal="0.00";
    private TextView tvSubtotal;
    private double dblSubTotal= 0.00;
    private double dblPrecio =0.00;

    //Contar Subcoleciones
    private int cantidadDocumentos;;

    private int numDoc=0;

    //Double format
    private DecimalFormat formatoDbl = new DecimalFormat("#.00");
    private Double precDbl;

    private String precDbl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir_articulos_fra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Recibe datos

        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");
        strFechaFra = recibir.getStringExtra("fechaFactura");

        //Cliente
        strClienteFra = recibir.getStringExtra("idCliente");
        strClienteNombre= recibir.getStringExtra("nombreCliente");
        strClienteNif = recibir.getStringExtra("nifCliente");


        //Instancia listViewArticulos
        lvArticulos = findViewById(R.id.listViewArticulos);

        btnAnadir =findViewById(R.id.buttonAnadir);
        btnSiguiente =findViewById(R.id.buttonSiguiente);
        btnAnterior =findViewById(R.id.buttonAnterior);
        btnGenerar =findViewById(R.id.buttonGenerar);
        btnCancelar =findViewById(R.id.buttonAtras);

        //Instancia textView
        tvTitulo = findViewById(R.id.textViewTitulo);
        tvIdArticulo = findViewById(R.id.textViewIdArticulo);
        tvRefArticulo = findViewById(R.id.textViewRefArticulo);
        tvNomArticulo = findViewById(R.id.textViewNomArticulo);
        tvPrecArticulo = findViewById(R.id.textViewPrecArticlo);

        tvClienteFra = findViewById(R.id.textViewClienteFra);
        tvFechaFra = findViewById(R.id.textViewFechaFra);
        tvSubtotal = findViewById(R.id.textViewSubTotal);

        //Insertamos datos en tv
        tvClienteFra.setText(strClienteFra);
        tvFechaFra.setText(strFechaFra);


        //creamos un arraylist para almacenar articulos
        alArticulos = new ArrayList<claseArticulo>();

        // creamos adaptador simple a arrayList
        adaptador1=new ArrayAdapter<claseArticulo>(this,android.R.layout.simple_list_item_1,alArticulos);

        //asignamos adaptador a listView
        lvArticulos.setAdapter(adaptador1);

        tvSubtotal.setText(strSubTotal);

        setup();
    }

    //Setup
    private void setup(){

        //Contar docs subcoleciones
        contarDocumentosSubcoleccion("/users/"+strEmail,"/articulos");

        // Llamamos a la función para obtener el documento de la subcolección
        getDoc((strEmail));  // Pasa el ID del usuario

        btnAnadir.setOnClickListener(new AnadirArticulosFraActivity.listenerAnadirArt());
        btnCancelar.setOnClickListener(new AnadirArticulosFraActivity.listenerCancelar());
        btnSiguiente.setOnClickListener(new AnadirArticulosFraActivity.listenerSiguienteArt());
        btnAnterior.setOnClickListener(new AnadirArticulosFraActivity.listenerAnteriorArt());
        btnGenerar.setOnClickListener(new AnadirArticulosFraActivity.listenerGenerarFra());

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

                            // Mostrar el resultado
                            Log.d("Firestore", "Cantidad de documentos en la subcolección: " + cantidadDocumentos);
                        } else {
                            Log.e("Firestore", "Error obteniendo los documentos de la subcolección: ", task.getException());
                        }
                    }
                });
    }
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
                                String docRef = thirdPostDocument.getString("refArticulo");
                                String docNombre = thirdPostDocument.getString("nombreArticulo");
                                String docPrecio = thirdPostDocument.getString("precio");
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



    //Boton Generar Factura
    class listenerGenerarFra implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showGenerarFactura();
        }
    }

    //Muestra generar factura
    private void showGenerarFactura(){

        Intent i;
        i = new Intent(this, GenerarFacturaActivity.class);

        i.putExtra("DatosEmail", strEmail);
        i.putExtra("clienteFactura", strClienteFra);
        i.putExtra("clienteNombre", strClienteNombre);
        i.putExtra("clienteNif", strClienteNif);


        i.putExtra("fechaFactura", tvFechaFra.getText().toString());
        //Envia subtotal
        i.putExtra("subTotal", tvSubtotal.getText().toString());
        //Enviamos arrays
        i.putExtra("arrayArticulos", alArticulos);

        //Genera nueva factura
        //inciarNuevaFactura();

        startActivity(i);

    }

    //Boton Añadir Articulo
    class listenerAnadirArt implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            //Recogemos resto de datos
            idArticulo = tvIdArticulo.getText().toString();
            refArticulo = tvRefArticulo.getText().toString();
            nomArticulo = tvNomArticulo.getText().toString();
            precArticulo = tvPrecArticulo.getText().toString();
            strSubTotal = tvSubtotal.getText().toString();

            //Calcula nuevo subtotal

            dblSubTotal = Double.valueOf(tvSubtotal.getText().toString());
            dblPrecio = Double.valueOf(tvPrecArticulo.getText().toString());

            dblSubTotal = dblSubTotal + dblPrecio;

            //Aplica formato
            precDbl2 = formatoDbl.format(dblSubTotal);

            //Modifica precio articulo
            strSubTotal =(String.valueOf(precDbl2.replaceAll(",",".")));

            //Inserta datos en subtotal
            tvSubtotal.setText(strSubTotal);

            //Modifica precio articulo
            //strSubTotal = tvSubtotal.getText().toString();

            alArticulos.add(new claseArticulo("Id:" + idArticulo, "Ref:" + refArticulo,
                    "Nombre:" + nomArticulo, "Precio:" + precArticulo + "€"));
            adaptador1.notifyDataSetChanged();


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