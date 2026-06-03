package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll(); // Limpiar base de datos antes de cada prueba
    }

    @Test
    public void testCreateTask() throws Exception {
        Task task = new Task("Aprender JUnit", "Escribir pruebas para el Kanban", "TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Aprender JUnit")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        taskRepository.save(new Task("Tarea 1", "Descripción 1", "IN_PROGRESS"));
        taskRepository.save(new Task("Tarea 2", "Descripción 2", "DONE"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Tarea 1")))
                .andExpect(jsonPath("$[1].status", is("DONE")));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task savedTask = taskRepository.save(new Task("Tarea Original", "Desc", "TODO"));

        Task updatedDetails = new Task();
        updatedDetails.setTitle("Tarea Actualizada");
        updatedDetails.setStatus("IN_PROGRESS");

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Tarea Actualizada")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.description", is("Desc"))); // Mantiene la original
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task savedTask = taskRepository.save(new Task("Tarea Borrar", "Desc", "DONE"));

        mockMvc.perform(delete("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateTaskWithoutStatus() throws Exception {

        Task task = new Task();
        task.setTitle("Nueva tarea");
        task.setDescription("Sin estado");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void testCreateTaskWithEmptyStatus() throws Exception {

        Task task = new Task();
        task.setTitle("Nueva tarea");
        task.setDescription("Sin estado");
        task.setStatus("");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void testUpdateOnlyDescription() throws Exception {

        Task saved = taskRepository.save(
                new Task("Titulo", "Original", "TODO"));

        Task update = new Task();
        update.setDescription("Nueva descripcion");

        mockMvc.perform(put("/api/tasks/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titulo"))
                .andExpect(jsonPath("$.description").value("Nueva descripcion"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void testUpdateOnlyStatus() throws Exception {

        Task saved = taskRepository.save(
                new Task("Titulo", "Desc", "TODO"));

        Task update = new Task();
        update.setStatus("DONE");

        mockMvc.perform(put("/api/tasks/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titulo"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testUpdateTaskNotFound() {
        assertThrows(ServletException.class, () -> {

            Task update = new Task();
            update.setTitle("No existe");

            mockMvc.perform(put("/api/tasks/999999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(update)));

        });
    }

    @Test
    void testDeleteNonExistingTask() throws Exception {

        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllTasksEmpty() throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
