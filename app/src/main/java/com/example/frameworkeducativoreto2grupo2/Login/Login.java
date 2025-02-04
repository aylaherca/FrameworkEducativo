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

import com.example.frameworkeducativoreto2grupo2.Clases.MailSender;
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
    private String emailTo;
    private String txtUsuario;
    private String txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Metodos metodos = new Metodos();

        new Thread(() -> {

            try {
                concectarConServidor();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();


        //variables
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView textViewRecuperarContrasena = findViewById(R.id.textViewRecuperarContrasena);
        EditText usuario = findViewById(R.id.editTextUsuario);
        EditText contrasena = findViewById(R.id.editTextContrasena);

        //listener recuperar contraseña
        textViewRecuperarContrasena.setOnClickListener(view -> {
            new Thread(() -> {
                txtUsuario = String.valueOf(usuario.getText()).trim();

                //comprobar el campo vacio del usuario
                if (metodos.comprobarUserVacio(txtUsuario)) {
                    //llamamos al servidor para validar el login
                    try {
                        //Opcion seleccionada 16 obtener contraseña usuario
                        dos.writeInt(16);
                        dos.flush();

                        //mandar el user
                        dos.writeUTF(txtUsuario);
                        dos.flush();

                        //leer el email del user
                        emailTo = dis.readUTF();

                        if (emailTo != null) {
                            String contrasenaNueva = Metodos.generarNuevaContraseña();

                            //mandamos el email con la contraseña nueva y updateamos la bd
                            mandarEmailRecContraseña(emailTo, contrasenaNueva);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastCamposVacios), Toast.LENGTH_SHORT).show());
                }


            }).start();
        });

        //boton login
        btnLogin.setOnClickListener(view -> {

            new Thread(() -> {

                txtUsuario = String.valueOf(usuario.getText()).trim();
                txtContrasena = String.valueOf(contrasena.getText()).trim();

                //comprobar campos vacios en el login
                if (metodos.comprobarLoginCamposVacios(txtUsuario, txtContrasena)) { //no hay campos vacios

                    //llamamos al servidor para validar el login
                    try {
                        //opcion seleccionada 1 Login
                        dos.writeInt(1);
                        dos.flush();

                        //string para el server saber que viene del movil (contraseña hash)
                        dos.writeUTF("movil");
                        dos.flush();

                        //campo nombre
                        dos.writeUTF(txtUsuario);
                        dos.flush();

                        //campo contraseña
                        dos.writeUTF(txtContrasena);
                        dos.flush();
                        Log.d("LOGIN", "boton clickado...");//*****************************************************************************
                        //leer servidor
                        boolean conexionCorrecta = dis.readBoolean();
                        readIDUsuario = dis.readInt();
                        readTipoUsuario = dis.readUTF();

                        if (conexionCorrecta) {
                            runOnUiThread(() -> {
                                Toast.makeText(Login.this, getString(R.string.toastLoginCorrecto), Toast.LENGTH_SHORT).show();
                                // Iniciar la actividad en el UI thread
                                if (readTipoUsuario.equals(TipoUsuario.ALUMNO.getTipoUser())) {
                                    Log.d("LOGIN", "Iniciando actividad de estudiante...");
                                    Intent intentEstudiante = new Intent(Login.this, MenuEstudiante.class);
                                    startActivity(intentEstudiante);
                                } else if (readTipoUsuario.equals(TipoUsuario.PROFESOR.getTipoUser())) {
                                    Log.d("LOGIN", "Iniciando actividad de profesor...");
                                    Intent intentProfesor = new Intent(Login.this, MenuProfesor.class);
                                    startActivity(intentProfesor);
                                }

                                // Opcional: Cerrar la actividad de login si no quieres que vuelvan atrás
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastNoExisteUser), Toast.LENGTH_SHORT).show());
                        }
                    } catch (IOException e) {
                        Log.e("LOGIN", "Error en la conexión con el servidor", e);
                        runOnUiThread(() -> Toast.makeText(Login.this, "Error en la conexión", Toast.LENGTH_SHORT).show());
                    }

                    // Resetear los campos en el UI thread
                    runOnUiThread(() -> {
                        usuario.setText("");
                        contrasena.setText("");
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastCamposVacios), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    //METODO MANDAR EL EMAIL
    protected void mandarEmailRecContraseña(String emailTo, String contrasenaNueva) {
        new Thread(() -> {
            try {
                if (emailTo != null && !emailTo.isEmpty()) {
                    MailSender.sendEmail(emailTo, contrasenaNueva);

                    //cuando se manda el email se cambia la contraseña en la base de datos
                    //opcion seleccionada 17 update contraseña
                    dos.writeInt(17);
                    dos.flush();

                    //email usuario
                    dos.writeUTF(emailTo);
                    dos.flush();

                    //contraseña nueva
                    dos.writeUTF(contrasenaNueva);
                    dos.flush();

                    runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastRecuperarC), Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toatsEmailNoEncontrado), Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastErrorMandarEmail), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    //METODO CONECTAR CON EL SERVIDOR
    private void concectarConServidor() throws IOException, ClassNotFoundException {
        Cliente.getInstance();
        dos = Cliente.getInstance().getDataOutputStream();
        dis = Cliente.getInstance().getDataInputStream();

    }


}