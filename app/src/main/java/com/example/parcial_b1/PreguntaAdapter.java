package com.example.parcial_b1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder> {

    public interface OnPreguntaClickListener {
        void onPreguntaClick(Pregunta pregunta);
    }

    private List<Pregunta> preguntas;
    private OnPreguntaClickListener listener;

    public PreguntaAdapter(List<Pregunta> preguntas, OnPreguntaClickListener listener) {
        this.preguntas = preguntas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pregunta, parent, false);
        return new PreguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaViewHolder holder, int position) {
        Pregunta pregunta = preguntas.get(position);
        holder.tvPregunta.setText(pregunta.getTexto());
        holder.tvOpciones.setText(
                "1) " + pregunta.getOpcion1() + "\n" +
                        "2) " + pregunta.getOpcion2() + "\n" +
                        "3) " + pregunta.getOpcion3() + "\n" +
                        "4) " + pregunta.getOpcion4()
        );
        holder.tvRespuesta.setText("Respuesta correcta: " + pregunta.getRespuestaCorrecta());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPreguntaClick(pregunta);
            }
        });
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta, tvOpciones, tvRespuesta;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPregunta = itemView.findViewById(R.id.tvPregunta);
            tvOpciones = itemView.findViewById(R.id.tvOpciones);
            tvRespuesta = itemView.findViewById(R.id.tvRespuesta);
        }
    }
}