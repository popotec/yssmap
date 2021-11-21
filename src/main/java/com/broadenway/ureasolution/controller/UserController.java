package com.broadenway.ureasolution.controller;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.broadenway.ureasolution.domain.User;
import com.broadenway.ureasolution.dto.UserResponse;
import com.broadenway.ureasolution.dto.UserRequest;
import com.broadenway.ureasolution.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller()
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
	// 	UserResponse user = userService.saveUser(userRequest);
	// 	return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
	// }
	//
	// @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<List<UserResponse>> getUsers() {
	// 	List<UserResponse> users = userService.findAll();
	// 	return ResponseEntity.ok().body(users);
	// }
	//
	// @GetMapping(path="searchByName",produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<List<UserResponse>> getUsersByName(@RequestParam("name") String name) {
	// 	List<UserResponse> users = userService.findUsersByName(name);
	// 	return ResponseEntity.ok().body(users);
	// }
	//
	// @GetMapping(path="searchByEmployeeId",produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<UserResponse> getUsersByEmployeeId(@RequestParam("employeeId") int employeeId) {
	// 	UserResponse user = userService.findUsersByEmployeeId(employeeId);
	// 	return ResponseEntity.ok().body(user);
	// }

	@RequestMapping()
	public ModelAndView getUsers() {
		List<UserResponse> users = userService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", users);
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@GetMapping(value = "/new")
	public ModelAndView createForm() {
		List<UserResponse> users = userService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", users);
		modelAndView.setViewName("register");
		return modelAndView;
	}

	@PostMapping(value = "/new")
	public String create(UserRequest userRequest) {
		validateNotExistIP(userRequest);
		UserResponse user = userService.saveUser(userRequest);
		return "redirect:/users";
	}

	@GetMapping(value = "/new/excel-upload")
	public ModelAndView createExcelUploadForm() {
		List<UserResponse> users = userService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", users);
		modelAndView.setViewName("register-with-excel");
		return modelAndView;
	}

	@PostMapping(value = "/new/excel-upload")
	public String createUsersWithExcel(@RequestParam("file") MultipartFile file, Model model) {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3
		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IllegalArgumentException("엑셀파일만 업로드 해주세요.");
		}
		userService.saveUsersFromExcel(file);
		return "redirect:/users";
	}

	@PutMapping(value = "/{employeeId}")
	public String update(@PathVariable int employeeId, UserRequest userRequest) {
		validateNotExistIP(userRequest);
		userService.updateUserByEmployeeId(employeeId,userRequest);
		return "redirect:/users";
	}

	private void validateNotExistIP(UserRequest userRequest) {
	}

	@GetMapping(value = "/{employeeId}")
	public ModelAndView getUser(@PathVariable int employeeId) {
		User user = userService.findUserByEmployeeId(employeeId);
		UserResponse userResponse = UserResponse.from(user);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", userResponse);
		modelAndView.setViewName("user-detail");

		return modelAndView;
	}
}
