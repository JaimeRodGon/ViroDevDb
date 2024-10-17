package com.android.virodevdb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PerfilActivity extends AppCompatActivity {
    //Variables
    private String StrEmail;
    private TextView tvEmail;


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

        //Variables TextView
        tvEmail = findViewById(R.id.tvEmail);

        //Recibe datosEmail
        Intent recibir = getIntent();
        StrEmail = recibir.getStringExtra("DatosEmail");

        //Llama a Setup
        Setup();

    }
    //Setup
    private void Setup (){
    //Recibe datos variable AuthActivity
    this.tvEmail.setText(StrEmail);

}


}