package br.com.gabrielsiqueira.GSScheduler.repository;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    Page<Task> findByStatus(String status, Pageable pageable);
    List<Task> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
