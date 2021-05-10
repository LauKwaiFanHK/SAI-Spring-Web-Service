package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="professors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
class Professor {
	
	//primary key
	@Id
	@Column(name="professionID")
	@GeneratedValue(strategy=GenerationType.AUTO) // generate auto incremental value
	private int professionID;
	
	@Column(name="name")
	private String name;
	
	@Column(name="salary")
	private double salary;
	
	@Column(name="phoneNumber")
	private String phoneNumber;
	
	@Column(name="email")
	private String emailAddress;
	
	public Professor() {}
	
	public Professor(String name, double salary, String phoneNumber, String emailAddress) {
		this.name = name;
		this.salary = salary;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	public int getProfessionID() {
		return professionID;
	}

	public void setProfessionID(int professionID) {
		this.professionID = professionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}
	
	public double getSalary() {
		return salary;
	}

}
