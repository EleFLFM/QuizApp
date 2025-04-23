package com.example.parcial_b1;

import android.content.Intent;
import android.os.Bundle;
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

public class GestionEstudiantesActivity extends AppCompatActivity
        implements EstudianteAdapter.OnEstudianteClickListener {

    private RecyclerView recyclerView;
    private EstudianteAdapter adapter;
    private List<Estudiante> estudianteList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_estudiantes);

        // Configurar botón de volver
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("estudiantes");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rvEstudiantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EstudianteAdapter(estudianteList, this); // 'this' es válido porque implementa la interfaz
        recyclerView.setAdapter(adapter);

        // Configurar búsqueda
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
        });

        // Cargar estudiantes desde Firebase
        loadEstudiantes();
    }

    private void loadEstudiantes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estudianteList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Estudiante estudiante = dataSnapshot.getValue(Estudiante.class);
                    if (estudiante != null && "estudiante".equals(estudiante.getRol())) {
                        estudianteList.add(estudiante);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionEstudiantesActivity.this,
                        "Error al cargar estudiantes: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        List<Estudiante> filteredList = new ArrayList<>();
        for (Estudiante estudiante : estudianteList) {
            if (estudiante.getNombre().toLowerCase().contains(text.toLowerCase()) ||
                    estudiante.getDocumento().contains(text)) {
                filteredList.add(estudiante);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onEstudianteClick(Estudiante estudiante) {
        // Navegar a DetalleEstudianteActivity con los datos del estudiante
        Intent intent = new Intent(this, DetalleEstudianteActivity.class);
        intent.putExtra("documento", estudiante.getDocumento());
        intent.putExtra("nombre", estudiante.getNombre());
        intent.putExtra("email", estudiante.getEmail());
        startActivity(intent);
    }

    // Clase auxiliar para simplificar TextWatcher
    private abstract class TextWatcherAdapter implements android.text.TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(android.text.Editable s) {}
    }
}