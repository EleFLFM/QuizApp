package com.example.parcial_b1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GestionPreguntasActivity extends AppCompatActivity {

    private EditText etPregunta, etOpcion1, etOpcion2, etOpcion3, etOpcion4, etRespuestaCorrecta;
    private Button btnGuardar, btnVolver;
    private RecyclerView rvPreguntas;
    private PreguntaAdapter preguntasAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_preguntas);

        // Inicializar el DBHelper
        dbHelper = new DBHelper(this);

        // Referenciar los campos del diseño
        etPregunta = findViewById(R.id.etPregunta);
        etOpcion1 = findViewById(R.id.etOpcion1);
        etOpcion2 = findViewById(R.id.etOpcion2);
        etOpcion3 = findViewById(R.id.etOpcion3);
        etOpcion4 = findViewById(R.id.etOpcion4);
        etRespuestaCorrecta = findViewById(R.id.etRespuestaCorrecta);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        // Configurar el RecyclerView
        rvPreguntas = findViewById(R.id.rvPreguntas);
        rvPreguntas.setLayoutManager(new LinearLayoutManager(this));

        // Cargar preguntas iniciales
        cargarPreguntas();

        // Configurar el botón "Guardar"
        btnGuardar.setOnClickListener(v -> guardarPregunta());

        // Configurar el botón "Volver"
        btnVolver.setOnClickListener(v -> finish());
    }

    /**
     * Método para cargar y mostrar las preguntas en un RecyclerView.
     * Obtiene las preguntas desde la base de datos, verifica su disponibilidad,
     * y configura el adaptador para visualizarlas en la interfaz de usuario.
     */
    private void cargarPreguntas() {
        // Obtener la lista de preguntas desde la base de datos utilizando DBHelper
        List<Pregunta> preguntas = dbHelper.obtenerTodasLasPreguntas();

        // Verificar si la lista de preguntas está vacía o es nula
        if (preguntas == null || preguntas.isEmpty()) {
            // Registrar en Logcat que no hay preguntas disponibles
            Log.d("GestionPreguntas", "No hay preguntas disponibles.");
            // Mostrar un mensaje al usuario indicando que no hay preguntas
            Toast.makeText(this, "No hay preguntas disponibles", Toast.LENGTH_SHORT).show();
            return; // Finalizar el método si no hay preguntas
        }

        // Configurar el adaptador para el RecyclerView
        preguntasAdapter = new PreguntaAdapter(
                preguntas, // Pasar la lista de preguntas al adaptador
                pregunta -> {
                    // Acción para eliminar una pregunta cuando se interactúa con ella
                    eliminarPregunta(pregunta);
                }
        );

        // Asignar el adaptador al RecyclerView
        rvPreguntas.setAdapter(preguntasAdapter);

        // Registrar en Logcat el número de preguntas cargadas para depuración
        Log.d("GestionPreguntas", "Preguntas cargadas: " + preguntas.size());
    }


    private void guardarPregunta() {
        // Obtener los datos ingresados por el usuario
        String pregunta = etPregunta.getText().toString().trim();
        String opcion1 = etOpcion1.getText().toString().trim();
        String opcion2 = etOpcion2.getText().toString().trim();
        String opcion3 = etOpcion3.getText().toString().trim();
        String opcion4 = etOpcion4.getText().toString().trim();
        String respuestaCorrectaStr = etRespuestaCorrecta.getText().toString().trim();

        if (pregunta.isEmpty() || opcion1.isEmpty() || opcion2.isEmpty() ||
                opcion3.isEmpty() || opcion4.isEmpty() || respuestaCorrectaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int respuestaCorrecta;
        try {
            respuestaCorrecta = Integer.parseInt(respuestaCorrectaStr);
            if (respuestaCorrecta < 1 || respuestaCorrecta > 4) {
                Toast.makeText(this, "La respuesta correcta debe ser un número entre 1 y 4", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La respuesta correcta debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        long resultado = dbHelper.agregarPregunta(pregunta, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta);

        if (resultado != -1) {
            Toast.makeText(this, "Pregunta guardada correctamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            cargarPreguntas(); // Recargar la lista de preguntas después de guardar
        } else {
            Toast.makeText(this, "Error al guardar la pregunta", Toast.LENGTH_SHORT).show();
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

    private void eliminarPregunta(Pregunta pregunta) {
        boolean resultado = dbHelper.eliminarPregunta(pregunta.getId());

        if (resultado) {
            Toast.makeText(this, "Pregunta eliminada correctamente", Toast.LENGTH_SHORT).show();
            cargarPreguntas(); // Recargar la lista después de eliminar
        } else {
            Toast.makeText(this, "Error al eliminar la pregunta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
