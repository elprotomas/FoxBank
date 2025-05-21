<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el id_usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener las transferencias relacionadas con el usuario
    $query = "SELECT 
                tipo_transaccion,
                monto,
                fecha,
                descripcion,
                CASE
                    WHEN numero_tarjeta_origen IN (
                        SELECT numero_tarjeta 
                        FROM Tarjetas 
                        WHERE id_usuario = '$id_usuario'
                    ) THEN 'Saliente'
                    ELSE 'Entrante'
                END AS tipo_transferencia
            FROM Transacciones
            WHERE numero_tarjeta_origen IN (
                SELECT numero_tarjeta 
                FROM Tarjetas 
                WHERE id_usuario = '$id_usuario'
            ) OR numero_tarjeta_destino IN (
                SELECT numero_tarjeta 
                FROM Tarjetas 
                WHERE id_usuario = '$id_usuario'
            )";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Transferencias encontradas, obtener detalles
        $transferencias = array();
        while ($row = $result->fetch_assoc()) {
            // Añadir símbolo al monto dependiendo del tipo de transferencia
            if ($row['tipo_transferencia'] == 'Saliente') {
                $row['monto'] = '-' . $row['monto'];
            } else {
                $row['monto'] = '+' . $row['monto'];
            }
            $transferencias[] = $row;
        }
        echo json_encode($transferencias);
    } else {
        // No se encontraron transferencias para este usuario
        echo "No se encontraron transferencias para este usuario.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
