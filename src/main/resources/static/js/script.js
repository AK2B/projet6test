// Récupérer les éléments span et div par leur ID
var sourceSpan = document.getElementById('sourceSpan');
var targetDiv = document.getElementById('targetDiv');

// Ajouter un gestionnaire d'événement pour le clic sur le span
sourceSpan.addEventListener('click', function() {
    // Copier le contenu du span dans le div
    targetDiv.textContent = sourceSpan.textContent;
});
