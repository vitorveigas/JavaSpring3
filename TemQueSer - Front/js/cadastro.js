// Função para formatar CPF (000.000.000-00)
function formatarCPF(cpf) {
    cpf = cpf.replace(/\D/g, ''); // Remove tudo que não é dígito
    cpf = cpf.substring(0, 11); // Limita a 11 caracteres
    
    // Aplica a formatação
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    
    return cpf;
}

// Função para formatar telefone (00) 00000-0000 ou (00) 0000-0000
function formatarTelefone(telefone) {
    telefone = telefone.replace(/\D/g, ''); // Remove tudo que não é dígito
    telefone = telefone.substring(0, 11); // Limita a 11 caracteres
    
    if (telefone.length > 10) { // Celular com 9 dígitos (11 dígitos no total)
        telefone = telefone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (telefone.length > 6) { // Fixo com 8 dígitos (10 dígitos no total)
        telefone = telefone.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    } else if (telefone.length > 2) {
        telefone = telefone.replace(/(\d{2})(\d)/, '($1) $2');
    } else if (telefone.length > 0) {
        telefone = `(${telefone}`;
    }
    
    return telefone;
}

// Adiciona os event listeners para formatação em tempo real
document.getElementById("cpf").addEventListener("input", function(e) {
    const input = e.target;
    input.value = formatarCPF(input.value);
});

document.getElementById("telefone").addEventListener("input", function(e) {
    const input = e.target;
    input.value = formatarTelefone(input.value);
});

document.getElementById("cadastroForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    // Remove a formatação antes de enviar
    const nome = document.getElementById("nome").value;
    const cpf = document.getElementById("cpf").value.replace(/\D/g, '');
    const telefone = document.getElementById("telefone").value.replace(/\D/g, '');
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    // Objeto com os nomes EXATOS que o backend espera
    const cadastroData = {
        nomeCliente: nome,
        cpfCliente: cpf,
        telefoneCliente: telefone,
        emailCliente: email,
        senhaCliente: senha
    };

    console.log("CPF capturado:", cpf);
    console.log("Dados enviados para cadastro:", JSON.stringify(cadastroData, null, 2));

    try {
        const response = await fetch("http://localhost:8080/clientes/cadastrar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cadastroData)
        });

        console.log("Resposta do servidor:", response);

        if (!response.ok) {
            const erro = await response.json();
            console.error("Erro do servidor:", erro);
        
            // Se o erro for uma mensagem simples:
            throw new Error(erro.message || JSON.stringify(erro));
        }

        const resultadoTexto = await response.text();

        try {
            const resultadoJson = JSON.parse(resultadoTexto);
            console.log("Cliente cadastrado com sucesso:", resultadoJson);
        } catch (e) {
            console.log("Resposta sem JSON (ou JSON inválido):", resultadoTexto);
        }

        alert("Cadastro realizado com sucesso!");
    } catch (error) {
        console.error("Erro ao tentar cadastrar:", error);
        alert("Erro ao cadastrar: " + error.message);
    }
});