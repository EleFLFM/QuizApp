package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainDocente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_docente);

        // Inicializar botones
        Button btnCerrarSesion = findViewById(R.id.btnLogout);
        Button btnGestionEstudiantes = findViewById(R.id.btnGestionEstudiantes);
        Button btnGestionPreguntas = findViewById(R.id.btnGestionPreguntas);

        // Configurar listeners
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
        btnGestionEstudiantes.setOnClickListener(v -> irAGestionEstudiantes());
        btnGestionPreguntas.setOnClickListener(v -> irAGestionPreguntas());
    }

    private void cerrarSesion() {
        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut();

        // Redirigir a MainActivity y limpiar el stack
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void irAGestionEstudiantes() {
        startActivity(new Intent(this, GestionEstudiantesActivity.class));
    }

    private void irAGestionPreguntas() {
        startActivity(new Intent(this, GestionPreguntasActivity.class));
    }

    @Override
    public void onBackPressed() {
        // Opcional: Mostrar diálogo de confirmación para salir
        super.onBackPressed();
    }
}