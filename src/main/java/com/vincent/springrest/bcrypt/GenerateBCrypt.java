package com.vincent.springrest.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GenerateBCrypt {
	public static void main(String[] args){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hash = encoder.encode("admin");
		boolean result = encoder.matches("Yuki0715", "$2a$10$aOyZwxGHC08bybnbj58oXOQao5M7zvAJYstcg5VnmBuNhwfsWJyxe "); 
		System.out.println(result);
		System.out.println(hash);
	}
}
