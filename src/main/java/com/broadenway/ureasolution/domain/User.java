package com.broadenway.ureasolution.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private int employeeId;

	@Column(nullable = false)
	private String name;

	private String ip1;

	private String ip2;

	private String ip3;

	private boolean deleted = false;

	protected User() {
	}

	public User(String name, int employeeId) {
		this.name = name;
		this.employeeId = employeeId;
		this.deleted = false;
	}

	public User(String name, int employeeId, String ip1, String ip2, String ip3) {
		this.name = name;
		this.employeeId = employeeId;
		this.ip1 = ip1;
		this.ip2 = ip2;
		this.ip3 = ip3;
		this.deleted = false;
	}

	public User(Long id, String name, int employeeId,  String ip1, String ip2, String ip3) {
		this.id = id;
		this.employeeId = employeeId;
		this.name = name;
		this.ip1 = ip1;
		this.ip2 = ip2;
		this.ip3 = ip3;
	}

	public void changeIp1(String ip1) {
		this.ip1 = ip1;
	}

	public void changeIp2(String ip2) {
		this.ip2 = ip2;
	}

	public void changeIp3(String ip3) {
		this.ip3 = ip3;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void delete(){
		this.deleted=true;
	}

	public void changeInfo(User user) {
		changeName(user.getName());
		changeIp1(user.getIp1());
		changeIp2(user.getIp2());
		changeIp3(user.getIp3());
	}
}
