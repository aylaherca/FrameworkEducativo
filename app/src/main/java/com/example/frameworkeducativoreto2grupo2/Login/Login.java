package com.example.frameworkeducativoreto2grupo2.Login;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frameworkeducativoreto2grupo2.Clases.TipoUsuario;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazEstudiante.MenuEstudiante;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.Metodos.Metodos;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Login extends AppCompatActivity {

    private DataInputStream dis;
    private DataOutputStream dos;
    private int readIDUsuario;
    private String readTipoUsuario;

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
        textViewRecuperarContrasena.setOnClickListener(view -> {
            //query buscar email + mandar email + guardar en la bd la contraseña nueva hash*************************

            sendEmail();

            Toast.makeText(Login.this, getString(R.string.toastRecuperarC), Toast.LENGTH_SHORT).show();

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
                        readIDUsuario = dis.readInt();
                        readTipoUsuario = dis.readUTF();
                        if (conexionCorrecta) {
                            //leemos el ID del usuario logueado para usarlo en la app
                            if ((tipoUser.equals("Alumno") && readTipoUsuario.equals("Alumno"))) {
                                runOnUiThread(() -> Toast.makeText(this, "login correcto." + String.valueOf(readIDUsuario), Toast.LENGTH_SHORT).show());
                                Intent intentEstudiante = new Intent(Login.this, MenuEstudiante.class);
                                intentEstudiante.putExtra("IDUserLog", readIDUsuario);
                                intentEstudiante.putExtra("tipoUser", tipoUser);
                                startActivity(intentEstudiante);

                            } else if ((tipoUser.equals("Profesor") && readTipoUsuario.equals("Profesor"))) {
                                runOnUiThread(() -> Toast.makeText(this, "login correcto." + String.valueOf(readIDUsuario), Toast.LENGTH_SHORT).show());
                                Intent intentProfesor = new Intent(Login.this, MenuProfesor.class);
                                intentProfesor.putExtra("IDUserLog", readIDUsuario);
                                intentProfesor.putExtra("tipoUser", tipoUser);
                                startActivity(intentProfesor);

                            }

                        } else {
                            runOnUiThread(() -> Toast.makeText(Login.this, "No existe el usuario" + String.valueOf(readIDUsuario), Toast.LENGTH_SHORT).show());
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //resetear los campos
                    usuario.setText("");
                    contrasena.setText("");
                    txtUsuario = "";
                    txtContrasena = "";

                } else {
                    runOnUiThread(() -> Toast.makeText(Login.this, "Hay campos vacios.", Toast.LENGTH_SHORT).show());
                }
            }).start();


        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"ayla.hernandezca@elorrieta-errekamari.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: contraseña nueva");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Contraseña nueva: 123contraseña");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Login.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    //METODO CONECTAR CON EL SERVIDOR
    private void concectarConServidor() throws IOException, ClassNotFoundException {
        Cliente.getInstance();
        dos = Cliente.getInstance().getDataOutputStream();
        dis = Cliente.getInstance().getDataInputStream();

    }


}