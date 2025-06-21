// === INÍCIO PERFIL SÓCIO MASTER ===

// Verificação do token e carregamento inicial
window.addEventListener("load", async () => {
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("Você precisa estar logado para acessar esta página!");
        window.location.replace("../index.html");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/sociosMaster/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const socioMaster = await response.json();
            
            // Preenche os dados do formulário
            document.getElementById("nome").value = socioMaster.nomeSocioMaster || "";
            document.getElementById("telefone").value = socioMaster.telefoneSocioMaster || "";
            document.getElementById("cpf").value = socioMaster.cpfSocioMaster || "";
            document.getElementById("email").value = socioMaster.emailSocioMaster || "";
            document.getElementById("senha").value = socioMaster.senhaSocioMaster || "";
            
            // Gera o avatar com iniciais
            gerarAvatarComIniciais(socioMaster);
            
            // Atualiza o nome no cabeçalho
            document.getElementById("user-name").textContent = socioMaster.nomeSocioMaster || "Sócio Master";
            
        } else {
            alert("Erro ao carregar os dados do perfil do Sócio Master.");
        }
    } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        alert("Erro de conexão. Tente novamente mais tarde.");
    }
});

// Função para gerar avatar com iniciais
function gerarAvatarComIniciais(socioMaster) {
    const nomeCompleto = socioMaster.nomeSocioMaster || "SM";
    const avatarElement = document.getElementById("user-avatar");
    
    // Extrai as iniciais (2 primeiras letras do primeiro e último nome)
    const nomes = nomeCompleto.split(' ').filter(nome => nome.length > 0);
    let iniciais = "";
    
    if (nomes.length === 1) {
        iniciais = nomes[0].substring(0, 2).toUpperCase();
    } else {
        iniciais = (nomes[0][0] + nomes[nomes.length - 1][0]).toUpperCase();
    }

    // Cores da paleta TemQueSer
    const colors = ['#1b4332', '#2d6a4f', '#40916c', '#52b788', '#74c69d'];
    const colorIndex = Math.abs(hashCode(nomeCompleto)) % colors.length;
    const bgColor = colors[colorIndex];
    
    // Cria um canvas com as iniciais
    const canvas = document.createElement('canvas');
    canvas.width = 120;
    canvas.height = 120;
    const ctx = canvas.getContext('2d');
    
    // Fundo como círculo
    ctx.beginPath();
    ctx.arc(60, 60, 60, 0, Math.PI * 2);
    ctx.fillStyle = bgColor;
    ctx.fill();
    
    // Texto (iniciais)
    ctx.fillStyle = '#ffffff';
    ctx.font = 'bold 48px "Segoe UI", Tahoma, Geneva, Verdana, sans-serif';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(iniciais, 60, 60);
    
    // Converte para URL de imagem e define como src
    avatarElement.src = canvas.toDataURL();
    avatarElement.style.display = 'block';
}

// Função hash para gerar valor consistente
function hashCode(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    return hash;
}

// Elementos do formulário
const btnEditar = document.getElementById("btnEditar");
const inputNome = document.getElementById("nome");
const inputTelefone = document.getElementById("telefone");
const inputCPF = document.getElementById("cpf");
const inputEmail = document.getElementById("email");
const inputSenha = document.getElementById("senha");

// CPF somente leitura
inputCPF.readOnly = true;

// Variável de controle de edição
let editando = false;

// Botão editar/salvar
btnEditar.addEventListener("click", async () => {
    editando = !editando;

    // Alterna estado dos campos
    inputNome.disabled = !editando;
    inputTelefone.disabled = !editando;
    inputEmail.disabled = !editando;
    inputSenha.disabled = !editando;

    // Atualiza o botão
    const icon = btnEditar.querySelector("i");
    const text = btnEditar.querySelector("span");
    
    if (editando) {
        icon.className = "bi bi-check2-circle";
        text.textContent = "Salvar Alterações";
    } else {
        btnEditar.disabled = true; // Desabilita durante a requisição
        
        icon.className = "bi bi-pencil-square";
        text.textContent = "Editar Conta";

        try {
            const dadosAtualizados = {
                nomeSocioMaster: inputNome.value,
                telefoneSocioMaster: inputTelefone.value,
                emailSocioMaster: inputEmail.value,
                senhaSocioMaster: inputSenha.value || undefined // Só envia se foi alterada
            };

            const token = localStorage.getItem("jwtToken");
            const response = await fetch("http://localhost:8080/sociosMaster/editarSocioMaster", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify(dadosAtualizados)
            });

            if (response.ok) {
                alert("Dados do Sócio Master atualizados com sucesso!");
                inputSenha.value = ""; // Limpa o campo senha
                
                // Atualiza o nome no cabeçalho e avatar
                const socioMaster = await response.json();
                document.getElementById("user-name").textContent = socioMaster.nomeSocioMaster;
                gerarAvatarComIniciais(socioMaster);
            } else {
                const error = await response.json();
                alert(error.message || "Erro ao atualizar os dados!");
            }
        } catch (error) {
            console.error("Erro ao atualizar dados:", error);
            alert("Erro de conexão ao atualizar dados!");
        } finally {
            btnEditar.disabled = false;
        }
    }
});

// Botão excluir conta
const btnExcluir = document.getElementById("btnExcluir");
btnExcluir.addEventListener("click", async () => {
    if (confirm("Deseja realmente excluir sua conta de Sócio Master? Esta ação é irreversível e removerá todos os seus dados permanentemente.")) {
        const token = localStorage.getItem("jwtToken");

        try {
            const response = await fetch("http://localhost:8080/sociosMaster/me", {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if (response.ok) {
                alert("Conta de Sócio Master excluída com sucesso!");
                localStorage.removeItem("jwtToken");
                window.location.replace("../pagina-home.html");
            } else {
                const error = await response.json();
                alert(error.message || "Erro ao excluir a conta!");
            }
        } catch (error) {
            console.error("Erro ao excluir conta:", error);
            alert("Erro de conexão ao excluir a conta!");
        }
    }
});

// Toggle para mostrar/esconder senha
document.getElementById("toggleSenha").addEventListener("click", function() {
    const senhaInput = document.getElementById("senha");
    const icon = this.querySelector("i");
    
    if (senhaInput.type === "password") {
        senhaInput.type = "text";
        icon.className = "bi bi-eye-slash";
    } else {
        senhaInput.type = "password";
        icon.className = "bi bi-eye";
    }
});

// === FIM PERFIL SÓCIO MASTER ===