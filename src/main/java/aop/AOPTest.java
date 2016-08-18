package aop;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import common.junit.SpringJunitTest;

public class AOPTest extends SpringJunitTest  {  

	@Autowired
    PersonService personService;  
	
/*	@Before
	public void init(){
		BeanFactory cxt = new ClassPathXmlApplicationContext("classpath*:application-context.xm");
		personService = (PersonService) cxt.getBean("personService");
	}*/

    @Test  
    public void saveTest() {  
        personService.save(new Person());  
    }  
}  
