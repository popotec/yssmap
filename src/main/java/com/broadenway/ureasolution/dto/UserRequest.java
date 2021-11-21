package com.broadenway.ureasolution.dto;

import com.broadenway.ureasolution.domain.User;

import lombok.Data;

@Data
public class UserRequest {
	private Long id;

	private String name;

	private int employeeId;

	private String ip1;

	private String ip2;

	private String ip3;

	private UserRequest() {
	}

	public User toUser() {
		return new User(id,name,employeeId,ip1,ip2,ip3);
	}
}
