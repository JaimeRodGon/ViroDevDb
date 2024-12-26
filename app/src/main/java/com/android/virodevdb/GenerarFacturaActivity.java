package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GenerarFacturaActivity extends AppCompatActivity {

    private String strNumFra ="0", strEmail, strClienteFra, strFechaFra, mensaje, strSubTotal="0.00",
            strClienteNombre, strClienteNif, strIva, strTotal;

    private double dblSubTotal, dblIva, dblTotal;

    private TextView tvSubTotal, tvFecha, tvNumFra, tvNombre, tvApellidos, tvDniCif, tvEmail, tvDireccion, tvCp, tvTelefono,
    tvIdCliente, tvNombreCliente, tvNifCliente, tvIva, tvTotal;

    private Button btnAtras, btnGenerar;

    //Variable listView
    private ListView lvArticulos;

    //Variable arrayList
    private ArrayList<claseArticulo> alArticulos;
    private ArrayAdapter<claseArticulo> adaptador1;

    //String de articulos usaremos para almacenar en fireBase
    private String strAlArticulos;

    //Variables para de perfil
    private String miNombre;
    private String misApellidos;
    private String miDniCif;
    private String miDireccion;
    private String miCp;
    private String miTelefono;


    DecimalFormat formatoDbl = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generar_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DecimalFormat formatoDbl = new DecimalFormat("#.00");

        //Recibe datos
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        strFechaFra = recibir.getStringExtra("fechaFactura");
        strSubTotal = recibir.getStringExtra("subTotal");

        strClienteFra = recibir.getStringExtra("clienteFactura");
        strClienteNombre = recibir.getStringExtra("clienteNombre");
        strClienteNif = recibir.getStringExtra("clienteNif");



        tvSubTotal =findViewById(R.id.textViewSubTotal);
        tvFecha =findViewById(R.id.textViewFecha);
        tvNumFra =findViewById(R.id.textViewNumFra);

        tvIva = findViewById(R.id.textViewIva);
        tvTotal = findViewById(R.id.textViewTotal);


        //Cliente

        tvIdCliente = findViewById(R.id.textViewIdCliente);
        tvNombreCliente = findViewById(R.id.textViewNombreCliente);
        tvNifCliente = findViewById(R.id.textViewNifCliente);


        btnAtras = findViewById(R.id.buttonAtras);
        btnGenerar = findViewById(R.id.buttonGenerar);

        //Instancia listViews
        lvArticulos = findViewById(R.id.listViewArticulos);

        //creamos un arraylist para almacenar articulos
        alArticulos = new ArrayList<claseArticulo>();


        //Recibe arrayList articulos
        ArrayList<claseArticulo> alArticulos = (ArrayList<claseArticulo> ) getIntent().getSerializableExtra("arrayArticulos");

        //Pasa a string alArticulos para almacenar en fireBase
        strAlArticulos = alArticulos.toString();

        // creamos adaptador simple a arrayList
        adaptador1=new ArrayAdapter<claseArticulo>(this,android.R.layout.simple_list_item_1,alArticulos);


        //asignamos adaptador a listView
        lvArticulos.setAdapter(adaptador1);

        //Variables View
        tvEmail = findViewById(R.id.textViewTitulo);
        tvNombre = findViewById(R.id.textViewNombre);
        tvApellidos = findViewById(R.id.textViewApellidos);
        tvDniCif = findViewById(R.id.textViewDniCif);
        tvDireccion = findViewById(R.id.textViewDireccion);
        tvCp = findViewById(R.id.textViewCp);
        tvTelefono = findViewById(R.id.textViewTelefono);

        setup();
    }

    //setup
    public void setup(){

        CalculaTotalFactura();


        btnAtras.setOnClickListener(new GenerarFacturaActivity.listenerAtras());
        btnGenerar.setOnClickListener(new GenerarFacturaActivity.listenerGenerar());

    }

    //Calcular Iva y TotalFra
    private void CalculaTotalFactura() {

        dblSubTotal = Double.valueOf(strSubTotal);
        dblIva = dblSubTotal * 21 / 100;
        dblTotal = dblSubTotal + dblIva;


        try {
            //strIva = new Double(dblIva).toString();
            //strTotal = new Double(dblTotal).toString();

            tvIva.setText(formatoDbl.format(dblIva));
            tvTotal.setText(formatoDbl.format(dblTotal));
        } catch (Exception e) {
            Log.e("text", e.toString());


        }
        cargaPerfil();
    }

    //Carga Perfil
    private void cargaPerfil(){
        //Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail).collection("perfil")
                .document("perfil");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //Si el Doc existe
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Crea hashMap para almacenar document
                        Map<String, Object> mapPerfil = new HashMap<>();
                        mapPerfil = document.getData();

                        //Recogemos los datos del HashMap en Strings
                        miNombre = mapPerfil.get("nombre").toString();
                        misApellidos = mapPerfil.get("apellidos").toString();
                        miDniCif = mapPerfil.get("dni/cif").toString();
                        miDireccion = mapPerfil.get("direccion").toString();
                        miCp= mapPerfil.get("cp").toString();
                        miTelefono = mapPerfil.get("telefono").toString();

                        //Llama a función para insertar datos en textViews
                        insertaDatosTextViews();

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());

                }
            }
        });
    }

    //Inserta los datos en TextViews
    private void insertaDatosTextViews(){

        //Mis datos
        tvNombre.setText(miNombre);
        tvApellidos.setText(misApellidos);
        tvEmail.setText(strEmail);
        tvDniCif.setText(miDniCif);
        tvDireccion.setText(miDireccion);
        tvCp.setText(miCp);
        tvTelefono.setText(miTelefono);

        //Datos Cliente
        tvIdCliente.setText(strClienteFra);
        tvNombreCliente.setText(strClienteNombre);
        tvNifCliente.setText(strClienteNif);

        //Datos factura
        tvFecha.setText(strFechaFra);
        tvSubTotal.setText(strSubTotal);

    }


    private void inciarNuevaFactura(){

        //Inicializa FireStore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail).collection
                ("facturas").document(strNumFra);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //Si el Doc existe
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //Pide que se asigne nuevo Id a factura
                        strNumFra = devuelveId(strNumFra);

                        //Prueba de generar nueva factura
                        inciarNuevaFactura();

                        //Si no existe
                    } else {
                        Log.d(TAG, "No such document");
                        creaNuevaFactura();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //Calculo nuevos IdFra
    private String devuelveId (String strNumFra){

        int numIdFra;
        String strIdFactura="";
        try{
            //Parse a Int strNumFra
            numIdFra= Integer.valueOf(strNumFra);
            numIdFra++;

            //Parse a String numId
            strIdFactura = Integer.toString(numIdFra);

        }
        catch (NumberFormatException e){
            System.out.println("Error");

        }

        //Devuelve String
        return strIdFactura;
    }



    private void creaNuevaFactura(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map factura datosFactura
        Map<String, Object> datosFactura = new HashMap<>();

        // Datos de Factura
        datosFactura.put("idCliente", strClienteFra);
        datosFactura.put("numFactura", strNumFra);
        datosFactura.put("fechaFactura", strFechaFra);

        datosFactura.put("detalle", strAlArticulos);
        datosFactura.put("subtotal", strSubTotal);
        datosFactura.put("ivaFactura", formatoDbl.format(dblIva));
        datosFactura.put("totalFactura", formatoDbl.format(dblTotal));

        //Inserta datos en Factura
        db.collection("/users").document(strEmail).collection
                ("facturas").document(strNumFra).set(datosFactura);

        tvNumFra.setText(strNumFra);
        //Lanza alerta
        mensaje = "FACTURA " + strNumFra + " CREADA!";

        showAlert();
        showHomeActivity();

    }

    //Boton Cerrar

    class listenerAtras implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showHomeActivity();
        }
    }

    //Boton Generar

    class listenerGenerar implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            showdialog();
        }
    }

    //Mustra dialogo
    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// Configura el titulo.
        alertDialogBuilder.setTitle("CONFIRMAR");

// Configura el mensaje.
        alertDialogBuilder
                .setMessage("Quieres generar nueva factura?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                        inciarNuevaFactura();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    //Muestra anadirArticulosFra
    private void showHomeActivity(){
        //Crea Intents para volver anadirArticulosFra

        Intent i = new Intent(this, FacturaActivity.class);

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