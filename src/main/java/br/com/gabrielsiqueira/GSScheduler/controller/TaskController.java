package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import br.com.gabrielsiqueira.GSScheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping
    public Page<Task> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        return service.listAll(PageRequest.of(page, size, Sort.by("scheduledAt").descending()));
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        return service.save(task);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada."));
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task t) {
        Task existing = service.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada."));
        if (t.getTitle() != null) existing.setTitle(t.getTitle());
        if (t.getDescription() != null) existing.setDescription(t.getDescription());
        if (t.getScheduledAt() != null) existing.setScheduledAt(t.getScheduledAt());
        if (t.getStatus() != null) existing.setStatus(t.getStatus());
        return service.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
