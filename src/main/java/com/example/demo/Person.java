package com.example.demo;

public abstract class Person {
	
	protected String name;
	protected int age = 0;
	protected String phoneNumber;
	protected String emailAddress;
	protected String address = "";

	public enum Gender {
		Male, Female
	};

	private Gender g;

	public Person(String name, int age, String phoneNumber, String emailAddress, Gender g, String address) {
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.g = g;
		this.address = address;
	}
	
	public Gender getGender() {
		return g;
	} 
	
	public void setGender(Gender g) {
		this.g = g;
	} 
	
	abstract void greet();

}

