package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class VerClientesActivity extends AppCompatActivity {

    // Variables String
    private String strEmail;
    private String idCliente = "0";
    private String nombreEmpresa;
    private String NIF;
    private String codigoPostal;
    private String localidad;
    private String calle;
    private String informacion;

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
        btnSiguiente = findViewById(R.id.buttonSiguiente);
        btnAnterior = findViewById(R.id.buttonAnterior);
        btnEliminar = findViewById(R.id.buttonEliminar);  // Añadir el botón Eliminar
        btnActualizar = findViewById(R.id.buttonActualizar); // Añadir el botón Actualizar

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

        // Listener para el botón Eliminar
        btnEliminar.setOnClickListener(new listenerEliminarCliente());

        // Listener para el botón Actualizar
        btnActualizar.setOnClickListener(new listenerActualizarCliente());
    }

    private void mostrarCliente() {
        // Inicializa Firestore
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
                        nombreEmpresa = (mapClientes.get("nombreEmpresa") != null) ? mapClientes.get("nombreEmpresa").toString() : "Valor no disponible";
                        NIF = (mapClientes.get("NIF") != null) ? mapClientes.get("NIF").toString() : "Valor no disponible";
                        codigoPostal = (mapClientes.get("codigoPostal") != null) ? mapClientes.get("codigoPostal").toString() : "Valor no disponible";
                        localidad = (mapClientes.get("localidad") != null) ? mapClientes.get("localidad").toString() : "Valor no disponible";
                        calle = (mapClientes.get("calle") != null) ? mapClientes.get("calle").toString() : "Valor no disponible";
                        informacion = (mapClientes.get("informacion") != null) ? mapClientes.get("informacion").toString() : "Valor no disponible";

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
        etEmpresa.setText(nombreEmpresa);
        etNIF.setText(NIF);
        etCP.setText(codigoPostal);
        etLocalidad.setText(localidad);
        etCalle.setText(calle);
        etInfoAdicional.setText(informacion);
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

    // Botón Eliminar Cliente
    class listenerEliminarCliente implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            eliminarCliente();
        }
    }

    private void eliminarCliente() {
        // Mostrar un cuadro de diálogo de confirmación antes de eliminar
        new AlertDialog.Builder(VerClientesActivity.this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este cliente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario confirma, procedemos con la eliminación
                        // Inicializa Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Referencia al documento del cliente que quieres eliminar
                        DocumentReference docRef = db.collection("/users").document(strEmail).collection("clientes")
                                .document(idCliente);

                        // Elimina el documento de Firestore
                        docRef.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Cliente eliminado correctamente");
                                            // Muestra un mensaje o realiza cualquier otra acción después de eliminar
                                            etEmpresa.setText("Cliente eliminado");

                                            // Opcional: Muestra el siguiente cliente o vuelve a la lista de clientes
                                            siguienteCliente();  // Muestra el siguiente cliente
                                        } else {
                                            Log.d(TAG, "Error al eliminar cliente: ", task.getException());
                                            etEmpresa.setText("Error al eliminar cliente");
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("No", null)  // Botón para cancelar
                .show();
    }

    // Botón Actualizar Cliente
    class listenerActualizarCliente implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            actualizarCliente();
        }
    }

    private void actualizarCliente() {
        // Obtener los nuevos valores de los EditTexts
        String nuevaEmpresa = etEmpresa.getText().toString();
        String nuevoNIF = etNIF.getText().toString();
        String nuevoCP = etCP.getText().toString();
        String nuevaLocalidad = etLocalidad.getText().toString();
        String nuevaCalle = etCalle.getText().toString();
        String nuevaInfo = etInfoAdicional.getText().toString();

        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crea el Map con los datos a actualizar
        Map<String, Object> actualizaciones = new HashMap<>();
        actualizaciones.put("nombreEmpresa", nuevaEmpresa);
        actualizaciones.put("NIF", nuevoNIF);
        actualizaciones.put("codigoPostal", nuevoCP);
        actualizaciones.put("localidad", nuevaLocalidad);
        actualizaciones.put("calle", nuevaCalle);
        actualizaciones.put("informacion", nuevaInfo);

        // Referencia al documento del cliente
        DocumentReference docRef = db.collection("/users").document(strEmail).collection("clientes")
                .document(idCliente);

        // Actualiza los datos en Firestore
        docRef.update(actualizaciones)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Cliente actualizado correctamente");

                            // Actualiza los EditTexts con los nuevos datos
                            etEmpresa.setText(nuevaEmpresa);
                            etNIF.setText(nuevoNIF);
                            etCP.setText(nuevoCP);
                            etLocalidad.setText(nuevaLocalidad);
                            etCalle.setText(nuevaCalle);
                            etInfoAdicional.setText(nuevaInfo);

                            // Crear y mostrar el cuadro de diálogo de éxito
                            new AlertDialog.Builder(VerClientesActivity.this)
                                    .setTitle("Actualización Exitosa")
                                    .setMessage("Los datos del cliente se han actualizado correctamente.")
                                    .setPositiveButton("OK", null) // Botón para cerrar el cuadro de diálogo
                                    .show();

                        } else {
                            Log.d(TAG, "Error al actualizar cliente: ", task.getException());
                        }
                    }
                });
    }
}

