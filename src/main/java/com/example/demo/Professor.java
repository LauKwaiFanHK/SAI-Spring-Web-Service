package com.example.demo;

import java.util.ArrayList;

class Professor extends Person {
	private ArrayList<Student> listOfStudents = new ArrayList<Student>();
	private double salary;
	
	public Professor(String name, int age, String phoneNumber, String emailAddress, Gender g, double salary, ArrayList<Student> listOfStudents, String address) {
		super(name, age, phoneNumber, emailAddress, g, address);
		this.salary = salary;
		this.listOfStudents = listOfStudents;
	}
	
	public String getProfName() {
		return name;
	}
	
	public void setProfName(String name) {
		this.name = name;
	}
	
	public int getProfAge() {
		return age;
	}
	
	public void setProfAge(int age) {
		this.age = age;
	}
	
	public String getProfPhone() {
		return phoneNumber;
	}
	
	public void setProfPhone(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getProfEmail() {
		return emailAddress;
	}
	
	public void setProfEmail(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getProfAddress() {
		return address;
	}
	
	public void setProfAddress(String address) {
		this.address = address;
	}
	
	public String getStudentName() {
		return name;
	}
	
	public double getSalary() {
		return salary;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public ArrayList<Student> getList(){
		return listOfStudents;
	}

	public void greet() {
		switch (getGender().toString()) {
		case "Male":
			System.out.println("Hallo! Mein Name ist " + name + " und ich bin ein Professor.");
			break;
		case "Female":
			System.out.println("Hallo! Mein Name ist " + name + " und ich bin eine Professorin.");
			break;
		}
	}


}
