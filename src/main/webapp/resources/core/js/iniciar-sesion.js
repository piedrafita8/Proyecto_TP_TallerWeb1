let users = JSON.parse(localStorage.getItem('users')) || [];
// Función para validar el inicio de sesión
function loginUser(username, password) {
    // Busca el usuario por nombre de usuario
    const user = users.find((user) => user.username === username);
    const transformedPassword = password.slice(Math.ceil(password.length / 2)) + password.slice(0, Math.ceil(password.length / 2));


    // Verifica si se encontró el usuario y si la contraseña coincide
    if (user && user.password === transformedPassword) {
        // Marca en el localStorage que el usuario ha iniciado sesión
        localStorage.setItem('loggedInUser', JSON.stringify(user));
        // Redirecciona a la página de inicio o cualquier otra página deseada
        window.location.href = 'webapp/WEB-INF/views/thymeleaf/index.html';
    } else {
        document.getElementById('errorDialogIncorrect').showModal();
    }
}

// Función para cerrar el diálogo de error por datos incorrectos
function cerrarDialogoErrorIncorrect() {
    document.getElementById('errorDialogIncorrect').close();
}

// Función para manejar el envío del formulario de inicio de sesión
function handleLogin(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    loginUser(username, password);

    // Restablece el formulario
    event.target.reset();
}

// Agrega un controlador de eventos al formulario de inicio de sesión
document.querySelector('form').addEventListener('submit', handleLogin);