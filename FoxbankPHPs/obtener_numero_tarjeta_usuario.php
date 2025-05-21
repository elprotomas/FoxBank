<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el ID de usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener el número de tarjeta del usuario
    $query = "SELECT numero_tarjeta FROM Tarjetas WHERE id_usuario = '$id_usuario'";
    $result = $mysql->query($query);

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $numero_tarjeta = $row['numero_tarjeta'];
        echo $numero_tarjeta;
    } else {
        // El usuario no existe o no se encontró el número de tarjeta asociado al usuario
        echo "Error: No se pudo obtener el número de tarjeta del usuario";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
