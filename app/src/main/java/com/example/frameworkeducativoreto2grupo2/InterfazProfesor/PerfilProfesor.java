package com.example.frameworkeducativoreto2grupo2.InterfazProfesor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.InterfazEstudiante.MenuEstudiante;
import com.example.frameworkeducativoreto2grupo2.InterfazEstudiante.PerfilEstudiante;
import com.example.frameworkeducativoreto2grupo2.R;

public class PerfilProfesor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtras);
        Button btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        TextView txtNombre = findViewById(R.id.txtNombrePerfil);
        TextView txtApellidos = findViewById(R.id.txtApellidosPerfil);
        TextView txtUsuario = findViewById(R.id.txtUsuarioPerfil);
        TextView txtEmail = findViewById(R.id.txtEmailPerfil);
        TextView txtDNI = findViewById(R.id.txtDNIPerfil);


        //listener boton cambiar foto ------------------------------------------------------------------------------- BOTON CAMBIAR FOTO
        btnCambiarFoto.setOnClickListener(view -> {
            //abrir camara + guardar foto nueva en bd *******
        });

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(PerfilProfesor.this, MenuProfesor.class);
            startActivity(menuEstudiante);
        });
    }
}