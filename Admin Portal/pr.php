<?php
session_start();
echo "Welcome  ".$_SESSION['user'];
?>
<link href="//db.onlinewebfonts.com/c/a4e256ed67403c6ad5d43937ed48a77b?family=Core+Sans+N+W01+35+Light" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="pr.css" type="text/css">
<div class="body-content">
  <div class="module">
    <h1>patient registration</h1>
    <form class="form" action="pr1.php" method="post" enctype="multipart/form-data" autocomplete="off">
      <div class="alert alert-error"></div>
      <input type="text" placeholder="Name" name="name" required />
      <input type="text" placeholder="Father name" name="fathers_name" required />
      <input type="text" placeholder="Age" name="age"  />
        

        <select id="sex" name="sex">
          <option value="male">Male</option>
          <option value="Female">Female</option>
          <option value="others">others</option>
        </select>

      <input type="submit" value="Register" name="register" class="btn btn-block btn-primary" onclick="a();"/>
      <a href="logout.php">Logout</a>
        
      
      
      
      
      
      
   
   
   
   
    </form>
  </div>
</div>
