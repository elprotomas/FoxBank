package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
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

public class InfoUserActivity extends AppCompatActivity implements View.OnClickListener{

    TextView etName, etApellido, etDireccion, etTelefono, etCorreo_electronico, etContrasena;

    ImageButton ibtnDevolver;
    ImageView imageView;
    RequestQueue requestQueue;
    private boolean isPasswordVisible = false;
    private static final String URL_GET_USER_INFO = "http://192.168.1.46/proyecto/obtener_usuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_user);

        requestQueue = Volley.newRequestQueue(this);

        initUI();

        // Obtener el id_usuario de la actividad anterior
        String idUsuario = getIntent().getStringExtra("id_usuario");
        if (idUsuario != null) {
            obtenerInformacionUsuario(idUsuario);
        } else {
            Toast.makeText(this, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
        }

        ibtnDevolver.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }
    private void initUI() {
        etName = findViewById(R.id.etName);
        etApellido = findViewById(R.id.etApellido);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo_electronico = findViewById(R.id.etCorreo_electronico);
        etContrasena = findViewById(R.id.etContrasena);
        ibtnDevolver = findViewById(R.id.ibtnDevolver);
        imageView = findViewById(R.id.imageView);
    }

        @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ibtnDevolver){
            Intent intent = new Intent(InfoUserActivity.this, InicioActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
            startActivity(intent);
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

    // Devolver a la actividad anterior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoUserActivity.this, InicioActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }
    private void obtenerInformacionUsuario(String idUsuario) { // Cambiado a recibir una cadena
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_USER_INFO + "?id_usuario=" + idUsuario, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombre = response.getString("nombre");
                            String apellido = response.getString("apellido");
                            String direccion = response.getString("direccion");
                            String telefono = response.getString("telefono");
                            String correo = response.getString("correo_electronico");
                            String contrasena = response.getString("contrasena");


                            etName.setText(nombre);
                            etApellido.setText(apellido);
                            etDireccion.setText(direccion);
                            etTelefono.setText(telefono);
                            etCorreo_electronico.setText(correo);
                            etContrasena.setText(contrasena);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfoUserActivity.this, "Error al obtener la información del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(request);
    }


}
