package com.cts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.model.User; // Corrected: Changed import to your custom User model
import com.cts.repository.UserRepository;
import com.cts.utility.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String authenticate(String username, String rawPassword) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		// Corrected: Changed static calls to instance calls
		return jwtUtil.generateToken(user.getUsername(), user.getRole());
	}

	public void registerUser(User user) {
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		userRepository.save(user);
	}
}
