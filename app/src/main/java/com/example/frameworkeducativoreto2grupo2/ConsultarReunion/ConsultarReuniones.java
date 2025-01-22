package com.example.frameworkeducativoreto2grupo2.ConsultarReunion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.HorariosProfesor;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.InformacionReunion;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.MenuProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

public class ConsultarReuniones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_reuniones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasMP4); //vuelve al menu de profesor
        TableLayout tablaReuniones = findViewById(R.id.tablaReuniones); //tabla

        //rellenar la tabla ------------------------------------------------------------------------------- RELLENAR LA TABLA
        //datos de las columnas y filas
        String[] dias = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        String[] horas = {"Hora 1", "Hora 2", "Hora 3", "Hora 4", "Hora 5", "Hora 6"};

        //columnas
        TableRow headerRow = new TableRow(this);
        for (String dia : dias) {
            TextView headerCell = new TextView(this);
            headerCell.setText(dia);
            headerCell.setTextSize(16);
            headerCell.setTextColor(getResources().getColor(R.color.azulOscuro));
            headerCell.setGravity(Gravity.CENTER);
            headerCell.setPadding(16, 16, 16, 16);
            headerCell.setBackgroundResource(R.drawable.table_cell_border);
            headerRow.addView(headerCell);
        }
        tablaReuniones.addView(headerRow);

        //filas
        for (String hora : horas) {
            TableRow row = new TableRow(this);
            TextView hourCell = new TextView(this);
            hourCell.setText(hora);
            hourCell.setTextSize(14);
            hourCell.setTextColor(getResources().getColor(R.color.black));
            hourCell.setGravity(Gravity.CENTER);
            hourCell.setPadding(16, 16, 16, 16);
            hourCell.setBackgroundResource(R.drawable.table_cell_border);
            row.addView(hourCell);

            //añadir celdas vacias para cada dia
            for (int i = 1; i < dias.length; i++) {
                TextView cell = new TextView(this);
                cell.setText("");
                cell.setTextSize(14);
                cell.setTextColor(getResources().getColor(R.color.black));
                cell.setGravity(Gravity.CENTER);
                cell.setPadding(16, 16, 16, 16);
                cell.setBackgroundResource(R.drawable.table_cell_border);

                //*************primero mirar el tipo de user que es**************************
                //si es profesor --> informacionreuion --- si es estudiante  --> informacionReunionEstudiante

                //hacer clickables las celdas solo de reuniones
                //mirar si la celda está vacia o si el texto contiene la palabra reunion
                String textoCelda = cell.getText().toString().toLowerCase();
                if (!cell.getText().toString().isEmpty() && (textoCelda.contains("reunión") || textoCelda.contains("reunion"))) {
                    //se hace clickable
                    cell.setClickable(true);
                    cell.setOnClickListener(view -> {
                        //listener al activity de informacion de reuniones
                        Intent intent = new Intent(ConsultarReuniones.this, InformacionReunion.class);
                        startActivity(intent);
                    });
                }
                row.addView(cell);
            }
            tablaReuniones.addView(row);
        }


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(ConsultarReuniones.this, MenuProfesor.class);
            startActivity(menuProfesor);
        });
    }
}