package br.com.gabrielsiqueira.GSScheduler.service;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository repo;

    public Page<Task> listAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Task> listByUser(User user, Pageable pageable) {
        return repo.findByUserId(user.getId(), pageable);
    }

    public java.util.List<Task> listAllByUser(User user) {
        return repo.findByUserId(user.getId());
    }

    public Page<Task> listByStatus(String status, Pageable pageable) {
        return repo.findByStatus(status, pageable);
    }

    @Async("taskExecutor")
    public CompletableFuture<Task> createAsync(Task task) {
        return CompletableFuture.completedFuture(repo.save(task));
    }

    public Task save(Task t) {
        return repo.save(t);
    }

    public Optional<Task> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void deleteByUser(User user) {
        repo.deleteByUserId(user.getId());
    }
}
