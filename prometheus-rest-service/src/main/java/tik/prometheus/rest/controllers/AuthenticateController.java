package tik.prometheus.rest.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.AuthenticationManager;

import tik.prometheus.rest.services.impl.JwtService;
import tik.prometheus.rest.dtos.AuthRequest;
import tik.prometheus.rest.repositories.UserRepository;
import tik.prometheus.rest.services.impl.CustomUserDetailsService;

@RestController
@RequestMapping(value = "connect")
public class AuthenticateController {
	
	@Autowired
	private JwtService jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/login")
	public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest, @RequestHeader Map<String, String> headers) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
			);
		} catch (Exception e) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		//Tra ve access token
		return new ResponseEntity<Object>(new Object() {
						public String token = jwtUtil.generateToken(authRequest.getUsername(), headers.get("origin"));
						public String refreshToken = "";
						}, HttpStatus.OK);
	}
	
}
