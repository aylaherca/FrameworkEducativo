package com.example.frameworkeducativoreto2grupo2.CrearReunion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CrearReunion extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    int IDUserLog;

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

        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getExtra

        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasMP3);
        Button btnCrearNuevaReunion = findViewById(R.id.btnCrearNuevaReunion);

        EditText txtTitulo = findViewById(R.id.editTextTitulo);
        EditText txtTema = findViewById(R.id.editTextTextMultiLineTema);
        CalendarView diaReunion = findViewById(R.id.calendarView);
        TextView txtNombreUser = findViewById(R.id.txtNombreUser);
        Spinner spinnerUserReunion = findViewById(R.id.spinnerUser);
        EditText txtUbicacion = findViewById(R.id.editTextUbicacion);
        EditText txtAula = findViewById(R.id.editTextAula);







        //listener boton nueva reunion ------------------------------------------------------------------------------- BOTON CREAR NUEVA REUNION
        btnCrearNuevaReunion.setOnClickListener(view -> {
            //*****agregar los datos a la db
        });


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(CrearReunion.this, MenuProfesor.class);
            menuProfesor.putExtra("IDUserLog", IDUserLog);
            startActivity(menuProfesor);
        });



    }
}