package com.example.parcial_b1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class JuegoActivity extends AppCompatActivity {
    private TextView tvPregunta, tvPuntaje;
    private Button btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4, btnFinalizarQuiz;
    private DBHelper dbHelper;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private int puntaje = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        // Inicializar vistas
        tvPregunta = findViewById(R.id.tvPregunta);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        btnOpcion1 = findViewById(R.id.btnOpcion1);
        btnOpcion2 = findViewById(R.id.btnOpcion2);
        btnOpcion3 = findViewById(R.id.btnOpcion3);
        btnOpcion4 = findViewById(R.id.btnOpcion4);
        btnFinalizarQuiz = findViewById(R.id.btnFinalizarQuiz);

        dbHelper = new DBHelper(this);
        preguntas = new ArrayList<>();

        // Cargar preguntas desde la base de datos
        cargarPreguntas();

        // Configurar listeners de botones
        View.OnClickListener opcionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preguntas.isEmpty()) {
                    Toast.makeText(JuegoActivity.this, "No hay preguntas disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                int opcionSeleccionada;
                if (v.getId() == R.id.btnOpcion1) opcionSeleccionada = 1;
                else if (v.getId() == R.id.btnOpcion2) opcionSeleccionada = 2;
                else if (v.getId() == R.id.btnOpcion3) opcionSeleccionada = 3;
                else opcionSeleccionada = 4;

                verificarRespuesta(opcionSeleccionada);
            }
        };

        btnOpcion1.setOnClickListener(opcionClickListener);
        btnOpcion2.setOnClickListener(opcionClickListener);
        btnOpcion3.setOnClickListener(opcionClickListener);
        btnOpcion4.setOnClickListener(opcionClickListener);

        btnFinalizarQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarQuiz();
            }
        });

        // Mostrar la primera pregunta
        mostrarPreguntaActual();
    }
    private void cargarPreguntas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Verificar si hay preguntas en la base de datos
            cursor = db.rawQuery("SELECT COUNT(*) FROM preguntas", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count == 0) {
                Toast.makeText(this, "No hay preguntas en la base de datos", Toast.LENGTH_LONG).show();
                return;
            }

            // Cargar las preguntas desde la base de datos
            cursor = db.rawQuery("SELECT * FROM preguntas", null);

            // Obtener los índices de las columnas
            int idIndex = cursor.getColumnIndexOrThrow("id"); // ID único de la pregunta
            int preguntaIndex = cursor.getColumnIndexOrThrow("pregunta");
            int opcion1Index = cursor.getColumnIndexOrThrow("opcion1");
            int opcion2Index = cursor.getColumnIndexOrThrow("opcion2");
            int opcion3Index = cursor.getColumnIndexOrThrow("opcion3");
            int opcion4Index = cursor.getColumnIndexOrThrow("opcion4");
            int respuestaCorrectaIndex = cursor.getColumnIndexOrThrow("respuesta_correcta");

            while (cursor.moveToNext()) {
                // Extraer valores de la fila actual
                int id = cursor.getInt(idIndex);
                String pregunta = cursor.getString(preguntaIndex);
                String opcion1 = cursor.getString(opcion1Index);
                String opcion2 = cursor.getString(opcion2Index);
                String opcion3 = cursor.getString(opcion3Index);
                String opcion4 = cursor.getString(opcion4Index);
                int respuestaCorrecta = cursor.getInt(respuestaCorrectaIndex);

                // Crear un objeto Pregunta y agregarlo a la lista
                preguntas.add(new Pregunta(id, pregunta, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta));
            }

            if (preguntas.isEmpty()) {
                Toast.makeText(this, "Error al cargar las preguntas", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Se cargaron " + preguntas.size() + " preguntas", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void mostrarPreguntaActual() {
        if (preguntas.isEmpty()) {
            tvPregunta.setText("No hay preguntas disponibles");
            deshabilitarBotones();
            return;
        }

        if (preguntaActual >= preguntas.size()) {
            finalizarQuiz();
            return;
        }

        Pregunta preguntaActualObj = preguntas.get(preguntaActual);
        tvPregunta.setText(preguntaActualObj.getPregunta());
        btnOpcion1.setText(preguntaActualObj.getOpcion1());
        btnOpcion2.setText(preguntaActualObj.getOpcion2());
        btnOpcion3.setText(preguntaActualObj.getOpcion3());
        btnOpcion4.setText(preguntaActualObj.getOpcion4());
        tvPuntaje.setText("Puntaje: " + puntaje);
    }

    private void verificarRespuesta(int opcionSeleccionada) {
        Pregunta preguntaActualObj = preguntas.get(preguntaActual);
        if (opcionSeleccionada == preguntaActualObj.getRespuestaCorrecta()) {
            puntaje += 10;
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrecto. La respuesta correcta era la opción " +
                    preguntaActualObj.getRespuestaCorrecta(), Toast.LENGTH_SHORT).show();
        }

        preguntaActual++;
        if (preguntaActual < preguntas.size()) {
            mostrarPreguntaActual();
        } else {
            finalizarQuiz();
        }
    }

    private void finalizarQuiz() {
        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra("puntaje", puntaje);
        intent.putExtra("totalPreguntas", preguntas.size());
        startActivity(intent);
        finish();
    }

    private void deshabilitarBotones() {
        btnOpcion1.setEnabled(false);
        btnOpcion2.setEnabled(false);
        btnOpcion3.setEnabled(false);
        btnOpcion4.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}