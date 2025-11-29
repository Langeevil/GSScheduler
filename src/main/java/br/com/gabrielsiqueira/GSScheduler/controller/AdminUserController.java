package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import br.com.gabrielsiqueira.GSScheduler.service.TaskService;
import br.com.gabrielsiqueira.GSScheduler.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserSettingService userSettingService;

    @GetMapping("/admin/users")
    public String usersPage() {
        return "admin/users";
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public List<UserView> list() {
        return userRepository.findAll().stream().map(UserView::from).toList();
    }

    @PutMapping("/api/admin/users/{id}")
    @ResponseBody
    public UserView update(@PathVariable Long id, @RequestBody User payload) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
        if (payload.getFirstName() != null) user.setFirstName(payload.getFirstName());
        if (payload.getLastName() != null) user.setLastName(payload.getLastName());
        if (payload.getEmail() != null) user.setEmail(payload.getEmail());
        if (payload.getRoles() != null) user.setRoles(payload.getRoles());
        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(payload.getPassword()));
        }
        return UserView.from(userRepository.save(user));
    }

    @DeleteMapping("/api/admin/users/{id}")
    @ResponseBody
    @Transactional
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            taskService.deleteByUser(user);
            userSettingService.deleteByUser(user);
            userRepository.delete(user);
        });
    }

    public static class UserView {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String roles;

        public static UserView from(User u) {
            UserView v = new UserView();
            v.id = u.getId();
            v.username = u.getUsername();
            v.firstName = u.getFirstName();
            v.lastName = u.getLastName();
            v.email = u.getEmail();
            v.roles = u.getRoles();
            return v;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getRoles() { return roles; }
    }
}
