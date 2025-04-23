package com.example.parcial_b1;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText etDocumento, etUsuario, etNombre, etContraseña;
    private Button btnRegister;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Inicializar vistas
        etDocumento = findViewById(R.id.etDocumento);
        etUsuario = findViewById(R.id.etUsuario);
        etNombre = findViewById(R.id.etNombre);
        etContraseña = findViewById(R.id.etContraseña);
        btnRegister = findViewById(R.id.btnRegister);

        // Configurar botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }
    public void goToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    private void registrarUsuario() {
        final String documento = etDocumento.getText().toString().trim();
        final String usuario = etUsuario.getText().toString().trim();
        final String nombre = etNombre.getText().toString().trim();
        final String contraseña = etContraseña.getText().toString().trim();

        // Validar campos vacíos
        if (documento.isEmpty() || usuario.isEmpty() || nombre.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el documento ya existe
        databaseReference.child(documento).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Documento ya registrado
                    Toast.makeText(RegisterActivity.this, "Este documento ya está registrado", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar si el usuario ya existe
                    databaseReference.orderByChild("usuario").equalTo(usuario)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(RegisterActivity.this, "Este nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Crear nuevo usuario
                                        User nuevoUsuario = new User(documento, usuario, contraseña);
                                        nuevoUsuario.setNombre(nombre);
                                        nuevoUsuario.setRol("estudiante"); // Rol por defecto

                                        databaseReference.child(documento).setValue(nuevoUsuario)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                        finish(); // Volver al login
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegisterActivity.this, "Error al verificar usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Error al verificar documento", Toast.LENGTH_SHORT).show();
            }
        });
    }
}