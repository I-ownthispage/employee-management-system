package com.crudapi.mysqlapi.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.crudapi.mysqlapi.entity.User;
import com.crudapi.mysqlapi.repository.UserRepository;

@Component
public class AdminBootstrap implements ApplicationRunner{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final BootstrapAdminProperties properties;
	
	public AdminBootstrap(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            BootstrapAdminProperties properties) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
    }
	
	@Override
	public void run(ApplicationArguments args) {
		
		if(!properties.isEnabled()) {
			return;
		}
		
		validateConfiguration();
		
		userRepository.findByUsername(properties.getUsername())
				.ifPresentOrElse(
						
						existingUser -> {
							
						if("ADMIN".equals(existingUser.getRole())) {
							existingUser.setRole("SUPER_ADMIN");
							userRepository.save(existingUser);
							
							System.out.println(
									"Existing ADMIN promoted to SUPER_ADMIN: "
									+ properties.getUsername()
							);
						}
						else if(!"SUPER_ADMIN".equals(existingUser.getRole())) {
								throw new IllegalStateException(
										"Bootstrap admin username already exists "
										+ "but is neither ADMIN nor SUPER_ADMIN: "
										+ properties.getUsername()
								);
							}
						},
						
						() -> {
							
							User admin = new User();
							
							admin.setUsername(properties.getUsername());
							
							admin.setPassword(passwordEncoder.encode(properties.getPassword()));
							
							admin.setRole("SUPER_ADMIN");
							
							userRepository.save(admin);
							
							System.out.println(
									"Bootstrap SUPER_ADMIN user created: "
									+ properties.getUsername()
							);
						}
					);
	}
	
	private void validateConfiguration() {
		
		if(properties.getUsername() == null || properties.getUsername().isBlank()) {
			
			throw new IllegalStateException("BOOTSTRAP_ADMIN_USERNAME must be configured");
		}
		
		if(properties.getPassword() == null || properties.getPassword().isBlank()){
			
			throw new IllegalStateException("BOOTSTRAP_ADMIN_PASSWORD must be configured");
		}
	}
}
