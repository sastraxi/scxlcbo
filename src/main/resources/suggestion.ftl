<html>
<head>
    <title>Suggestion - SCX Beer Suggestbot</title>
    <style type="text/css">

        body {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
            color: #333;



            background-image: url('/images/background-600.png');
            background-repeat: repeat-both;
            background-attachment: fixed;

        }

        #suggestion-wrap {
            box-shadow: 0px 3px 30px rgba(0, 0, 0, 0.15);
            background: white;
            min-height: 300px;
            padding: 0px;
        }

        #suggestion {
            padding: 25px 0px;
            width: 800px;
            margin: 0px auto;
            height: 280px;
            position: relative;
            left: -30px;
        }

            #suggestion .before {
                padding-top: 20px;
                font-style: italic;
                font-size: 12px;
            }

            #suggestion img {
                height: 300px;
                float: left;
                min-width: 200px;
            }

            #suggestion .image-placeholder {
                width: 100px;
                float: left;
                margin-right: 20px;
                background-color: #feb;
                height: 70px;
                margin-top: 20px;
                margin-bottom: 80px;
                border-radius: 100px;
                padding: 50px;
                padding-top: 80px;
                text-align: center;
                font-style: italic;
            }

            #suggestion .beer-style {
                text-align: left;
            }

            #suggestion .beer-name {
                font-size: 40px;
                line-height: 32px;
                padding: 4px 0px;
                text-transform: uppercase;
                font-weight: bold;
            }

            #suggestion p {
                font-style: italic;
            }

        #try-again {
            margin: 0 auto;
            display: block;
            width: 250px;
            z-index: 999;
            position: relative; top: -50px;
        }

            #try-again .message {
                text-align: center;
                font-size: 80%;
                display: block;
            }

            #try-again > a {
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

                #try-again > a:hover {
                    background: rgba(40, 133, 43, 1.0);
                }



        #history-wrap {

            margin-top: -90px;

            clear: both;

            padding: 20px;
        }

        #history {
            margin: 0 auto;
            border-spacing: 0;
            width: 900px;
            padding: 10px 0px;

            background-image: url('/images/background-600-blur.png');
            background-repeat: repeat-both;
            background-attachment: fixed;

            box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.15);

        }

            #history-wrap h2 {
                font-size: 120%;
                text-transform: uppercase;
                width: 900px; margin: 0 auto;
                margin-top: 2em;
                margin-bottom: 0.5em;

                color: rgba(70, 35, 17, 1.0);
            }

            tbody td {
                padding: 5px 15px;
                font-size: 12px;
            }

            td.price {
                text-align: right;
            }

    </style>

</head>
<body>
    <#if beer??>
        <div id="suggestion-wrap">
            <div id="suggestion" data-product-number="#${beer.productNumber}">
                <#if beer.imageURI?has_content>
                    <img src="${beer.imageURI}" />
                <#else>
                    <div class="image-placeholder">
                        No Image Available
                    </div>
                </#if>
                <div class="before">Why not try...</div>
                <div class="beer-name">${beer.name}</div>
                <div class="beer-style">#${beer.productNumber?c} - ${beer.style} - ${beer.mL} mL / ${(beer.cadPrice)?string.currency}</div>
                <p>${beer.tastingNote}</p>
            </div>
        </div>
        <div id="try-again">
            <span class="message">${message}</span>
            <a href="/suggest">Another One</a>
        </div>
    <#else>
        <p>There are no beers in stock at your local LCBO that I haven't already suggested!</p>
    </#if>

    <#if history?has_content>
        <div id="history-wrap">
            <h2>Suggestion History</h2>
            <table id="history">
                <tbody>
                    <#list history as entry>
                        <#assign beer = entry.value/>
                        <tr>
                            <td>${entry.key.format(dateTimeFormatter)}</td>
                            <td>${beer.name}</td>
                            <td>${beer.style}</td>
                            <td class="price">${beer.mL} mL / ${(beer.cadPrice)?string.currency}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </#if>

</body>
</html>
