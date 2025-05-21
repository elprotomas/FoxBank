<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener los datos de la solicitud GET
    $numero_tarjeta_destino = $_GET['numero_tarjeta_destino'];
    $monto = $_GET['monto'];
    $descripcion = $_GET['descripcion'];
    $id_usuario = $_GET['id_usuario'];

    // Obtener el número de tarjeta de origen del usuario
    $query_tarjeta_origen = "SELECT numero_tarjeta FROM Tarjetas WHERE id_usuario = '$id_usuario'";
    $result_tarjeta_origen = $mysql->query($query_tarjeta_origen);

    if ($result_tarjeta_origen->num_rows > 0) {
        $tarjeta_origen = $result_tarjeta_origen->fetch_assoc();
        $numero_tarjeta_origen = $tarjeta_origen['numero_tarjeta'];

        // Insertar la transacción en la base de datos
        $query_insert_transaccion = "INSERT INTO Transacciones (numero_tarjeta_origen, numero_tarjeta_destino, tipo_transaccion, monto, fecha, descripcion) VALUES ('$numero_tarjeta_origen', '$numero_tarjeta_destino', 'Transferencia', '$monto', CURDATE(), '$descripcion')";
        if ($mysql->query($query_insert_transaccion) === TRUE) {
            echo "Transferencia realizada correctamente";
        } else {
            echo "Error al realizar la transferencia: " . $mysql->error;
        }
    } else {
        echo "Error: No se encontró la tarjeta de origen del usuario.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
