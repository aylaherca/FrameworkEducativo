package com.example.frameworkeducativoreto2grupo2.CrearReunion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.frameworkeducativoreto2grupo2.Clases.TipoUsuario;
import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.PerfilProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Modelo.Users;

public class CrearReunion extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    Users user = new Users();
    private List<Users> listaUsers = new ArrayList<>();

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

        //setear el nombre del solicitante en el campo nombreuser*********************
        txtNombreUser.setText(user.getNombre());

        //setear en el spinner los profesores o alumnos, dependiendo del tipo de user
        if (user.getTipo().equals(TipoUsuario.PROFESOR.getTipoUser())) { //si es un profesor --> lista de alumnos
            //alumnos
            new Thread(() -> {
                try {
                    //Opcion seleccionada 10 recoger alumnos
                    dos.writeInt(10);
                    dos.flush();

                    //leer el List
                    listaUsers = (List<Users>) ois.readObject();

                    //actualizamos el spinner con alumnos***


                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else { //si es un alumno --> lista de profesores
            //profesores
            new Thread(() -> {
                try {
                    //opcion seleccionada 11 obtener profesores
                    dos.writeInt(11);
                    dos.flush();

                    //leer el List
                    listaUsers = (List<Users>) ois.readObject();

                    //actualizamos el spinner con profesores***


                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        String userReunion = ""; //la persona con la que se quiere tener la reunion recogido del spinner

        //listener boton nueva reunion ------------------------------------------------------------------------------- BOTON CREAR NUEVA REUNION
        btnCrearNuevaReunion.setOnClickListener(view -> {
            //mandar los datos al servidor
            new Thread(() -> {
                try {
                    //Opcion seleccionada - enviar datos para crear reunion
                    dos.writeInt(9);
                    dos.flush();

                    //datos
                    dos.writeUTF(String.valueOf(txtTitulo.getText()));
                    dos.flush();

                    dos.writeUTF(String.valueOf(txtTema.getText()));
                    dos.flush();

                    //calendario DATE**********************************

                    dos.writeUTF((String) txtNombreUser.getText());
                    dos.flush();

                    dos.writeUTF(userReunion);
                    dos.flush();

                    dos.writeUTF(String.valueOf(txtUbicacion.getText()));
                    dos.flush();

                    dos.writeUTF(String.valueOf(txtAula.getText())); //QUE DATATYPE ES??************************
                    dos.flush();

                    runOnUiThread(() -> Toast.makeText(this, "rEUNION CREADA", Toast.LENGTH_SHORT).show());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        });


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(CrearReunion.this, MenuProfesor.class);
            startActivity(menuProfesor);
        });



    }
}