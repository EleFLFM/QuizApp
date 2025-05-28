package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ResultadoActivity extends AppCompatActivity {

    private TextView tvResultado;
    private MaterialButton btnCompartir, btnSalir, btnIntentarOtraVez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        btnIntentarOtraVez = findViewById(R.id.btnIntentarOtraVez);
        tvResultado = findViewById(R.id.tvResultado);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnSalir = findViewById(R.id.btnSalir);

        // Obtener datos del intent (puntaje)
        int puntaje = getIntent().getIntExtra("puntaje", 0);
        int total = getIntent().getIntExtra("total", 100); // valor por defecto

        String resultado = "Puntaje final: " + puntaje + "/" + total;
        tvResultado.setText(resultado);

        btnCompartir.setOnClickListener(v -> compartirResultado(resultado));
        // Intentar otra vez
        btnIntentarOtraVez.setOnClickListener(v -> {
            Intent intent = new Intent(ResultadoActivity.this, MainActivity.class); // o tu actividad de preguntas
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    public void compartirResultado(String resultado) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "¡Mi resultado del quiz!");
        intent.putExtra(Intent.EXTRA_TEXT, resultado);
        startActivity(Intent.createChooser(intent, "Compartir usando"));
    }

    // Método conectado desde XML: android:onClick="salir"
    public void salir(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad y regresa a la anterior
    }
}
