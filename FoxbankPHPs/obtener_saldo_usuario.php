<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el ID del usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener el saldo del usuario
    $query = "SELECT saldo FROM Cuentas WHERE id_usuario = $id_usuario";
    $result = $mysql->query($query);

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $saldo = $row['saldo'];
        echo $saldo;
    } else {
        // El usuario no existe o no se encontró el saldo asociado al ID de usuario
        echo "Error: No se pudo obtener el saldo del usuario";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
