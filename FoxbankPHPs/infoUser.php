<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el id_usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener los detalles del usuario
    $query = "SELECT 
                nombre, 
                correo_electronico, 
                (SELECT saldo FROM Cuentas WHERE id_usuario = '$id_usuario') AS saldo
            FROM Usuarios
            WHERE id_usuario = '$id_usuario'";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Usuario encontrado, obtener detalles
        $usuario_info = $result->fetch_assoc();

        echo json_encode($usuario_info);
    } else {
        // No se encontraron detalles para este usuario
        echo "No se encontraron detalles para este usuario.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
