document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    const isSocio = email.toLowerCase().endsWith('@temqueser.com');
    const isSocioMaster =  email.toLowerCase().endsWith('@temquesermaster.com');

    let loginData;
    let endpoint;
    let userType;

    if (isSocioMaster) {
        loginData = {
            emailSocioMaster: email,
            senhaSocioMaster: senha
        };
        endpoint = 'http://localhost:8080/auth/login-socioMaster';
        userType = 'sócio master';
    } else if (isSocio) {
        loginData = {
            emailSocio: email,
            senhaSocio: senha
        };
        endpoint = 'http://localhost:8080/auth/login-socio';
        userType = 'sócio';
    } else {
        loginData = {
            emailCliente: email,
            senhaCliente: senha
        };
        endpoint = 'http://localhost:8080/auth/login';
        userType = 'cliente';
    }

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            const token = await response.text();
            localStorage.setItem('jwtToken', token);

            alert(`Login bem-sucedido como ${userType}!`);
            window.location.replace(
                isSocioMaster
                    ? '../pages/socioMasterPages/pagina-inicial-socio-master.html'
                    : isSocio
                        ? '../pages/socioPages/pagina-inicial-socio.html'
                        : '../pages/clientePages/pagina-inicial-cliente.html'
            );
        } else {
            const errorText = await response.text();
            alert(errorText || 'Credenciais incorretas.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro de conexão. Tente novamente mais tarde.');
    }
});
