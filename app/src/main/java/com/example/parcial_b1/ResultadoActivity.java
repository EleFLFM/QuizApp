package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ResultadoActivity extends AppCompatActivity {

    private TextView tvResultado;
    private MaterialButton btnCompartir, btnSalir;
    private int puntaje;
    private int total_preguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        // Inicializar vistas
        initViews();

        // Obtener datos del intent
        getIntentData();

        // Mostrar resultado
        mostrarResultado();

    }

    private void initViews() {
        tvResultado = findViewById(R.id.tvResultado);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnSalir = findViewById(R.id.btnSalir);
    }

    private void getIntentData() {
        puntaje = getIntent().getIntExtra("puntaje", 0);
        total_preguntas = getIntent().getIntExtra("totalPreguntas", 0);
    }

    private void mostrarResultado() {
        int puntajeMaximo = total_preguntas * 10; // Cada pregunta vale 10 puntos
        String resultado = String.format("Puntaje final: %d/%d", puntaje, puntajeMaximo);
        tvResultado.setText(resultado);

        // Opcional: Cambiar color según el puntaje
        if (puntaje >= puntajeMaximo * 0.8) {
            tvResultado.setTextColor(getResources().getColor(R.color.green));
        } else if (puntaje >= puntajeMaximo * 0.5) {
            tvResultado.setTextColor(getResources().getColor(R.color.orange));
        } else {
            tvResultado.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void setupListeners() {
        // Botón compartir
        btnCompartir.setOnClickListener(v -> compartirResultado());

        // Botón salir - CORRECCIÓN: Usar finish() en lugar de crear nueva instancia
        btnSalir.setOnClickListener(v -> finish());
    }

    private void compartirResultado() {
        String mensaje = String.format(
                "¡Obtuve %d de %d puntos en el Quiz App! ¿Crees que puedes superarme?",
                puntaje,
                total_preguntas * 10
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        startActivity(Intent.createChooser(
                shareIntent,
                getString(R.string.share_via)
        ));
    }
    public void salir(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}