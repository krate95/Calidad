package seleniumTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestRenfe {

public static void main(String[] args) {
        
		// declaration and instantiation of objects/variables
		System.setProperty("webdriver.chrome.driver","C:/Users/rvela/Documents/chromedriver_win32/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
    	
		String baseUrl = "http://www.renfe.com";
        String loginUrl = "https://venta.renfe.com/vol/login.do?Idioma=es&Pais=ES&inirenfe=SI";
        String ventasUrl = "https://venta.renfe.com/vol/home.do?c=_EvUZ";
        String actualUrl = "";
        
        // Scenario 1
        driver.get(baseUrl);
        
        WebElement logo = driver.findElement(By.className("logo logo_gob"));
        if (logo.isDisplayed()) {
        	System.out.println("Scenario 1 passed!");
        }else {
        	System.out.println("Scenario 1 failed!");
        }
        
        // Scenario 2
        driver.get(baseUrl);
        
        WebElement loginLink = driver.findElement(By.linkText("Clientes Renfe. Identifícate."));
        if (loginLink.isDisplayed()) {
        	System.out.println("Scenario 2 passed!");
        }else {
        	System.out.println("Scenario 2 failed!");
        }
        
     // Scenario 3
        driver.get(baseUrl);
        
        driver.findElement(By.linkText("Clientes Renfe. Identifícate.")).click();
        actualUrl = driver.getCurrentUrl();
        
        if (actualUrl.contentEquals(loginUrl)) {
        	System.out.println("Scenario 3 passed!");
        }else {
        	System.out.println("Scenario 3 failed!");
        }
        
     // Scenario 4
        driver.get(loginUrl);
        
        WebElement userField = driver.findElement(By.id("txtoUsuario"));
        WebElement userPass = driver.findElement(By.id("txtoPass"));
        
        if (userField.isDisplayed()&&userPass.isDisplayed()) {
        	System.out.println("Scenario 4 passed!");
        }else {
        	System.out.println("Scenario 4 failed!");
        }
        
        //Scenario 5
        driver.get(loginUrl);
        
        userField.sendKeys("mail@gmail.com");
        userPass.sendKeys("pass");
        driver.findElement(By.id("login-button")).click();
        driver.getCurrentUrl();
        
        if (actualUrl.contentEquals(ventasUrl)) {
        	System.out.println("Scenario 5 passed!");
        }else {
        	System.out.println("Scenario 5 failed!");
        }
       
    }
	
}
