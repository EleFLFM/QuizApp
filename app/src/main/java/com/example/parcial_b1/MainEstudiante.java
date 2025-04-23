package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estudiante);

        // Configurar botones
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnPresentarQuiz = findViewById(R.id.btnPresentarQuiz);

        // Botón Cerrar Sesión
        btnLogout.setOnClickListener(v -> {
            // Cerrar sesión en Firebase
            FirebaseAuth.getInstance().signOut();

            // Redirigir al login y limpiar el stack de actividades
            Intent intent = new Intent(MainEstudiante.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Botón Presentar Quiz
        btnPresentarQuiz.setOnClickListener(v -> {
            // Redirigir a la actividad del quiz
            startActivity(new Intent(MainEstudiante.this, JuegoActivity.class));
        });
    }
}