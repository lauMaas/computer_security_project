<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

$conn = new mysqli("localhost", "webuser", "webpass", "vulnerable_db");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Store credentials in cookies for CSRF attack
if (isset($_POST["remember"])) {
    setcookie("username", $_POST["username"], time() + (86400 * 30), "/"); // 30 days
    setcookie("password", $_POST["password"], time() + (86400 * 30), "/"); // Plaintext storage
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $password = md5($_POST["password"]); // Weak hashing

    // **Vulnerable to SQL Injection**
    $sql = "SELECT * FROM users WHERE username=\"" . $_POST["username"] . "\" AND password=\"" . md5($_POST["password"]) . "\"";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $_SESSION['loggedin'] = true;
        $_SESSION['username'] = $username;
        session_write_close();
        header("Location: bitcoin_dashboard.php");
        exit();
    } else {
        echo "Invalid login for user: " . $_POST['username'];
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bitcoin Exchange - Login</title>
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
        .login-box {
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
    <div class="login-box">
        <h2 class="text-center">Bitcoin Exchange Login</h2>
        <form method="POST">
            <div class="mb-3">
                <label>Username</label>
                <input type="text" class="form-control" name="username" value="<?php echo isset($_COOKIE['username']) ? $_COOKIE['username'] : ''; ?>" required>
            </div>
            <div class="mb-3">
                <label>Password</label>
                <input type="password" class="form-control" name="password" value="<?php echo isset($_COOKIE['password']) ? $_COOKIE['password'] : ''; ?>" required>
            </div>
            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" name="remember">
                <label class="form-check-label">Remember Me</label>
            </div>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <p class="text-center mt-3"><a href="register.php" class="text-light">Create an Account</a></p>
    </div>
</div>

</body>
</html>
