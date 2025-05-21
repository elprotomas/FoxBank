package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoTarjetaActivity extends AppCompatActivity implements View.OnClickListener{

    TextView etNombreUsuario, etContrasenaUsuario, etTipoTarjeta, etNumeroTarjeta, etAnioVencimiento, etMesVencimiento, etDiaVencimiento, etCVV;
    ImageButton ibtnDevolver;
    ImageView imageView,imageView7;
    RequestQueue requestQueue;

    private static final String URL_GET_CARD_INFO = "http://192.168.1.46/proyecto/obtener_tarjeta.php";
    private boolean isPasswordVisible = false;
    private boolean isCvvVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_tarjeta);

        requestQueue = Volley.newRequestQueue(this);

        initUI();


        // Obtener el id_usuario de la actividad anterior
        String idUsuario = getIntent().getStringExtra("id_usuario");
        if (idUsuario != null) {
            obtenerInformacionTarjeta(idUsuario);
        } else {
            Toast.makeText(this, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
        }

        ibtnDevolver.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView7.setOnClickListener(this);

    }
    private void initUI() {
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etContrasenaUsuario = findViewById(R.id.etContrasenaUsuario);
        etTipoTarjeta = findViewById(R.id.etTipoTarjeta);
        etNumeroTarjeta = findViewById(R.id.etNumeroTarjeta);
        etAnioVencimiento = findViewById(R.id.etAnioVencimiento);
        etMesVencimiento = findViewById(R.id.etMesVencimiento);
        etDiaVencimiento = findViewById(R.id.etDiaVencimiento);
        etCVV = findViewById(R.id.etCVV);
        ibtnDevolver = findViewById(R.id.ibtnDevolver);
        imageView = findViewById(R.id.imageView);
        imageView7 = findViewById(R.id.imageView7);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ibtnDevolver){
            Intent intent = new Intent(InfoTarjetaActivity.this, InicioActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivity(intent);
        }else if (v.getId() == R.id.imageView) { // Cambiar visibilidad de la contraseña
            togglePasswordVisibility();
        }
        else if (v.getId() == R.id.imageView7) { // Cambiar visibilidad de la contraseña
            toggleCvvVisibility();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Cambiar a contraseña oculta
            etContrasenaUsuario.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.eye_closed);
            isPasswordVisible = false;
        } else {
            // Cambiar a contraseña visible
            etContrasenaUsuario.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.eye_open);
            isPasswordVisible = true;
        }
    }
    private void toggleCvvVisibility() {
        if (isCvvVisible) {
            // Cambiar a contraseña oculta
            etCVV.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView7.setImageResource(R.drawable.eye_closed);
            isCvvVisible = false;
        } else {
            // Cambiar a contraseña visible
            etCVV.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView7.setImageResource(R.drawable.eye_open);
            isCvvVisible = true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoTarjetaActivity.this, InicioActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }
    private void obtenerInformacionTarjeta(String idUsuario) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_CARD_INFO + "?id_usuario=" + idUsuario, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombreUsuario = response.getString("nombre");
                            String contrasenaUsuario = response.getString("contrasena");
                            String tipoTarjeta = response.getString("tipo_tarjeta");
                            String numeroTarjeta = response.getString("numero_tarjeta");
                            String fechaVencimiento = response.getString("fecha_vencimiento");
                            String cvv = response.getString("CVV");

                            // Dividir la fecha en año, mes y día
                            String[] partesFecha = fechaVencimiento.split("-");
                            String anio = partesFecha[0];
                            String mes = partesFecha[1];
                            String dia = partesFecha[2];

                            etNombreUsuario.setText(nombreUsuario);
                            etContrasenaUsuario.setText(contrasenaUsuario);
                            etTipoTarjeta.setText(tipoTarjeta);
                            etNumeroTarjeta.setText(numeroTarjeta);
                            etAnioVencimiento.setText(anio);
                            etMesVencimiento.setText(mes);
                            etDiaVencimiento.setText(dia);
                            etCVV.setText(cvv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InfoTarjetaActivity.this, "Error en el formato de la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfoTarjetaActivity.this, "Error al obtener la información de la tarjeta", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(request);
    }
}