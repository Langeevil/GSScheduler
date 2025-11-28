package br.com.gabrielsiqueira.GSScheduler.service;

import br.com.gabrielsiqueira.GSScheduler.model.User;
import br.com.gabrielsiqueira.GSScheduler.model.UserSetting;
import br.com.gabrielsiqueira.GSScheduler.repository.UserSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserSettingService {

    @Autowired
    private UserSettingRepository repo;

    public UserSetting findOrCreate(User user) {
        return repo.findByUserId(user.getId()).orElseGet(() -> {
            UserSetting s = new UserSetting();
            s.setUser(user);
            return repo.save(s);
        });
    }

    public UserSetting save(UserSetting s) {
        return repo.save(s);
    }
}
