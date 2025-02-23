<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

$conn = new mysqli("localhost", "webuser", "webpass", "vulnerable_db");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $password = md5($_POST["password"]); // Hash before storing

    // Save credentials in cookies (for CSRF exploitation)
    setcookie("username", $username, time() + (86400 * 30), "/");
    setcookie("password", $_POST["password"], time() + (86400 * 30), "/"); // Plaintext storage

    // **XSS vulnerability (No sanitization)**
    echo "<script>alert('Account created for: " . $_POST["username"] . "');</script>";

    // **Generate an ID for the user**
    $id = rand(1000, 9999); // Random 4-digit ID

    // **SQL Injection vulnerability**
    $sql = "INSERT INTO users (id, username, password) VALUES (\"$id\", \"" . $_POST["username"] . "\", \"$password\")";

    if ($conn->query($sql) === TRUE) {
        echo "<p class='text-success text-center'>Account successfully created! <a href='login.php'>Login here</a></p>";
    } else {
        echo "<p class='text-danger text-center'>Error: " . $conn->error . "</p>";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bitcoin Exchange - Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #121212; 
            color: white;
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 400px;
            margin-top: 100px;
        }
        .register-box {
            background: #1E1E1E;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }
        .btn-primary {
            background-color: #ffcc00;
            border: none;
            color: black;
        }
        .btn-primary:hover {
            background-color: #ffd633;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="register-box">
        <h2 class="text-center">Create an Account</h2>
        <form method="POST">
            <div class="mb-3">
                <label>Username</label>
                <input type="text" class="form-control" name="username" value="<?php echo isset($_COOKIE['username']) ? $_COOKIE['username'] : ''; ?>" required>
            </div>
            <div class="mb-3">
                <label>Password</label>
                <input type="password" class="form-control" name="password" value="<?php echo isset($_COOKIE['password']) ? $_COOKIE['password'] : ''; ?>" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Sign Up</button>
        </form>
        <p class="text-center mt-3"><a href="login.php" class="text-light">Already have an account? Login here</a></p>
    </div>
</div>

</body>
</html>
