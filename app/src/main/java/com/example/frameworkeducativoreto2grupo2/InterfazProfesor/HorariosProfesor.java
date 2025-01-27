package com.example.frameworkeducativoreto2grupo2.InterfazProfesor;

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

import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import Modelo.Users;

public class HorariosProfesor extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    int IDUserLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_horarios_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getExtra

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasME); //vuelve al menu de profesor
        TableLayout tablaHorarios = findViewById(R.id.tablaHorarios); //tabla

        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                //Opcion seleccionada 2 mostrarHorario
                dos.writeInt(2);
                dos.flush();

                //mandar el id del usuario logeado
                dos.writeInt(IDUserLog);
                dos.flush();

                //recoger horario
                String[][] horarioUser = (String[][]) ois.readObject();

                //actualizar la tabla con los datos obtenidos
                runOnUiThread(() -> {
                    //rellenar la tabla con los datos obtenidos
                    rellenarTabla(horarioUser);
                });

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(HorariosProfesor.this, MenuProfesor.class);
            menuProfesor.putExtra("IDUserLog", IDUserLog);
            startActivity(menuProfesor);
        });



    }

    private void rellenarTabla(String[][] horarioUser) {
        //limpiar filas
        TableLayout tablaHorarios = findViewById(R.id.tablaHorarios);
        tablaHorarios.removeAllViews();

        //header columnas
        String[] dias = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        TableRow headerRow = new TableRow(this);
        //añadir cabeceras de los dias
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
        tablaHorarios.addView(headerRow);

        //header filas
        String[] horas = {"Hora 1", "Hora 2", "Hora 3", "Hora 4", "Hora 5", "Hora 6"};
        for (int i = 0; i < horas.length; i++) {
            TableRow row = new TableRow(this);

            //columna hora
            TextView hourCell = new TextView(this);
            hourCell.setText(horas[i]);
            hourCell.setTextSize(14);
            hourCell.setTextColor(getResources().getColor(R.color.black));
            hourCell.setGravity(Gravity.CENTER);
            hourCell.setPadding(16, 16, 16, 16);
            hourCell.setBackgroundResource(R.drawable.table_cell_border);
            row.addView(hourCell);

            //rellenar las celdas de cada dia desde los datos del array
            for (int j = 1; j < dias.length; j++) { // 5 dias
                TextView cell = new TextView(this);
                String clase = horarioUser[i][j-1];//el índice j-1 porque el primer elemento (vacío) es para el header
                cell.setText(clase.isEmpty() ? "" : clase);
                cell.setTextSize(14);
                cell.setTextColor(getResources().getColor(R.color.black));
                cell.setGravity(Gravity.CENTER);
                cell.setPadding(16, 16, 16, 16);
                cell.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(cell);
            }
            tablaHorarios.addView(row);

            /*
            FATAL EXCEPTION: main
                                                                                                    Process: com.example.frameworkeducativoreto2grupo2, PID: 13323
                                                                                                    java.lang.ArrayIndexOutOfBoundsException: length=5; index=5
                                                                                                    	at com.example.frameworkeducativoreto2grupo2.InterfazProfesor.HorariosProfesor.rellenarTabla(HorariosProfesor.java:141)
                                                                                                    	at com.example.frameworkeducativoreto2grupo2.InterfazProfesor.HorariosProfesor.lambda$onCreate$1$com-example-frameworkeducativoreto2grupo2-InterfazProfesor-HorariosProfesor(HorariosProfesor.java:83)
                                                                                                    	at com.example.frameworkeducativoreto2grupo2.InterfazProfesor.HorariosProfesor$$ExternalSyntheticLambda3.run(D8$$SyntheticClass:0)
                                                                                                    	at android.os.Handler.handleCallback(Handler.java:959)
                                                                                                    	at android.os.Handler.dispatchMessage(Handler.java:100)
                                                                                                    	at android.os.Looper.loopOnce(Looper.java:232)
                                                                                                    	at android.os.Looper.loop(Looper.java:317)
                                                                                                    	at android.app.ActivityThread.main(ActivityThread.java:8705)
                                                                                                    	at java.lang.reflect.Method.invoke(Native Method)
                                                                                                    	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
                                                                                                    	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
            * */


        }
    }


}