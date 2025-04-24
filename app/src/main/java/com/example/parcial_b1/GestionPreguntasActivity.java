package com.example.parcial_b1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionPreguntasActivity extends AppCompatActivity {

    private EditText etPregunta, etOpcion1, etOpcion2, etOpcion3, etOpcion4, etRespuestaCorrecta;
    private Button btnGuardar, btnVolver;
    private RecyclerView rvPreguntas;
    private PreguntaAdapter preguntasAdapter;
    private DatabaseReference databaseReference;
    private List<Pregunta> listaPreguntas = new ArrayList<>();

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
            Pregunta nuevaPregunta = new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta);

            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(nuevaPregunta)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Pregunta guardada", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
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