/**
 * 
 */
package com.uat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.uat.base.TestBase;

/**
 * @author Admin
 *
 *
 *  This method will store all the locators and methods of login page
 *
 */
public class Login extends TestBase
{
	WebDriver driver;
	String pageTitle = null;

	By username= By.id("Email");
	By password=By.id("Password");
	By signinButton = By.id("Login");
	By testManagement_Id= By.id("testMgnt");
	By use_another_account_button = By.className("use_another_account");
	By alert_label = By.id("lbl_alert");
	
	public Login(WebDriver driver)
	{
		this.driver=driver;
		pageTitle = this.driver.getTitle();
	}
	
	private void enterUsername(String Username)
	{
		this.driver.findElement(username).sendKeys(Username);
	}
	
	private void enterPassword(String Password)
	{
		this.driver.findElement(password).sendKeys(Password);
	}
	
	private void signIn()
	{
		this.driver.findElement(signinButton).click();
	}
	
	private String getPageTitle()
	{
		String pageTitle = this.driver.getTitle();
		
		return pageTitle;
	}
	
	
	
	
	public boolean CloudLogin(String User, String Pass)
	{
		
		enterUsername(User);
		enterPassword(Pass);			
		
		signIn();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setImplicitWait(0);
		try
		{
			if (!this.driver.findElement(alert_label).getText().equals("Invalid UserName or Password..!!"))
				return false;
		}
		catch (Exception e)
		{
			e.getMessage();
		}
		
		resetImplicitWait();		
		
		return true;
		
	}
	
	public boolean CloudLogin(String role)
	{
		
		String username = null;
		String password = null;
		String xlsRole;
		
		APP_LOGS.debug("Trying login for role "+role); 
		
		try
		{
			if (!role.isEmpty()) 
			{				
				
				for(int i =2 ; i<=userXls.getRowCount("Users") ; i++)
				{					
					xlsRole=userXls.getCellData("Users", "Role", i);
					
					if(role.equalsIgnoreCase(xlsRole) )
					{
						APP_LOGS.debug("Role found in Users file. Fetching credentials");
						
						username = userXls.getCellData("Users", "Username", i);
						
						password = userXls.getCellData("Users", "Password", i);														
							
					}									
														
				}
				if (username == null || password == null)
				{
					APP_LOGS.debug("Role not found in Xls file. Quitting driver");				
					closeBrowser();
					return false;
				}			
					
			}
			else 
			{
				APP_LOGS.debug("Role not provided. Quitting driver");
				closeBrowser();
				return false;
			}	
				
				
		}
		
		catch(Throwable t)
		{
			t.printStackTrace();
			APP_LOGS.debug("Exception occurred in login function. Quitting driver");
			closeBrowser();
			return false;
			
		}
	
	
		
		return CloudLogin(username, password);
		
	}
		
		
}
