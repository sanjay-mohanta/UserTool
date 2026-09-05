<html>
<head>
<title>Bid Floor</title>
</head>
<body>

<h1 id="h1BidPrice">Waiting for bid...</h1>

<script>
    var socket = new WebSocket("ws://localhost:8080/Test/bidupdates");

    socket.onmessage = function(event) {
        document.getElementById("h1BidPrice").innerHTML = 
            "Current Highest Bid: ₹ " + event.data;
    };

    socket.onopen = function() {
        console.log("Connected to bid updates");
    };

    socket.onerror = function(error) {
        console.log("WebSocket Error: ", error);
    };
</script>

</body>
</html>
