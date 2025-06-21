// cadastro-socio.js

document.addEventListener("DOMContentLoaded", function() {
    const btnCriarConta = document.getElementById("criarConta");

    btnCriarConta.addEventListener("click", async function(event) {
        event.preventDefault(); // Impede o envio tradicional do form

        // Captura os dados do formulário
        const nome = document.getElementById("nome").value.trim();
        const cpf = document.getElementById("cpf").value.trim();
        const telefone = document.getElementById("telefone").value.trim();
        const email = document.getElementById("email").value.trim();
        const senha = document.getElementById("senha").value.trim();

        // Monta o objeto que o backend espera
        const socio = {
            nomeSocio: nome,
            cpfSocio: cpf,
            telefoneSocio: telefone,
            emailSocio: email,
            senhaSocio: senha
        };

        try {
            const response = await fetch("http://localhost:8080/socios/cadastrarSocio", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(socio)
            });

            if (response.ok) {
                const novoSocio = await response.json();
                alert("Sócio cadastrado com sucesso!");
                console.log(novoSocio);
                // Aqui você pode redirecionar para outra página, se quiser
                 window.location.href = "../socioMasterPages/visualizar-socios.html";
            } else {
                const erro = await response.text();
                alert("Erro ao cadastrar sócio: " + erro);
                console.error(erro);
            }
        } catch (error) {
            alert("Erro inesperado: " + error.message);
            console.error(error);
        }
    });
});
