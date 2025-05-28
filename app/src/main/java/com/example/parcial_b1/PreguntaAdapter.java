package com.example.parcial_b1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder> {

    private List<Pregunta> listaPreguntas;
    private OnPreguntaClickListener listener;

    public interface OnPreguntaClickListener {
        void onPreguntaClick(Pregunta pregunta);
    }

    public PreguntaAdapter(List<Pregunta> listaPreguntas, OnPreguntaClickListener listener) {
        this.listaPreguntas = listaPreguntas;
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
        Pregunta pregunta = listaPreguntas.get(position);
        holder.bind(pregunta);
        holder.itemView.setOnClickListener(v -> listener.onPreguntaClick(pregunta));
    }

    @Override
    public int getItemCount() {
        return listaPreguntas.size();
    }

    static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPregunta;
        private ImageView ivImagen;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPregunta = itemView.findViewById(R.id.tvPregunta);
            ivImagen = itemView.findViewById(R.id.ivImagen);
        }

        public void bind(Pregunta pregunta) {
            tvPregunta.setText(pregunta.getTexto());

            if (pregunta.getImagenUrl() != null && !pregunta.getImagenUrl().isEmpty()) {
                ivImagen.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(pregunta.getImagenUrl())
                        .apply(new RequestOptions().centerCrop())
                        .into(ivImagen);
            } else {
                ivImagen.setVisibility(View.GONE);
            }
        }
    }
}