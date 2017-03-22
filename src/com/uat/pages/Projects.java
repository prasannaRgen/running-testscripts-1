package com.uat.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Projects extends TestManagement {
	
	WebDriver driver;
	
	By create_new_link = By.xpath("//div[@id='pageTab']/h2[2]");
	By save_button = By.id("btnSave");
	
	By group_dropdown = By.id("group");
	By portfolio_dropdown = By.id("process");
	By project_dropdown = By.id("pName");
	
	By group_add_button = By.xpath("//a[@id='addGroup']/img[@title='Add Group']");
	By portfolio_add_button = By.xpath("//a[@id='addPortfolio']/img[@title='Add Portfolio']");
	By project_add_button = By.xpath("//a[@id='addProject']/img[@title='Add Project']");	
	By group_portfolio_project_textfield = By.id("txtPortfolioPopUp");
	By group_portfolio_save_btn = By.xpath("//div[@class='ui-dialog-buttonset']//span[text()='Save']");
	By project_add_btn = By.xpath("//span[text()='Add']");
	
	By startDateImage = By.xpath("//div[@id='leftdiv']//div[2]/img[@title='Select Date']");
	By endDateImage = By.xpath("//div[@id='leftdiv']//div[3]/img[@title='Select Date']");
	By monthDropDown = By.className("ui-datepicker-month");
	By yearDropDown = By.className("ui-datepicker-year");
	By dateTable = By.xpath("//div[@id='ui-datepicker-div']//tbody");
	
	By version_text_field = By.id("version");
	
	By versionLeadTextField = By.id("VersionLead");
	By versionLead_autocomplete_list = By.xpath("//ul[@role='listbox']");
	
	By alertPopUp = By.id("divAlert");
	
	public Projects(WebDriver driver)
	{
		super(driver);
		this.driver = driver;		
	}
	
	public void goToCreateNewPage()
	{
		waitForElementVisibility(create_new_link, 5).click();
		//this.driver.findElement(create_new_link);
	}
	
	public void saveProject()
	{
		this.driver.findElement(save_button).click();
	}
	
	public void addGroup(String groupName)
	{
		if(!verifyAndSelectGroupPortfolioProject(group_dropdown, groupName))
		{
			//click(waitForElementVisibility(group_add_button, 5));
			waitForElementVisibility(group_add_button, 5).click();
			
			this.driver.findElement(group_portfolio_project_textfield).sendKeys(groupName);
			
			this.driver.findElement(group_portfolio_save_btn).click();			
		}
		
	}
	
	
	public void addPortfolio(String portfolioName)
	{
		if (!verifyAndSelectGroupPortfolioProject(portfolio_dropdown, portfolioName))
		{
			//this.driver.findElement(portfolio_add_button).click();
			waitForElementVisibility(portfolio_add_button, 5).click();
			
			this.driver.findElement(group_portfolio_project_textfield).sendKeys(portfolioName);
			
			this.driver.findElement(group_portfolio_save_btn).click();
		}	
		
	}
	
	public void addProject(String projectName)
	{
		if (!verifyAndSelectGroupPortfolioProject(project_dropdown, projectName))
		{
			//click(this.driver.findElement(project_add_button));
			waitForElementVisibility(project_add_button, 5).click();
			waitForElementVisibility(group_portfolio_project_textfield, 2).sendKeys(projectName);
			
			this.driver.findElement(project_add_btn).click();
		}		
		
	}
	
	public void enterVersion(String version)
	{
		this.driver.findElement(version_text_field).sendKeys(version);
	}
	
	public void enterVersionLead(String versionLead)
	{
		this.driver.findElement(versionLeadTextField).sendKeys(versionLead);
		
		 
		 WebElement autoOptions = waitForElementVisibility(versionLead_autocomplete_list, 5);

		List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
		for(WebElement option : optionsToSelect){
	        if(option.getText().equals(versionLead)) {
	            option.click();
	            break;
	        }
	    }
	}
	
	
	
	public void selectStartEndDate(WebElement datePicker, String date)
	{		 
		String year = date.split("/")[2];
		String day = date.split("/")[0];
		String month = getMonth(Integer.parseInt(date.split("/")[1]));
		
		datePicker.click();
		
		Select yearDD = new Select(this.driver.findElement(yearDropDown));
		yearDD.selectByValue(year);
		
		Select monthDD = new Select(this.driver.findElement(monthDropDown));
		monthDD.selectByVisibleText(month);
		
		WebElement datepicker= this.driver.findElement(dateTable);
		
		List<WebElement> cols = datepicker.findElements(By.tagName("td"));
		for(WebElement cell :cols)
		{
			if(cell.getText().equals(day))
			{
				cell.findElement(By.linkText(""+day+"")).click();
				break;
			}
		}
	}
	
	public void createNewProject(String groupName, String portfolio, String projectName, String version,
			String startDate, String endDate, String versionLead)
	{
		goToCreateNewPage();
		
		addGroup(groupName);
		addPortfolio(portfolio);
		addProject(projectName);
		
		enterVersion(version);
		
		WebElement datePicker = this.driver.findElement(startDateImage);				
		
		selectStartEndDate(datePicker, startDate);
		
		datePicker = this.driver.findElement(endDateImage);
		
		selectStartEndDate(datePicker, endDate);
		
		enterVersionLead(versionLead);		
		
		saveProject();
		
		
	}
	
	public String getValidationMessage()
	{
		String msg = "";
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			//e.printStackTrace();
		}
		WebElement alertField = waitForElementVisibility(alertPopUp, 1);
		
		if (alertField != null)
			msg = alertField.getText();
		
		return msg;
	}
	
	private String getMonth(int month)
	{
		
		
		switch (month) {
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dec";

		default:
			return null;
		}
	}
	
	public boolean verifyAndSelectGroupPortfolioProject(By locator, String text)
	{
		
		WebElement dropDownList = driver.findElement(locator);
		
		List<WebElement> elements = dropDownList.findElements(By.tagName("option"));
		  
		  
		  
		for(int i =0 ;i<elements.size();i++)
		{
				
	  
				if(elements.get(i).getText().equals(text))
				{
						
		  	
						elements.get(i).click();
						
						APP_LOGS.debug( text + " : is selected...");
						
						return true;
						
				}
		}
		
		return false;
	}
	
	

}
