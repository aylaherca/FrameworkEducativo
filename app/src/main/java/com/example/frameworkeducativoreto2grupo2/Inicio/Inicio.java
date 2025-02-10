package com.example.frameworkeducativoreto2grupo2.Inicio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import com.example.frameworkeducativoreto2grupo2.R;

import com.example.frameworkeducativoreto2grupo2.Login.Login;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //imageview del gif
        ImageView gifImageView = findViewById(R.id.gifImageView);

        //cargar el gif con Glide (dependencia en build.gradle.kts (Module :app))
        Glide.with(this)
                .asGif()
                .load(R.raw.logo_gif)
                .into(gifImageView);


        //delay de 3 segundos antes de ir al siguiente activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Inicio.this, Login.class);
            startActivity(intent);
            finish(); //cierra el splash
        }, 3000); //duracion en milisegundos
    }
}