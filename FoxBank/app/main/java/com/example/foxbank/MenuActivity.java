package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    CardView btnInicio,btnInfoUser,btnInfoTarjeta,btnTransferencias,btnSalir;
    ImageButton ibtnDevolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        initUI();

        btnInicio.setOnClickListener(this);
        btnInfoUser.setOnClickListener(this);
        btnInfoTarjeta.setOnClickListener(this);
        btnTransferencias.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        ibtnDevolver.setOnClickListener(this);

        String idUsuario = getIntent().getStringExtra("id_usuario");

    }
    private void initUI() {
        btnInicio = findViewById(R.id.btnInicio);
        btnInfoUser = findViewById(R.id.btnInfoUser);
        btnInfoTarjeta = findViewById(R.id.btnInfoTarjeta);
        btnTransferencias = findViewById(R.id.btnTransferencias);
        btnSalir = findViewById(R.id.btnSalir);
        ibtnDevolver = findViewById(R.id.ibtnDevolver);
    }

        @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.btnInicio) {
            intent = new Intent(MenuActivity.this, InicioActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.btnInfoUser) {
            intent = new Intent(MenuActivity.this, InfoUserActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.btnInfoTarjeta) {
            intent = new Intent(MenuActivity.this, InfoTarjetaActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.btnTransferencias) {
            intent = new Intent(MenuActivity.this, TransferenciasActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.btnSalir) {
            intent = new Intent(MenuActivity.this, LoginActivity.class);
        } else if (v.getId() == R.id.ibtnDevolver) {
            intent = new Intent(MenuActivity.this, InicioActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }
}
