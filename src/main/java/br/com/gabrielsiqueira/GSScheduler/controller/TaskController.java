package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import br.com.gabrielsiqueira.GSScheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<Task> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Authentication authentication) {
        User current = currentUser(authentication);
        return service.listByUser(current, PageRequest.of(page, size, Sort.by("scheduledAt").descending()));
    }

    @PostMapping
    public Task create(@RequestBody Task task, Authentication authentication) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titulo obrigatorio");
        }
        if (task.getStatus() == null || task.getStatus().isBlank()) {
            task.setStatus("PENDING");
        }
        task.setUser(currentUser(authentication));
        return service.save(task);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id, Authentication authentication) {
        Task task = service.findById(id).orElseThrow(() -> new RuntimeException("Tarefa nao encontrada."));
        authorize(task, authentication);
        return task;
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task t, Authentication authentication) {
        Task existing = service.findById(id).orElseThrow(() -> new RuntimeException("Tarefa nao encontrada."));
        authorize(existing, authentication);
        if (t.getTitle() != null) existing.setTitle(t.getTitle());
        if (t.getDescription() != null) existing.setDescription(t.getDescription());
        if (t.getScheduledAt() != null) existing.setScheduledAt(t.getScheduledAt());
        if (t.getStatus() != null) existing.setStatus(t.getStatus());
        return service.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication authentication) {
        Task existing = service.findById(id).orElseThrow(() -> new RuntimeException("Tarefa nao encontrada."));
        authorize(existing, authentication);
        service.delete(id);
    }

    private User currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));
    }

    private void authorize(Task task, Authentication authentication) {
        User current = currentUser(authentication);
        if (task.getUser() == null || !task.getUser().getId().equals(current.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tarefa nao pertence ao usuario");
        }
    }
}
