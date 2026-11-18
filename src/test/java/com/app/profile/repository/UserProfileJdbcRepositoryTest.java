package com.app.profile.repository;

import com.app.profile.entity.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserProfileJdbcRepositoryTest {

    @Autowired
    private UserProfileRepository profileRepository;

    @Test
    void shouldPersistEachRequestCountIncrement() {
        UserProfile profile = profileRepository.save(
                new UserProfile("counter-user", "counter-user@regimenu.com")
        );

        profileRepository.incrementRequestCount(profile.getId(), 3);
        profileRepository.incrementRequestCount(profile.getId(), 1);

        UserProfile reloaded = profileRepository.findById(profile.getId()).orElseThrow();

        assertThat(reloaded.getRequestCount()).isEqualTo(4);
    }
}
