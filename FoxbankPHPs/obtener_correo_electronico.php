<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el correo electrónico desde la solicitud GET
    $correo_electronico = $_GET['correo_electronico'];

    // Consultar la base de datos para verificar si el correo existe
    $query = "SELECT id_usuario, contrasena FROM Usuarios WHERE correo_electronico = '$correo_electronico'";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Encontró la contraseña, devuelve el id_usuario
        $row = $result->fetch_assoc();
        $id_usuario = $row['id_usuario'];
        echo "success|" . $id_usuario;
    } else {
        // No se encontró la contraseña para este correo electrónico
        echo "No se encontró ninguna contraseña asociada a este correo electrónico.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
