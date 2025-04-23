package com.example.parcial_b1;

public class Pregunta {
    private int id; // Atributo para almacenar el ID único de la pregunta
    private String pregunta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private int respuestaCorrecta;

    // Constructor que incluye el ID
    public Pregunta(int id, String pregunta, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta) {
        this.id = id;
        this.pregunta = pregunta;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
