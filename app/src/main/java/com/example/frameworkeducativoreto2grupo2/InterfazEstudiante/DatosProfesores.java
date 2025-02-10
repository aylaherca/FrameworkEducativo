package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Modelo.Users;

import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatosProfesores extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private List<Users> listaUsers = new ArrayList<>();
    private UserAdapter adapter;

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
        Spinner spinnerApellido = findViewById(R.id.spinnerApellido);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);
        Button btnRestablecerFiltros = findViewById(R.id.btnRestablecerFiltro);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            try {
                Cliente cliente = Cliente.getInstance();
                oos = cliente.getObjectOutputStream();
                ois = cliente.getObjectInputStream();
                dos = cliente.getDataOutputStream();
                dis = cliente.getDataInputStream();

                //opcion seleccionada 11 obtener profesores
                dos.writeInt(11);
                dos.flush();

                //leemos la lista de profesores
                listaUsers = (List<Users>) ois.readObject();

                //actualizamos el recycler
                runOnUiThread(() -> {
                    adapter = new UserAdapter(DatosProfesores.this, listaUsers, user -> {
                        Intent intentDatosProfesorHorario = new Intent(DatosProfesores.this, DatosProfesorHorario.class);
                        intentDatosProfesorHorario.putExtra("IDProfesorSelec", user.getId());
                        Toast.makeText(this, "Profesor seleccionado: " + user.getId(), Toast.LENGTH_SHORT).show();
                        startActivity(intentDatosProfesorHorario);
                    });
                    recyclerViewEstudiantes.setAdapter(adapter);

                    //recogemos los nombres y apellidos de esta manera para que si hay repetidos, solo aparezcan una vez
                    Set<String> nombresSet = new HashSet<>();
                    Set<String> apellidosSet = new HashSet<>();


                    nombresSet.add(""); //primera opcion vacia por defecto
                    apellidosSet.add(""); //primera opcion vacia por defecto

                    for (Users user : listaUsers) {
                        nombresSet.add(user.getNombre());
                        apellidosSet.add(user.getApellidos());
                    }

                    //rellenar los spinners con los datos unicos
                    List<String> nombres = new ArrayList<>(nombresSet);
                    List<String> apellidos = new ArrayList<>(apellidosSet);

                    ArrayAdapter<String> nombreAdapter = new ArrayAdapter<>(DatosProfesores.this, android.R.layout.simple_spinner_item, nombres);
                    nombreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNombre.setAdapter(nombreAdapter);

                    ArrayAdapter<String> apellidoAdapter = new ArrayAdapter<>(DatosProfesores.this, android.R.layout.simple_spinner_item, apellidos);
                    apellidoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerApellido.setAdapter(apellidoAdapter);

                    //listeners a las selecciones de los spinner
                    spinnerNombre.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                            filtrarLista(spinnerNombre.getSelectedItem().toString(), spinnerApellido.getSelectedItem().toString(), adapter);
                        }

                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) {
                        }
                    });

                    spinnerApellido.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                            filtrarLista(spinnerNombre.getSelectedItem().toString(), spinnerApellido.getSelectedItem().toString(), adapter);
                        }

                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) {
                        }
                    });


                });

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //listener boton restablecer filtros ------------------------------------------------------------------------------- BOTON RESTABLECER FILTROS
        btnRestablecerFiltros.setOnClickListener(view -> {
            //resetear las opciones de los spinner al de por defecto
            spinnerNombre.setSelection(0);
            spinnerApellido.setSelection(0);

            //restablecer
            filtrarLista("", "", adapter);
        });


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(DatosProfesores.this, MenuEstudiante.class);
            startActivity(menuEstudiante);
        });
    }

    private void filtrarLista(String selectedNombre, String selectedApellido, UserAdapter adapter) {
        List<Users> listaFiltro = new ArrayList<>();

        for (Users user : listaUsers) {
            boolean matchNombre = selectedNombre.isEmpty() || user.getNombre().equals(selectedNombre);
            boolean matchApellido = selectedApellido.isEmpty() || user.getApellidos().equals(selectedApellido);

            if (matchNombre && matchApellido) {
                listaFiltro.add(user);
            }
        }

        adapter.actualizarLista(listaFiltro);
    }


}