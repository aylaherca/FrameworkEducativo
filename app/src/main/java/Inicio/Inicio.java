package Inicio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;


import com.example.frameworkeducativoreto2grupo2.R;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Reference to the ImageView for the GIF
        ImageView gifImageView = findViewById(R.id.gifImageView);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.raw.logo_gif) // Path to the GIF in res/raw
                .into(gifImageView);

        // Delay for 3 seconds before navigating to MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Inicio.this, Inicio.class);
            startActivity(intent);
            finish(); // Close the splash activity
        }, 3000); //duracion en milisegundos
    }
}