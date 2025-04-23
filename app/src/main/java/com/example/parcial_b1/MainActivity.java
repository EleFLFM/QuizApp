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

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etContraseña;
    private Button btnLogin;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Inicializar vistas
        etUsuario = findViewById(R.id.etUsuario);
        etContraseña = findViewById(R.id.etContraseña);
        btnLogin = findViewById(R.id.btnLogin);

        // Configurar botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString().trim();
                String contraseña = etContraseña.getText().toString().trim();

                if (usuario.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    verificarCredencialesFirebase(usuario, contraseña);
                }
            }
        });
    }

    private void verificarCredencialesFirebase(String usuario, String contraseña) {
        // Buscar usuario en Firebase
        databaseReference.orderByChild("usuario").equalTo(usuario)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Usuario encontrado, verificar contraseña
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null && user.getContraseña().equals(contraseña)) {
                                    // Credenciales correctas
                                    redirigirSegunRol(user.getRol());
                                    return;
                                }
                            }
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirigirSegunRol(String rol) {
        Intent intent;
        if ("docente".equals(rol)) {
            intent = new Intent(MainActivity.this, MainDocente.class);
        } else if ("estudiante".equals(rol)) {
            intent = new Intent(MainActivity.this, JuegoActivity.class);
        } else {
            Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No necesitamos cerrar conexiones con Firebase
    }
}