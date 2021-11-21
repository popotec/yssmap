package com.broadenway.ureasolution.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.broadenway.ureasolution.domain.User;
import com.broadenway.ureasolution.dto.UserResponse;
import com.broadenway.ureasolution.repository.UserRepository;
import com.broadenway.ureasolution.utils.ExcelStoreService;
import com.broadenway.ureasolution.dto.UserRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ExcelStoreService excelStoreService;

	@Transactional
	public UserResponse saveUser(UserRequest request) {
		validataIP(request);
		validateNotExistUser(request.getEmployeeId());
		User user = userRepository.save(request.toUser());
		return UserResponse.from(user);
	}

	//
	// public List<UserResponse> findUsersByCondition(String name, String employeeId) {
	// 	if (isEmptyString(name) && isEmptyString(employeeId)) {
	// 		return findAll();
	// 	}
	// 	if (!isEmptyString(name) && !isEmptyString(employeeId)) {
	// 		throw new IllegalArgumentException("이름이나 사번 둘 중 하나의 조건으로만 조회해주세요.");
	// 	}
	// 	if (isEmptyString(name)) {
	// 		return findUsersByName(name);
	// 	}
	//
	// 	int id = convertToNumberFormat(employeeId);
	// 	return findUserByEmployeeId(id);
	// }

	private int convertToNumberFormat(String employeeId) {
		try {
			return Integer.parseInt(employeeId);
		} catch (NumberFormatException ex) {
			throw new NumberFormatException("사번 형식이 잘못되었습니다.");
		}
	}

	private boolean isEmptyString(String name) {
		if (name == null || name.equals("")) {
			return true;
		}
		return false;
	}

	public List<UserResponse> findAll() {
		List<User> users = userRepository.findAll();
		return users.stream().map(UserResponse::from).collect(Collectors.toList());
	}

	public List<UserResponse> findUsersByName(String name) {
		List<User> users = userRepository.findByName(name);
		return users.stream().map(UserResponse::from).collect(Collectors.toList());
	}

	public User findUserByEmployeeId(int employeeId) {
		return userRepository.findByEmployeeId(employeeId);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 접근입니다."));
	}

	public void updateUser(UserRequest request) {
		User findUser = getUserById(request.getId());
		findUser.changeInfo(request.toUser());
	}

	public void deleteUser(long id) {
		User findUser = getUserById(id);
		findUser.delete();
	}

	private void validateNotUsedIp(UserRequest request) {
	}

	private void validateIPFormat(UserRequest request) {
	}


	private void validataIP(UserRequest request) {
		validateNotUsedIp(request);
		validateIPFormat(request);
	}

	private void validateNotExistUser(int employeeId) {
		if(findUserByEmployeeId(employeeId)!=null){
			throw new IllegalArgumentException("이미 등록된 사용자 입니다.");
		}
	}
	@Transactional
	public void saveUsersFromExcel(MultipartFile file) {
		List<User> users = excelStoreService.readDatas(file);
		for (User user : users) {
			User findUser = findUserByEmployeeId(user.getEmployeeId());
			saveUser(findUser, user);
		}
	}

	private void saveUser(User findUser, User user) {
		if(findUser == null){
			userRepository.save(user);
		}else{
			findUser.changeInfo(user);
		}
	}

	@Transactional
	public void updateUserByEmployeeId(int employeeId, UserRequest userRequest) {
		User findUser = findUserByEmployeeId(employeeId);
		findUser.changeInfo(userRequest.toUser());
	}
}
