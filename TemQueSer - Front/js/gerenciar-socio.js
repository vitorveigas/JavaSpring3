// gerenciar-socios.js

document.addEventListener("DOMContentLoaded", function() {
    const sociosTabela = document.getElementById("sociosTabela");

    // Função para carregar todos os sócios
    async function carregarSocios() {
        try {
            const response = await fetch("http://localhost:8080/socios/todosSocios"); // <<< precisa criar esse endpoint no seu controller se ainda não existir!
            
            if (response.ok) {
                const socios = await response.json();
                sociosTabela.innerHTML = ""; // limpa a tabela antes de popular

                socios.forEach((socio, index) => {
                    const tr = document.createElement("tr");

                    tr.innerHTML = `
                        
                        <td>${socio.nomeSocio}</td>
                        <td>${socio.telefoneSocio}</td>
                        <td>${socio.cpfSocio}</td>
                        <td>${socio.emailSocio}</td>
                        <td>
                            <button class="btn btn-danger btn-sm" onclick="excluirSocio(${socio.id})">
                                <i class="bi bi-trash"></i> Excluir
                            </button>
                        </td>
                    `;

                    sociosTabela.appendChild(tr);
                });
            } else {
                console.error("Erro ao carregar sócios:", await response.text());
            }
        } catch (error) {
            console.error("Erro inesperado:", error);
        }
    }

    // Função para excluir um sócio
    window.excluirSocio = async function(id) {
        if (confirm("Tem certeza que deseja excluir este sócio?")) {
            try {
                const response = await fetch(`http://localhost:8080/socios/${id}`, {
                    method: "DELETE"
                });

                if (response.ok) {
                    alert("Sócio excluído com sucesso!");
                    carregarSocios(); // recarrega a lista
                } else {
                    alert("Erro ao excluir sócio: " + await response.text());
                }
            } catch (error) {
                alert("Erro inesperado ao excluir: " + error.message);
            }
        }
    };

    // Inicializa
    carregarSocios();
});
