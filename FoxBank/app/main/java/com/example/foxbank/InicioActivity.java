package com.example.foxbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    TextView etName, etCorreoElectronico, etSaldo;
    Button btTransferencias;
    ImageButton ibtnMenu;
    RecyclerView recyclerView;
    TransferenciaAdapter transferenciaAdapter;
    ArrayList<Transferencia> transferenciasList;

    // URL del servidor donde se encuentra el archivo PHP para obtener la información del usuario
    private static final String URL_USUARIO = "http://192.168.1.46/proyecto/infoUser.php";

    // URL del servidor donde se encuentra el archivo PHP para obtener las transferencias del usuario
    private static final String URL_TRANSFERENCIAS = "http://192.168.1.46/proyecto/obtener_transferencias.php";

    // Objeto de la cola de solicitudes de Volley
    private RequestQueue requestQueue;
    private static final int REQUEST_CODE_TRANSFERENCIA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la cola de solicitudes de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Inicializar los elementos de la interfaz
        initUI();

        // Obtener el id_usuario de la actividad anterior
        String idUsuario = getIntent().getStringExtra("id_usuario");
        if (idUsuario != null) {
            obtenerInformacionUsuario(idUsuario);
        } else {
            Toast.makeText(this, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        // Inicializar los EditText, Button, TextView y RecyclerView
        etName = findViewById(R.id.etName);
        etCorreoElectronico = findViewById(R.id.etCorreo_electronico);
        etSaldo = findViewById(R.id.etSaldo);
        btTransferencias = findViewById(R.id.btTransferencias);
        ibtnMenu = findViewById(R.id.ibtnMenu); // Inicializar ibtnMenu

        recyclerView = findViewById(R.id.etInfoTranferencias);

        // Inicializar ArrayList para almacenar las transferencias
        transferenciasList = new ArrayList<>();

        // Inicializar el adaptador del RecyclerView
        transferenciaAdapter = new TransferenciaAdapter(this, transferenciasList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(transferenciaAdapter);

        // Configurar OnClickListener para el botón de transferencias
        btTransferencias.setOnClickListener(this);

        // Configurar OnClickListener para el botón de menú
        ibtnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btTransferencias) {
            Intent intent = new Intent(InicioActivity.this, TransferenciasActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
            startActivityForResult(intent, REQUEST_CODE_TRANSFERENCIA); // Iniciar TransferenciasActivity con un código de solicitud
        } else if (v.getId() == R.id.ibtnMenu) {
            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
            startActivity(intent);
        }
    }

    private void obtenerInformacionUsuario(String idUsuario) {
        // Realizar la solicitud HTTP para obtener la información del usuario
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_USUARIO + "?id_usuario=" + idUsuario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear la respuesta JSON para obtener los datos del usuario
                            JSONObject jsonObject = new JSONObject(response);
                            String nombre = jsonObject.getString("nombre");
                            String correo = jsonObject.getString("correo_electronico");
                            String saldo = jsonObject.getString("saldo");

                            // Mostrar los datos del usuario en los TextView
                            etName.setText(nombre);
                            etCorreoElectronico.setText(correo);
                            etSaldo.setText(saldo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioActivity.this, "Error al obtener información del usuario", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);

        // Realizar la solicitud HTTP para obtener las transferencias del usuario
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_TRANSFERENCIAS + "?id_usuario=" + idUsuario,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    transferenciasList.clear(); // Limpiar la lista antes de agregar nuevos datos
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String tipoTransaccion = jsonObject.getString("tipo_transaccion");
                        String monto = jsonObject.getString("monto");
                        String fecha = jsonObject.getString("fecha");
                        String descripcion = jsonObject.getString("descripcion");

                        // Crear un objeto Transferencia con el tipo de transferencia y agregarlo a la lista
                        Transferencia transferencia = new Transferencia(tipoTransaccion, monto, fecha, descripcion);
                        transferenciasList.add(transferencia);
                    }
                    // Ordenar la lista en orden descendente (del más reciente al más antiguo)
                    Collections.reverse(transferenciasList);

                    transferenciaAdapter.notifyDataSetChanged();

                    // Verificar si la lista de transferencias está vacía y no mostrar ningún mensaje
                    if (transferenciasList.isEmpty()) {
                        // Ocultar el RecyclerView si no hay transferencias
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioActivity.this, "Error al obtener transferencias", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InicioActivity.this, LoginActivity.class);
        intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TRANSFERENCIA) { // Verificar el código de solicitud correcto
            if (resultCode == RESULT_OK) { // Verificar si la transferencia fue exitosa
                // Volver a cargar los datos del usuario y las transferencias
                obtenerInformacionUsuario(getIntent().getStringExtra("id_usuario"));
            }
        }
    }
}
