package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    @GetMapping("/tasks")
    public String tasks() {
        return "task/tasks";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings/settings";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports/reports";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirm,
                           Model model) {
        if (!password.equals(confirm)) {
            model.addAttribute("error", "Senhas nao conferem");
            return "auth/register";
        }
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Usuario ja existe");
            return "auth/register";
        }
        User user = new User(username, passwordEncoder.encode(password), true, "ROLE_USER");
        userRepository.save(user);
        model.addAttribute("success", "Conta criada com sucesso. Acesse o login.");
        return "auth/register";
    }
}
