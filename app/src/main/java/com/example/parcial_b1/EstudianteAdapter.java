package com.example.parcial_b1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder> {

    private List<Estudiante> estudianteList;
    private OnEstudianteClickListener listener;

    public interface OnEstudianteClickListener {
        void onEstudianteClick(Estudiante estudiante);
    }

    public EstudianteAdapter(List<Estudiante> estudianteList, OnEstudianteClickListener listener) {
        this.estudianteList = estudianteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EstudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estudiante, parent, false);
        return new EstudianteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudianteViewHolder holder, int position) {
        Estudiante estudiante = estudianteList.get(position);
        holder.tvNombre.setText(estudiante.getNombre());
        holder.tvDocumento.setText(estudiante.getDocumento());
        holder.tvEmail.setText(estudiante.getEmail());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEstudianteClick(estudiante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return estudianteList.size();
    }

    public void filterList(List<Estudiante> filteredList) {
        estudianteList = filteredList;
        notifyDataSetChanged();
    }

    static class EstudianteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDocumento, tvEmail;

        public EstudianteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDocumento = itemView.findViewById(R.id.tvDocumento);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}