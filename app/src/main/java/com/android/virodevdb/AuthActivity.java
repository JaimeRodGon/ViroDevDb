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


public class AuthActivity extends AppCompatActivity {

    //objetos
    private Button botonRegistrar;
    private Button botonAcceder;
    private TextView textoEmail;
    private TextView textoPass;
    private String StrEmail;
    private String StrPass;
    private String mensaje = "";

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
    private void setup(){
        textoEmail = findViewById(R.id.emailEditText);
        textoPass = findViewById(R.id.passwordEditText);
        botonRegistrar = findViewById(R.id.signUpButton);
        botonAcceder = findViewById(R.id.logintButton);

        //Listeners botones
        botonRegistrar.setOnClickListener(new listenerPerfil());
        botonAcceder.setOnClickListener(new listenerAcceder());

    }
    //Boton perfil
    class listenerPerfil implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            Intent intentPerfil = new Intent(AuthActivity.this, PerfilActivity.class);
            showPerfil();

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

                                mensaje="EXITO EN ACCESO";
                                showHome(StrEmail);

                            }else{
                                mensaje="ERROR DE ACCESO";
                                showAlert();

                            }

                        }
                    });

                }
                catch (Exception errorRegistro){
                    mensaje = "ERROR DE ACCESO";
                    showAlert();
                }

        }
    }
    //Muestra homeActivity
    private void showHome(String StrEmail){

        //Crea Intents para homeActivity y PerfilActivity
        Intent i = new Intent(this, homeActivity.class);
        Intent i2 = new Intent(this, FacturaActivity.class);

        //Manda datos a homeActivity
        i.putExtra("DatosEmail", StrEmail);
        i2.putExtra("DatosEmail", StrEmail);

        startActivity(i);

    }

    //Muestra PerfilActivity
    private void showPerfil(){

        //Crea Intents para homeActivity y PerfilActivity

        Intent i = new Intent(this, PerfilActivity.class);

        //Manda datos a homeActivity
        //i.putExtra("DatosEmail", StrEmail);

        startActivity(i);

    }

    //Lanza Alerta
    public void showAlert(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje);
        alerta.show();


    }

}