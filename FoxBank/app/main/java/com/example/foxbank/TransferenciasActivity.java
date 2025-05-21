    package com.example.foxbank;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;

    public class TransferenciasActivity extends AppCompatActivity {

        EditText etNumeroTarjetaDestino, etMonto, etDescripcion;
        Button btnTransaccion, btnCancel;
        ImageButton ibtnDevolver;
        RequestQueue requestQueue;
        String idUsuario;
        private static final String URL_TRANSFERENCIA = "http://192.168.1.46/proyecto/transferencias.php";
        private static final String URL_OBTENER_NUMERO_TARJETA_USUARIO = "http://192.168.1.46/proyecto/obtener_numero_tarjeta_usuario.php";
        private static final String URL_OBTENER_SALDO_USUARIO = "http://192.168.1.46/proyecto/obtener_saldo_usuario.php";
        private static final String URL_VERIFICAR_TARJETA = "http://192.168.1.46/proyecto/verificar_tarjeta.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.transaction);

            requestQueue = Volley.newRequestQueue(this);

            initUI();


            idUsuario = getIntent().getStringExtra("id_usuario");

            btnTransaccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los valores de los campos de texto
                    String numeroTarjetaDestino = etNumeroTarjetaDestino.getText().toString().trim();
                    String monto = etMonto.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();

                    // Verificar si algún campo está vacío
                    if (numeroTarjetaDestino.isEmpty() || monto.isEmpty() || descripcion.isEmpty()) {
                        // Mostrar mensaje de error
                        Toast.makeText(TransferenciasActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    } else if (numeroTarjetaDestino.length() != 16) {
                        // Verificar si el número de tarjeta tiene 16 dígitos
                        Toast.makeText(TransferenciasActivity.this, "El número de tarjeta debe tener 16 dígitos", Toast.LENGTH_SHORT).show();
                    } else {
                        verificarTarjeta(numeroTarjetaDestino, monto, descripcion);
                        limpiarCamposYVolver();
                   }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Limpiar los campos de entrada y volver a InicioActivity
                    limpiarCamposYVolver();
                    Intent intent = new Intent(TransferenciasActivity.this, InicioActivity.class);
                    intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
                    startActivity(intent);
                }
            });

            ibtnDevolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TransferenciasActivity.this, InicioActivity.class);
                    intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario")); // Pasar el ID de usuario al intent
                    startActivity(intent);
                }
            });

        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent intent = new Intent(TransferenciasActivity.this, InicioActivity.class);
            intent.putExtra("id_usuario", getIntent().getStringExtra("id_usuario"));
            startActivity(intent);
        }

        private void initUI() {
            // EditTexts
            etNumeroTarjetaDestino = findViewById(R.id.etNumeroTarjetaDestino);
            etMonto = findViewById(R.id.etMonto);
            etDescripcion = findViewById(R.id.etDescripcion);

            // Buttons
            btnTransaccion = findViewById(R.id.btnTransaccion);
            btnCancel = findViewById(R.id.btnCancelar);
            ibtnDevolver = findViewById(R.id.ibtnDevolver);
        }

        private void verificarTarjeta(final String numeroTarjetaDestino, final String monto, final String descripcion) {
            // Primero, obtener el número de tarjeta del usuario actual
            String urlNumeroTarjetaUsuario = URL_OBTENER_NUMERO_TARJETA_USUARIO + "?id_usuario=" + idUsuario;
            StringRequest numeroTarjetaRequest = new StringRequest(
                    Request.Method.GET,
                    urlNumeroTarjetaUsuario,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals(numeroTarjetaDestino)) {
                                // El usuario intenta transferirse a sí mismo
                                Toast.makeText(TransferenciasActivity.this, "No puedes transferirte a ti mismo", Toast.LENGTH_SHORT).show();
                            } else {
                                // Continuar con la verificación normal
                                String url = URL_VERIFICAR_TARJETA + "?numero_tarjeta_destino=" + numeroTarjetaDestino;
                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.GET,
                                        url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("success")) {
                                                    // La tarjeta existe, ahora verificar el saldo del usuario
                                                    verificarSaldoUsuario(numeroTarjetaDestino, monto, descripcion);

                                                } else {
                                                    // La tarjeta no existe, mostrar mensaje de error
                                                    Toast.makeText(TransferenciasActivity.this, "Transferencia cancelada. El número de tarjeta no se ha encontrado", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(TransferenciasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                requestQueue.add(stringRequest);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TransferenciasActivity.this, "Error al obtener el número de tarjeta del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Agregar la solicitud a la cola de solicitudes
            requestQueue.add(numeroTarjetaRequest);
        }

        private void verificarSaldoUsuario(final String numeroTarjetaDestino, final String monto, final String descripcion) {
            // Primero, obtener el saldo del usuario
            String urlSaldo = URL_OBTENER_SALDO_USUARIO + "?id_usuario=" + idUsuario;
            StringRequest saldoRequest = new StringRequest(
                    Request.Method.GET,
                    urlSaldo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Verificar si se pudo obtener el saldo del usuario
                            try {
                                double saldoUsuario = Double.parseDouble(response);
                                double montoTransferencia = Double.parseDouble(monto);
                                if (saldoUsuario >= montoTransferencia) {
                                    // El saldo es suficiente, realizar la transferencia
                                    transferir(numeroTarjetaDestino, monto, descripcion, idUsuario);
                                } else {
                                    // El saldo no es suficiente, mostrar mensaje de error
                                    Toast.makeText(TransferenciasActivity.this, "Saldo insuficiente para realizar la transferencia", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                // Manejar errores de conversión de datos
                                Toast.makeText(TransferenciasActivity.this, "Error al obtener el saldo del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar errores de conexión
                            Toast.makeText(TransferenciasActivity.this, "Error de conexión al obtener el saldo del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Agregar la solicitud a la cola de solicitudes
            requestQueue.add(saldoRequest);
        }

        private void transferir(final String numeroTarjetaDestino, final String monto, final String descripcion, final String idUsuario) {
            String url = URL_TRANSFERENCIA + "?numero_tarjeta_destino=" + numeroTarjetaDestino +
                    "&monto=" + monto +
                    "&descripcion=" + descripcion +
                    "&id_usuario=" + idUsuario;

            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(TransferenciasActivity.this, response, Toast.LENGTH_SHORT).show();
                            // Si la transferencia es exitosa, solo finaliza la actividad
                            if (response.equals("success")) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TransferenciasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            requestQueue.add(stringRequest);
        }

        private void limpiarCamposYVolver() {
            // Limpiar los campos de entrada
            etNumeroTarjetaDestino.setText("");
            etMonto.setText("");
            etDescripcion.setText("");
        }


    }
