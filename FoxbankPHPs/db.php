<?php
    $mysql = new mysqli(
        "localhost",
        "root",
        "xxcarajo",
        "proyecto"
    );

    if ($mysql->connect_error){
        die("Faild to connect ". $mysql ->connect_error);
    }