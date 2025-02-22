<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

if (!isset($_SESSION['loggedin'])) {
    header("Location: login.php");
    exit();
}

$upload_dir = "uploads/";
if (!file_exists($upload_dir)) {
    mkdir($upload_dir, 0777, true); // Dangerous permissions
}

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES["file"])) {
    $filename = $_FILES["file"]["name"];
    $target_file = $upload_dir . basename($filename);

    // **VULNERABILITY: No file type restriction**
    if (move_uploaded_file($_FILES["file"]["tmp_name"], $upload_dir . $_FILES["file"]["name"])) {
        echo "<p class='text-success text-center'>File uploaded successfully: <a href='$target_file'>$filename</a></p>";
    } else {
        echo "<p class='text-danger text-center'>Error uploading file.</p>";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Upload</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
body, h1, h2, h3, h4, h5, h6, p, label, a, input, button {
    color: #FFFFFF !important;  /* Pure bright white */
    text-shadow: 0px 0px 5px rgba(255, 255, 255, 0.7); /* Glow effect for visibility */
}
        body {
            background-color: #121212;
            color: white;
            font-family: Arial, sans-serif;
        }
        .navbar {
            background: #1E1E1E;
            padding: 10px;
        }
        .container {
            max-width: 500px;
            margin-top: 50px;
        }
        .card {
            background: #1E1E1E;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }
        .btn-warning {
            background-color: #ffcc00;
            border: none;
            color: black;
        }
        .btn-warning:hover {
            background-color: #ffd633;
        }
    </style>
</head>
<body>

<nav class="navbar">
    <a class="navbar-brand text-light" href="bitcoin_dashboard.php">Bitcoin Exchange</a>
    <a class="nav-link text-light" href="upload.php">Upload</a>
    <a class="nav-link text-light" href="logout.php">Logout</a>
</nav>

<div class="container">
    <div class="card">
        <h2 class="text-center">Upload a File</h2>
        <form action="upload.php" method="POST" enctype="multipart/form-data">
            <div class="mb-3">
                <input type="file" class="form-control" name="file" required>
            </div>
            <button type="submit" class="btn btn-warning w-100">Upload</button>
        </form>
    </div>
</div>

</body>
</html>
