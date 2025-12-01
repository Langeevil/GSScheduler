package br.com.gabrielsiqueira.GSScheduler.repository;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    Page<Task> findByStatus(String status, Pageable pageable);
    List<Task> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    @Query("select t from Task t where t.user.id = :userId and (lower(t.title) like lower(concat('%', :q, '%')) or lower(t.description) like lower(concat('%', :q, '%')))")
    Page<Task> searchByUser(@Param("userId") Long userId, @Param("q") String query, Pageable pageable);
}
