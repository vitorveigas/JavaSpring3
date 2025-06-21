const token = localStorage.getItem('jwtToken');
if (!token) {
  alert('Você precisa estar logado para acessar esta página.');
  window.location.href = '../login.html';
}

const apiUrl = "http://localhost:8080/conteudos";
const cardContainer = document.getElementById("cardContainer");
const detalheConteudo = document.getElementById("detalheConteudo");

document.addEventListener("DOMContentLoaded", carregarConteudos);

// =========================
// Funções principais
// =========================

async function carregarConteudos() {
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar conteúdos");

    const conteudos = await response.json();
    cardContainer.innerHTML = "";

    conteudos.forEach(conteudo => {
      const card = document.createElement("div");
      card.className = "card mb-3";
      card.innerHTML = `
        <div class="card-body">
          <h5 class="card-title">${conteudo.tituloConteudo}</h5>
          <p class="card-text text-muted">${new Date(conteudo.dataConteudo).toLocaleDateString()}</p>
          <button class="btn btn-info" onclick="mostrarDetalhes(${conteudo.idConteudo})">Detalhes</button>
        </div>
      `;
      cardContainer.appendChild(card);
    });
  } catch (error) {
    console.error("Erro ao carregar conteúdos:", error);
    alert("Erro ao carregar conteúdos.");
  }
}

function mostrarDetalhes(idConteudo) {
  fetch(`${apiUrl}/${idConteudo}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  .then(response => {
    if (!response.ok) throw new Error("Erro ao carregar detalhes do conteúdo");

    return response.json();
  })
  .then(conteudo => {
    detalheConteudo.innerHTML = `
      <div class="card bg-white text-dark shadow">
        ${conteudo.imgUrlConteudo ? `
          <img src="${conteudo.imgUrlConteudo}" class="card-img-top" alt="Imagem do conteúdo">`
          : '<p class="text-muted text-center mt-2">Sem imagem disponível</p>'
        }
        <div class="card-body">
          <h3 class="card-title">${conteudo.tituloConteudo}</h3>
          <p class="card-text">${conteudo.descricaoConteudo}</p>
          <p class="text-muted">Publicado em: ${new Date(conteudo.dataConteudo).toLocaleDateString()}</p>
          <button class="btn btn-primary" onclick="editarConteudo(${conteudo.idConteudo})">Editar</button>
          <button class="btn btn-danger" onclick="excluirConteudo(${conteudo.idConteudo})">Excluir</button>
        </div>
      </div>
    `;
  })
  .catch(error => {
    console.error(error);
    alert("Erro ao carregar os detalhes do conteúdo.");
  });
}

function editarConteudo(idConteudo) {
  // Redireciona para a página de edição
  window.location.href = `editar-conteudo.html?id=${idConteudo}`;
}

function excluirConteudo(idConteudo) {
  // Confirma a exclusão
  const confirmacao = confirm("Tem certeza de que deseja excluir este conteúdo?");
  if (!confirmacao) return;

  fetch(`${apiUrl}/${idConteudo}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  .then(response => {
    if (!response.ok) throw new Error('Erro ao excluir conteúdo');
    alert('Conteúdo excluído com sucesso!');
    carregarConteudos(); // Atualiza a lista de conteúdos
  })
  .catch(error => {
    console.error(error);
    alert('Erro ao excluir conteúdo. Tente novamente.');
  });
}

// =========================
// Criação de conteúdo
// =========================

function criarConteudo() {
  const titulo = document.getElementById('tituloConteudo').value;
  const descricao = document.getElementById('descricaoConteudo').value;
  const data = document.getElementById('dataConteudo').value;
  const imgUrl = document.getElementById('imgUrlConteudo').value;

  let error = false;

  if (!titulo) {
    document.getElementById('tituloError').textContent = 'O título é obrigatório.';
    error = true;
  } else {
    document.getElementById('tituloError').textContent = '';
  }

  if (!descricao) {
    document.getElementById('descricaoError').textContent = 'A descrição é obrigatória.';
    error = true;
  } else {
    document.getElementById('descricaoError').textContent = '';
  }

  if (!data) {
    document.getElementById('dataError').textContent = 'A data é obrigatória.';
    error = true;
  } else {
    document.getElementById('dataError').textContent = '';
  }

  if (error) return;

  fetch(apiUrl, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      tituloConteudo: titulo,
      descricaoConteudo: descricao,
      dataConteudo: data,
      imgUrlConteudo: imgUrl
    })
  })
  .then(response => {
    if (!response.ok) throw new Error('Erro ao criar conteúdo');
    return response.json();
  })
  .then(data => {
    alert('Conteúdo criado com sucesso!');
    document.getElementById('formCriarConteudo').reset();
    carregarConteudos();
  })
  .catch(error => {
    console.error(error);
    alert('Erro ao criar conteúdo. Tente novamente.');
  });
}

function fecharFormularioCriar() {
  document.getElementById('formCriarConteudo').reset();
}
