package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.Task;
import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.model.UserSetting;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import br.com.gabrielsiqueira.GSScheduler.service.TaskService;
import br.com.gabrielsiqueira.GSScheduler.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingService userSettingService;

    @GetMapping("/summary")
    public ReportSummary summary(Authentication authentication) {
        User user = currentUser(authentication);
        UserSetting setting = userSettingService.findOrCreate(user);

        List<Task> tasks = taskService.listAllByUser(user);
        LocalDateTime cutoff = setting.getRetentionDays() > 0
                ? LocalDateTime.now().minusDays(setting.getRetentionDays())
                : null;
        final LocalDateTime finalCutoff = cutoff;
        if (finalCutoff != null) {
            tasks = tasks.stream()
                    .filter(t -> {
                        LocalDateTime ref = t.getScheduledAt() != null ? t.getScheduledAt() : t.getCreatedAt();
                        return ref == null || !ref.isBefore(finalCutoff);
                    })
                    .toList();
        }

        Map<String, Long> byStatus = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        List<Task> recent = tasks.stream()
                .sorted(Comparator.comparing(Task::getScheduledAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();

        ReportSummary summary = new ReportSummary();
        summary.setTotal(tasks.size());
        summary.setCompleted(byStatus.getOrDefault("COMPLETED", 0L));
        summary.setPending(byStatus.getOrDefault("PENDING", 0L));
        summary.setInProgress(byStatus.getOrDefault("IN_PROGRESS", 0L));
        summary.setByStatus(byStatus);
        summary.setRecent(recent);
        return summary;
    }

    private User currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));
    }

    public static class ReportSummary {
        private long total;
        private long completed;
        private long pending;
        private long inProgress;
        private Map<String, Long> byStatus;
        private List<Task> recent;

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getCompleted() { return completed; }
        public void setCompleted(long completed) { this.completed = completed; }
        public long getPending() { return pending; }
        public void setPending(long pending) { this.pending = pending; }
        public long getInProgress() { return inProgress; }
        public void setInProgress(long inProgress) { this.inProgress = inProgress; }
        public Map<String, Long> getByStatus() { return byStatus; }
        public void setByStatus(Map<String, Long> byStatus) { this.byStatus = byStatus; }
        public List<Task> getRecent() { return recent; }
        public void setRecent(List<Task> recent) { this.recent = recent; }
    }
}
