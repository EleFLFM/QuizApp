package com.example.parcial_b1;

public class User {
    private String documento;
    private String usuario;
    private String nombre;
    private String contraseña;
    private String rol;

    // Constructor vacío necesario para Firebase
    public User() {}

    public User(String documento, String usuario, String contraseña) {
        this.documento = documento;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    // Getters y setters
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}