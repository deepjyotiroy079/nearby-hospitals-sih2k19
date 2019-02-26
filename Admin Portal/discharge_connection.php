
<?php
    
    // 
    if(isset($_POST["Discharge"])){
        $servername = "localhost";
        $username = "root";
        $password = ""; 
        $dbname = "bed_managementcreate";
      
        // Connection    
    $dbconnect = mysqli_connect("localhost","root","",  "bed_managementcreate") or die(mysqli_error($dbconnect));
    // Check Connection 
         if ($dbconnect->connect_error) {
               die("Connection failed: " . $dbconnect->connect_error);
            } 
            $p_id = $_POST['patient_id'];

            $sql = "Delete FROM patients_info WHERE patient_id = '$p_id'";

            if (mysqli_query($dbconnect, $sql)) {
                $query = "SELECT Total_beds_vacant , Total_beds, Total_beds_allocated FROM hospitals WHERE hospital_id = '1'";
                $result = mysqli_query($dbconnect, $query) or die(mysqli_error($dbconnect));
                if($result) {
                   while($row = mysqli_fetch_assoc($result)) {
                     $available_beds = $row['Total_beds_vacant'];
                     $total_beds = $row['Total_beds'];
                     $allocated_beds = $row['Total_beds_allocated'];
                   }
                } else {
                    echo 'nah';
                }

                $available_beds +=1;
                $allocated_beds -=1;
                $update_query = "UPDATE hospitals SET Total_beds_vacant = '$available_beds', Total_beds_allocated = '$allocated_beds' WHERE hospital_id='1'";
                $res = mysqli_query($dbconnect, $update_query) or die(mysqli_error($dbconnect));
                if($res) {
                   while($row = mysqli_fetch_assoc($result)) {
                     $available_beds = $row['Total_beds_vacant'];
                     $allocated_beds = $row['Total_beds_allocated'];
                   }
                   echo $available_beds;
                   echo $allocated_beds;

                }
            } 
            else 
            {
               echo "Error: " . $sql . "" . mysqli_error($dbconnect);
            }
            $dbconnect->close();
        }
?>   