const API_URL = "http://localhost:8080/api/properties";

// Al cargar la página
document.addEventListener("DOMContentLoaded", loadProperties);

// Manejo del formulario (crear/actualizar)
document.getElementById("propertyForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("propertyId").value;

    const property = {
        address: document.getElementById("address").value,
        price: parseFloat(document.getElementById("price").value),
        size: parseFloat(document.getElementById("size").value),
        description: document.getElementById("description").value
    };

    let response;
    if (id) {
        // Actualizar
        response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(property)
        });
        showMessage("Propiedad actualizada correctamente");
    } else {
        // Crear
        response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(property)
        });
        showMessage("Propiedad creada correctamente");
    }

    if (response.ok) {
        e.target.reset();
        document.getElementById("propertyId").value = "";
        document.getElementById("formButton").textContent = "Agregar Propiedad";
        loadProperties();
    } else {
        showMessage("Error al guardar la propiedad", true);
    }
});

// Cargar todas las propiedades
async function loadProperties() {
    const response = await fetch(API_URL);
    const properties = await response.json();

    const container = document.getElementById("properties");
    container.innerHTML = "";

    properties.forEach(prop => {
        const div = document.createElement("div");
        div.className = "property";
        div.innerHTML = `
            <h3>${prop.address}</h3>
            <p><strong>ID:</strong> ${prop.id}</p>
            <p><strong>Precio:</strong> $${prop.price}</p>
            <p><strong>Tamaño:</strong> ${prop.size} m²</p>
            <p>${prop.description || "Sin descripción"}</p>
            <button onclick="editProperty(${prop.id})">Editar</button>
            <button onclick="deleteProperty(${prop.id})">Eliminar</button>
        `;
        container.appendChild(div);
    });
}

// Obtener una propiedad por ID y llenar formulario (para editar)
async function editProperty(id) {
    const response = await fetch(`${API_URL}/${id}`);
    if (response.ok) {
        const prop = await response.json();
        document.getElementById("propertyId").value = prop.id;
        document.getElementById("address").value = prop.address;
        document.getElementById("price").value = prop.price;
        document.getElementById("size").value = prop.size;
        document.getElementById("description").value = prop.description || "";

        document.getElementById("formButton").textContent = "Actualizar Propiedad";
    } else {
        showMessage("Propiedad no encontrada", true);
    }
}

// Eliminar propiedad (desde la lista)
async function deleteProperty(id) {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (response.ok) {
        showMessage("Propiedad eliminada correctamente");
        loadProperties();
    } else {
        showMessage("Error al eliminar propiedad", true);
    }
}

// Buscar propiedad por ID y cargar en formulario
document.getElementById("editByIdForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("editId").value;

    const response = await fetch(`${API_URL}/${id}`);
    if (response.ok) {
        const prop = await response.json();
        document.getElementById("propertyId").value = prop.id;
        document.getElementById("address").value = prop.address;
        document.getElementById("price").value = prop.price;
        document.getElementById("size").value = prop.size;
        document.getElementById("description").value = prop.description || "";

        document.getElementById("formButton").textContent = "Actualizar Propiedad";
        showMessage(`Propiedad ${id} cargada para edición`);
    } else {
        showMessage("Propiedad no encontrada", true);
    }
});

// Eliminar propiedad por ID
document.getElementById("deleteByIdForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("deleteId").value;

    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (response.ok) {
        showMessage(`Propiedad ${id} eliminada correctamente`);
        loadProperties();
    } else {
        showMessage("Error al eliminar propiedad", true);
    }
});

// Mostrar mensajes
function showMessage(msg, error = false) {
    const el = document.getElementById("responseMsg");
    el.textContent = msg;
    el.style.color = error ? "red" : "#ff4d94";
    setTimeout(() => (el.textContent = ""), 3000);
}
