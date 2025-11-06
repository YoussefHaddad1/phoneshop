<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>📱 PhoneShop API</title>
  <link rel="icon" href="https://cdn-icons-png.flaticon.com/512/992/992700.png" type="image/png">
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Poppins', sans-serif; }
body {
  background: linear-gradient(135deg, #0077ff, #00c6ff);
  color: white;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  text-align: center;
}
h1 {
  font-size: 3em;
  margin-bottom: 0.3em;
  text-shadow: 0 3px 8px rgba(0, 0, 0, 0.3);
}
p {
  font-size: 1.2em;
  opacity: 0.9;
  margin-bottom: 1.5em;
}
    .links {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1em;
}
    .link {
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.8em 1.4em;
  border-radius: 12px;
  color: white;
  text-decoration: none;
  transition: 0.3s;
  font-weight: 500;
}
    .link:hover {
  background: white;
  color: #0077ff;
  transform: scale(1.05);
}
footer {
  position: absolute;
  bottom: 20px;
  font-size: 0.9em;
  opacity: 0.7;
}
  </style>
</head>
<body>
  <h1>📱 PhoneShop API</h1>
<p>Welcome to your backend application — explore available endpoints:</p>

  <div class="links">
    <a href="/api/brands" class="link">/api/brands</a>
    <a href="/api/phones" class="link">/api/phones</a>
    <a href="/api/customers" class="link">/api/customers</a>
    <a href="/api/orders" class="link">/api/orders</a>
  </div>

<footer>Made by Yassine — Powered by Spring Boot</footer>
</body>
</html>
