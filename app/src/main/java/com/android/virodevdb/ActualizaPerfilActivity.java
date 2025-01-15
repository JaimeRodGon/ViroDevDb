package com.android.virodevdb;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ActualizaPerfilActivity extends AppCompatActivity {

    //String recibido con intent
    private String strEmail;

    //Variables textView
    private TextView tvEmail;
    private EditText tvNombre;
    private EditText tvApellidos;
    private EditText tvDniCif;
    private EditText tvDireccion;
    private EditText tvCP;
    private EditText tvTelefono;

    //Variables para HashMap
    private String miNombre;
    private String misApellidos;
    private String miEmail;
    private String miDniCif;
    private String miDireccion;
    private String miCp;
    private String miTelefono;

    //Variables boton
    private Button btnGuardar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualiza_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Recibe datosEmail
        Intent recibir = getIntent();

        //Actualiza datos strEmail
        strEmail = recibir.getStringExtra("DatosEmail");

        setup();
    }
    private void setup(){
        //FindById botones
        btnCancelar = findViewById(R.id.btnCancelarActualizaPerfil);
        btnGuardar = findViewById(R.id.btnGuardarActualizaPerfil);

        //FindById textView
        tvEmail = findViewById(R.id.textViewTitulo);
        tvNombre = findViewById(R.id.textViewNombre);
        tvApellidos = findViewById(R.id.textViewApellidos);
        tvDniCif =findViewById(R.id.textViewDniCif);
        tvDireccion = findViewById(R.id.textViewDireccion);
        tvCP = findViewById(R.id.textViewCp);
        tvTelefono = findViewById(R.id.textViewTelefono);

        //inserta datos en tvEmail
        tvEmail.setText(strEmail.toString());

        mostrarPerfil();

        //Listener botones
        btnGuardar.setOnClickListener(new ActualizaPerfilActivity.listenerGuardar());
        btnCancelar.setOnClickListener(new ActualizaPerfilActivity.listenerCancelar());

    }

    private void mostrarPerfil(){
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
                        miEmail = mapPerfil.get("email").toString();
                        miDniCif = mapPerfil.get("dni/cif").toString();
                        miDireccion = mapPerfil.get("direccion").toString();
                        miCp= mapPerfil.get("cp").toString();
                        miTelefono = mapPerfil.get("telefono").toString();

                        //Llama a función para insertar datos en textViews
                        insertaDatosTextViews();

                    } else {
                        Log.d(TAG, "No such document");
                        tvNombre.setText("No existe el doc");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    tvNombre.setText("Fallo en get");
                }
            }
        });

    }
    //Inserta los datos en TextViews
    private void insertaDatosTextViews(){
        tvNombre.setText(miNombre);
        tvApellidos.setText(misApellidos);
        tvDniCif.setText(miDniCif);
        tvDireccion.setText(miDireccion);
        tvCP.setText(miCp);
        tvTelefono.setText(miTelefono);

    }

    //Crear DocsPerfil
    private void crearDocsPerfil(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user Map
        Map<String, Object> datosPerfil = new HashMap<>();

        // Datos de de Perfil
        datosPerfil.put("email",strEmail);
        datosPerfil.put("nombre", tvNombre.getText().toString());
        datosPerfil.put("apellidos", tvApellidos.getText().toString());
        datosPerfil.put("dni/cif", tvDniCif.getText().toString());
        datosPerfil.put("direccion", tvDireccion.getText().toString());
        datosPerfil.put("cp", tvCP.getText().toString());
        datosPerfil.put("telefono", tvTelefono.getText().toString());

        //Inserta datos en nodos
        db.collection("/users").document(strEmail).collection("perfil").document("perfil").set(datosPerfil);

        showAlert();

    }

    //Boton Guardar
    class listenerGuardar implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            showdialog();

        }
    }

    //Boton Cancelar
    class listenerCancelar implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            showVerPerfilActivity();

        }
    }

    //Muestra VerPerfilActivity
    private void showVerPerfilActivity(){
        //Crea Intents para volver VerPerfilActivity

        Intent i = new Intent(this, VerPerfilActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Muestra dialogo
    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// Configura el titulo.
        alertDialogBuilder.setTitle("CONFIRMAR");

// Configura el mensaje.
        alertDialogBuilder
                .setMessage("Quieres modificar el perfil?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                        crearDocsPerfil();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }
    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("PERFIL MODIFICADO CORRECTAMENTE");
        alerta.show();

    }

}