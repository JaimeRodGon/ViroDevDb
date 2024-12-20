package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class VerClientesActivity extends AppCompatActivity {

    // Variables String
    private String strEmail;
    private String idCliente = "0";
    private String empresa;
    private String nif;
    private String cp;
    private String localidad;
    private String calle;
    private String infoAdicional;

    // Variables EditText
    private EditText etEmpresa;
    private EditText etNIF;
    private EditText etCP;
    private EditText etLocalidad;
    private EditText etCalle;
    private EditText etInfoAdicional;

    // Variables Button
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
        setContentView(R.layout.activity_ver_clientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recibe datos del Intent
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        setup();
    }

    // Configuración inicial
    private void setup() {
        btnCancelar = findViewById(R.id.buttonAtras);
        btnNuevo = findViewById(R.id.buttonNuevo);
        btnSiguiente = findViewById(R.id.buttonSiguienteCl);
        btnAnterior = findViewById(R.id.buttonAnteriorCl);

        etEmpresa = findViewById(R.id.editTextClienteEmpresa);
        etNIF = findViewById(R.id.editTextClienteNIF);
        etCP = findViewById(R.id.editTextClienteCP);
        etLocalidad = findViewById(R.id.editTextClienteLocalidad);
        etCalle = findViewById(R.id.editTextClienteCalle);
        etInfoAdicional = findViewById(R.id.editTextClienteInfo);

        mostrarCliente();

        // Listener botones
        btnNuevo.setOnClickListener(new listenerNuevo());
        btnCancelar.setOnClickListener(new listenerCancelar());
        btnSiguiente.setOnClickListener(new listenerSiguienteCliente());
        btnAnterior.setOnClickListener(new listenerAnteriorCliente());
    }

    private void mostrarCliente() {
        // Inicializa FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Busca el documento 0 de clientes (modificar para ir buscando)
        DocumentReference docRef = db.collection("/users").document(strEmail).collection("clientes")
                .document(idCliente);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    // Si el Doc existe
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        // Crea hashMap para almacenar document
                        Map<String, Object> mapClientes = new HashMap<>();
                        mapClientes = document.getData();

                        // Recogemos los datos del HashMap en Strings
                        empresa = mapClientes.get("empresa").toString();
                        nif = mapClientes.get("nif").toString();
                        cp = mapClientes.get("cp").toString();
                        localidad = mapClientes.get("localidad").toString();
                        calle = mapClientes.get("calle").toString();
                        infoAdicional = mapClientes.get("infoAdicional").toString();

                        // Inserta los datos en los EditTexts
                        insertaDatosEditTexts();

                    } else {
                        Log.d(TAG, "No such document");
                        etEmpresa.setText("No existe el documento");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    etEmpresa.setText("Fallo en la obtención del documento");
                }
            }
        });
    }

    // Inserta los datos en los EditTexts
    private void insertaDatosEditTexts() {
        etEmpresa.setText(empresa);
        etNIF.setText(nif);
        etCP.setText(cp);
        etLocalidad.setText(localidad);
        etCalle.setText(calle);
        etInfoAdicional.setText(infoAdicional);
    }

    // Botón Siguiente Cliente
    class listenerSiguienteCliente implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            siguienteCliente();
        }
    }

    private void siguienteCliente() {
        int numIdCliente;

        try {
            // Parse a Int idCliente
            numIdCliente = Integer.valueOf(idCliente);
            numIdCliente++;

            // Parse a String numId
            idCliente = Integer.toString(numIdCliente);

        } catch (NumberFormatException e) {
            System.out.println("Error");
            idCliente = "0";
        }

        mostrarCliente();
    }

    // Botón Anterior Cliente
    class listenerAnteriorCliente implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            anteriorCliente();
        }
    }

    private void anteriorCliente() {
        int numIdCliente;

        try {
            // Parse a Int idCliente
            numIdCliente = Integer.valueOf(idCliente);
            if (numIdCliente > 0) {
                numIdCliente--;
                // Parse a String numId
                idCliente = Integer.toString(numIdCliente);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error");
            idCliente = "0";
        }

        mostrarCliente();
    }

    // Muestra NuevoClienteActivity
    private void showNuevoClienteActivity(String strEmail) {
        Intent i = new Intent(this, ClientesActivity.class);
        i.putExtra("DatosEmail", strEmail);
        startActivity(i);
    }

    // Botón Nuevo
    class listenerNuevo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showNuevoClienteActivity(strEmail);
        }
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
