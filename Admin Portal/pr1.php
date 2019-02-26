
<?php
    
    // 
    if(isset($_POST["register"])){
        $servername = "localhost";
        $username = "root";
        $password = ""; 
        $dbname = "bed_managementcreate";

      
        // Connection    
    $dbconnect = mysqli_connect("localhost","root","","bed_managementcreate") or die(mysqli_error($dbconnect));
    // Check Connection 
         if ($dbconnect->connect_error) {
               die("Connection failed: " . $dbconnect->connect_error);
            } 
        


            $sql = "INSERT INTO patients_info (patient_name, Fathers_name, age , Gender) VALUES('".$_POST["name"]."','".$_POST["fathers_name"]."','".$_POST["age"]."','".$_POST["sex"]."')";

            if (mysqli_query($dbconnect, $sql)) {
                $query = "SELECT Total_beds_vacant FROM hospitals, admin WHERE hospitals.hospital_id = admin.hospital_id";
                $result = mysqli_query($dbconnect, $query) or die(mysqli_error($dbconnect));
                if($result) {
                   while($row = mysqli_fetch_assoc($result)) {
                     $available_beds = $row['Total_beds_vacant'];
                   }
                } else {
                    echo 'nah';
                }

                $available_beds -=1;
                $update_query = "UPDATE hospitals SET Total_beds_vacant = '$available_beds' WHERE hospital_id='1'";
                $res = mysqli_query($dbconnect, $update_query) or die(mysqli_error($dbconnect));
                if($res) {
                   while($row = mysqli_fetch_assoc($result)) {
                     $available_beds = $row['Total_beds_vacant'];
                   }
                   echo $available_beds;
                }
            
            } 
            else 
            {
               echo "Error: " . $sql . "" . mysqli_error($dbconnect);
            }
            $dbconnect->close();
        }
?>   