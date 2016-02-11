<html>
<head>
    <title>Welcome - SCX Beer Suggestbot</title>
    <style type="text/css">

        body {
            margin: 0;
            font-family: sans-serif;
            color: #333;

            background-image: url('/images/background-600.png');
            background-repeat: repeat-both;
            background-attachment: fixed;
        }

        #main {
            position: fixed;
            top: 50%;
            left: 50%;

            margin-left: -240px; /* half of width + padding */
            margin-top: -165px; /* half of height + padding */

            width: 400px;
            height: 250px;

            background: white;
            box-shadow: 0px 3px 30px rgba(0, 0, 0, 0.15);

            padding: 40px;
        }

            #main > img {
                height: 256px;
                margin: 0 auto;
                margin-top: -150px;
                display: block;
            }

            #main > a {
                display: block;
                padding: 15px 5px;
                margin: 15px 5px;
                border: 1px solid rgba(25, 105, 30, 1.0);
                background: rgba(30, 115, 37, 1.0);
                color: white;
                text-transform: uppercase;
                text-align: center;
                text-decoration: none;
                transition: background-color 0.5s ease;
                box-shadow: 0px 3px 30px rgba(0, 0, 0, 0.25);
            }

            #main > a:hover {
                background: rgba(40, 133, 43, 1.0);
            }

            #main > h1 {
                text-align: center;
            }

            #main > span {
                font-size: 80%;
                font-style: italic;
                display: block;
                text-align: center;
            }

    </style>
</head>
<body>

    <div id="main">
        <img src="/images/beer-200.png" />
        <h1>${greeting}</h1>
        <a href="/suggest">It's beer time!</a>
        <span>${message}</span>
    </div>

</body>
</html>
