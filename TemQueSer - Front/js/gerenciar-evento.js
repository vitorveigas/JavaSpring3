// Verificação do token de autenticação
const token = localStorage.getItem('jwtToken');
if (!token) {
    alert('Você precisa estar logado para acessar esta página.');
    window.location.href = '../index.html';
}

// Função para criar evento (com validação do detalheEvento)
function criarEvento() {
    const titulo = document.getElementById('tituloEvento').value;
    const detalhe = document.getElementById('detalheEvento').value;
    const data = document.getElementById('dataEvento').value;
    const organizador = document.getElementById('organizadorEvento').value;
    const parceiro = document.getElementById('parceiroEvento').value;
    const local = document.getElementById('localEvento').value;
    const hora = document.getElementById('horaEvento').value;
    const imgUrl = document.getElementById('imgUrlEvento').value;
    const preco = document.getElementById('precoEvento').value;

    let error = false;

    // Validação dos campos obrigatórios
    if (!titulo) {
        document.getElementById('tituloError').textContent = 'O título é obrigatório.';
        error = true;
    } else {
        document.getElementById('tituloError').textContent = '';
    }

    if (!detalhe) {
        document.getElementById('detalheError').textContent = 'Os detalhes são obrigatórios.';
        error = true;
    } else {
        document.getElementById('detalheError').textContent = '';
    }

    if (!data) {
        document.getElementById('dataError').textContent = 'A data é obrigatória.';
        error = true;
    } else {
        document.getElementById('dataError').textContent = '';
    }

    if (!organizador) {
        document.getElementById('organizadorError').textContent = 'O organizador é obrigatório.';
        error = true;
    } else {
        document.getElementById('organizadorError').textContent = '';
    }

    if (!local) {
        document.getElementById('localError').textContent = 'O local é obrigatório.';
        error = true;
    } else {
        document.getElementById('localError').textContent = '';
    }

    if (!hora) {
        document.getElementById('horaError').textContent = 'A hora é obrigatória.';
        error = true;
    } else {
        document.getElementById('horaError').textContent = '';
    }

    if (error) return;

    // Envio da requisição para criar o evento
    fetch('http://localhost:8080/eventos', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            tituloEvento: titulo,
            detalheEvento: detalhe,
            dataEvento: data,
            organizadorEvento: organizador,
            parceiroEvento: parceiro,
            localEvento: local,
            horaEvento: hora,
            imgUrlEvento: imgUrl,
            precoEvento: preco ? parseFloat(preco) : null
        })
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('Token inválido ou expirado');
        }
        if (response.status === 403) {
            throw new Error('Somente sócios podem criar eventos');
        }
        if (!response.ok) {
            throw new Error('Erro ao criar evento');
        }
        return response.json();
    })
    .then(data => {
        alert('Evento criado com sucesso!');
        window.location.href = 'visualizar-evento-socio.html';
    })
    .catch(error => {
        console.error(error);
        alert(error.message || 'Erro ao criar evento. Tente novamente.');
    });
}

function fecharFormularioEvento() {
    // Fechar ou resetar o formulário
    document.getElementById('formEditarEvento').reset();
}

// Função para buscar os eventos (com detalheEvento posicionado após o título)
function fetchEventos() {
    fetch('http://localhost:8080/eventos', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('Token inválido ou expirado');
        }
        if (!response.ok) {
            throw new Error('Erro ao buscar eventos');
        }
        return response.json();
    })
    .then(data => {
        const container = document.getElementById('eventos');
        container.innerHTML = ''; // Limpa a lista antes de atualizar

        if (data.length === 0) {
            container.innerHTML = "<p>Nenhum evento encontrado.</p>";
            return;
        }

        data.forEach(evento => {
            const card = document.createElement('div');
            card.classList.add('card', 'mb-3');
            card.innerHTML = `
                <div class="card-body">
                    <h5 class="card-title">${evento.tituloEvento}</h5>
                    <p class="card-text text-muted">${evento.detalheEvento || 'Sem detalhes adicionais'}</p>
                    <p class="card-text">
                        <strong>Organizador:</strong> ${evento.organizadorEvento}<br>
                        <strong>Local:</strong> ${evento.localEvento}<br>
                        <strong>Data:</strong> ${new Date(evento.dataEvento).toLocaleDateString()} às ${evento.horaEvento}<br>
                        ${evento.precoEvento ? `<strong>Preço:</strong> R$ ${evento.precoEvento.toFixed(2)}` : ''}
                    </p>
                    ${evento.imgUrlEvento ? `<img src="${evento.imgUrlEvento}" class="img-fluid rounded mb-2" alt="Imagem do evento" style="max-height: 200px;">` : ''}
                    <button onclick="editarEvento(${evento.idEvento})" class="btn btn-sm btn-primary me-2">Editar</button>
                    <button onclick="deletarEvento(${evento.idEvento})" class="btn btn-sm btn-danger">Excluir</button>
                </div>
            `;
            container.appendChild(card);
        });
    })
    .catch(error => {
        console.error(error);
        alert(error.message || 'Erro ao carregar os eventos. Tente novamente.');
    });
}

// Função para deletar evento
function deletarEvento(idEvento) {
    if (!confirm('Tem certeza que deseja excluir este evento?')) return;
    
    fetch(`http://localhost:8080/eventos/excluirEvento/${idEvento}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('Token inválido ou expirado');
        }
        if (response.status === 403) {
            throw new Error('Somente sócios podem excluir eventos');
        }
        if (!response.ok) {
            throw new Error('Erro ao excluir evento');
        }
        return response.text();
    })
    .then(message => {
        alert(message || 'Evento excluído com sucesso!');
        fetchEventos(); // Recarrega a lista de eventos
    })
    .catch(error => {
        console.error(error);
        alert(error.message || 'Erro ao excluir evento. Tente novamente.');
    });
}

// Função para editar evento (carrega o formulário)
function editarEvento(idEvento) {
    window.location.href = `../socioPages/editar-evento.html?id=${idEvento}`;
}

// Se estiver na página de visualização, carrega os eventos
if (window.location.pathname.includes('visualizar-evento')) {
    fetchEventos();
}

// Se estiver na página de criação, adiciona o preview da imagem
if (document.getElementById('imgUrlEvento')) {
    document.getElementById('imgUrlEvento').addEventListener('change', function() {
        const preview = document.getElementById('previewImagem');
        if (this.value) {
            preview.src = this.value;
            preview.style.display = 'block';
        } else {
            preview.style.display = 'none';
        }
    });
}