<?php session_start(); ?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="stylesheet" href="assets/css/style.css">

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="assets/js/logged_user.js"></script>

  <title>Vote System</title>
</head>
<body>

  <div id="current_user_info" class="hide">
    <p>Logged in user: <?php echo $_SESSION['username'] ?></p>
    <a class="logout">Logout</a>
  </div>

  <div id="main_container">
    <div class="container-inner">
      <header class="header">
        <div class="header-inner">
          <a href="index.php">
            <figure class="logo">
              <img src="assets/images/logo.png" alt="logo" />
            </figure>
          </a>
          
          <h1>Vote System</h1>
    
          <a href="index.php">
            <figure class="logo">
              <img src="assets/images/logo.png" alt="logo" />
            </figure>
          </a>
        </div>

        <p class="welcome-text">Welcome your visitation!</p>
      </header>

      <main>