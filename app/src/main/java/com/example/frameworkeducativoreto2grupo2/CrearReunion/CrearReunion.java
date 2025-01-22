package com.example.frameworkeducativoreto2grupo2.CrearReunion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

public class CrearReunion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_reunion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasMP3);
        Button btnCrearNuevaReunion = findViewById(R.id.btnCrearNuevaReunion);




        //listener boton nueva reunion ------------------------------------------------------------------------------- BOTON CREAR NUEVA REUNION
        btnCrearNuevaReunion.setOnClickListener(view -> {
            //*****
        });


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(CrearReunion.this, MenuProfesor.class);
            startActivity(menuProfesor);
        });



    }
}