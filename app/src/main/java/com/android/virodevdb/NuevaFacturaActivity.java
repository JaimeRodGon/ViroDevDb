package com.android.virodevdb;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NuevaFacturaActivity extends AppCompatActivity {

    //Variables String
    public String strClienteFra;
    private String strNumFra;
    private String strFechaFra;
    private String strDetalleFra;
    private String strImporteFra;
    private String strIvaFra;
    private String strTotalFra;
    private String strEmail;

    //Variables TexView
    private TextView tvClienteFra;
    private TextView tvNumFra;
    private TextView tvFechaFra;
    private TextView tvDetalleFra;
    private TextView tvImporteFra;
    private TextView tvIvaFra;
    private TextView tvTotalFra;

    //Variables botones
    private Button btnGuardar;
    private Button btnCancelar;
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
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);

        //Find by ID
        tvClienteFra = findViewById(R.id.etDetalleFra);
        tvNumFra = findViewById(R.id.etNumFra);
        tvFechaFra = findViewById(R.id.etFechaFactura);
        tvDetalleFra = findViewById(R.id.etDetalleFra);
        tvImporteFra = findViewById(R.id.etImporteFra);
        tvIvaFra = findViewById(R.id.etIvaFra);
        tvTotalFra = findViewById(R.id.etTotalFra);

        //Listener botones
        btnGuardar.setOnClickListener(new NuevaFacturaActivity.listenerGuardar());
        btnCancelar.setOnClickListener(new NuevaFacturaActivity.listenerCancelar());
    }
    //Boton Guardar

        class listenerGuardar implements View.OnClickListener{

            @Override
            public void onClick(View v) {

                //Recogemos datos de forumulario
                strClienteFra = tvClienteFra.getText().toString();
                strNumFra = tvNumFra.getText().toString();
                strFechaFra = tvFechaFra.getText().toString();
                strDetalleFra = tvDetalleFra.getText().toString();
                strImporteFra = tvImporteFra.getText().toString();
                strIvaFra = tvIvaFra.getText().toString();
                strTotalFra = tvTotalFra.getText().toString();

                crearDocsFactura();

            }
        }

    //Crea Factura en FireStore
    private void crearDocsFactura(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user Map
        Map<String, Object> datosFactura = new HashMap<>();

        // Datos de Factura
        datosFactura.put("idCliente", strClienteFra);
        datosFactura.put("numFactura", strNumFra);
        datosFactura.put("fechaFactura", strFechaFra);
        datosFactura.put("detalleFactura", strDetalleFra);
        datosFactura.put("importeFactura", strImporteFra);
        datosFactura.put("ivaFactura", strIvaFra);
        datosFactura.put("totalFactura", strTotalFra);

        //Inserta datos en nodos
        db.collection("/users").document(strEmail).collection("facturas").document(strNumFra).set(datosFactura);

        // Mensaje
        mensaje="FACTURA CREADA";

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