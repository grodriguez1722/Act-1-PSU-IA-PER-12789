const API_URL = '/api/tasks';

document.addEventListener('DOMContentLoaded', loadTasks);

async function loadTasks() {
    const response = await fetch(API_URL);
    const tasks = await response.json();
    
    document.getElementById('tasks-TODO').innerHTML = '';
    document.getElementById('tasks-IN_PROGRESS').innerHTML = '';
    document.getElementById('tasks-DONE').innerHTML = '';
    
    tasks.forEach(task => renderTask(task));
}

function renderTask(task) {
    const taskEl = document.createElement('div');
    taskEl.className = 'task-card';
    taskEl.draggable = true;
    taskEl.id = 'task-' + task.id;
    taskEl.ondragstart = drag;
    
    taskEl.innerHTML = `
        <button class="delete-btn" onclick="deleteTask(${task.id})">✖</button>
        <h3>${task.title}</h3>
        <p>${task.description || ''}</p>
    `;
    
    const container = document.getElementById('tasks-' + task.status);
    if(container) {
        container.appendChild(taskEl);
    }
}

async function createTask() {
    const titleInput = document.getElementById('newTaskTitle');
    const descInput = document.getElementById('newTaskDesc');
    
    const title = titleInput.value.trim();
    const description = descInput.value.trim();
    
    if (!title) {
        alert('El título es obligatorio');
        return;
    }
    
    const task = {
        title,
        description,
        status: 'TODO'
    };
    
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
    });
    
    if (response.ok) {
        const newTask = await response.json();
        renderTask(newTask);
        titleInput.value = '';
        descInput.value = '';
    }
}

async function deleteTask(id) {
    if (confirm('¿Eliminar esta tarea?')) {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        const el = document.getElementById('task-' + id);
        if(el) el.remove();
    }
}

function allowDrop(event) {
    event.preventDefault();
}

function drag(event) {
    event.dataTransfer.setData("taskId", event.target.id.split('-')[1]);
}

async function drop(event) {
    event.preventDefault();
    const taskId = event.dataTransfer.getData("taskId");
    let target = event.target;
    
    // Find column
    while(target && !target.classList.contains('column')) {
        target = target.parentElement;
    }
    
    if(target) {
        const newStatus = target.id;
        
        // Update in backend
        const response = await fetch(`${API_URL}/${taskId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });
        
        if (response.ok) {
            // Update UI
            const taskEl = document.getElementById('task-' + taskId);
            const container = document.getElementById('tasks-' + newStatus);
            container.appendChild(taskEl);
        }
    }
}
