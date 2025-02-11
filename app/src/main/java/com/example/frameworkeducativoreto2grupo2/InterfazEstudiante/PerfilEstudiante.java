package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.PerfilProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Modelo.Users;

public class PerfilEstudiante extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    Users user = new Users();

    private ImageView imageViewFotoPerfil;
    private Uri argazkiUri;             // URI non gordeko dugun
    private String egungoArgazkiBidea; // Fitxategiaren bide osoa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarIdioma();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_estudiante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }

        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtras);
        Button btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        Button btnbtnCambiarIdiomaEstudiante = findViewById(R.id.btnCambiarIdiomaE);

        TextView txtNombre = findViewById(R.id.txtNombrePerfil);
        TextView txtApellidos = findViewById(R.id.txtApellidosPerfil);
        TextView txtUsuario = findViewById(R.id.txtUsuarioPerfil);
        TextView txtEmail = findViewById(R.id.txtEmailPerfil);
        TextView txtDNI = findViewById(R.id.txtDNIPerfil);

        TextView txtCicloCursado = findViewById(R.id.txtCicloCursadoPerfil);
        TextView txtNumCurso = findViewById(R.id.txtNumCursoPerfil);
        TextView txtModalidadDUAL = findViewById(R.id.txtModalidadDUALPerfil);

        imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil);

        new Thread(() -> {
            try {
                //Opcion seleccionada 9 recoger datos
                dos.writeInt(9);
                dos.flush();

                //leer el usuario
                user = (Users) ois.readObject();

                //comprobar si el dato esta vacio desde la bd y cambiar los campos
                txtNombre.setText(comprobarDatos(user.getNombre()));
                txtApellidos.setText(comprobarDatos(user.getApellidos()));
                txtUsuario.setText(comprobarDatos(user.getUsername()));
                txtEmail.setText(comprobarDatos(user.getEmail()));
                txtDNI.setText(comprobarDatos(user.getDni()));

                //foto placeholder si la imagen es null/no tiene
                if (user.getArgazkia() == null || user.getArgazkia().isEmpty()) {
                    imageViewFotoPerfil.setImageResource(R.drawable.placeholder);
                } else { //ponemos la foto de la db
                    Glide.with(PerfilEstudiante.this)
                            .load(user.getArgazkia())
                            .into(imageViewFotoPerfil);
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();


        //listener boton cambiar foto ------------------------------------------------------------------------------- BOTON CAMBIAR FOTO
        btnCambiarFoto.setOnClickListener(view -> {
            //abrir camara + guardar foto nueva en bd
            abrirCamara();
        });

        //listener boton idioma ------------------------------------------------------------------------------- BOTON IDIOMA
        btnbtnCambiarIdiomaEstudiante.setOnClickListener(view -> {
            Log.d("IDDIOMS", "entramos en el click");
            alternarIdioma();
        });

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(PerfilEstudiante.this, MenuEstudiante.class);
            startActivity(menuEstudiante);
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

    private void alternarIdioma() {
        String idiomaActual = obtenerIdioma();
        String nuevoIdioma = idiomaActual.equals("eu") ? "es" : "eu";
        Log.d("IdiomaDebug", "Nuevo idioma seleccionado: " + nuevoIdioma);
        guardarIdioma(nuevoIdioma);

        // Reiniciar la actividad para aplicar el nuevo idioma
        Intent intent = new Intent(this, PerfilProfesor.class);
        finish();
        startActivity(intent);
    }

    private void guardarIdioma(String idioma) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Idioma", idioma);
        editor.apply();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        runOnUiThread(() -> {
            if (requestCode == 100) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Kamera baimena eman da!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Kamera baimena beharrezkoa da!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //metodo abrir camara
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setPackage("com.android.camera2"); //especificar la cámara instalada

        File photoFile = null;
        try {
            photoFile = crearArchivoImagen();
        } catch (IOException e) {
            Log.e("Camera", "Error al crear el archivo de imagen", e);
        }

        if (photoFile != null) {
            argazkiUri = FileProvider.getUriForFile(this, "com.example.frameworkeducativoreto2grupo2.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, argazkiUri);

            runOnUiThread(() -> cameraLauncher.launch(intent));
        } else {
            Log.e("CameraDebug", "No se pudo crear el archivo de imagen");
            runOnUiThread(() -> Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show());
        }
    }

    //crear la imager como archivo
    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        egungoArgazkiBidea = image.getAbsolutePath();
        return image;
    }

    //camera laucher
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(egungoArgazkiBidea);
                    if (imageBitmap != null) {
                        runOnUiThread(() -> {
                            imageViewFotoPerfil.setImageBitmap(imageBitmap);
                            Toast.makeText(this, "Foto capturada con éxito", Toast.LENGTH_SHORT).show();
                        });
                        enviarImagenAlServidor();
                    } else {
                        Log.e("Camera", "Error al decodificar la imagen capturada");
                    }
                }
            });

    //enviar la imagen al servidor
    private void enviarImagenAlServidor() {
        new Thread(() -> {
            try {
                File imageFile = new File(egungoArgazkiBidea);
                FileInputStream fis = new FileInputStream(imageFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                fis.close();
                byte[] imagenBytes = bos.toByteArray();

                //opcion seleccionada 9 recoger datos
                dos.writeInt(22);
                dos.flush();

                //tamaño de la imagen
                dos.writeInt(imagenBytes.length);
                dos.flush();

                //imagen en bytes
                dos.write(imagenBytes);
                dos.flush();

                Log.d("Servidor", "Imagen enviada al servidor correctamente");
                runOnUiThread(() -> Toast.makeText(this, "Imagen enviada al servidor", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log.e("Servidor", "Error al enviar la imagen al servidor", e);
            }
        }).start();
    }

    //Metodo para comprobar los datos
    //comprueba si el dato recogido es null o esta vacio y si es asi le setea un string por defecto (-)
    private String comprobarDatos(String dato) {
        if (dato == null || dato.isEmpty()) {
            //devuelve el string de dato no encontrado
            return getString(R.string.datoNoEncontrado);
        } else {
            //devuelve el dato de la bd
            return dato;
        }
    }

}