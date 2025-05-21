<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el id_usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener los detalles del usuario
    $query = "SELECT nombre, apellido, direccion, telefono, correo_electronico, contrasena FROM Usuarios WHERE id_usuario = '$id_usuario'";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Usuario encontrado, obtener detalles del usuario
        $usuario = $result->fetch_assoc();
        echo json_encode($usuario);
    } else {
        // No se encontraron detalles del usuario
        echo "No se encontraron detalles del usuario.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
