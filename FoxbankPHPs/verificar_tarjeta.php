<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el número de tarjeta destino desde la solicitud GET
    $numero_tarjeta_destino = $_GET['numero_tarjeta_destino'];

    // Consultar la base de datos para verificar si el número de tarjeta existe
    $query = "SELECT COUNT(*) AS count FROM Tarjetas WHERE numero_tarjeta = '$numero_tarjeta_destino'";
    $result = $mysql->query($query);

    if ($result) {
        $row = $result->fetch_assoc();
        $count = $row['count'];
        if ($count > 0) {
            // El número de tarjeta existe en la base de datos
            echo "success";
        } else {
            // El número de tarjeta no existe en la base de datos
            echo "El número de tarjeta no se ha encontrado";
        }
    } else {
        // Error en la consulta
        echo "Error en la consulta";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>