package view;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class LoginTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 System.setProperty("webdriver.gecko.driver", "C:/Users/coolb/geckodriver.exe");
		  FirefoxOptions capabilities = new FirefoxOptions();  
//		  Map<String, Boolean> map = new HashMap<>();
//		  map.put("marionette",true);
		  capabilities.setCapability("marionette",true);  
		  WebDriver driver = new FirefoxDriver(capabilities);
		  
		  driver.navigate().to("http://localhost:8080/shop/shopView.xhtml");
//	    driver.get("http://localhost:8080/shop/shopView.xhtml");
	    // 2 | setWindowSize | 1728x1157 | 
	    driver.manage().window().setSize(new Dimension(1728, 1157));
	    // 3 | click | linkText=Einloggen | 
	    driver.findElement(By.linkText("Einloggen")).click();
	    // 4 | click | id=input_loginForm:username | 
	    driver.findElement(By.id("input_loginForm:username")).click();
	    // 5 | type | id=input_loginForm:password | ben
	    driver.findElement(By.id("input_loginForm:password")).sendKeys("ben");
	    // 6 | type | id=input_loginForm:username | ben
	    driver.findElement(By.id("input_loginForm:username")).sendKeys("ben");
	    // 7 | click | id=loginForm:j_idt39 | 
	    driver.findElement(By.id("loginForm:j_idt39")).click();
	    // 8 | click | id=dtLj_idt7:j_idt19 | 
	    driver.findElement(By.id("dtLj_idt7:j_idt19")).click();
	    // 9 | click | linkText=Abmelden | 
	    driver.findElement(By.linkText("Abmelden")).click();
	}

}
