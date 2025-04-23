package com.example.parcial_b1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase DBHelper para gestionar la base de datos SQLite de la aplicación.
 * Esta clase extiende SQLiteOpenHelper y permite realizar operaciones CRUD
 * (Crear, Leer, Actualizar, Eliminar) sobre las tablas de usuarios y preguntas.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizapp.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1; // Versión de la base de datos

    /**
     * Constructor de la clase DBHelper.
     *
     * @param context Contexto de la aplicación
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Método llamado cuando se crea la base de datos.
     * Crea las tablas necesarias y agrega datos iniciales.
     *
     * @param db Instancia de la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuarios
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "usuario TEXT UNIQUE, " +
                "contraseña TEXT, " +
                "rol TEXT CHECK(rol IN ('docente', 'estudiante')))");

        // Crear tabla preguntas
        db.execSQL("CREATE TABLE preguntas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pregunta TEXT, " +
                "opcion1 TEXT, " +
                "opcion2 TEXT, " +
                "opcion3 TEXT, " +
                "opcion4 TEXT, " +
                "respuesta_correcta INTEGER)");

        // Insertar usuarios iniciales
        db.execSQL("INSERT INTO usuarios (nombre, usuario, contraseña, rol) VALUES " +
                "('Profesor1', 'profesor', '1234', 'docente'), " +
                "('Estudiante1', 'estudiante', 'abcd', 'estudiante')");

        // Insertar preguntas de prueba usando el método insertarPreguntaPrueba
        insertarPreguntaPrueba(db, "¿Cuál es el lenguaje de programación más utilizado en el desarrollo de aplicaciones Android?",
                "Java", "Python", "C#", "Kotlin", 4);
        insertarPreguntaPrueba(db, "¿Qué es un algoritmo?",
                "Una máquina de procesamiento de datos", "Un conjunto de instrucciones para resolver un problema",
                "Un tipo de lenguaje de programación", "Un sistema operativo", 2);
        insertarPreguntaPrueba(db, "¿Qué significa HTML?",
                "Hyper Text Markup Language", "Hyper Text Machine Language", "High Text Markup Language",
                "None of the above", 1);
        insertarPreguntaPrueba(db, "¿Cuál es la función de un compilador?",
                "Traducir el código fuente a código máquina", "Ejecutar el código fuente directamente",
                "Almacenar el código fuente", "Detectar errores en el código fuente", 1);
        insertarPreguntaPrueba(db, "¿Qué es el OOP (Programación Orientada a Objetos)?",
                "Un enfoque basado en funciones", "Un enfoque basado en objetos y clases",
                "Un paradigma de programación funcional", "Un enfoque para la creación de bases de datos", 2);
    }

    /**
     * Método para obtener todas las preguntas almacenadas en la base de datos.
     *
     * @return Una lista de objetos Pregunta con los datos de las preguntas.
     */
    public List<Pregunta> obtenerTodasLasPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener todas las preguntas
        String query = "SELECT id, pregunta, opcion1, opcion2, opcion3, opcion4, respuesta_correcta FROM preguntas";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Extraer datos del cursor
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String pregunta = cursor.getString(cursor.getColumnIndex("pregunta"));
                String opcion1 = cursor.getString(cursor.getColumnIndex("opcion1"));
                String opcion2 = cursor.getString(cursor.getColumnIndex("opcion2"));
                String opcion3 = cursor.getString(cursor.getColumnIndex("opcion3"));
                String opcion4 = cursor.getString(cursor.getColumnIndex("opcion4"));
                int respuestaCorrecta = cursor.getInt(cursor.getColumnIndex("respuesta_correcta"));

                // Crear objeto Pregunta y agregarlo a la lista
                preguntas.add(new Pregunta(id, pregunta, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return preguntas;
    }

    /**
     * Método para eliminar una pregunta de la base de datos mediante su ID.
     *
     * @param id ID de la pregunta a eliminar.
     * @return true si la pregunta fue eliminada exitosamente, false en caso contrario.
     */
    public boolean eliminarPregunta(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete("preguntas", "id = ?", new String[]{String.valueOf(id)});
        return filasAfectadas > 0; // Retorna true si se eliminó al menos una fila
    }

    /**
     * Método privado para insertar una pregunta de prueba en la base de datos.
     *
     * @param db Instancia de la base de datos SQLite.
     * @param pregunta La pregunta que se quiere insertar.
     * @param op1 La primera opción de respuesta.
     * @param op2 La segunda opción de respuesta.
     * @param op3 La tercera opción de respuesta.
     * @param op4 La cuarta opción de respuesta.
     * @param respuestaCorrecta El índice de la opción correcta.
     */
    private void insertarPreguntaPrueba(SQLiteDatabase db, String pregunta,
                                        String op1, String op2, String op3, String op4,
                                        int respuestaCorrecta) {
        ContentValues values = new ContentValues();
        values.put("pregunta", pregunta);
        values.put("opcion1", op1);
        values.put("opcion2", op2);
        values.put("opcion3", op3);
        values.put("opcion4", op4);
        values.put("respuesta_correcta", respuestaCorrecta);
        db.insert("preguntas", null, values);
    }

    /**
     * Método para agregar una nueva pregunta a la base de datos.
     *
     * @param pregunta La pregunta a agregar.
     * @param op1 La primera opción de respuesta.
     * @param op2 La segunda opción de respuesta.
     * @param op3 La tercera opción de respuesta.
     * @param op4 La cuarta opción de respuesta.
     * @param respuestaCorrecta El índice de la opción correcta.
     * @return El ID de la pregunta recién insertada o -1 si hubo un error.
     */
    public long agregarPregunta(String pregunta, String op1, String op2, String op3, String op4, int respuestaCorrecta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pregunta", pregunta);
        values.put("opcion1", op1);
        values.put("opcion2", op2);
        values.put("opcion3", op3);
        values.put("opcion4", op4);
        values.put("respuesta_correcta", respuestaCorrecta);
        return db.insert("preguntas", null, values);
    }

    /**
     * Método llamado cuando la base de datos necesita ser actualizada.
     * Aquí se eliminan las tablas existentes y se crean de nuevo.
     *
     * @param db Instancia de la base de datos.
     * @param oldVersion Versión anterior de la base de datos.
     * @param newVersion Nueva versión de la base de datos.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS preguntas");
        onCreate(db); // Llamar a onCreate para crear las nuevas tablas
    }
}
