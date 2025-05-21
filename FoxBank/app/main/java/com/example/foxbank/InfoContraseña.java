package com.example.foxbank;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class InfoContraseña extends AppCompatActivity implements View.OnClickListener {

    EditText etCorreo;
    ImageButton ibtnDevolver;
    RequestQueue requestQueue;

    private static final String URL_GET_PASSWORD = "http://192.168.1.46/proyecto/obtener_correo_electronico.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_contrasena);

        requestQueue = Volley.newRequestQueue(this);

        etCorreo = findViewById(R.id.etCorreo);

        // Configurar el Listener para el botón de recuperación de contraseña
        findViewById(R.id.btnRecuperarContrasena).setOnClickListener(view -> recuperarContrasena());
        ibtnDevolver = findViewById(R.id.ibtnDevolver);
        ibtnDevolver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ibtnDevolver){
            Intent intent = new Intent(InfoContraseña.this, LoginActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivity(intent);
        }
    }

    private void recuperarContrasena() {
        String correo = etCorreo.getText().toString().trim();

        // Expresión regular para validar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Verificar si el correo electrónico tiene un formato válido
        if (correo.matches(emailPattern)) {
            // Realizar la solicitud para verificar si el correo existe en la base de datos
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_PASSWORD + "?correo_electronico=" + correo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.startsWith("success")) {
                                String[] parts = response.split("\\|");
                                if (parts.length >= 2) {
                                    String idUsuario = parts[1];
                                    Intent intent = new Intent(InfoContraseña.this, RecuperarContrasena.class);
                                    intent.putExtra("id_usuario", idUsuario);
                                    startActivity(intent);
                                }
                            } else {
                                // Mostrar un mensaje si el correo no existe en la base de datos
                                Toast.makeText(InfoContraseña.this, "Correo electrónico no registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // No hacer nada en caso de error
                            Toast.makeText(InfoContraseña.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Agregar la solicitud a la cola de solicitudes
            requestQueue.add(stringRequest);
        } else {
            // Mostrar un mensaje de error si el formato del correo electrónico no es válido
            Toast.makeText(InfoContraseña.this, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoContraseña.this, LoginActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }
}
