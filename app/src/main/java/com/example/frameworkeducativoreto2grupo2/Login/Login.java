package com.example.frameworkeducativoreto2grupo2.Login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //txtview recuperar contraseña
        TextView textViewRecuperarContrasena = findViewById(R.id.textViewRecuperarContrasena);

        //listener recuperar contraseña
        textViewRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Por favor, revisa tu correo para recuperar la contraseña.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}