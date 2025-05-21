<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("db.php");

    // Obtener el id_usuario, la nueva contraseña y la confirmación desde la solicitud GET
    $id_usuario = $_GET['id_usuario'];
    $nueva_contrasena = $_GET['nueva_contrasena'];
    $confirmar_contrasena = $_GET['confirmar_contrasena'];

    // Verificar que el id_usuario, la nueva contraseña y la confirmación no estén vacíos
    if (!empty($id_usuario) && !empty($nueva_contrasena) && !empty($confirmar_contrasena)) {
        // Verificar que la nueva contraseña y la confirmación coincidan
        if ($nueva_contrasena === $confirmar_contrasena) {
            // Actualizar la contraseña del usuario en la base de datos
            $query = "UPDATE Usuarios SET contrasena = '$nueva_contrasena' WHERE id_usuario = '$id_usuario'";
            $result = $mysql->query($query);

            if ($result) {
                // Contraseña actualizada exitosamente
                echo "Tu contraseña ha sido restablecida exitosamente.";
            } else {
                // Error al actualizar la contraseña
                echo "Hubo un error al restablecer la contraseña. Por favor, inténtalo de nuevo.";
            }
        } else {
            // La nueva contraseña y la confirmación no coinciden
            echo "La nueva contraseña y la confirmación no coinciden. Por favor, asegúrate de escribir la misma contraseña en ambos campos.";
        }
    } else {
        // El id_usuario, la nueva contraseña o la confirmación están vacíos
        echo "Por favor, proporciona tanto el id_usuario como la nueva contraseña y su confirmación.";
    }

    // Cerrar la conexión
    $mysql->close();
}
?>
