<?php
session_start();
echo "Welcome  ".$_SESSION['user'];
?>
<link href="//db.onlinewebfonts.com/c/a4e256ed67403c6ad5d43937ed48a77b?family=Core+Sans+N+W01+35+Light" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="pr.css" type="text/css">
<div class="body-content">
  <div class="module">
    <h1>Select Action</h1>
    <form class="form" action="pr.php" method="post" enctype="multipart/form-data" autocomplete="off">

      <input type="submit" value="Patient Register" name="pr" class="btn btn-block btn-primary" onclick="a();"/>
    </form>
    <form class="form" action="discharge.php" method="post" enctype="multipart/form-data" autocomplete="off">
      <input type="submit" value="Patient Discharge" name="pd" class="btn btn-block btn-primary" onclick="a();"/>
      </form>
      <a href="logout.php">Logout</a>
  </div>
</div>
