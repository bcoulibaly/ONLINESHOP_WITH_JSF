package view;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class LoginPageTest {
  private WebDriver driver;
  @Before
  public void setUp() throws MalformedURLException {
	  System.setProperty("webdriver.gecko.driver", "C:/Users/coolb/geckodriver.exe");
	  FirefoxOptions capabilities = new FirefoxOptions();  
	  capabilities.setCapability("marionette",true);  
	  driver = new FirefoxDriver(capabilities);
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void loginTest() {
	  
    // Step # | name | target | value
	  
    // 1 | open | /shop/shopView.xhtml | 
	 driver.navigate().to("http://localhost:8080/shop/shopView.xhtml");
	 
    // 2 | setWindowSize | 1728x1157 | 
    driver.manage().window().setSize(new Dimension(1728, 1157));
    
    // 3 | click | linkText=Einloggen | 
    driver.findElement(By.linkText("Einloggen")).click();
    
    // 4 | click | id=input_loginForm:username | 
    driver.findElement(By.id("input_loginForm:username")).click();
    
    // 6 | type | id=input_loginForm:username | ben
    driver.findElement(By.id("input_loginForm:username")).sendKeys("ben");
    
    // 5 | type | id=input_loginForm:password | ben
    driver.findElement(By.id("input_loginForm:password")).sendKeys("ben");
    
    // 7 | click | id=loginForm:j_idt39 | 
    driver.findElement(By.id("loginForm:j_idt39")).click();
    
    // 8 | click | id=dtLj_idt7:j_idt19 | 
    driver.findElement(By.id("dtLj_idt7:j_idt19")).click();
    
    // 9 | click | linkText=Abmelden | 
    driver.findElement(By.linkText("Abmelden")).click();
  }
}
