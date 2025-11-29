package br.com.gabrielsiqueira.GSScheduler.controller;

import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.model.UserSetting;
import br.com.gabrielsiqueira.GSScheduler.repository.UserRepository;
import br.com.gabrielsiqueira.GSScheduler.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private UserSettingService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public UserSetting get(Authentication authentication) {
        User current = currentUser(authentication);
        return service.findOrCreate(current);
    }

    @PutMapping
    public UserSetting update(@RequestBody UserSetting payload, Authentication authentication) {
        User current = currentUser(authentication);
        UserSetting settings = service.findOrCreate(current);
        if (payload.getTheme() != null) settings.setTheme(payload.getTheme());
        if (payload.getLanguage() != null) settings.setLanguage(payload.getLanguage());
        settings.setIntervalHours(payload.getIntervalHours());
        settings.setRetentionDays(payload.getRetentionDays());
        settings.setTwoFactorEnabled(payload.isTwoFactorEnabled());
        return service.save(settings);
    }

    private User currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));
    }
}
