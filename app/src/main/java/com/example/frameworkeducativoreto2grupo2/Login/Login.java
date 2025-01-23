package com.example.frameworkeducativoreto2grupo2.Login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.frameworkeducativoreto2grupo2.InterfazEstudiante.PerfilEstudiante;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.Metodos.Metodos;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity {

    private DataInputStream dis;
    private DataOutputStream dos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Metodos metodos = new Metodos();

        new Thread(() -> {

            try {
                concectarConServidor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();


        //variables
        Button btnLogin = findViewById(R.id.btnLogin);
        Spinner spinnerTipoUser = findViewById(R.id.spinnerTipoUser);
        TextView textViewRecuperarContrasena = findViewById(R.id.textViewRecuperarContrasena);
        EditText usuario = findViewById(R.id.editTextUsuario);
        EditText contrasena = findViewById(R.id.editTextContrasena);

        //listener recuperar contraseña
        textViewRecuperarContrasena.setOnClickListener(v -> {
            Toast.makeText(Login.this, "Por favor, revisa tu correo para recuperar la contraseña.", Toast.LENGTH_SHORT).show();

            //query buscar email + mandar email + guardar en la bd la contraseña nueva hash(?)

        });

        //poner los valores al spinner
        //arrayadapter usando los valores del enum para mostrar los nombres
        ArrayAdapter<TipoUsuario> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TipoUsuario.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoUser.setAdapter(adapter);

        //boton login
        btnLogin.setOnClickListener(view -> {
            new Thread(() -> {
            String txtUsuario = String.valueOf(usuario.getText()).trim();
            String txtContrasena = String.valueOf(contrasena.getText()).trim();
            //recoger el tipo de usuario seleccionado del spinner, usar name() para obtener el string representante del enum seleccionado ("profesor" o "estudiante")
            TipoUsuario tipoUsuarioSeleccionado = (TipoUsuario) spinnerTipoUser.getSelectedItem();
            String tipoUser = tipoUsuarioSeleccionado != null ? tipoUsuarioSeleccionado.getTipoUser() : "";

            //comprobar campos vacios en el login
            if (metodos.comprobarLoginCamposVacios(txtUsuario, txtContrasena)) { //no hay campos vacios


                //llamamos al servidor para validar el login*****


                    try {
                        //Opcion seleccionada 1 Login
                        dos.writeInt(1);
                        dos.flush();

                        //1º Campo nombre
                        dos.writeUTF(txtUsuario);
                        dos.flush();

                        //contras
                        dos.writeUTF(txtContrasena);
                        dos.flush();

                        //tipo user
                        dos.writeUTF(tipoUser);
                        dos.flush();

                        boolean conexionCorrecta = dis.readBoolean();
                        if(conexionCorrecta){
                            if (tipoUser.equals("Alumno")) {
                                Intent intentEstudiante = new Intent(Login.this, MenuEstudiante.class);
                                startActivity(intentEstudiante);

                            } else if (tipoUser.equals("Profesor")) {
                                Intent intentProfesor = new Intent(Login.this, MenuProfesor.class);
                                startActivity(intentProfesor);
                            }
                            //Toast.makeText(Login.this, "login correcto.", Toast.LENGTH_SHORT).show();


                        } else {
                        //    Toast.makeText(Login.this, "No existe el usuario", Toast.LENGTH_SHORT).show();

                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                //resetear los campos
                txtUsuario = "";
                txtContrasena = "";

            } else {
                //Toast.makeText(Login.this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();

            }
        }).start();

        });


    }

    private void concectarConServidor() throws IOException, ClassNotFoundException {
        Cliente.getInstance();
        dos = Cliente.getInstance().getDataOutputStream();
        dis = Cliente.getInstance().getDataInputStream();

    }


}