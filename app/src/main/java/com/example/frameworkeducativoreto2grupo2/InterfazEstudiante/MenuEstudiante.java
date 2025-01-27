package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.ConsultarReunion.ConsultarReuniones;
import com.example.frameworkeducativoreto2grupo2.CrearReunion.CrearReunion;
import com.example.frameworkeducativoreto2grupo2.R;

public class MenuEstudiante extends AppCompatActivity {

    int IDUserLog;
    String tipoUserLogeado;

    private boolean subBtnVisible = false; //para checkear si los botones secundarios estan desplegados o no

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudiante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getIntExtra
        tipoUserLogeado = intent.getStringExtra("tipoUser");

        //botones
        Button btnConsultarHorarios = findViewById(R.id.btnConsultarHorarios);
        Button btnReuniones = findViewById(R.id.btnReuniones);
        Button btnHorariosPropios = findViewById(R.id.btnHorariosPropios);
        Button btnHorariosProfesores = findViewById(R.id.btnHorariosProfesores);
        Button btnCrearReunion = findViewById(R.id.btnCrearReunion);
        Button btnConsultarReuniones = findViewById(R.id.btnConsultarReuniones);

        ImageButton btnPerfil = findViewById(R.id.btnPerfil);
        ImageButton btnDesconectar = findViewById(R.id.btnDesconectar);

        //esconder los botones secundarios
        btnHorariosPropios.setVisibility(View.GONE);
        btnHorariosProfesores.setVisibility(View.GONE);
        btnCrearReunion.setVisibility(View.GONE);
        btnConsultarReuniones.setVisibility(View.GONE);


        //listener boton horarios ------------------------------------------------------------------------------- BOTON CONSULTAR HORARIOS
        btnConsultarHorarios.setOnClickListener(view -> {
            if (subBtnVisible) { //si los botones secundarios estan visibles al ir a clikar
                //esconder los botones secundarios y hacer visible el de reuniones
                btnHorariosPropios.setVisibility(View.GONE);
                btnHorariosProfesores.setVisibility(View.GONE);
                btnReuniones.setVisibility(View.VISIBLE);

                //resetear las posiciones de los botones
                btnConsultarHorarios.animate().translationY(0).setDuration(300);
                btnReuniones.animate().translationY(0).setDuration(300);

                //resetear el boton principal al tamaño original
                btnConsultarHorarios.animate().scaleX(1f).scaleY(1f).setDuration(200);


            } else {
                //hacer visibles los botones secundarios
                btnHorariosPropios.setVisibility(View.VISIBLE);
                btnHorariosProfesores.setVisibility(View.VISIBLE);
                btnReuniones.setVisibility(View.GONE);

                //ajustar las posiciones
                btnConsultarHorarios.animate().translationY(-100).setDuration(300); //mover hacia arriba
                btnHorariosPropios.animate().translationY(25).setDuration(300);
                btnHorariosProfesores.animate().translationY(100).setDuration(300);

                //encoger el boton principal
                btnConsultarHorarios.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200);
            }

            //alternar la visibilidad de los botones secundarios
            subBtnVisible = !subBtnVisible;
        });

        //listener boton reuniones ------------------------------------------------------------------------------- BOTON REUNIONES
        btnReuniones.setOnClickListener(view -> {
            if (subBtnVisible) { //si los botones secundarios estan visibles al clikar
                //esconder los botones secundarios y hacer visible el de reuniones
                btnCrearReunion.setVisibility(View.GONE);
                btnConsultarReuniones.setVisibility(View.GONE);
                btnConsultarHorarios.setVisibility(View.VISIBLE);

                //resetear las posiciones de los botones
                btnReuniones.animate().translationY(0).setDuration(300);
                btnConsultarHorarios.animate().translationY(0).setDuration(300);

                //resetear el boton principal al tamaño original
                btnReuniones.animate().scaleX(1f).scaleY(1f).setDuration(200);

            } else {
                //hacer visibles los botones secundarios
                btnCrearReunion.setVisibility(View.VISIBLE);
                btnConsultarReuniones.setVisibility(View.VISIBLE);
                btnConsultarHorarios.setVisibility(View.GONE);

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

        //listener boton mis horarios ------------------------------------------------------------------------------- BOTON MIS HORARIOS
        btnHorariosPropios.setOnClickListener(view -> {
            Intent intentHorarioEstudiante = new Intent(MenuEstudiante.this, HorarioEstudiante.class);
            intentHorarioEstudiante.putExtra("IDUserLog", IDUserLog);
            startActivity(intentHorarioEstudiante);
        });

        //listener boton horarios profesores ------------------------------------------------------------------------------- BOTON HORARIOS PROFESORES
        btnHorariosProfesores.setOnClickListener(view -> {
            Intent intentListaProfesores = new Intent(MenuEstudiante.this, DatosProfesores.class);
            intentListaProfesores.putExtra("IDUserLog", IDUserLog);
            intentListaProfesores.putExtra ("tipoUser", tipoUserLogeado);
            startActivity(intentListaProfesores);
        });

        //listener boton crear reunion ------------------------------------------------------------------------------- BOTON CREAR REUNION
        btnCrearReunion.setOnClickListener(view -> {
            Intent intentCrearReunion = new Intent(MenuEstudiante.this, CrearReunion.class);
            startActivity(intentCrearReunion);
        });

        //listener boton consultar reuniones ------------------------------------------------------------------------------- BOTON CONSULTAR REUNIONES
        btnConsultarReuniones.setOnClickListener(view -> {
            Intent intentConsultarReuniones = new Intent(MenuEstudiante.this, ConsultarReuniones.class);
            startActivity(intentConsultarReuniones);
        });


        //listener boton imagen perfil ------------------------------------------------------------------------------- BOTON IMAGEN PERFIL
        btnPerfil.setOnClickListener(view -> {
            Intent intentEstudiantePerfil = new Intent(MenuEstudiante.this, PerfilEstudiante.class);
            startActivity(intentEstudiantePerfil);
        });

        //listener boton desconectar ------------------------------------------------------------------------------- BOTON DESCONECTAR
        btnDesconectar.setOnClickListener(view -> {
            //desconectar + cerrar(?)/ir al login
        });


    }
}