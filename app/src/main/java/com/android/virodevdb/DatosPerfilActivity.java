package com.android.virodevdb;

import static android.content.ContentValues.TAG;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DatosPerfilActivity extends AppCompatActivity {

    private String strEmail;
    //Variables textView
    private TextView tvEmail;
    private TextView tvEmail2;
    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvDniCif;
    private TextView tvDireccion;
    private TextView tvCP;
    private TextView tvTelefono;

    //Variables para HashMap
    private String miNombre;
    private String misApellidos;
    private String miEmail;
    private String miDniCif;
    private String miDireccion;
    private String miCp;
    private String miTelefono;

    //Variables boton
    private Button btnCerrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recibe datosEmail
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");

        //Variables View
        tvEmail = findViewById(R.id.textViewEmail);
        tvNombre = findViewById(R.id.textViewNombre);
        tvApellidos = findViewById(R.id.textViewApellidos);
        tvEmail2 = findViewById(R.id.textViewEmail2);
        tvDniCif = findViewById(R.id.textViewDniCif);
        tvDireccion = findViewById(R.id.textViewDireccion);
        tvCP = findViewById(R.id.textViewCp);
        tvTelefono = findViewById(R.id.textViewTelefono);

        //Variables botones
        btnCerrar = findViewById(R.id.buttonCerrar);

        // Ejecuta Setup
        Setup();
    }

    //Setup
    private void Setup (){

        //Recibe datos variables homeActivity
        this.tvEmail.setText(strEmail);

        //Listeners Botones
        btnCerrar.setOnClickListener(new DatosPerfilActivity.listenerCerrar());

        mostrarPerfil();


    }

    public void mostrarPerfil(){
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
    public void insertaDatosTextViews(){
        tvNombre.setText(miNombre);
        tvApellidos.setText(misApellidos);
        tvEmail2.setText(miEmail);
        tvDniCif.setText(miDniCif);
        tvDireccion.setText(miDireccion);
        tvCP.setText(miCp);
        tvTelefono.setText(miTelefono);

    }

    //Boton Cerrar

    class listenerCerrar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            Intent intentHome = new Intent(DatosPerfilActivity.this, homeActivity.class);
            startActivity(intentHome);
        }
    }


}