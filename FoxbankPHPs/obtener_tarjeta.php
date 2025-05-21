<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el id_usuario desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];

    // Consultar la base de datos para obtener los detalles de la tarjeta del usuario
    $query = "SELECT 
                tipo_tarjeta, 
                numero_tarjeta, 
                fecha_vencimiento, 
                CVV,
                (SELECT nombre FROM Usuarios WHERE id_usuario = '$id_usuario') as nombre,
                (SELECT telefono FROM Usuarios WHERE id_usuario = '$id_usuario') as telefono,
                (SELECT contrasena FROM Tarjetas WHERE id_usuario = '$id_usuario') as contrasena
            FROM Tarjetas 
            WHERE id_usuario = '$id_usuario'";
    $result = $mysql->query($query);

    if ($result->num_rows > 0) {
        // Tarjeta encontrada, obtener detalles de la tarjeta
        $tarjeta = $result->fetch_assoc();
        echo json_encode($tarjeta);
    } else {
        // No se encontraron detalles de la tarjeta
        echo "No se encontraron detalles de la tarjeta para este usuario.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>

