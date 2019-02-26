
<?php
   session_start();
    // 
    if(isset($_POST["login"])){
        $servername = "localhost";
        $username = "root";
        $password = ""; 
        $dbname = "bed_managementcreate";
      
        // Connection    
    $dbconnect = mysqli_connect("localhost","root","",  "bed_managementcreate") or die(mysqli_error($dbconnect)); 
    $username = $_POST['username'];
    $password = $_POST['password'];

    $sql = "SELECT * FROM admin WHERE username = '$username' AND password = '$password'";
    $result = mysqli_query($dbconnect,$sql) or die(mysqli_error($dbconnect));
    $numrows = mysqli_num_rows($result);
            if($numrows == 1)
   {
      $_SESSION['user'] = $username;
      header('location:action.php');
   }
else
   {
    echo "Wrong username or password";
    header('location:admin_form.php');
   }
        }
?>   