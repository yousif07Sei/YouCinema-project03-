package com.ga.YouCINEMA.seeder;

import com.ga.YouCINEMA.enums.UserRole;
import com.ga.YouCINEMA.enums.UserStatus;
import com.ga.YouCINEMA.model.User;
import com.ga.YouCINEMA.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserSeeder {

    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seed() {
        logger.info("🎬 Seeding users...");

        if (userRepository.count() > 0) {
            logger.info("⏭️  Users already exist (count: {}), skipping", userRepository.count());
            return;
        }

        // Admin
        userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("YouCINEMA")
                .email("admin@youcinema.com")
                .password(passwordEncoder.encode("Admin1234!"))
                .role(UserRole.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build());

        // Customer 1
        userRepository.save(User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@youcinema.com")
                .password(passwordEncoder.encode("John1234!"))
                .role(UserRole.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build());

        // Customer 2
        userRepository.save(User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@youcinema.com")
                .password(passwordEncoder.encode("Jane1234!"))
                .role(UserRole.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .build());

        logger.info("✅ Created 3 users (1 admin, 2 customers)");
        logger.info("   admin@youcinema.com / Admin1234!");
        logger.info("   john@youcinema.com  / John1234!");
        logger.info("   jane@youcinema.com  / Jane1234!");
    }
}
