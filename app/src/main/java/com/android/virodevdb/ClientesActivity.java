package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClientesActivity extends AppCompatActivity {

    private String strEmail;

    // Variables para EditText
    private EditText tvClienteFra, tvNumFra, tvNIF, tvCodigoPostal, tvLocalidad, tvCalle, tvInfo;

    // Variables String
    private String strClienteFra, strNumFra = "0", strNIF, strCodigoPostal, strLocalidad, strCalle, strInfo;

    private Button btnGuardar, btnVerClientes, btnCancelar;

    private String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Asegúrate de usar el diseño correcto
        setContentView(R.layout.activity_clientes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recibe datos de la actividad previa
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        // Ejecuta Setup
        Setup();
    }

    // Setup inicial
    private void Setup() {
        // Referencias a botones
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnVerClientes = findViewById(R.id.buttonVerClientes);
        btnCancelar = findViewById(R.id.buttonAtras);

        // Referencias a EditText
        tvClienteFra = findViewById(R.id.etClienteFra);
        tvNumFra = findViewById(R.id.etNumFra);
        tvNIF = findViewById(R.id.etNIF);
        tvCodigoPostal = findViewById(R.id.etCodigoPostal);
        tvLocalidad = findViewById(R.id.etLocalidad);
        tvCalle = findViewById(R.id.etCalle);
        tvInfo = findViewById(R.id.etInfo);

        // Inicializa tvNumFra
        this.tvNumFra.setText(strNumFra);

        // Listeners para botones
        btnGuardar.setOnClickListener(new listenerGuardar());
        btnVerClientes.setOnClickListener(new listenerVerClientes());
        btnCancelar.setOnClickListener(new listenerCancelar());
    }

    // Botón Guardar
    class listenerGuardar implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Recoge datos de EditText
            strClienteFra = tvClienteFra.getText().toString();
            strNIF = tvNIF.getText().toString();
            strCodigoPostal = tvCodigoPostal.getText().toString();
            strLocalidad = tvLocalidad.getText().toString();
            strCalle = tvCalle.getText().toString();
            strInfo = tvInfo.getText().toString();

            // Inicia creación de cliente
            iniciarNuevoCliente();
        }
    }

    private void iniciarNuevoCliente() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("/users").document(strEmail)
                .collection("clientes").document(strNumFra);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    strNumFra = devuelveId(strNumFra);
                    iniciarNuevoCliente();
                } else {
                    Log.d(TAG, "No such document");
                    creaNuevoCliente();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    // Genera nuevo ID
    private String devuelveId(String strNumFra) {
        int numIdFra;
        try {
            numIdFra = Integer.parseInt(strNumFra);
            numIdFra++;
        } catch (NumberFormatException e) {
            numIdFra = 1;
        }
        return String.valueOf(numIdFra);
    }

    // Crea nuevo cliente
    private void creaNuevoCliente() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> datosCliente = new HashMap<>();
        datosCliente.put("idCliente", strNumFra);
        datosCliente.put("nombreEmpresa", strClienteFra);
        datosCliente.put("NIF", strNIF);
        datosCliente.put("codigoPostal", strCodigoPostal);
        datosCliente.put("localidad", strLocalidad);
        datosCliente.put("calle", strCalle);
        datosCliente.put("informacion", strInfo);

        db.collection("/users").document(strEmail).collection("clientes")
                .document(strNumFra).set(datosCliente);

        mensaje = "¡CLIENTE CREADO!";
        showAlert();
    }
    class listenerVerClientes implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            verClientes();
        }
    }

    // Muestra lista de clientes
    private void verClientes() {
        Intent intent = new Intent(this, VerClientesActivity.class);
        intent.putExtra("DatosEmail", strEmail);
        startActivity(intent);
    }

    // Lanza una alerta
    public void showAlert() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();
    }

    // Botón Cancelar
    class listenerCancelar implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showHomeActivity();
        }
    }

    // Muestra homeActivity
    private void showHomeActivity() {
        Intent i = new Intent(this, homeActivity.class);
        i.putExtra("DatosEmail", strEmail);
        startActivity(i);
    }
}
