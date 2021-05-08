package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hsqldb.Session;
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
		
		List<String> studentList = new ArrayList<>();
		

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
    
    @PostMapping(path = "/students")
    public int createStudent(@RequestParam(value = "name") /*double studentNumber,*/ String name, @RequestParam(value = "age") int age/*, String phoneNumber, String emailAddress, String address*/) {
    	if(!(name instanceof String) || name.length() < 2) {
    		System.out.println("Error: invalid name");
    		throw new WrongNameException();
    	} else {
    		Student student = new Student(name, age /*, phoneNumber, emailAddress, address*/);
    		
    		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    		Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    		SessionFactory factory = meta.getSessionFactoryBuilder().build();
    		org.hibernate.Session session = factory.openSession();
    		session.beginTransaction();
    		session.persist(student); 
    		System.out.println("connected? " + session.isConnected());
    		session.flush();
    		session.close();
    		factory.close();
    		
    		int id = student.getStudentNumber();
    		System.out.println("Student is added to database successfully.");
    		return id;
    	}
    }
    
    @GetMapping("/students")
    public String viewStudents() {
    	ArrayList<Student> students = new ArrayList<>();
    	/*Student student = new Student(100001, "Willy Wonka", 20, "134657685", "wd@gmail.com", "Hauptstrasse 20, Berlin, 10254");
    	Student student2 = new Student(100002, "Billy Jai", 19, "123562685", "bj@gmail.com", "Turmstrasse 13, Berlin, 10589");
    	students.add(student);
    	students.add(student2);*/
    	
		// Serialisation 
		ObjectMapper om = new ObjectMapper();
		String studentObjectMappedToJSONString = "";
		try {
			studentObjectMappedToJSONString = om.writeValueAsString(students);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return studentObjectMappedToJSONString;
		
    }

}
