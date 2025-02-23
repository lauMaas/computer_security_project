<?php
session_start();
session_destroy(); // Ends session
header("Location: login.php");
exit();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logging Out...</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #121212;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
            padding-top: 100px;
        }
        .logout-container {
            background: #1E1E1E;
            padding: 20px;
            border-radius: 8px;
            display: inline-block;
            box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }
    </style>
</head>
<body>

<div class="logout-container">
    <h2>You have been logged out.</h2>
    <p>Redirecting to login page...</p>
</div>

<script>
    setTimeout(() => {
        window.location.href = "login.php";
    }, 2000); // Auto-redirect after 2 seconds
</script>

</body>
</html>
