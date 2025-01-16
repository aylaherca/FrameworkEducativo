package com.example.frameworkeducativoreto2grupo2.Login;

import android.os.Bundle;
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
import com.example.frameworkeducativoreto2grupo2.Metodos.Metodos;
import com.example.frameworkeducativoreto2grupo2.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Metodos metodos = new Metodos();

        //variables
        Button btnLogin = findViewById(R.id.btnLogin);
        Spinner spinnerTipoUser = findViewById(R.id.spinnerTipoUser);
        TextView textViewRecuperarContrasena = findViewById(R.id.textViewRecuperarContrasena);
        EditText usuario = findViewById(R.id.editTextUsuario);
        EditText contrasena = findViewById(R.id.editTextContrasena);

        boolean correcto = false;


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
            String txtUsuario = String.valueOf(usuario.getText()).trim();
            String txtcontrasena = String.valueOf(contrasena.getText()).trim();
            //recoger el tipo de usuario seleccionado del spinner, usar name() para obtener el string representante del enum seleccionado ("profesor" o "estudiante")
            TipoUsuario tipoUsuarioSeleccionado = (TipoUsuario) spinnerTipoUser.getSelectedItem();
            String tipoUser = tipoUsuarioSeleccionado != null ? tipoUsuarioSeleccionado.name() : "";

            //comprobar login
            if(metodos.comprobarLogin(txtUsuario, txtcontrasena)) { //no hay campos vacios

                //aqui mirar el login respecto a la base de datos --> toast de login correcto o incorrecto

                Toast.makeText(Login.this, "Login correcto.", Toast.LENGTH_SHORT).show();

                //resetear los campos
                usuario.setText("");
                contrasena.setText("");


            }else { //hay un campo vacio
                Toast.makeText(Login.this, "Hay un campo vacio.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}