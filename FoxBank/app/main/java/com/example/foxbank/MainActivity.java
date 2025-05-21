package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etApellido, etDireccion, etTelefono, etCorreo_electronico, etContrasena;
    Button btnCreate, btnCancelar;
    TextView tvIniciaSession; // Nuevo botón de texto
    ImageView imageView;
    RequestQueue requestQueue;

    private static final String URL1 = "http://192.168.1.46/proyecto/save.php";
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

        requestQueue = Volley.newRequestQueue(this);

        initUI();

        btnCreate.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        tvIniciaSession.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private void initUI() {
        // EditText
        etName = findViewById(R.id.etName);
        etApellido = findViewById(R.id.etApellido);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo_electronico = findViewById(R.id.etCorreo_electronico);
        etContrasena = findViewById(R.id.etContrasena);

        // Buttons
        btnCreate = findViewById(R.id.btnCreate);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Nuevo botón de texto
        tvIniciaSession = findViewById(R.id.tvIniciaSession);

        imageView = findViewById(R.id.imageView);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCreate) {
            String nombre = etName.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String correo_electronico = etCorreo_electronico.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            createUser(nombre, apellido, direccion, telefono, correo_electronico, contrasena);

        } else if (id == R.id.btnCancelar) {
            // Limpiar los campos
            limpiarCampos();
            // Redirigir al usuario a la ventana de inicio de sesión
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Operación cancelada", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.tvIniciaSession) {
            // Redirigir al usuario a LoginActivity.java
            onIniciaSesionClick(v);
        }else if (v.getId() == R.id.imageView) { // Cambiar visibilidad de la contraseña
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
    // Método para manejar el clic en el botón "Inicia sesión"
    public void onIniciaSesionClick(View view) {
        // Redirigir al usuario a LoginActivity.java
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void createUser(final String nombre, final String apellido, final String direccion, final String telefono, final String correo_electronico, final String contrasena) {
        // Verificar si algún campo está vacío
        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || correo_electronico.isEmpty() || contrasena.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            Toast.makeText(MainActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        // Verificar si la contraseña tiene al menos 8 caracteres
        if (contrasena.length() < 8) {
            // Mostrar un mensaje de error si la contraseña es demasiado corta
            Toast.makeText(MainActivity.this, "La contraseña debe contener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Expresión regular para validar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Verificar si el correo electrónico tiene un formato válido
        if (!correo_electronico.matches(emailPattern)) {
            // Mostrar un mensaje de error si el formato del correo electrónico no es válido
            Toast.makeText(MainActivity.this, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el número de teléfono tiene entre 9 y 10 dígitos
        if (telefono.length() > 9 || telefono.length() < 9) {
            // Mostrar un mensaje de error si el número de teléfono no tiene la longitud adecuada
            Toast.makeText(MainActivity.this, "El número de teléfono debe tener entre 9 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la solicitud de cadena de solicitud (StringRequest) para enviar los datos al servidor
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Imprimir la respuesta recibida del servidor
                        Log.d("ServerResponse", "Response: " + response);

                        // Manejar la respuesta del servidor
                        if (response.trim().equals("existe")) {
                            // Los datos ya están en uso, mostrar un mensaje de error
                            Toast.makeText(MainActivity.this, "Correo electrónico o teléfono ya están en uso", Toast.LENGTH_SHORT).show();
                        } else {
                            // Los datos no están en uso, continuar con la creación del usuario
                            Toast.makeText(MainActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                            // Redirigir al usuario a LoginActivity.java
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            limpiarCampos();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Manejar errores de la solicitud
                        Toast.makeText(MainActivity.this, "Error al conectar al servidor", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Enviar los datos del usuario al script PHP
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido", apellido);
                params.put("direccion", direccion);
                params.put("telefono", telefono);
                params.put("correo_electronico", correo_electronico);
                params.put("contrasena", contrasena);

                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }
    private void limpiarCampos() {
        etName.setText("");
        etApellido.setText("");
        etDireccion.setText("");
        etTelefono.setText("");
        etCorreo_electronico.setText("");
        etContrasena.setText("");
    }
}
