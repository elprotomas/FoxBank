package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RecuperarContrasena extends AppCompatActivity implements View.OnClickListener {

    EditText etContrasenaNueva, etConfirmarContrasena;
    Button btnRecuperarContrasena;
    ImageButton ibtnDevolver;
    ImageView imageView,imageView7;
    RequestQueue requestQueue;

    private static final String URL_RESET_PASSWORD = "http://192.168.1.46/proyecto/restablecer_contraseña.php";
    private boolean isPasswordVisible = false;
    private boolean isConfirmPassowordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_contrasena);

        requestQueue = Volley.newRequestQueue(this);

        initUI();

        btnRecuperarContrasena.setOnClickListener(this);
        ibtnDevolver.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView7.setOnClickListener(this);
    }
    private void initUI() {
        etContrasenaNueva = findViewById(R.id.etContrasenaNueva);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnRecuperarContrasena = findViewById(R.id.btnRecuperarContrasena);
        ibtnDevolver = findViewById(R.id.ibtnDevolver);
        imageView = findViewById(R.id.imageView);
        imageView7 = findViewById(R.id.imageView7);
    }

        @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRecuperarContrasena) {
            recuperarContrasena();
        } else if (v.getId() == R.id.ibtnDevolver) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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
            etContrasenaNueva.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.eye_closed);
            isPasswordVisible = false;
        } else {
            // Cambiar a contraseña visible
            etContrasenaNueva.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.eye_open);
            isPasswordVisible = true;
        }
    }
    private void toggleCvvVisibility() {
        if (isConfirmPassowordVisible) {
            // Cambiar a contraseña oculta
            etConfirmarContrasena.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView7.setImageResource(R.drawable.eye_closed);
            isConfirmPassowordVisible = false;
        } else {
            // Cambiar a contraseña visible
            etConfirmarContrasena.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView7.setImageResource(R.drawable.eye_open);
            isConfirmPassowordVisible = true;
        }
    }
    private void recuperarContrasena() {
        String idUsuario = getIntent().getStringExtra("id_usuario");
        String contrasenaNueva = etContrasenaNueva.getText().toString().trim();
        String confirmarContrasena = etConfirmarContrasena.getText().toString().trim();

        if (!contrasenaNueva.isEmpty() && !confirmarContrasena.isEmpty()) {
            if (contrasenaNueva.equals(confirmarContrasena)) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_RESET_PASSWORD +
                        "?id_usuario=" + idUsuario +
                        "&nueva_contrasena=" + contrasenaNueva +
                        "&confirmar_contrasena=" + confirmarContrasena,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Respuesta del servidor: " + response);
                                Toast.makeText(RecuperarContrasena.this, response, Toast.LENGTH_SHORT).show();
                                if (response.equals("Tu contraseña ha sido restablecida exitosamente.")) {
                                    startActivity(new Intent(RecuperarContrasena.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RecuperarContrasena.this, "Error al restablecer la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });

                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(this, "La nueva contraseña y la confirmación no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
