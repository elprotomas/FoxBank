package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etCorreo, etContrasena;
    Button btnIniciarSesion;
    TextView tvContrasenaOlvidada, tvRegistrate;
    ImageView imageView;

    RequestQueue requestQueue;

    private static final String URL_LOGIN = "http://192.168.1.46/proyecto/login.php";

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        requestQueue = Volley.newRequestQueue(this);

        initUI();

        btnIniciarSesion.setOnClickListener(this);
        tvContrasenaOlvidada.setOnClickListener(this);
        tvRegistrate.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private void initUI() {
        //EditText
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);

        //Buttons
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // TextViews
        tvContrasenaOlvidada = findViewById(R.id.tvContrasenaOlvidada);
        tvRegistrate = findViewById(R.id.tvRegistrate);

        // ImageView
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnIniciarSesion) {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();
            iniciarSesion(correo, contrasena);
        } else if (v.getId() == R.id.tvContrasenaOlvidada) { // Contraseña olvidada
            onPasswordForgottenClick(v);
        } else if (v.getId() == R.id.tvRegistrate) { // Regístrate
            onRegisterClick(v);
        } else if (v.getId() == R.id.imageView) { // Cambiar visibilidad de la contraseña
            togglePasswordVisibility();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Cambiar a contraseña oculta
            etContrasena.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.eye_closed);
            isPasswordVisible = false;
        } else {
            // Cambiar a contraseña visible
            etContrasena.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.eye_open);
            isPasswordVisible = true;
        }
    }

    public void onPasswordForgottenClick(View view) {
        // Redirigir al usuario a LoginActivity.java
        Intent intent = new Intent(LoginActivity.this, InfoContraseña.class);
        startActivity(intent);
    }

    public void onRegisterClick(View view) {
        // Redirigir al usuario a LoginActivity.java
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void iniciarSesion(final String correo, final String contrasena) {
        String url = URL_LOGIN + "?correo_electronico=" + correo + "&contrasena=" + contrasena;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.startsWith("success")) {
                            String[] parts = response.split("\\|");
                            if (parts.length >= 2) {
                                String idUsuario = parts[1];
                                Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                                intent.putExtra("id_usuario", idUsuario);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error en el formato de la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(stringRequest);
    }
}
