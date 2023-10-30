package com.springJWTProject.springSecurity;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springJWTProject.springSecurity.entities.User;
import com.springJWTProject.springSecurity.enums.Role;
import com.springJWTProject.springSecurity.repository.UserRepository;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner{


	@Autowired
	private UserRepository userRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		User adminUser = userRepo.findByRole(Role.ADMIN);
		
		if(adminUser == null) {
			User user = new User();
			
			user.setEmail("sabuj@gmail.com");
			user.setFirstName("sabuj");
			user.setLastName("kumar"); 
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("abcd"));
			userRepo.save(user);
		}
		
	}
}
