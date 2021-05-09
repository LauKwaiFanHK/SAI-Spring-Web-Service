package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// add annotations
@Entity
@Table(name="students")
class Student {
	
	//primary key
	@Id
	@Column(name="studentNumber")
	@GeneratedValue(strategy=GenerationType.AUTO) // generate auto incremental value
	private int studentNumber;
	
	@Column(name="name")
	private String name;
	
	@Column(name="age")
	private int age;
	
	
	@Column(name="phoneNumber")
	private String phoneNumber;
	
	@Column(name="email")
	private String emailAddress;
	
	@Column(name="address")
	private String address; 
	
	public int getStudentNumber() {
		return studentNumber;
	}
	
	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}
	
	public Student() {}
	
	public Student(String name, int age, String phoneNumber, String emailAddress,
			String address) {
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.address = address;
	}

	public String getStudentName() {
		return name;
	}
	
	public void setStudentName(String name) {
		this.name = name;
	}
	
	public int getStudentAge() {
		return age;
	}
	
	public void setStudentAge(int age) {
		this.age = age;
	}
	
	public String getStudentPhoneNo() {
		return phoneNumber;
	}
	
	public void setStudentPhoneNo(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getStudentEmail() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress ;
	}
	
	public String getStudentAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address ;
	}
	
}
