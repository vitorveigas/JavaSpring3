// Verificação do token e carregamento inicial
window.addEventListener("load", async () => {
    const token = localStorage.getItem("jwtToken");
    if (!token) {
        alert("Você precisa estar logado para acessar esta página!");
        window.location.replace("../index.html");
        return;
    }

    try {
        // Verifica se é sócio master
        const isMaster = window.location.pathname.includes("master");
        const endpoint = isMaster ? "sociosMaster/me" : "socios/me";

        const response = await fetch(`http://localhost:8080/${endpoint}`, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const userData = await response.json();
            preencherDadosUsuario(userData, isMaster);
            gerarAvatarComIniciais(userData);
        } else {
            alert("Erro ao carregar os dados do perfil.");
        }
    } catch (error) {
        console.error("Erro ao carregar perfil:", error);
        alert("Erro de conexão. Tente novamente mais tarde.");
    }
});

// Função para gerar avatar com iniciais
function gerarAvatarComIniciais(userData) {
    const nomeCompleto = userData.nomeSocio || userData.nomeSocioMaster || "Usuário";
    const avatarElement = document.getElementById("user-avatar");
    
    // Extrai as iniciais (2 primeiras letras do primeiro e último nome)
    const nomes = nomeCompleto.split(' ').filter(nome => nome.length > 0);
    let iniciais = "";
    
    if (nomes.length === 1) {
        iniciais = nomes[0].substring(0, 2).toUpperCase();
    } else {
        iniciais = (nomes[0][0] + nomes[nomes.length - 1][0]).toUpperCase();
    }

    // Cores fixas da paleta TemQueSer para melhor consistência
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
    
    // Atualiza o nome no cabeçalho
    document.getElementById("user-name").textContent = nomeCompleto;
}

// Função hash para gerar valor consistente a partir de string
function hashCode(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    return hash;
}

// Preenche os dados do usuário no formulário
function preencherDadosUsuario(userData, isMaster) {
    document.getElementById("nome").value = userData.nomeSocio || userData.nomeSocioMaster || "";
    document.getElementById("telefone").value = userData.telefoneSocio || userData.telefoneSocioMaster || "";
    document.getElementById("email").value = userData.emailSocio || userData.emailSocioMaster || "";
    document.getElementById("cpf").value = userData.cpfSocio || userData.cpfSocioMaster || "";
    document.getElementById("senha").value = userData.senhaSocio || userData.senhaSocioMaster || ""; 
    
}

// Configuração do modo de edição
const btnEditar = document.getElementById("btnEditar");
const inputsEditaveis = [
    document.getElementById("nome"),
    document.getElementById("telefone"),
    document.getElementById("email"),
    document.getElementById("senha")
];

let editando = false;

btnEditar.addEventListener("click", async () => {
    editando = !editando;

    // Alterna estado dos inputs
    inputsEditaveis.forEach(input => input.disabled = !editando);

    // Atualiza o botão
    const icon = btnEditar.querySelector("i");
    const text = btnEditar.querySelector("span");
    
    if (editando) {
        icon.className = "bi bi-check2-circle";
        text.textContent = "Salvar Alterações";
    } else {
        icon.className = "bi bi-pencil-square";
        text.textContent = "Editar Conta";
        await salvarAlteracoes();
    }
});

// Função para salvar alterações
async function salvarAlteracoes() {
    const isMaster = window.location.pathname.includes("master");
    const endpoint = isMaster ? "sociosMaster/editarSocioMaster" : "socios/editarSocio";
    
    const dadosAtualizados = {
        nomeSocio: document.getElementById("nome").value,
        telefoneSocio: document.getElementById("telefone").value,
        emailSocio: document.getElementById("email").value,
        senhaSocio: document.getElementById("senha").value || undefined // Envia apenas se foi alterada
    };

    // Adiciona campos específicos para master
    if (isMaster) {
        dadosAtualizados.nomeSocioMaster = dadosAtualizados.nomeSocio;
        dadosAtualizados.telefoneSocioMaster = dadosAtualizados.telefoneSocio;
        dadosAtualizados.emailSocioMaster = dadosAtualizados.emailSocio;
    }

    const token = localStorage.getItem("jwtToken");
    try {
        const response = await fetch(`http://localhost:8080/${endpoint}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(dadosAtualizados)
        });

        if (response.ok) {
            alert("Dados atualizados com sucesso!");
            // Atualiza o avatar e nome se o nome foi alterado
            const userData = await response.json();
            document.getElementById("user-name").textContent = userData.nomeSocio || userData.nomeSocioMaster;
            gerarAvatarComIniciais(userData);
        } else {
            const error = await response.json();
            alert(error.message || "Erro ao atualizar os dados!");
        }
    } catch (error) {
        console.error("Erro ao atualizar dados:", error);
        alert("Erro de conexão ao atualizar dados!");
    }
}

// Exclusão de conta
const btnExcluir = document.getElementById("btnExcluir");
btnExcluir.addEventListener("click", async () => {
    if (confirm("Tem certeza que deseja excluir sua conta permanentemente? Esta ação não pode ser desfeita.")) {
        const isMaster = window.location.pathname.includes("master");
        const endpoint = isMaster ? "sociosMaster/me" : "socios/me";
        
        const token = localStorage.getItem("jwtToken");
        try {
            const response = await fetch(`http://localhost:8080/${endpoint}`, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if (response.ok) {
                alert("Conta excluída com sucesso!");
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