package com.example.frameworkeducativoreto2grupo2.CrearReunion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.example.frameworkeducativoreto2grupo2.Clases.TipoUsuario;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazEstudiante.MenuEstudiante;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Modelo.Colegio;
import Modelo.Reuniones;
import Modelo.Users;

public class CrearReunion extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private Users user = new Users();
    private Colegio colegio = new Colegio();
    private List<Colegio> listaColegios = new ArrayList<>();
    private List<Users> listaUsers = new ArrayList<>();
    private List<String[]> listaUsersSpinner = new ArrayList<>();
    private int IDUserReunion;
    private int IDColegio;

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
        Spinner spinnerColegio = findViewById(R.id.spinnerColegio);
        EditText txtAula = findViewById(R.id.editTextAula);

        //hilos recoger datos
        new Thread(() -> {
            //primero recoge datos de usuario
            recogerDatosUser(txtNombreUser, spinnerUserReunion);

            //cuando el anterior termine, recoge datos de los colegios
            recogerDatosColegios(spinnerColegio);
        }).start();

        //setear fecha y hora picker
        ponerDateTimePickers(btnEscogerFecha, btnEscogerHora);

        //listener boton nueva reunion ------------------------------------------------------------------------------- BOTON CREAR NUEVA REUNION
        btnCrearNuevaReunion.setOnClickListener(view -> {
            Reuniones nuevaReunion = new Reuniones();

            //datos
            if (user.getTipo() != null && user.getTipo().equals(TipoUsuario.PROFESOR.getTipoUser())) {
                nuevaReunion.setUsersByProfesorId(user);
                nuevaReunion.setUsersByAlumnoId(listaUsers.get(IDUserReunion));
            } else {
                nuevaReunion.setUsersByProfesorId(listaUsers.get(IDUserReunion));
                nuevaReunion.setUsersByAlumnoId(user);
            }

            nuevaReunion.setTitulo(txtTitulo.getText().toString());
            nuevaReunion.setAsunto(txtTema.getText().toString());
            nuevaReunion.setAula(txtAula.getText().toString());
            nuevaReunion.setIdCentro(String.valueOf(IDColegio));
            nuevaReunion.setFecha(obtenerFechayHora());

            //mandar los datos de la reunion al servidor
            new Thread(() -> enviarDatosReunion(nuevaReunion)).start();
        });

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            if (user.getTipo() != null && user.getTipo().equals(TipoUsuario.PROFESOR.getTipoUser())) {
                Intent menuProfesor = new Intent(CrearReunion.this, MenuProfesor.class);
                startActivity(menuProfesor);
            } else {
                Intent menuProfesor = new Intent(CrearReunion.this, MenuEstudiante.class);
                startActivity(menuProfesor);
            }
        });
    }

    //obtener datos
    //user
    private void recogerDatosUser(TextView txtNombreUser, Spinner spinnerUserReunion) {
        try {
            //opcion seleccionada 18 recoger datos user
            int accion = 18;
            dos.writeInt(accion);
            dos.flush();

            //leer el usuario
            //Users userLog = (Users) ois.readObject();

            //leer datos
            int ID = dis.readInt();
            String tipoUserLog = dis.readUTF();

            runOnUiThread(() -> {
                //setear el id del solicitante en el campo nombreuser
                txtNombreUser.setText(String.valueOf(ID));

                //actualizar los spinner en el main
                if (tipoUserLog != null && tipoUserLog.equals(TipoUsuario.PROFESOR.getTipoUser())) {
                    obtenerAlumnosParaProfesores(spinnerUserReunion);
                } else {
                    obtenerProfesoresParaAlumnos(spinnerUserReunion);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //colegios
    private void recogerDatosColegios(Spinner spinnerColegio) {
        try {
            //opcion seleccionada 15 recoger datos colegios
            int accion = 15;
            dos.writeInt(accion);
            dos.flush();

            Log.d("ACCIONNNNNNNNNNNNNN", "accion: " + accion);

            //lista colegios
            listaColegios = (List<Colegio>) ois.readObject();

            //recogemos los nombres de los colegios obtenidos
            List<String> nombresColegios = new ArrayList<>();
            for (Colegio colegio : listaColegios) {
                nombresColegios.add(colegio.getNombre());
            }

            //actualizar el spinner
            runOnUiThread(() -> {
                //arrayadapter para el spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearReunion.this, android.R.layout.simple_spinner_item, nombresColegios);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //seteamos el adapter al spinner
                spinnerColegio.setAdapter(adapter);

                //listener para el colegio seleccionado
                spinnerColegio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //recogemos el id del colegio seleccionado
                        IDColegio = listaColegios.get(position).getIdCentro();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //si no se selecciona nada poner uno por defecto
                        if (!listaColegios.isEmpty()) {
                            colegio = listaColegios.get(0);
                        }
                    }
                });
            });
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //fecha y hora picker
    private void ponerDateTimePickers(Button btnEscogerFecha, Button btnEscogerHora) {
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
    }

    //obtener fecha y hora
    private Timestamp obtenerFechayHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            String fechaString = sdf.format(Calendar.getInstance().getTime());
            return Timestamp.valueOf(fechaString + ":00");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    //enviar la reunion creada al servidor
    private void enviarDatosReunion(Reuniones nuevaReunion) {
        try {
            //opcion seleccionada 14 enviar datos para crear reunion
            int accion = 14;
            dos.writeInt(accion);
            dos.flush();

            Log.d("ACCIONNNNNNNNNNNNNN", "accion: " + accion);

            synchronized (oos) { //para que no salte error
                //enviar objeto reunion
                oos.writeObject(nuevaReunion);
                oos.flush();
            }
            runOnUiThread(() -> Toast.makeText(this, "REUNION CREADA", Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //lista de alumnos
    private void obtenerAlumnosParaProfesores(Spinner spinnerUserReunion) {
        new Thread(() -> {
            try {
                //opcion seleccionada 20 recoger datos alumnos
                int accion = 20;
                dos.writeInt(accion);
                dos.flush();

                //lista de alumnos
                listaUsersSpinner = (List<String[]>) ois.readObject();
                runOnUiThread(() -> {
                    if (listaUsersSpinner != null && !listaUsersSpinner.isEmpty()) {
                        actualizarSpinner(spinnerUserReunion, listaUsersSpinner);
                    } else {
                        Log.d("ERROR", "Lista de usuarios está vacía.");
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //lista de profesores
    private void obtenerProfesoresParaAlumnos(Spinner spinnerUserReunion) {
        new Thread(() -> {
            try {
                //opcion seleccionada 19 recoger datos profesores
                int accion = 19;
                dos.writeInt(accion);
                dos.flush();

                Log.d("LISTAAAAAAAAAAAAA", "Lista de usuarios recibida1: " + listaUsersSpinner.size());
                //lista de profesores
                listaUsersSpinner = (List<String[]>) ois.readObject();
                Log.d("LISTAAAAAAAAAAAAA", "Lista de usuarios recibida2length: " + listaUsersSpinner.size());
                runOnUiThread(() -> {
                    if (listaUsersSpinner != null && !listaUsersSpinner.isEmpty()) {
                        actualizarSpinner(spinnerUserReunion, listaUsersSpinner);
                    } else {
                        Log.d("ERROR", "Lista de usuarios está vacía.");
                    }
                });
                Log.d("LISTAAAAAAAAAAAAA", "Lista de usuarios recibida2: " + listaUsersSpinner.size());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //actualizar el spinner
    private void actualizarSpinner(Spinner spinnerUserReunion, List<String[]> listaUsersSpinner) {
        List<String> nombresApellidos = new ArrayList<>();

        //combinamos nombre y apellido para cada usuario
        Log.d("LISTAAAAAAAAAAAAA", "Lista de usuarios recibida3: " + listaUsersSpinner.size());
        for (String[] userInfo : listaUsersSpinner) {
            //concatenar nombre y apellido desde el String[] (userInfo)
            String nombreCompleto = userInfo[1] + " " + userInfo[2]; // [1] = Name, [2] = Surname
            Log.d("LISTAAAAAAAAAAAAA", "Nombre completo4: " + nombreCompleto); // Log the name
            nombresApellidos.add(nombreCompleto);
        }

        //arrayAdapter con el arraylist de nombre y apellido
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearReunion.this, android.R.layout.simple_spinner_item, nombresApellidos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setear el adapter al spinner
        spinnerUserReunion.setAdapter(adapter);

        //listener para el spinner
        spinnerUserReunion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //obtener el ID del usuario seleccionado (desde listaUsersSpinner)
                IDUserReunion = Integer.parseInt(listaUsersSpinner.get(position)[0]); //ID
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                IDUserReunion = -1;  //si no se ha seleccionado nada
            }
        });
    }

}
