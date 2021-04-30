package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
	
	// create global variable for name list
		List<String> nameList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	// return hello + name per localhost:8080/hello
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    
    // return name list per localhost:8080/person
    @GetMapping("/person")
    public List<String> personList() {
        return nameList;
    }
    
    // return the name of an id per localhost:8080/person/id
    @GetMapping("/person/id")
    public String personName(@RequestParam(value = "id") int id) {
    	String name = "";
    	name = nameList.get(id);
    	return name;
    }

    // greet a person 
    @GetMapping("/person/id/hello")
    public String greetPerson(@RequestParam(value = "id") int id) {
    	String name = "";
    	name = nameList.get(id);
    	return String.format("Hello %s!", name);
    }
    
    // create resources
    @PostMapping(path = "/person", consumes = "application/json")
    public int createPerson(@RequestBody String name) {
    	if(!(name instanceof String) || name.length() < 2) {
    		System.out.println("Error: invalid name");
    		throw new WrongNameException();
    	} else {
    		nameList.add(name);
    		int id = nameList.indexOf(name);
    		return id;
    	}
    }
    
    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="Wrong name") 
    public class WrongNameException extends RuntimeException {
        
    }

}
