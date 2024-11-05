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
    private Button  btnCerrar;
    private Button  btnFacturas;
    private Button  btnPerfil;
    private TextView tvEmail;
    private String strEmail;

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
        tvEmail = findViewById(R.id.textViewEmail);

        //Variables botones
        btnCerrar = findViewById(R.id.buttonCerrar);
        btnFacturas = findViewById(R.id.buttonFacturas);
        btnPerfil = findViewById(R.id.buttonPerfil);

        //Recibe datosEmail datosPass
        Intent recibir = getIntent();
        strEmail = recibir.getStringExtra("DatosEmail");


        //Setup
        setup();


    }
    private void setup (){

        //Recibe datos variables AuthActivity
        this.tvEmail.setText(strEmail);

        //Listeners Botones
        btnCerrar.setOnClickListener(new homeActivity.listenerCerrar());
        btnFacturas.setOnClickListener(new homeActivity.listenerFacturas());
        btnPerfil.setOnClickListener(new homeActivity.listenerPerfil());

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

    //Boton Facturas
    class listenerFacturas implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showNuevaFacturaActivity(strEmail);
        }
    }

    //Boton Facturas
    class listenerPerfil implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showDatosPerfilActivity(strEmail);
        }
    }
    //Muestra PerfilActivity
    public void showNuevaFacturaActivity(String strEmail){

        //Crea Intents para NuevaFacturaActivity

        Intent i = new Intent(this, NuevaFacturaActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Muestra DatosPerfilActivity
    public void showDatosPerfilActivity(String strEmail){

        //Crea Intents para DatosPerfilActivity

        Intent i = new Intent(this, DatosPerfilActivity.class);

        //Manda datos a DatosPerfilActivity

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }
    
}
