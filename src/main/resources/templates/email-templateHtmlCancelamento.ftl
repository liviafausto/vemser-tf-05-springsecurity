<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WBHEALTH</title>
    <style>
        p {
            font-size: 1.313rem;
            color: #000000;
        }

        li {
            font-size: 1.313rem;
            display: inline;
        }

        .tipo {
            display: grid;
            gap: 4px;
            position: relative;
        }

        .tipo div {
            display: block;
            justify-items: start;
            align-items: start;
        }

        .tipo img {
            position: absolute;
            /* background-color: aqua; */
            display: inline;
            width: 23px;
            justify-items: center;
            align-items: center;
        }

        .tipo li {
            position: relative;
            margin-left: 30px;
            top: 1px;
        }

        .tipo div {
            display: block;
            justify-items: center;
            align-items: center;
        }

        .secao-1 {
            display: inline-block;
        }

        .secao-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            background-color: rgba(217, 217, 217, 0.2);
            border-radius: 16px 16px 0px 0px;
            margin: 0 40px 0 40px;
            padding: 20px 20px 20px 20px;
        }

        .secao-2 .social {
            grid-column: 1;
        }

        .secao-2 .icone {
            grid-column: 2;
        }

        .social p {
            display: inline-block;
            /* justify-self: end; */
            font-size: 1.1rem;
        }
    </style>
</head>

<body>
<div style="display: block; width: 600px; margin: 0px; padding: 0px;">

    <div style="display: block; background-color: rgba(217, 217, 217, 0.2); margin-top: 10px; margin: 40px; border-radius: 16px; padding: 10px 0 40px 0px;">

        <div style="text-align: center; margin: 20px 0px 20px 0px;">
            <img src="https://uploaddeimagens.com.br/images/004/574/918/original/Logo_WBHEALTH2.jpg?1691946037" alt="Logo WBHealth" />
        </div>

        <div style="margin-left: 40px;">
            <p>Olá, ${nome}.<br><br>
                Seu atendimento foi cancelado!</p>
        </div>

        <div style="display: grid; margin: 0px 3% 0px 3%; margin-top: 0px; grid-template-columns: 1fr 1fr; border-radius: 17px;">
            <div style="text-align: start; margin: 0; padding: 10px; grid-column: 1; border-radius: 17px 0px 0px 17px;">
                <p style="display: inline; font-size: 20px;">Dúvidas? Entre em contato conosco: <br>${emailSuporte}</p>
            </div>
            <div style="text-align: end; grid-column: 2; border-radius: 0px 17px 17px 0px;">
                <a href="http://localhost:8080/confirmar/atendimento">Cancelar atendimento</a>
            </div>
        </div>

    </div>

    <div class="secao-2" style="display: grid; grid-template-columns: 1fr 1fr;">
        <div class="social">
            <div style="display: block;">
                <img src="https://uploaddeimagens.com.br/images/004/574/921/full/Facebook.png?1691946261" alt="Facebook" />
                <p style="display: inline; margin-left: 10px;">wbhealthOficial</p>
            </div>
            <div style="display: inline-block; margin-top: 5px;">
                <img src="https://uploaddeimagens.com.br/images/004/574/924/full/Instagram.png?1691946290" alt="Instagram" />
                <p style="display: inline; margin-left: 10px;">@WbhealthOficial</p>
            </div>
        </div>
    </div>

</div>

</body>

</html>