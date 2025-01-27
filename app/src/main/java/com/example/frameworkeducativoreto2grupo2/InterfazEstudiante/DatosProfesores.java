package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Modelo.Users;
import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.R;

import java.util.ArrayList;
import java.util.List;

public class DatosProfesores extends AppCompatActivity {

    int IDUserLog;
    String tipoUserLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos_profesores);
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


        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasME2);
        Spinner spinnerNombre = findViewById(R.id.spinnerNombre);
        Spinner spinnerApeliido = findViewById(R.id.spinnerApellido);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));

        //crear lista de user de prueba******************************************** BORRAR
        List<Users> listaUsers = new ArrayList<>();
        listaUsers.add(new Users(
                1,
                "irakasle1@irakasle.com",
                "irakasle1",
                "123",
                "Nerea",
                "Garcia",
                "12345678A",
                "c/ sabino arana",
                123456789,
                null,
                "Profesor",
                "https://www.fastweb.com/uploads/article_photo/photo/2036641/10-ways-to-be-a-better-student.jpeg"
        ));

        listaUsers.add(new Users(
                2,
                "irakasle2@irakasle.com",
                "irakasle2",
                "456",
                "Jon",
                "Garcia",
                "87654321B",
                "c/ bilbao",
                987654321,
                null,
                "Profesor",
                "https://studyportals.com/app/uploads/2024/11/shutterstock_2484576879-640x560.jpg"
        ));

        //setear el adapter y los clicks
        UserAdapter adapter = new UserAdapter(this, listaUsers, new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(Users user) {
                    Intent intentDatosProfesorHorario = new Intent(DatosProfesores.this, DatosProfesorHorario.class);
                    intentDatosProfesorHorario.putExtra("IDUserLog", IDUserLog);
                    intentDatosProfesorHorario.putExtra("tipoUser", IDUserLog);
                    startActivity(intentDatosProfesorHorario);

            }
        });
        recyclerViewEstudiantes.setAdapter(adapter);


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(DatosProfesores.this, MenuEstudiante.class);
            menuEstudiante.putExtra("IDUserLog", IDUserLog);
            startActivity(menuEstudiante);
        });
    }
}