package com.example.parcial_b1;

public class Estudiante {
    private String documento;
    private String nombre;
    private String email;
    private String rol;

    public Estudiante() {
        // Constructor vacío requerido por Firebase
    }

    public Estudiante(String documento, String nombre, String email, String rol) {
        this.documento = documento;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    // Getters y setters
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}