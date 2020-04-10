package view;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;

public class MakeShop {
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
  public void makeAShop() {
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
	    driver.findElement(By.id("input_loginForm:username")).sendKeys("kone");
	    
	    // 5 | type | id=input_loginForm:password | ben
	    driver.findElement(By.id("input_loginForm:password")).sendKeys("sali");
	    
	    // 7 | click | id=loginForm:j_idt39 | 
	    driver.findElement(By.id("loginForm:j_idt39")).click();
    
    // 8 | click | css=#j_idt7\3Aj_idt29\3A 1\3Aj_idt36 > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt29\\3A 1\\3Aj_idt36 > .ui-button-text")).click();
    
    // 9 | click | id=j_idt7:spinner_input | 
    driver.findElement(By.id("j_idt7:spinner_input")).click();
    
    // 10 | type | id=j_idt7:spinner_input | 9
    driver.findElement(By.id("j_idt7:spinner_input")).sendKeys("9");
    
    // 11 | click | css=#j_idt7\3Aj_idt51 > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt51 > .ui-button-text")).click();
    
    // 12 | click | css=#j_idt7\3Aj_idt29\3A 0\3Aj_idt36 > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt29\\3A 0\\3Aj_idt36 > .ui-button-text")).click();
    
    // 13 | click | id=j_idt7:spinner_input | 
    driver.findElement(By.id("j_idt7:spinner_input")).click();
    
    // 14 | type | id=j_idt7:spinner_input | 12
    driver.findElement(By.id("j_idt7:spinner_input")).sendKeys("12");
    
    // 15 | click | css=#j_idt7\3Aj_idt51 > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt51 > .ui-button-text")).click();
    
    // 16 | click | css=#j_idt7\3Aj_idt29\3A 0\3Aj_idt36 > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt29\\3A 0\\3Aj_idt36 > .ui-button-text")).click();
    
    // 17 | mouseOver | css=#j_idt7\3Aj_idt29\3A 0\3Aj_idt36 > .ui-button-text | 
    {
      WebElement element = driver.findElement(By.cssSelector("#j_idt7\\3Aj_idt29\\3A 0\\3Aj_idt36 > .ui-button-text"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    // 18 | mouseOut | css=#j_idt7\3Aj_idt29\3A 0\3Aj_idt36 > .ui-button-text | 
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    // 19 | click | id=j_idt7:spinner_input | 
    driver.findElement(By.id("j_idt7:spinner_input")).click();
    // 20 | type | id=j_idt7:spinner_input | 2
    driver.findElement(By.id("j_idt7:spinner_input")).sendKeys("2");
    
    // 23 | click | id=j_idt7:j_idt39_modal | 
    driver.findElement(By.id("j_idt7:j_idt39_modal")).click();
    
    
    // 25 | mouseOver | css=.ui-dialog-titlebar-icon > .ui-icon | 
    {
      WebElement element = driver.findElement(By.cssSelector(".ui-dialog-titlebar-icon > .ui-icon"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    // 26 | mouseOut | css=#j_idt7\3Aj_idt24 > #j_idt7\3Aj_idt39 > .ui-dialog-titlebar .ui-icon | 
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    // 27 | click | id=dtLj_idt7:j_idt19 | 
    driver.findElement(By.id("dtLj_idt7:j_idt19")).click();
    // 28 | click | linkText=Warenkorb | 
    driver.findElement(By.linkText("Warenkorb")).click();
    // 29 | click | css=#j_idt7\3A confirmShop > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3A confirmShop > .ui-button-text")).click();
    // 30 | click | css=#j_idt7\3A confirmShop > .ui-button-text | 
    driver.findElement(By.cssSelector("#j_idt7\\3A confirmShop > .ui-button-text")).click();
    // 31 | click | id=dtLj_idt7:j_idt19 | 
    driver.findElement(By.id("dtLj_idt7:j_idt19")).click();
    // 32 | click | linkText=Abmelden | 
    driver.findElement(By.linkText("Abmelden")).click();
  }
}
