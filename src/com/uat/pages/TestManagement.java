package com.uat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.uat.base.TestBase;

public class TestManagement extends TestBase {


	WebDriver driver;	
	By testManagement_Id= By.id("testMgnt");
	By projectsPageId= By.id("navProjects");
	
	public TestManagement(WebDriver driver)
	{
		this.driver=driver;
	}
	
	public void goToTestManagement()
	{
		driver.findElement(testManagement_Id).click();
	}
	
	
	
}
