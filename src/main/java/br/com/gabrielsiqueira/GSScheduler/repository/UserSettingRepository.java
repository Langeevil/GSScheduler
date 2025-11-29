package br.com.gabrielsiqueira.GSScheduler.repository;

import br.com.gabrielsiqueira.GSScheduler.model.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    Optional<UserSetting> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
