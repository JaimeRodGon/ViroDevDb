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
    private Button  btnArticulos;
    private Button  btnClientes;
    private TextView tvEmail;

    //Variables String
    private String strEmail;
    private String strPassword;
    private String strNombre;
    private String strApellidos;
    private String strDniCif;
    private String strDireccion;
    private String strCp;
    private String strTelefono;


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
        tvEmail = findViewById(R.id.textViewTitulo);

        //Variables botones
        btnCerrar = findViewById(R.id.buttonCerrarPerfil);
        btnFacturas = findViewById(R.id.buttonFacturas);
        btnPerfil = findViewById(R.id.buttonPerfil);
        btnArticulos = findViewById(R.id.buttonArticulos);
        btnClientes = findViewById(R.id.buttonClientes);

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
        btnArticulos.setOnClickListener(new homeActivity.listenerArticulos());
        btnClientes.setOnClickListener(new homeActivity.listenerClientes());

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

    //Listener boton Facturas
    class listenerFacturas implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showFacturaActivity(strEmail);
        }
    }

    //Listener boton Perfil
    class listenerPerfil implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showDatosPerfilActivity(strEmail);
        }
    }

    class listenerClientes implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showClientesActivity(strEmail);
        }
    }

    //Listener boton Articulos
    class listenerArticulos implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            showArticulosActivity(strEmail);
        }
    }

    //Muestra PerfilActivity
    private void showFacturaActivity(String strEmail){

        //Crea Intents para NuevaFacturaActivity

        Intent i = new Intent(this, VerFacturaActivity.class);

        i.putExtra("DatosEmail", strEmail);

        startActivity(i);

    }

    //Muestra DatosPerfilActivity
    private void showDatosPerfilActivity(String strEmail){

        //Crea Intents para DatosPerfilActivity

        Intent i2 = new Intent(this, VerPerfilActivity.class);

        //Manda datos a DatosPerfilActivity

        i2.putExtra("DatosEmail", strEmail);

        startActivity(i2);

    }

    private void showClientesActivity(String strEmail){

        //Crea un Intent para VerClientesActivity
        Intent i3 = new Intent(this, VerClientesActivity.class);

        //Pasa el email como extra
        i3.putExtra("DatosEmail", strEmail);

        //Inicia la actividad
        startActivity(i3);
    }

    //Muestra ArticulosActivity
    private void showArticulosActivity(String strEmail){

        //Crea Intents para NuevaFacturaActivity

        Intent i3 = new Intent(this, VerArticuloActivity.class);

        i3.putExtra("DatosEmail", strEmail);

        startActivity(i3);

    }
    
}
