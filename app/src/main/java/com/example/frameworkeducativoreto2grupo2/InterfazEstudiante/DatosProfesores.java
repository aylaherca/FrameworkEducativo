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

import com.example.frameworkeducativoreto2grupo2.Clases.User;
import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.util.ArrayList;
import java.util.List;

public class DatosProfesores extends AppCompatActivity {

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

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasME2);
        Spinner spinnerNombre = findViewById(R.id.spinnerNombre);
        Spinner spinnerApeliido = findViewById(R.id.spinnerApellido);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));

        //crear lista de user de prueba******************************************** BORRAR
        List<User> listaUsers = new ArrayList<>();
        listaUsers.add(new User(
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

        listaUsers.add(new User(
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

        //setear el adapter
        UserAdapter adapter = new UserAdapter(this, listaUsers);
        recyclerViewEstudiantes.setAdapter(adapter);


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(DatosProfesores.this, MenuEstudiante.class);
            startActivity(menuEstudiante);
        });
    }
}