package com.example.parcial_b1;

public class Pregunta {
    private String id;
    private String texto;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private int respuestaCorrecta;

    // Constructor vacío necesario para Firebase
    public Pregunta() {
    }
    // Añade este método a tu clase Pregunta
    public boolean esRespuestaCorrecta(int opcionSeleccionada) {
        return opcionSeleccionada == respuestaCorrecta;
    }

    public Pregunta(String texto, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta) {
        this.texto = texto;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getOpcion1() { return opcion1; }
    public void setOpcion1(String opcion1) { this.opcion1 = opcion1; }
    public String getOpcion2() { return opcion2; }
    public void setOpcion2(String opcion2) { this.opcion2 = opcion2; }
    public String getOpcion3() { return opcion3; }
    public void setOpcion3(String opcion3) { this.opcion3 = opcion3; }
    public String getOpcion4() { return opcion4; }
    public void setOpcion4(String opcion4) { this.opcion4 = opcion4; }
    public int getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(int respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
}