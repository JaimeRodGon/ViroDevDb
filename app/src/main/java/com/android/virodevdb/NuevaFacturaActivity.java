package com.android.virodevdb;
import static android.content.ContentValues.TAG;
import android.app.AlertDialog;
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

public class NuevaFacturaActivity extends AppCompatActivity {

    private String strEmail;

    //Variables TexView
    private TextView tvClienteFra;
    private TextView tvNumFra;
    private TextView tvFechaFra;
    private TextView tvDetalleFra;
    private TextView tvImporteFra;
    private TextView tvIvaFra;
    private TextView tvTotalFra;

    //Variables String
    private String strClienteFra;
    private String strNumFra="0";
    private String strFechaFra;
    private String strDetalleFra;
    private String strImporteFra;
    private String strIvaFra;
    private String strTotalFra;

    private String mensaje="";

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

        //Recibe datosEmail datosPass
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        // Ejecuta Setup
        Setup();
    }

    //Setup
    private void Setup (){

        //Variable boton guardar
        //Variables botones
        Button btnGuardar = findViewById(R.id.buttonGuardar);
        Button btnCancelar = findViewById(R.id.buttonCancelar);

        //Find by ID
        tvClienteFra = findViewById(R.id.etClienteFra);
        tvNumFra = findViewById(R.id.etNumFra);
        tvFechaFra = findViewById(R.id.etFechaFactura);
        tvDetalleFra = findViewById(R.id.etDetalleFra);
        tvImporteFra = findViewById(R.id.etImporteFra);
        tvIvaFra = findViewById(R.id.etIvaFra);
        tvTotalFra = findViewById(R.id.etTotalFra);

        //Inserta en tvNumFra "0"
        this.tvNumFra.setText(strNumFra);

        //Listener botones
        btnGuardar.setOnClickListener(new NuevaFacturaActivity.listenerGuardar());
        btnCancelar.setOnClickListener(new NuevaFacturaActivity.listenerCancelar());
    }
    //Boton Guardar

        class listenerGuardar implements View.OnClickListener{

            @Override
            public void onClick(View v) {

                //Recoge datos de TextViews

                strClienteFra = tvClienteFra.getText().toString();
                strFechaFra = tvFechaFra.getText().toString();
                strDetalleFra = tvDetalleFra.getText().toString();
                strImporteFra = tvImporteFra.getText().toString();
                strIvaFra = tvIvaFra.getText().toString();
                strTotalFra = tvTotalFra.getText().toString();

                //Crea la factura
                inciarNuevaFactura();

            }
        }

    private void inciarNuevaFactura(){

        //Inicializa FireStore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail).collection
                ("facturas").document("idFacturas").collection
                ("idFacturas").document(strNumFra);

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
                        creaIdFactura();
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


    private void creaIdFactura(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map idFacturas
        Map<String, Object> idFacturas = new HashMap<>();

        // Datos de idFacturas
        idFacturas.put("idFactura", strNumFra);

        //Inserta datos en idFacturas
        db.collection("/users").document(strEmail).collection
                ("facturas").document("idFacturas").collection
                ("idFacturas").document(strNumFra).set(idFacturas);
        //Lanza alerta
        mensaje = "ID FACTURA CREADO!";
        showAlert();

    }

    private void creaNuevaFactura(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map factura datosFactura
        Map<String, Object> datosFactura = new HashMap<>();

        // Datos de Factura
        datosFactura.put("idCliente", strClienteFra);
        datosFactura.put("numFactura", strNumFra);
        datosFactura.put("fechaFactura", strFechaFra);
        datosFactura.put("detalleFactura", strDetalleFra);
        datosFactura.put("importeFactura", strImporteFra);
        datosFactura.put("ivaFactura", strIvaFra);
        datosFactura.put("totalFactura", strTotalFra);

        //Inserta datos en Factura
        db.collection("/users").document(strEmail).collection
                ("facturas").document(strNumFra).set(datosFactura);

        //Lanza alerta
        mensaje = "FACTURA CREADA!";
        showAlert();


    }

    //Lanza Alerta
    private void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();

    }

    //Boton Cancelar

    class listenerCancelar implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            showHomeActivity();

        }
    }

    //Muestra homeActivity
    public void showHomeActivity(){
        //Crea Intents para volver homeActivity

        Intent i = new Intent(this, homeActivity.class);

        startActivity(i);

    }

}