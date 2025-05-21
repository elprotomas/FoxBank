<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    
    require_once("db.php");

    $correo_electronico = $_GET['correo_electronico'];
    $contrasena = $_GET['contrasena'];

    // Verificar si las credenciales son válidas
    $query = "SELECT id_usuario FROM usuarios WHERE correo_electronico = '$correo_electronico' AND contrasena = '$contrasena'";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Credenciales válidas, obtener el ID del usuario
        $row = $result->fetch_assoc();
        $id_usuario = $row['id_usuario'];
        // Devolver el ID del usuario junto con "success"
        echo "success|" . $id_usuario;
    } else {
        // Credenciales inválidas, enviar un mensaje de error
        echo "error";
    }

    $mysql->close();
}
