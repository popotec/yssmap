package com.broadenway.ureasolution.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.broadenway.ureasolution.dto.UserResponse;
import com.broadenway.ureasolution.dto.UserRequest;
import com.broadenway.ureasolution.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerAPI {

	private final UserService userService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
		UserResponse user = userService.saveUser(userRequest);
		return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getUsers() {
		List<UserResponse> users = userService.findAll();
		return ResponseEntity.ok().body(users);
	}

	@GetMapping(path="searchByName",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getUsersByName(@RequestParam("name") String name) {
		List<UserResponse> users = userService.findUsersByName(name);
		return ResponseEntity.ok().body(users);
	}

	// @GetMapping(path="searchByEmployeeId",produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<UserResponse> getUsersByEmployeeId(@RequestParam("employeeId") int employeeId) {
	// 	UserResponse user = userService.findUserByEmployeeId(employeeId);
	// 	return ResponseEntity.ok().body(user);
	// }

	@GetMapping(path="test")
	public String test() {
		// List<UserResponse> users = userService.findUsersByEmployeeId(employeeId);
		return "hello";
	}
}
