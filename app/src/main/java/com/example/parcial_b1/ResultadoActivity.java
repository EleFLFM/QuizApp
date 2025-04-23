package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad que muestra el resultado del quiz, mostrando el puntaje obtenido por el usuario
 * y ofreciendo opciones para compartir el resultado o salir de la aplicación.
 */
public class ResultadoActivity extends AppCompatActivity {
    private TextView tvResultado;  // TextView que muestra el puntaje final
    private Button btnCompartir, btnSalir;  // Botones para compartir el puntaje y salir

    /**
     * Método llamado cuando la actividad es creada.
     * Aquí se inicializan las vistas y se configuran los botones para las acciones del usuario.
     *
     * @param savedInstanceState Información previa guardada sobre el estado de la actividad (si es necesario).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);  // Establece el layout de la actividad

        // Inicializar las vistas
        tvResultado = findViewById(R.id.tvResultado);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnSalir = findViewById(R.id.btnSalir);

        // Obtener el puntaje y el total de preguntas desde el Intent
        int puntaje = getIntent().getIntExtra("puntaje", 0);  // Obtiene el puntaje desde el Intent
        int totalPreguntas = getIntent().getIntExtra("totalPreguntas", 0);  // Obtiene el total de preguntas

        // Mostrar el puntaje en la TextView
        tvResultado.setText("Puntaje final: " + puntaje + " / " + (totalPreguntas * 10));

        // Configurar el botón de compartir
        btnCompartir.setOnClickListener(v -> {
            // Crear el mensaje de puntaje para compartir
            String mensaje = "¡Obtuve " + puntaje + " puntos de " + (totalPreguntas * 10) +
                    " posibles en el Quiz App!";

            // Crear un Intent para compartir el mensaje
            Intent compartirIntent = new Intent(Intent.ACTION_SEND);
            compartirIntent.setType("text/plain");  // Especificar el tipo de contenido
            compartirIntent.putExtra(Intent.EXTRA_TEXT, mensaje);  // Agregar el mensaje al Intent

            // Iniciar la actividad de compartir
            startActivity(Intent.createChooser(compartirIntent, "Compartir usando"));
        });

        // Configurar el botón de salir
        btnSalir.setOnClickListener(v -> finish());  // Termina la actividad actual y regresa a la anterior
    }
}
