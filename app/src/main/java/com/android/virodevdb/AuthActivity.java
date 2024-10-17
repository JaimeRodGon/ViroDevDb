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

import java.security.KeyStore;

public class AuthActivity extends AppCompatActivity {

    //objetos
    private Button botonRegistrar;
    private Button botonAcceder;
    private TextView textoEmail;
    private TextView textoPass;
    public String StrEmail;
    private String StrPass;
    private String message = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //SETUP
        setup();
    }
    //SETUP
    public void setup(){
        textoEmail = findViewById(R.id.emailEditText);
        textoPass = findViewById(R.id.passwordEditText);
        botonRegistrar = findViewById(R.id.signUpButton);
        botonAcceder = findViewById(R.id.logintButton);
        botonRegistrar.setOnClickListener(new listenerRegistrar());
        botonAcceder.setOnClickListener(new listenerAcceder());

    }
    //Boton Registrar
    class listenerRegistrar implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //Recogemos datos del textoEmail i textoPass
            StrEmail = textoEmail.getText().toString();
            StrPass = textoPass.getText().toString();
                try {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(StrEmail,
                            StrPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                message="EXITO EN REGISTRO";
                                showAlert();

                            }else{
                                message="Error de registro";
                                showAlert();

                            }
                        }
                    });


                }
                catch (Exception errorRegistro){
                    message="Error de registro";
                    showAlert();
                }

        }
    }
    //Boton Acceder
    class listenerAcceder implements View.OnClickListener{


        @Override
        public void onClick(View v) {
            //Recogemos datos del textoEmail i textoPass
            StrEmail = textoEmail.getText().toString();
            StrPass = textoPass.getText().toString();

                try {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(StrEmail,
                            StrPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                message="EXITO EN ACCESO";
                                showHome(StrEmail, StrPass);

                            }else{
                                message="Error de registro";
                                showAlert();

                            }

                        }
                    });

                }
                catch (Exception errorRegistro){
                    message = "Error de acceso";
                    showAlert();
                }

        }
    }
    //Muestra homeActivity
    public void showHome(String StrEmail, String StrPass){

        //Crea Intents para homeActivity y PerfilActivity
        Intent i = new Intent(this, homeActivity.class);
        Intent i2 = new Intent(this, PerfilActivity.class);

        //Manda datos a homeActivity
        i.putExtra("DatosEmail", StrEmail);
        i.putExtra("DatosPass", StrPass);

        i2.putExtra("DatosEmail", StrEmail);

        startActivity(i);


    }
    //Lanza Alerta
    private void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(message);
        alerta.show();


    }



}