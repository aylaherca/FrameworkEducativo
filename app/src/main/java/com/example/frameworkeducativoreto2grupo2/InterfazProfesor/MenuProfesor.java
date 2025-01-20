package com.example.frameworkeducativoreto2grupo2.InterfazProfesor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.R;

public class MenuProfesor extends AppCompatActivity {

    private boolean subBtnVisible = false; //para checkear si los botones secundarios estan desplegados o no


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //botones
        Button btnConsultarDatosEstudiantes = findViewById(R.id.btnConsultarDatosEstudiantes);
        Button btnConsultarHorarios = findViewById(R.id.btnConsultarHorarios);
        Button btnReuniones = findViewById(R.id.btnReuniones);
        Button btnCrearReunion = findViewById(R.id.btnCrearReunion);
        Button btnConsultarReuniones = findViewById(R.id.btnConsultarReuniones);

        ImageButton btnPerfil = findViewById(R.id.btnPerfil);
        ImageButton btnDesconectar = findViewById(R.id.btnDesconectar);

        //esconder los botones secundarios
        btnCrearReunion.setVisibility(View.GONE);
        btnConsultarReuniones.setVisibility(View.GONE);


        //listener boton horarios ------------------------------------------------------------------------------- BOTON CONSULTAR HORARIOS
        btnConsultarHorarios.setOnClickListener(view -> {

            //activity horarios***

        });

        //listener boton reuniones ------------------------------------------------------------------------------- BOTON REUNIONES
        btnReuniones.setOnClickListener(view -> {
            if (subBtnVisible) { //si los botones secundarios estan visibles al clikar
                //esconder los botones secundarios y hacer visible el de reuniones
                btnCrearReunion.setVisibility(View.GONE);
                btnConsultarReuniones.setVisibility(View.GONE);
                btnConsultarHorarios.setVisibility(View.VISIBLE);
                btnConsultarDatosEstudiantes.setVisibility(View.VISIBLE);

                //resetear las posiciones de los botones
                btnReuniones.animate().translationY(0).setDuration(300);
                btnConsultarHorarios.animate().translationY(0).setDuration(300);
                btnConsultarDatosEstudiantes.animate().translationY(0).setDuration(300);

                //resetear el boton principal al tamaño original
                btnReuniones.animate().scaleX(1f).scaleY(1f).setDuration(200);

            } else {
                //hacer visibles los botones secundarios
                btnCrearReunion.setVisibility(View.VISIBLE);
                btnConsultarReuniones.setVisibility(View.VISIBLE);
                btnConsultarHorarios.setVisibility(View.GONE);
                btnConsultarDatosEstudiantes.setVisibility(View.GONE);

                //ajustar las posiciones
                btnReuniones.animate().translationY(-100).setDuration(300); //mover hacia arriba
                btnCrearReunion.animate().translationY(25).setDuration(300);
                btnConsultarReuniones.animate().translationY(100).setDuration(300);

                //encoger el boton principal
                btnReuniones.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200);
            }

            //alternar la visibilidad de los borones secundarios
            subBtnVisible = !subBtnVisible;

        });

        //listener boton crear reunion ------------------------------------------------------------------------------- BOTON CREAR REUNION
        //listener boton consultar reuniones ------------------------------------------------------------------------------- BOTON CONSULTAR REUNIONES


        //listener boton imagen perfil ------------------------------------------------------------------------------- BOTON IMAGEN PERFIL
        btnPerfil.setOnClickListener(view -> {
            Intent intentEstudiantePerfil = new Intent(MenuProfesor.this, PerfilProfesor.class);
            startActivity(intentEstudiantePerfil);
        });

        //listener boton desconectar ------------------------------------------------------------------------------- BOTON DESCONECTAR
        btnDesconectar.setOnClickListener(view -> {
            //desconectar + cerrar(?)/ir al login
        });

    }
}