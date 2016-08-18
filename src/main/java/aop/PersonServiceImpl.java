package aop;

import org.springframework.stereotype.Service;

@Service(value="personService")
public class PersonServiceImpl implements PersonService {  
    private String user;  
  
    public String getUser() {  
        return user;  
    }  
  
    public void setUser(String user) {  
        this.user = user;  
    }  
  
    public PersonServiceImpl() {  
    }  
  
    public PersonServiceImpl(String user) {  
        super();  
        this.user = user;  
    }  
  
    public void save(Person person) {  
        System.out.println("执行PerServiceBean的save方法");  
    }  
} 