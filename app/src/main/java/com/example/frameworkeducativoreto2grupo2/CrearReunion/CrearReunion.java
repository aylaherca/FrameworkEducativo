package com.example.frameworkeducativoreto2grupo2.CrearReunion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Modelo.Users;

public class CrearReunion extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    Users user = new Users();
    private List<Users> listaUsers = new ArrayList<>();
    private UserAdapter adapter;

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
        Button btnEscogerFecha = findViewById(R.id.btnEscogerFecha);
        Button btnEscogerHora = findViewById(R.id.btnEscogerHora);
        TextView txtNombreUser = findViewById(R.id.txtNombreUser);
        Spinner spinnerUserReunion = findViewById(R.id.spinnerUser);
        EditText txtUbicacion = findViewById(R.id.editTextUbicacion);
        EditText txtAula = findViewById(R.id.editTextAula);

        new Thread(() -> {
            try {
                //Opcion seleccionada 9 recoger datos
                dos.writeInt(9);
                dos.flush();

                //leer el usuario
                user = (Users) ois.readObject();

                //poner los datos necesarios
                // setear el id del solicitante en el campo nombreuser
                runOnUiThread(() -> txtNombreUser.setText(user.getNombre()));

                // Setear en el spinner los profesores o alumnos, dependiendo del tipo de usuario
                runOnUiThread(() -> {
                    if (user.getTipo() != null && user.getTipo().equals(TipoUsuario.PROFESOR.getTipoUser())) {
                        // Si es un profesor --> lista de alumnos
                        new Thread(() -> {
                            try {
                                dos.writeInt(10);  // Recoger alumnos
                                dos.flush();

                                listaUsers = (List<Users>) ois.readObject();
                                //actualizar el spinner con los alumnos
                                runOnUiThread(() -> {
                                    //creamos una lista que recoja los nombres
                                    List<String> nombres = new ArrayList<>();
                                    for (Users u : listaUsers) {
                                        nombres.add(u.getNombre()); //nombres de los alumnos
                                    }

                                    //arrayadapter con la lista de los nombres
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearReunion.this, android.R.layout.simple_spinner_item, nombres);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    //seteamos el adapter al spinner
                                    spinnerUserReunion.setAdapter(adapter);
                                });


                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } else {
                        // Si es un alumno --> lista de profesores
                        new Thread(() -> {
                            try {
                                dos.writeInt(11);  // Recoger profesores
                                dos.flush();

                                listaUsers = (List<Users>) ois.readObject();
                                //actualizar el spinner con los profesores
                                runOnUiThread(() -> {
                                    //creamos una lista que recoja los nombres
                                    List<String> nombres = new ArrayList<>();
                                    for (Users u : listaUsers) {
                                        nombres.add(u.getNombre()); //nombres de los profesores
                                    }

                                    //arrayadapter con la lista de los nombres
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearReunion.this, android.R.layout.simple_spinner_item, nombres);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    //seteamos el adapter al spinner
                                    spinnerUserReunion.setAdapter(adapter);
                                });


                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                });


            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        String userReunion = ""; //la persona con la que se quiere tener la reunion recogido del spinner

        //calendario para guardar la fecha y hora escogida
        Calendar fechaHoraEsc = Calendar.getInstance();

        //fecha PICKER
        btnEscogerFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnEscogerFecha.setText(selectedDate);
                    },
                    fechaHoraEsc.get(Calendar.YEAR),
                    fechaHoraEsc.get(Calendar.MONTH),
                    fechaHoraEsc.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        //hora PICKER
        btnEscogerHora.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        String selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                        btnEscogerHora.setText(selectedTime);
                    },
                    fechaHoraEsc.get(Calendar.HOUR_OF_DAY),
                    fechaHoraEsc.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        // Format Calendar date-time into MySQL DATETIME format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaReunion = sdf.format(fechaHoraEsc.getTime()); //dato para enviar a la db

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