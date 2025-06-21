 // Função para pré-visualizar imagem quando a URL muda
 document.getElementById('imgUrlConteudo').addEventListener('input', function() {
    const url = this.value;
    const preview = document.getElementById('previewImagem');
    
    if (url) {
      preview.src = url;
      preview.style.display = 'block';
    } else {
      preview.style.display = 'none';
    }
  });