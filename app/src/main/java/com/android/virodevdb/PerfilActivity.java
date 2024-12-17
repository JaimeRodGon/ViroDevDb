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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    //Variables String
    private String strEmail;
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

        //Variable boton guardar
        btnGuardar = findViewById(R.id.btnGuardarActualizaPerfil);
        btnCancelar = findViewById(R.id.buttonAtras);

        //Find by ID
        tvEmail = findViewById(R.id.etEmail);
        tvPassword = findViewById(R.id.etPassword);
        tvNombre = findViewById(R.id.etNombre);
        tvApellidos = findViewById(R.id.etApellidos);
        tvDniCif = findViewById(R.id.etDniCif);
        tvDireccion = findViewById(R.id.etDireccion);
        tvCP = findViewById(R.id.etCP);
        tvTelefono = findViewById(R.id.etTelefono);



        Setup();

    }
    //Setup
    private void Setup (){

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

            try {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(strEmail,
                        strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Si el registro ha sido correcto
                        if(task.isSuccessful()){

                            //Crea Docs perfil en fireBase
                            crearDocPerfil();

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
   private void showAuthActivity(){
        //Crea Intents para volver AuthActivity

        Intent i = new Intent(this, AuthActivity.class);

        startActivity(i);

    }

    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje2);
        alerta.show();

    }

    //Crear DocPerfil en fireBase
    private void crearDocPerfil(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user Map
        Map<String, Object> datosPerfil = new HashMap<>();

        // Datos de perfil
        datosPerfil.put("email",strEmail);
        datosPerfil.put("nombre", tvNombre.getText().toString());
        datosPerfil.put("apellidos", tvApellidos.getText().toString());
        datosPerfil.put("dni/cif", tvDniCif.getText().toString());
        datosPerfil.put("direccion", tvDireccion.getText().toString());
        datosPerfil.put("cp", tvCP.getText().toString());
        datosPerfil.put("telefono", tvTelefono.getText().toString());

        //Inserta datos en nodos 
        db.collection("/users").document(strEmail).collection("perfil").document("perfil").set(datosPerfil);

    }


}