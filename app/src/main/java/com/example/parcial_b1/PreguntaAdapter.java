package com.example.parcial_b1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder> {

    private final List<Pregunta> preguntas;
    private final OnPreguntaDeleteListener onDeleteListener;

    public interface OnPreguntaDeleteListener {
        void onDelete(Pregunta pregunta);
    }

    public PreguntaAdapter(List<Pregunta> preguntas, OnPreguntaDeleteListener onDeleteListener) {
        this.preguntas = preguntas;
        this.onDeleteListener = onDeleteListener;
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
        holder.tvPregunta.setText(pregunta.getPregunta());

        // Configurar botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            if (onDeleteListener != null) {
                onDeleteListener.onDelete(pregunta);
            }
        });
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta;
        ImageButton btnEliminar;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPregunta = itemView.findViewById(R.id.tvPregunta);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
