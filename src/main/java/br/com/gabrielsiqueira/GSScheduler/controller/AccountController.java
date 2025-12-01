package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/profile")
    public ProfileResponse profile(Authentication authentication) {
        User user = currentUser(authentication);
        return ProfileResponse.from(user);
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(@RequestBody ProfileRequest request, Authentication authentication) {
        User user = currentUser(authentication);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return ProfileResponse.from(user);
    }

    @PostMapping("/password")
    public Map<String, String> changePassword(@RequestBody PasswordChangeRequest request, Authentication authentication) {
        if (request == null || isBlank(request.getCurrentPassword()) || isBlank(request.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual e nova senha são obrigatórias");
        }
        if (request.getNewPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha deve ter pelo menos 6 caracteres");
        }
        User user = currentUser(authentication);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return Map.of("message", "Senha alterada");
    }

    @PostMapping("/logout-all")
    public Map<String, String> logoutAll(Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof UserDetails)
                .map(UserDetails.class::cast)
                .filter(details -> Objects.equals(details.getUsername(), username))
                .forEach(details -> sessionRegistry.getAllSessions(details, false)
                        .forEach(this::expireSession));

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return Map.of("message", "Sessões encerradas");
    }

    private void expireSession(SessionInformation info) {
        info.expireNow();
        sessionRegistry.removeSessionInformation(info.getSessionId());
    }

    private User currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class ProfileRequest {
        private String firstName;
        private String lastName;
        private String email;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ProfileResponse {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String roles;

        public static ProfileResponse from(User user) {
            ProfileResponse r = new ProfileResponse();
            r.username = user.getUsername();
            r.firstName = user.getFirstName();
            r.lastName = user.getLastName();
            r.email = user.getEmail();
            r.roles = user.getRoles();
            return r;
        }

        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getRoles() { return roles; }
    }
}
