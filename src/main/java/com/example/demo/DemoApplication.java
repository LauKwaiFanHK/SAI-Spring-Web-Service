package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
	
	// create global variable for name list
		List<String> nameList = new ArrayList<>();
		
		List<Student> studentList = new ArrayList<>();
		
		List<Professor> professorList = new ArrayList<>();
		

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
    public int createStudent(@RequestParam(value = "name") String name, @RequestParam(value = "age") int age, String phoneNumber, String emailAddress, String address) {
    	if(!(name instanceof String) || name.length() < 2) {
    		System.out.println("Error: invalid name");
    		throw new WrongNameException();
    	} else {
    		Student student = new Student(name, age, phoneNumber, emailAddress, address);
    		
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
    		studentList.add(student);
    		System.out.println("Student is added to database successfully.");
    		return id;
    	}
    }

    @GetMapping("/students/all")
    public List<Student> showStudentList() {
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
    	
        CriteriaBuilder cb = session.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> rootEntry = cq.from(Student.class);
        javax.persistence.criteria.CriteriaQuery<Student> all = cq.select(rootEntry);

        TypedQuery<Student> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }
    
    @GetMapping("/students/id")
    public String showStudent(@RequestParam(value = "id") int id) {
    		
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
   		session.beginTransaction();
   		System.out.println("connected? " + session.isConnected());
   		Student student = session.load(Student.class, id);

		// Serialisation 
    	String studentObjectMappedToJSONString = null;
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		try {
			studentObjectMappedToJSONString = om.writeValueAsString(student);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return studentObjectMappedToJSONString;
    }
    
    @PutMapping("/students/update_address")
    public void updateStudent(@RequestParam(value = "address") String address, @RequestParam(value = "id") int studentNumber ){
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
        Transaction tx = null;
        try{
           tx = session.beginTransaction();
           Student student = (Student)session.get(Student.class, studentNumber); 
           student.setAddress(address);
           session.update(student);
           tx.commit();
        }catch (HibernateException e) {
           if (tx!=null) tx.rollback();
           e.printStackTrace(); 
        }finally {
           session.close(); 
        }
     }
    
    @PostMapping(path = "/professors")
    public int createProfessor(@RequestParam(value = "name") String name, @RequestParam(value = "salary") double salary, String phoneNumber, String emailAddress) {
    	if(!(name instanceof String) || name.length() < 2) {
    		System.out.println("Error: invalid name");
    		throw new WrongNameException();
    	} else {
    		Professor professor = new Professor(name, salary, phoneNumber, emailAddress);
    		
    		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    		Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    		SessionFactory factory = meta.getSessionFactoryBuilder().build();
    		org.hibernate.Session session = factory.openSession();
    		session.beginTransaction();
    		session.persist(professor); 
    		System.out.println("connected? " + session.isConnected());
    		session.flush();
    		session.close();
    		factory.close();
    		
    		int id = professor.getProfessorID();
    		professorList.add(professor);
    		System.out.println("Professor is added to database successfully.");
    		return id;
    	}
    }
    
    @GetMapping("/professors/all")
    public List<Professor> showProfessorList() {
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
    	
        CriteriaBuilder cb = session.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Professor> cq = cb.createQuery(Professor.class);
        Root<Professor> rootEntry = cq.from(Professor.class);
        javax.persistence.criteria.CriteriaQuery<Professor> all = cq.select(rootEntry);

        TypedQuery<Professor> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }
    
    @GetMapping("/professor/id")
    public String showProfessor(@RequestParam(value = "id") int id) {
    		
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
   		session.beginTransaction();
   		System.out.println("connected? " + session.isConnected());
   		Professor professor = session.load(Professor.class, id);

		// Serialisation 
    	String professorObjectMappedToJSONString = null;
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		try {
			professorObjectMappedToJSONString = om.writeValueAsString(professor);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return professorObjectMappedToJSONString;
    }
    
    @PutMapping("/professor/update_email")
    public void updateProfessorEmail(@RequestParam(value = "email") String emailAddress, @RequestParam(value = "id") int professorID ){
    	StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
				
    	Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

    	SessionFactory factory = meta.getSessionFactoryBuilder().build();
    	org.hibernate.Session session = factory.openSession();
        Transaction tx = null;
        try{
           tx = session.beginTransaction();
           Professor professor = (Professor)session.get(Professor.class, professorID); 
           professor.setEmailAddress(emailAddress);
           session.update(professor);
           tx.commit();
        }catch (HibernateException e) {
           if (tx!=null) tx.rollback();
           e.printStackTrace(); 
        }finally {
           session.close(); 
        }
     }

}
