package com.android.virodevdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity {

    //Variables
    private Button  botonCerrar;
    private Button  botonPerfil;
    private TextView tvEmail;
    private TextView tvPass;
    private String StrEmail;
    private String StrPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //Variables View
        tvEmail = findViewById(R.id.emailTV);
        tvPass = findViewById(R.id.passTV);
        botonCerrar = findViewById(R.id.btnClose);
        botonPerfil = findViewById(R.id.btnPerfil);



        //Recibe datosEmail datosPass
        Intent recibir = getIntent();
        StrEmail = recibir.getStringExtra("DatosEmail");
        StrPass = recibir.getStringExtra("DatosPass");

        setup();

    //Setup
    }
    private void setup (){

        //Recibe datos variables AuthActivity
        this.tvEmail.setText(StrEmail);
        this.tvPass.setText(StrPass);
        //Listeners Botones
        botonCerrar.setOnClickListener(new homeActivity.listenerCerrar());
        botonPerfil.setOnClickListener(new homeActivity.listenerPerfil());


    }

    //Boton Cerrar
    class listenerCerrar implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            Intent intentAuth = new Intent(homeActivity.this, AuthActivity.class);
            startActivity(intentAuth);
        }
    }

    //Boton perfil
    class listenerPerfil implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            Intent intentPerfil = new Intent(homeActivity.this, PerfilActivity.class);
            showPerfil(StrEmail);

        }
    }

    //Muestra PerfilActivity
    public void showPerfil(String StrEmail){

        //Crea Intents para homeActivity y PerfilActivity

        Intent i = new Intent(this, PerfilActivity.class);

        //Manda datos a homeActivity
        i.putExtra("DatosEmail", StrEmail);

        startActivity(i);


    }




}
