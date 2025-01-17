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
            String txtContrasena = String.valueOf(contrasena.getText()).trim();
            //recoger el tipo de usuario seleccionado del spinner, usar name() para obtener el string representante del enum seleccionado ("profesor" o "estudiante")
            TipoUsuario tipoUsuarioSeleccionado = (TipoUsuario) spinnerTipoUser.getSelectedItem();
            String tipoUser = tipoUsuarioSeleccionado != null ? tipoUsuarioSeleccionado.name() : "";

            //comprobar campos vacios en el login
            if(!metodos.comprobarLogin(txtUsuario, txtContrasena)) { // hay campos vacios

                Toast.makeText(Login.this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            }

            //llamamos al servidor para validar el login
            new Thread(() -> {
                String result = metodos.login(txtUsuario, txtContrasena, tipoUser);

                //actualizamos la interfaz en base a la respuesta obtenida del servidor
                runOnUiThread(() -> {
                    if (result.startsWith("Welcome")) {
                        Toast.makeText(Login.this, result + "login correcto", Toast.LENGTH_SHORT).show();
                        // Navigate to the next screen or perform other actions
                        usuario.setText("");
                        contrasena.setText("");
                    } else {
                        Toast.makeText(Login.this, result + "login incorrecto", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();

        });


    }
}