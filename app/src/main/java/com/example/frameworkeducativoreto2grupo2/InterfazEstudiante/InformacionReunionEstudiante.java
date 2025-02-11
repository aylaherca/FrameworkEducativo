package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.R;

import java.util.Locale;

public class InformacionReunionEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarIdioma();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_reunion_estudiante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void aplicarIdioma() {
        String idioma = obtenerIdioma();
        Locale nuevoLocale = new Locale(idioma);
        Locale.setDefault(nuevoLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(nuevoLocale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private String obtenerIdioma() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        return prefs.getString("Idioma", "eu");
    }

}