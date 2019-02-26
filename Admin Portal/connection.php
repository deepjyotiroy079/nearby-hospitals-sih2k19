<?php
       
    $dbconnect = mysqli_connect("localhost","root","",  "bed_managementcreate") or die(mysqli_error($dbconnect));
    if(isset($_POST['name'])&&isset($_POST['fathers_name'])&&isset($_POST['age'])&&isset($_POST['reg_number'])) {
        $name = $_POST['name'];
        $fathers_name = $_POST['fathers_name'];
        $age = $_POST['age'];
        $reg_number = $_POST['reg_number'];

        if(!empty($name)||!empty($fathers_name)||!empty($age)||!empty($reg_number)) {
            $query = "INSERT INTO patients_info (Patient_name, Fathers_name, Unique_id, Gender) VALUES('$name','$fathers_name','$age','$reg_number')";
            $result = mysqli_query($dbconnect, $query) or die(mysqli_error($result));
        }
    }
        