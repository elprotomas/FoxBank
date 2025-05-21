package com.example.foxbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransferenciaAdapter extends RecyclerView.Adapter<TransferenciaAdapter.TransferenciaViewHolder> {

    private Context context;
    private ArrayList<Transferencia> transferenciasList;

    public TransferenciaAdapter(Context context, ArrayList<Transferencia> transferenciasList) {
        this.context = context;
        this.transferenciasList = transferenciasList;
    }

    @NonNull
    @Override
    public TransferenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transferencia, parent, false);
        return new TransferenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferenciaViewHolder holder, int position) {
        Transferencia transferencia = transferenciasList.get(position);
        holder.bind(transferencia);
    }

    @Override
    public int getItemCount() {
        return transferenciasList.size();
    }

    public static class TransferenciaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipoTransaccion, tvMonto, tvFecha, tvDescripcion;

        public TransferenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoTransaccion = itemView.findViewById(R.id.tvTipoTransaccion);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }

        public void bind(Transferencia transferencia) {
            tvTipoTransaccion.setText(transferencia.getTipoTransaccion());
            tvMonto.setText(transferencia.getMonto());
            tvFecha.setText(transferencia.getFecha());
            tvDescripcion.setText(transferencia.getDescripcion());
        }
    }
}
