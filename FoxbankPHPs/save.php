<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST'){
        require_once("db.php");

        $nombre = $_POST['nombre'];
        $apellido = $_POST['apellido'];
        $direccion = $_POST['direccion'];
        $telefono = $_POST['telefono'];
        $correo_electronico = $_POST['correo_electronico'];
        $contrasena = $_POST['contrasena'];

        // Verificar si el correo electrónico o el teléfono ya existen en la base de datos
        $checkQuery = "SELECT * FROM usuarios WHERE correo_electronico = '$correo_electronico' OR telefono = '$telefono'";
        $checkResult = $mysql->query($checkQuery);

        if ($checkResult->num_rows > 0) {
            // Los datos ya existen, enviar un mensaje de error al cliente
            echo "existe";
        } else {
            // Los datos no existen, crear el usuario
            $query = "INSERT INTO usuarios (nombre, apellido, direccion, telefono, correo_electronico, contrasena) VALUES ('$nombre','$apellido','$direccion','$telefono','$correo_electronico','$contrasena')";
            $result = $mysql->query($query);

            if($result === TRUE){
                // Usuario creado exitosamente
                echo "success";
            } else {
                // Error al crear el usuario
                echo "Error";
            }
        }

        $mysql->close();
    }

