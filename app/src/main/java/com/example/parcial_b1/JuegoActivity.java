package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoActivity extends AppCompatActivity {

    private TextView tvPregunta, tvPuntaje;
    private ImageView ivPreguntaImagen;
    private MaterialButton btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4;
    private DatabaseReference dbReference;
    private List<Pregunta> listaPreguntas = new ArrayList<>();
    private int preguntaActual = 0;
    private int puntaje = 0;
    private final int PUNTOS_POR_RESPUESTA = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_estudiante);

        // Inicializar Firebase
        dbReference = FirebaseDatabase.getInstance().getReference("preguntas");

        // Configurar vistas
        configurarVistas();

        // Cargar preguntas
        cargarPreguntasDeFirebase();
    }

    private void configurarVistas() {
        tvPregunta = findViewById(R.id.tvPregunta);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        ivPreguntaImagen = findViewById(R.id.ivPreguntaImagen);
        btnOpcion1 = findViewById(R.id.btnOpcion1);
        btnOpcion2 = findViewById(R.id.btnOpcion2);
        btnOpcion3 = findViewById(R.id.btnOpcion3);
        btnOpcion4 = findViewById(R.id.btnOpcion4);

        // Configurar listeners
        View.OnClickListener opcionListener = v -> {
            if (listaPreguntas.isEmpty()) return;

            int opcionSeleccionada = obtenerOpcionSeleccionada(v);
            verificarRespuesta(opcionSeleccionada);
        };

        btnOpcion1.setOnClickListener(opcionListener);
        btnOpcion2.setOnClickListener(opcionListener);
        btnOpcion3.setOnClickListener(opcionListener);
        btnOpcion4.setOnClickListener(opcionListener);

        findViewById(R.id.btnFinalizarQuiz).setOnClickListener(v -> finalizarQuiz());
    }

    private void cargarPreguntasDeFirebase() {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPreguntas.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Pregunta pregunta = ds.getValue(Pregunta.class);
                    if (pregunta != null) {
                        pregunta.setId(ds.getKey());
                        listaPreguntas.add(pregunta);
                    }
                }

                if (listaPreguntas.isEmpty()) {
                    mostrarError("No hay preguntas disponibles");
                    return;
                }

                Collections.shuffle(listaPreguntas);
                if (listaPreguntas.size() > 10) {
                    listaPreguntas = listaPreguntas.subList(0, 10);
                }

                mostrarPreguntaActual();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarError("Error al cargar preguntas: " + error.getMessage());
            }
        });
    }

    private void mostrarPreguntaActual() {
        if (preguntaActual >= listaPreguntas.size()) {
            finalizarQuiz();
            return;
        }

        Pregunta pregunta = listaPreguntas.get(preguntaActual);
        tvPregunta.setText(pregunta.getTexto());

        // Mostrar imagen si existe
        if (pregunta.getImagenUrl() != null && !pregunta.getImagenUrl().isEmpty()) {
            ivPreguntaImagen.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(pregunta.getImagenUrl())
                    .centerCrop()
                    .into(ivPreguntaImagen);
        } else {
            ivPreguntaImagen.setVisibility(View.GONE);
        }

        btnOpcion1.setText(pregunta.getOpcion1());
        btnOpcion2.setText(pregunta.getOpcion2());
        btnOpcion3.setText(pregunta.getOpcion3());
        btnOpcion4.setText(pregunta.getOpcion4());
        actualizarPuntaje();
        habilitarBotones();
    }


    private int obtenerOpcionSeleccionada(View v) {
        if (v.getId() == R.id.btnOpcion1) return 1;
        if (v.getId() == R.id.btnOpcion2) return 2;
        if (v.getId() == R.id.btnOpcion3) return 3;
        return 4;
    }

    private void verificarRespuesta(int opcionSeleccionada) {
        deshabilitarBotones();

        Pregunta pregunta = listaPreguntas.get(preguntaActual);
        boolean esCorrecta = opcionSeleccionada == pregunta.getRespuestaCorrecta();

        if (esCorrecta) {
            puntaje += PUNTOS_POR_RESPUESTA;
            mostrarMensaje("¡Correcto!");
        } else {
            mostrarMensaje("Incorrecto. La respuesta era: " + pregunta.getRespuestaCorrecta());
        }

        // Retraso antes de siguiente pregunta
        btnOpcion1.postDelayed(() -> {
            preguntaActual++;
            mostrarPreguntaActual();
        }, 1500);
    }

    private void actualizarPuntaje() {
        tvPuntaje.setText(String.format("Puntaje: %d", puntaje));
    }

    private void finalizarQuiz() {
        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra("puntaje", puntaje);
        intent.putExtra("total_preguntas", listaPreguntas.size());
        startActivity(intent);
        finish();
    }

    private void habilitarBotones() {
        btnOpcion1.setEnabled(true);
        btnOpcion2.setEnabled(true);
        btnOpcion3.setEnabled(true);
        btnOpcion4.setEnabled(true);
    }

    private void deshabilitarBotones() {
        btnOpcion1.setEnabled(false);
        btnOpcion2.setEnabled(false);
        btnOpcion3.setEnabled(false);
        btnOpcion4.setEnabled(false);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        finish();
    }
}