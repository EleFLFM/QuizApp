package com.example.parcial_b1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionPreguntasActivity extends AppCompatActivity {

    private EditText etPregunta, etOpcion1, etOpcion2, etOpcion3, etOpcion4, etRespuestaCorrecta;
    private Button btnGuardar, btnVolver;
    private FloatingActionButton fabAddImage;
    private ImageView ivPreview;
    private RecyclerView rvPreguntas;
    private PreguntaAdapter preguntasAdapter;
    private DatabaseReference databaseReference;
    private List<Pregunta> listaPreguntas = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private Bitmap imagenSeleccionada;
    private String imagenUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_preguntas);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("preguntas");

        // Referenciar vistas
        inicializarVistas();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Cargar preguntas desde Firebase
        cargarPreguntas();

        // Configurar listeners de botones
        btnGuardar.setOnClickListener(v -> guardarPregunta());
        btnVolver.setOnClickListener(v -> finish());
        fabAddImage.setOnClickListener(v -> mostrarDialogoImagen());
    }

    private void inicializarVistas() {
        etPregunta = findViewById(R.id.etPregunta);
        etOpcion1 = findViewById(R.id.etOpcion1);
        etOpcion2 = findViewById(R.id.etOpcion2);
        etOpcion3 = findViewById(R.id.etOpcion3);
        etOpcion4 = findViewById(R.id.etOpcion4);
        etRespuestaCorrecta = findViewById(R.id.etRespuestaCorrecta);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);
        rvPreguntas = findViewById(R.id.rvPreguntas);
        fabAddImage = findViewById(R.id.fabAddImage);
        ivPreview = findViewById(R.id.ivPreview);
    }

    private void mostrarDialogoImagen() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen");
        builder.setItems(new CharSequence[]{"Desde la galería", "Tomar foto"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    abrirGaleria();
                    break;
                case 1:
                    abrirCamara();
                    break;
            }
        });
        builder.show();
    }

    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    imagenSeleccionada = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ivPreview.setImageBitmap(imagenSeleccionada);
                    subirImagenImgBB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
                imagenSeleccionada = (Bitmap) data.getExtras().get("data");
                ivPreview.setImageBitmap(imagenSeleccionada);
                subirImagenImgBB();
            }
        }
    }

    private void subirImagenImgBB() {
        if (imagenSeleccionada == null) return;

        String url = "https://api.imgbb.com/1/upload?key=35dcd2b450b0ee6aecf7cf3884795fc1   "; // Reemplaza con tu API key de ImgBB

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagenSeleccionada.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            imagenUrl = data.getString("url");
                            Toast.makeText(GestionPreguntasActivity.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GestionPreguntasActivity.this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GestionPreguntasActivity.this, "Error al subir la imagen: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void configurarRecyclerView() {
        rvPreguntas.setLayoutManager(new LinearLayoutManager(this));
        preguntasAdapter = new PreguntaAdapter(listaPreguntas, this::eliminarPregunta);
        rvPreguntas.setAdapter(preguntasAdapter);
    }

    private void cargarPreguntas() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPreguntas.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Pregunta pregunta = dataSnapshot.getValue(Pregunta.class);
                    if (pregunta != null) {
                        pregunta.setId(dataSnapshot.getKey());
                        listaPreguntas.add(pregunta);
                    }
                }
                preguntasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionPreguntasActivity.this,
                        "Error al cargar preguntas: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void guardarPregunta() {
        String texto = etPregunta.getText().toString().trim();
        String opcion1 = etOpcion1.getText().toString().trim();
        String opcion2 = etOpcion2.getText().toString().trim();
        String opcion3 = etOpcion3.getText().toString().trim();
        String opcion4 = etOpcion4.getText().toString().trim();
        String respuestaStr = etRespuestaCorrecta.getText().toString().trim();

        if (validarCampos(texto, opcion1, opcion2, opcion3, opcion4, respuestaStr)) {
            int respuestaCorrecta = Integer.parseInt(respuestaStr);
            Pregunta nuevaPregunta = new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, imagenUrl);

            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(nuevaPregunta)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Pregunta guardada", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                                imagenUrl = null;
                                ivPreview.setImageResource(android.R.color.transparent);
                            } else {
                                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private boolean validarCampos(String texto, String op1, String op2, String op3, String op4, String respuesta) {
        if (texto.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || respuesta.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int resp = Integer.parseInt(respuesta);
            if (resp < 1 || resp > 4) {
                Toast.makeText(this, "La respuesta debe ser entre 1 y 4", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Respuesta debe ser un número", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void eliminarPregunta(Pregunta pregunta) {
        if (pregunta.getId() != null) {
            databaseReference.child(pregunta.getId()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Pregunta eliminada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void limpiarCampos() {
        etPregunta.setText("");
        etOpcion1.setText("");
        etOpcion2.setText("");
        etOpcion3.setText("");
        etOpcion4.setText("");
        etRespuestaCorrecta.setText("");
    }
}