package com.example.frameworkeducativoreto2grupo2.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

        //conectar con el servidor en un hilo separado
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
            txtUsuario = usuario.getText().toString().trim();
            recuperarContrasena(txtUsuario);
        });

        //boton login
        btnLogin.setOnClickListener(view -> {
            txtUsuario = usuario.getText().toString().trim();
            txtContrasena = contrasena.getText().toString().trim();
            iniciarSesion(txtUsuario, txtContrasena);
        });
    }

    //metodo para conectar con el servidor
    private void concectarConServidor() throws IOException, ClassNotFoundException {
        Cliente.getInstance();
        dos = Cliente.getInstance().getDataOutputStream();
        dis = Cliente.getInstance().getDataInputStream();
    }

    //metodo para recuperar la contraseña
    private void recuperarContrasena(String usuario) {
        new Thread(() -> {
            Metodos metodos = new Metodos();

            //comprobar el campo vacio del usuario
            if (metodos.comprobarUserVacio(usuario)) {
                try {
                    //opcion seleccionada 16 obtener contraseña usuario
                    dos.writeInt(16);
                    dos.flush();

                    //mandar el user
                    dos.writeUTF(usuario);
                    dos.flush();

                    //leer el email del usuario
                    emailTo = dis.readUTF();

                    if (emailTo != null) {
                        String contrasenaNueva = Metodos.generarNuevaContraseña();

                        //mandamos el email con la contraseña nueva y actualizamos la BD
                        mandarEmailRecContraseña(emailTo, contrasenaNueva);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastCamposVacios), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    //metodo para iniciar sesión
    private void iniciarSesion(String usuario, String contrasena) {
        new Thread(() -> {
            Metodos metodos = new Metodos();

            //comprobar campos vacíos en el login
            if (metodos.comprobarLoginCamposVacios(usuario, contrasena)) { //no hay campos vacíos

                try {
                    //opcion seleccionada 1 Login
                    dos.writeInt(1);
                    dos.flush();

                    //string para que el server sepa que viene del móvil (contraseña hash)
                    dos.writeUTF("movil");
                    dos.flush();

                    //campo nombre
                    dos.writeUTF(usuario);
                    dos.flush();

                    //campo contraseña
                    dos.writeUTF(contrasena);
                    dos.flush();


                    //leer respuesta del servidor
                    boolean conexionCorrecta = dis.readBoolean();
                    readIDUsuario = dis.readInt();
                    readTipoUsuario = dis.readUTF();

                    if (conexionCorrecta) {
                        runOnUiThread(() -> {
                            Toast.makeText(Login.this, getString(R.string.toastLoginCorrecto), Toast.LENGTH_SHORT).show();
                            abrirMenuUsuario();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastNoExisteUser), Toast.LENGTH_SHORT).show());
                    }

                } catch (IOException e) {
                    Log.e("LOGIN", "Error en la conexión con el servidor", e);
                    runOnUiThread(() -> Toast.makeText(Login.this, "Error en la conexión", Toast.LENGTH_SHORT).show());
                }

                //resetear los campos en el UI thread
                runOnUiThread(() -> {
                    EditText usuarioField = findViewById(R.id.editTextUsuario);
                    EditText contrasenaField = findViewById(R.id.editTextContrasena);
                    usuarioField.setText("");
                    contrasenaField.setText("");
                });

            } else {
                runOnUiThread(() -> Toast.makeText(Login.this, getString(R.string.toastCamposVacios), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    //metodo para abrir el menu del usuario
    private void abrirMenuUsuario() {
        runOnUiThread(() -> {
            if (readTipoUsuario.equals(TipoUsuario.ALUMNO.getTipoUser())) {
                Intent intentEstudiante = new Intent(Login.this, MenuEstudiante.class);
                startActivity(intentEstudiante);
            } else if (readTipoUsuario.equals(TipoUsuario.PROFESOR.getTipoUser())) {
                Intent intentProfesor = new Intent(Login.this, MenuProfesor.class);
                startActivity(intentProfesor);
            }
        });
    }

    //metodo para mandar el email de recuperación de contraseña
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
}