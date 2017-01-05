package com.vincent.springrest.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GenerateBCrypt {
	public static void main(String[] args){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hash = encoder.encode("Passw0rd");
		System.out.println(hash);
	}
}
