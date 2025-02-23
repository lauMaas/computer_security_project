<?php
session_start();
if (!isset($_SESSION['loggedin'])) {
    header("Location: login.php");
    exit();
}

$conn = new mysqli("localhost", "webuser", "webpass", "vulnerable_db");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Fetch user's Bitcoin balance
$username = $_SESSION['username'];
$safe_username = str_replace(["'", '"', "<", ">", ";"], "", $username);
$result = $conn->query("SELECT balance FROM users WHERE username='$safe_username'");
$row = $result->fetch_assoc();
$current_balance = $row['balance'] ?? '1.75'; // Default balance if not set

// Fetch last transaction
$transaction_result = $conn->query("SELECT details FROM transactions WHERE username='$safe_username' ORDER BY id DESC LIMIT 1");
$transaction_row = $transaction_result->fetch_assoc();
$last_transaction = $transaction_row['details'] ?? "No transactions yet";

// **Only process transaction if parameters are provided**
if (isset($_REQUEST["amount"]) && isset($_REQUEST["recipient"])) {
    $amount = $_REQUEST["amount"];
    $recipient = $_REQUEST["recipient"];

    // Prevent invalid amounts
    if (!is_numeric($amount) || $amount <= 0) {
        echo "<p class='text-danger text-center'>Invalid amount.</p>";
        exit();
    }

    // **Vulnerable to XSS**
    echo "<script>alert('₿$amount sent to $recipient!');</script>";

    // **Check if recipient exists**
    $recipient_check = $conn->query("SELECT balance FROM users WHERE username='$recipient'");
    if ($recipient_check->num_rows == 0) {
        echo "<p class='text-danger text-center'>Recipient does not exist.</p>";
    } else {
        // **Deduct from sender**
        $conn->query("UPDATE users SET balance = balance - $amount WHERE username='$username'");

        // **Add to recipient**
        $conn->query("UPDATE users SET balance = balance + $amount WHERE username='$recipient'");

        // **Log transaction**
        $transaction_id = rand(1000, 9999);
        $conn->query("INSERT INTO transactions (id, username, details) VALUES ('$transaction_id', '$username', '-₿$amount BTC (Sent to $recipient)')");

        echo "<p class='text-success text-center'>₿$amount sent to $recipient!</p>";
        echo "<script>setTimeout(() => { window.location.href = 'bitcoin_dashboard.php'; }, 2000);</script>";
        exit();
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bitcoin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #121212;
            color: white !important;
            font-family: Arial, sans-serif;
        }
        body, h1, h2, h3, h4, h5, h6, p, label, a, input {
            color: #FFFFFF !important;
            text-shadow: 0px 0px 5px rgba(255, 255, 255, 0.7);
        }
        .navbar {
            background: #1E1E1E;
            padding: 10px;
        }
        .container {
            max-width: 600px;
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
        input {
            color: black !important;
            background-color: white !important;
            border: 1px solid #ccc;
            padding: 10px;
            font-size: 16px;
            font-weight: bold;
        }
        .card strong {
            color: #FFFFFF !important;
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
        <h2 class="text-center">Bitcoin Investment Dashboard</h2>
        <p>Welcome, <strong><?php echo $_SESSION['username']; ?></strong></p>
        <p>Your current balance: <strong>₿<?php echo $current_balance; ?> BTC</strong></p>
        <p>Last transaction: <strong><?php echo $last_transaction; ?></strong></p>

        <hr>

        <h4>Transfer Bitcoin</h4>
        <form method="POST">
            <div class="mb-3">
                <label>Recipient Username</label>
                <input type="text" class="form-control" name="recipient" required>
            </div>
            <div class="mb-3">
                <label>Amount (BTC)</label>
                <input type="number" class="form-control" name="amount" step="0.01" required>
            </div>
            <button type="submit" class="btn btn-warning w-100">Send BTC</button>
        </form>
    </div>
</div>

</body>
</html>