package com.in.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.in.entity.UserDetails;
import com.in.exeption.InvalidOTP;
import com.in.exeption.InvalidUser;
import com.in.exeption.OTPexpired;
import com.in.serviceImpl.TotpImpl;

@RestController
public class TotpController {

	@Autowired
	TotpImpl totpImpl;
	

	@PostMapping("/genrate")
	public ResponseEntity<String> create(@RequestBody String secretKey) {
		if(totpImpl.genrateOtp(secretKey) != null)
			return new ResponseEntity<String>(totpImpl.genrateOtp(secretKey), HttpStatus.OK);
		return new ResponseEntity<String>("Please provide valid input", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/valid/{email}")
	public ResponseEntity<String> validate(@RequestBody String OTP,@PathVariable String email) {
		
		try {
			String s = totpImpl.valid(email,OTP);
			return new ResponseEntity<String>(s, HttpStatus.OK);
			
		} catch (InvalidOTP e) {
			throw new InvalidOTP("please insert a valid OTP.");
		} catch (OTPexpired e) {
			throw new OTPexpired("OTP expired,please login again");
		}
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<UserDetails> createUser(@Valid @RequestBody UserDetails userDetails) {
		try {
			return new ResponseEntity<UserDetails>(totpImpl.createUser(userDetails), HttpStatus.OK);
		}
		catch (HttpMessageNotReadableException e) {
			throw new InvalidUser(e.getLocalizedMessage());
		}
		catch (Exception e) {
			throw new InvalidUser("please provide a valid user");
		}
		
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<UserDetails>> GetAllUsers() {
		
		try {
			List<UserDetails> allUsers = totpImpl.getAllUsers();
			return new ResponseEntity<List<UserDetails>>(allUsers,HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
}
