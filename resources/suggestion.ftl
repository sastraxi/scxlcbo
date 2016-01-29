<html>
<head>
    <title>Suggestion - SCX Beer Suggestbot</title>
    <style type="text/css">

        body {
            margin: 0;
            font-family: sans-serif;
            color: #333;
        }

        #suggestion {
            width: 800px;
            margin: 20px auto;
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
                position: absolute;
                right: 20px;
                bottom: 20px;
            }

        #history-wrap {
            clear: both;
            margin-top: 20px;
            padding: 20px;

            background: #eee;
            box-shadow: inset 0px 3px 15px rgba(0, 0, 0, 0.1);
        }

        #history {
            margin: 0 auto;
            border-spacing: 0;
            width: 900px;
        }

            thead td {
                text-align: center;
                font-size: 120%;
                text-transform: uppercase;
                padding-bottom: 10px;
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

            <div id="try-again">
                <span>${message}</span>
                <a href="/suggest">Another One</a>
            </div>
        </div>
    <#else>
        <p>There are no beers in stock at your local LCBO that I haven't already suggested!</p>
    </#if>

    <#if history?has_content>
        <div id="history-wrap">
            <table id="history">
                <thead>
                    <td colspan="4">
                        Suggestion History
                    </td>
                </thead>
                <tbody>
                    <#list history as entry>
                        <#assign beer = entry.value/>
                        <tr>
                            <td>${entry.key}</td>
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
