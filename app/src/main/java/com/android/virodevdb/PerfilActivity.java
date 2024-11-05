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

public class PerfilActivity extends AppCompatActivity {
    //Variables String
    public String strEmail;
    private String strPassword;
    private String strNombre;
    private String strApellidos;
    private String strDniCif;
    private String strDireccion;
    private String strCP;
    private String strTelefono;

    //Variables TexView
    private TextView tvEmail;
    private TextView tvPassword;
    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvDniCif;
    private TextView tvDireccion;
    private TextView tvCP;
    private TextView tvTelefono;

    //Variables botones
    private Button btnGuardar;
    private Button btnCancelar;
    private String mensaje2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        Setup();

    }
    //Setup
    private void Setup (){

        //Variable boton guardar
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);

        //Find by ID
        tvEmail = findViewById(R.id.etEmail);
        tvPassword = findViewById(R.id.etPassword);
        tvNombre = findViewById(R.id.etNombre);
        tvApellidos = findViewById(R.id.etApellidos);
        tvDniCif = findViewById(R.id.etDniCif);
        tvDireccion = findViewById(R.id.etDireccion);
        tvCP = findViewById(R.id.etCP);
        tvTelefono = findViewById(R.id.etTelefono);

        //Listener botones
        btnGuardar.setOnClickListener(new PerfilActivity.listenerGuardar());
        btnCancelar.setOnClickListener(new PerfilActivity.listenerCancelar());

    }

    //Boton Guardar

    class listenerGuardar implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //Recogemos datos para FireBaseAuth
            strEmail = tvEmail.getText().toString();
            strPassword = tvPassword.getText().toString();

            //Recogemos resto de datos
            strNombre = tvNombre.getText().toString();
            strApellidos = tvApellidos.getText().toString();
            strDniCif = tvDniCif.getText().toString();
            strDireccion = tvDireccion.getText().toString();
            strCP = tvCP.getText().toString();
            strTelefono = tvTelefono.getText().toString();

            try {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(strEmail,
                        strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //Crea Docs perfil
                            crearDocsPerfil();

                            mensaje2="EXITO EN REGISTRO";
                            showAlert();

                            //Regresa AuthActivity
                            showAuthActivity();


                        }else{
                            mensaje2="DATOS INVALIDOS";
                            showAlert();

                        }
                    }
                });


            }
            catch (Exception errorRegistro){
                mensaje2="ERROR DE REGISTRO";
                showAlert();
            }

        }
    }

    //Boton Cancelar

    class listenerCancelar implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            showAuthActivity();

        }
    }

    //Muestra AuthActivity
    public void showAuthActivity(){
        //Crea Intents para volver AuthActivity

        Intent i = new Intent(this, AuthActivity.class);

        startActivity(i);

    }

    //Lanza Alerta
    private void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje2);
        alerta.show();

    }

    //Crear DocsPerfil
    private void crearDocsPerfil(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user Map
        Map<String, Object> datosPerfil = new HashMap<>();

        // Datos de Prueba
        datosPerfil.put("email",strEmail);
        datosPerfil.put("nombre", strNombre);
        datosPerfil.put("apellidos", strApellidos);
        datosPerfil.put("dni/cif", strDniCif);
        datosPerfil.put("direccion", strDireccion);
        datosPerfil.put("cp", strCP);
        datosPerfil.put("telefono", strTelefono);

        //Inserta datos en nodos 
        db.collection("/users").document(strEmail).collection("perfil").document("perfil").set(datosPerfil);

    }



}