-- Crear base de datos
CREATE DATABASE IF NOT EXISTS Proyecto;
USE Proyecto;

-- Crear tabla Usuarios
CREATE TABLE IF NOT EXISTS Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    direccion VARCHAR(100),
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100),
    contrasena VARCHAR(100)
);

-- Crear tabla Cuentas
CREATE TABLE IF NOT EXISTS Cuentas (
    id_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    tipo_cuenta VARCHAR(50),
    saldo DECIMAL(10, 2),
    fecha_apertura DATE,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Crear tabla Transacciones
CREATE TABLE IF NOT EXISTS Transacciones (
    id_transaccion INT AUTO_INCREMENT PRIMARY KEY,
    numero_tarjeta_origen VARCHAR(20),
    numero_tarjeta_destino VARCHAR(20),
    tipo_transaccion VARCHAR(50),
    monto DECIMAL(10, 2),
    fecha DATE,
    descripcion VARCHAR(255)
);

-- Crear tabla Tarjetas
CREATE TABLE IF NOT EXISTS Tarjetas (
    id_tarjeta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    tipo_tarjeta VARCHAR(50),
    numero_tarjeta VARCHAR(20),
    fecha_vencimiento DATE,
    CVV VARCHAR(10),
    contrasena VARCHAR(4), -- Nuevo campo para la contraseña
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Crear trigger para insertar automáticamente una cuenta con saldo inicial y una tarjeta asociada al crear un usuario
DELIMITER //
CREATE TRIGGER NuevoUsuarioTrigger
AFTER INSERT ON Usuarios
FOR EACH ROW
BEGIN
    -- Insertar una cuenta con saldo inicial de 1000 euros para el nuevo usuario
    INSERT INTO Cuentas (id_usuario, tipo_cuenta, saldo, fecha_apertura)
    VALUES (NEW.id_usuario, 'Cuenta Corriente', 1000.00, CURDATE());

    -- Obtener el ID de la cuenta recién insertada
    SET @id_cuenta = LAST_INSERT_ID();

    -- Generar número de tarjeta aleatorio (16 dígitos)
    SET @numero_tarjeta = LPAD(FLOOR(RAND() * POWER(10,15)), 16, '1');

    -- Generar CVV aleatorio (3 dígitos)
    SET @cvv = LPAD(FLOOR(RAND() * POWER(10, 3)), 3, '0');

    -- Generar contraseña de 4 dígitos
    SET @contrasena = LPAD(FLOOR(RAND() * POWER(10, 4)), 4, '0');

    -- Insertar detalles de la tarjeta asociada a la cuenta del nuevo usuario
    INSERT INTO Tarjetas (id_usuario, tipo_tarjeta, numero_tarjeta, fecha_vencimiento, CVV, contrasena)
    VALUES (NEW.id_usuario, 'Débito', @numero_tarjeta, DATE_ADD(CURDATE(), INTERVAL 10 YEAR), @cvv, @contrasena);
END;
//
DELIMITER ;

-- Trigger para realizar transferencia entre cuentas
DELIMITER //
CREATE TRIGGER Transferencia
AFTER INSERT ON Transacciones
FOR EACH ROW
BEGIN
    IF NEW.tipo_transaccion = 'Transferencia' THEN
        UPDATE Cuentas 
        SET saldo = saldo - NEW.monto 
        WHERE id_usuario = (SELECT id_usuario FROM Tarjetas WHERE numero_tarjeta = NEW.numero_tarjeta_origen);
        
        UPDATE Cuentas 
        SET saldo = saldo + NEW.monto 
        WHERE id_usuario = (SELECT id_usuario FROM Tarjetas WHERE numero_tarjeta = NEW.numero_tarjeta_destino);
    END IF;
END;
//
DELIMITER ;

-- Insertar dos usuarios
INSERT INTO Usuarios (nombre, apellido, direccion, telefono, correo_electronico, contrasena)
VALUES ('Juan', 'Pérez', 'Calle A #123', '123456789', 'juan@example.com', 'contrasena123');

INSERT INTO Usuarios (nombre, apellido, direccion, telefono, correo_electronico, contrasena)
VALUES ('María', 'Gómez', 'Avenida B #456', '987654321', 'maria@example.com', 'contrasena456');
