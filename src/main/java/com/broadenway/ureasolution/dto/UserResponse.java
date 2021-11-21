package com.broadenway.ureasolution.dto;

import com.broadenway.ureasolution.domain.User;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;

	private String name;

	private int employeeId;

	private String ip1;

	private String ip2;

	private String ip3;

	private UserResponse() {
	}

	public UserResponse(Long id, String name, int employeeId, String ip1, String ip2, String ip3) {
		this.id = id;
		this.name = name;
		this.employeeId = employeeId;
		this.ip1 = ip1;
		this.ip2 = ip2;
		this.ip3 = ip3;
	}

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmployeeId(), user.getIp1(),
			user.getIp2(), user.getIp3());
	}
}
