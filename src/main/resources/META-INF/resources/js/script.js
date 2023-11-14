function displayFileName(input) {
    const files = input.files;
    const fileSelectedText = document.getElementById('file-selected');

    if (files.length > 0) {
        fileSelectedText.innerText = files[0].name;
    } else {
        fileSelectedText.innerText = 'Ningún archivo seleccionado';
    }
}
document.addEventListener('DOMContentLoaded', function() {
    const compressButton = document.querySelector('.btn-comprimir');
    compressButton.addEventListener('click', function(event) {
        const filesInput = document.getElementById('files');
        if (filesInput.files.length === 0) {
            event.preventDefault(); // Detiene el envío del formulario
            const messageElement = document.getElementById('message');
            messageElement.textContent = "¡Debes seleccionar una imagen para comprimir!";
        }
    });
});
