package com.uat.base;





import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


import com.google.common.collect.ImmutableMap;
import com.uat.util.ErrorUtil;

import com.uat.util.Xls_Reader;

public class TestBase {
	
	
	public static Logger APP_LOGS=null;
	
	
	public static Properties CONFIG=null;
	//public static Properties OR=null;
	//public static Properties resourceFileConversion=null;
	
	//public static Xls_Reader suiteXls=null;
	public static Xls_Reader TM_projectSuiteXls=null;


	//private String userFile;
	public static Xls_Reader userXls =null;
	
	private static boolean isInitalized=false;
	private static boolean isBrowserOpened=false;	

	public static WebDriver driver =null;
	public static EventFiringWebDriver eventfiringdriver= null;
	public static WebDriverWait wait;
	WebDriverWait wait1;
	
	protected Calendar calendar;
	SimpleDateFormat Date;
	protected SimpleDateFormat Time;
	public static String screenshotDateTime;
	
	StackTraceElement[] stacktrace;
	StackTraceElement stackElement;
	String className;
	static int loginFailureCounter = 1;
	
	public static String osName = null;

	
	// initializing the Tests
	/**
	 * Initializes all the excel objects and properties files. Should be used at the very beginning of things.
	 * @throws Exception
	 */
	public void initialize()
	{
		
		
		// logs
		if(!isInitalized)
		{			
			APP_LOGS = Logger.getLogger("devpinoyLogger");
			// config
			APP_LOGS.debug("Loading Property files");

			CONFIG = new Properties();
			FileInputStream ip;
			try {
				ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//uat//config/config.properties");
				CONFIG.load(ip);
			} catch (Exception e) {
	
				e.printStackTrace();
			}
						
			
			/*OR = new Properties();
			ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//uat//config/OR.properties");
			OR.load(ip);
			
			
			resourceFileConversion=new Properties();
			ip=new FileInputStream(System.getProperty("user.dir")+"//src//com//uat//config/resourceFileConversion.properties");
			resourceFileConversion.load(ip);*/
			
			APP_LOGS.debug("Loaded Property files successfully");
			APP_LOGS.debug("Loading XLS Files");
	
			
			
			// xls file
			
			
			//suiteXls = new Xls_Reader(System.getProperty("user.dir")+"//src//com//uat/xls//Suite.xlsx");
			
			/*if (CONFIG.getProperty("environment").equals("Cloud")) 
				//userFile = 	"UAT Users_Cloud.xlsx";-rgensolutions domain
				userFile = 	"UAT Users_Click2Cloud.xlsx";//Click2Cloud domain		
			else
				userFile = "UAT Users_Local.xlsx";*/
			
			osName = System.getProperty("os.name").toUpperCase();
			
			if (osName.startsWith("WINDOW"))
			{
				userXls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//uat/xls//Users.xlsx");
				TM_projectSuiteXls = new Xls_Reader(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\TM_Project_Suite.xlsx");
			}
			else
			{
				userXls= new Xls_Reader(System.getProperty("user.dir")+"/src/com/uat/xls/Users.xlsx");
				TM_projectSuiteXls = new Xls_Reader(System.getProperty("user.dir")+"/src/com/uat/xls/TM_Project_Suite.xlsx");
			}
				
			
			APP_LOGS.debug("Loaded XLS Files successfully");
			
			calendar = Calendar.getInstance();
			Date = new SimpleDateFormat("dd-MM-yyyy");
			Time = new SimpleDateFormat("hh.mm.ss");
			screenshotDateTime = "Images captured on "+Date.format(calendar.getTime())+" at "+Time.format(calendar.getTime());
			
			isInitalized=true;
		
		}
		
	
	}
	
	
	// open a browser if its not opened
	/**
	 * Opens the browser based on the type mentioned in the Config file and intializes explicit and implicit wait
	 * @throws Exception
	 */
	public void openBrowser()
	{
		
		if(!isBrowserOpened)
		{
			if (CONFIG.getProperty("browserType").equals("IE"))
			{	
				 System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\com\\uat\\config\\IEDriverServer.exe");
				 System.out.println("I am in IE");
				 driver = new InternetExplorerDriver();
				 System.out.println("I got IE");
			}    
			else if(CONFIG.getProperty("browserType").equals("MOZILLA"))
			{	
				if (!osName.startsWith("WINDOW"))
				{
					System.out.println("I am in Mozilla");
					initializeForLinux();
					System.out.println("I am in Mozilla1111");
				}
				else
				{
					System.out.println("I am in FirefoxDriver");
					driver = new FirefoxDriver();
					System.out.println("I am in FirefoxDriver111");
				}
			}
			else if (CONFIG.getProperty("browserType").equals("CHROME"))
			{
				if (!osName.startsWith("WINDOW"))
				{
					System.out.println("I am in CHROME");
					initializeForLinux();
					System.out.println("I am in CHROME111");
				}
				else
					driver = new ChromeDriver();
			}
				
			
			isBrowserOpened=true;
			eventfiringdriver = new EventFiringWebDriver(driver);
			eventfiringdriver.manage().window().maximize();
			String waitTime=CONFIG.getProperty("default_implicitWait");
			eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			wait = new WebDriverWait(eventfiringdriver, 120);
			wait1 = new WebDriverWait(eventfiringdriver, 10);
			
			eventfiringdriver.get(CONFIG.getProperty("siteUrl"));
			  
			 
		}

	}
	
	private void initializeForLinux()
	{
		String Xport = System.getProperty("lmportal.xvfb.id", ":1");

		if(CONFIG.getProperty("browserType").equals("MOZILLA"))
		{
			final File firefoxPath = new File(System.getProperty("lmportal.deploy.firefox.path", "/usr/bin/firefox"));

			FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);

			firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			driver = new FirefoxDriver(firefoxBinary,firefoxProfile);
		}
		else if (CONFIG.getProperty("browserType").equals("CHROME"))
		{
			ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
	        .usingDriverExecutable(new File("/usr/local/chromedriver"))
	        .usingAnyFreePort().withEnvironment(ImmutableMap.of("DISPLAY", ":1")).build();
			
			try {
				chromeDriverService.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver = new ChromeDriver(chromeDriverService);
		}
		
		
	}
	
	// close browser
	public void closeBrowser()
	{
		if (isBrowserOpened) 
		{
			driver.quit();
			eventfiringdriver=null;
			driver=null;
			isBrowserOpened=false;
		}
		
	}
	
	//a utility to get package name of a class
	public String extractPackageName(String packName)
	{
		String actualPackageNameArray[] = packName.split("\\.");
		return actualPackageNameArray[actualPackageNameArray.length-1];
	}
		
	
	
	
	// Assert Strings
	/**
	 * Uses assert statement to compare strings. Used for Passing/failing a test case
	 * 
	 * @param expected
	 * @param actual
	 * @return true if strings match and false if dont
	 */
	public boolean compareStrings(String expected, String actual)
	{
		try
		{			
			Assert.assertEquals(actual , expected);
		}
		catch(Throwable t)
		{	
			ErrorUtil.addVerificationFailure(t);			
			APP_LOGS.debug("Strings do not match");
			return false;
		}
		
		return true;
	}
	
	
	
	
		//compare integers
	/**
	 * Uses assert statement to compare integers. Used for Passing/failing a test case
	 * 
	 * @param expected
	 * @param actual
	 * @return true if match and false if dont
	 */
		public boolean compareIntegers(int expected, int actual)
		{
			try
			{
				Assert.assertEquals(actual, expected);
			}
			catch(Throwable t)
			{
				ErrorUtil.addVerificationFailure(t);			
				APP_LOGS.debug("Values do not match");
				return false;
			}
			
			return true;
		}
		
		
		
		//Assert True
		/**		
		 *  Uses assert statement. Used for Passing/failing a test case
		 *  
		 * @param expected
		 * @param actual
		 * @return true if true and false if false
		 */
		public boolean assertTrue(boolean value)
		{
			
			try
			{
				Assert.assertTrue(value);
			}
			catch(Throwable t)
			{
				
				ErrorUtil.addVerificationFailure(t);			
				APP_LOGS.debug("Asserted False");
				return false;
			}
			
			return true;
			
		}
		
		
		
		
		
		 		
		/**
		   * This function will get all the user credentials based on the role passed to it from User Xls file.
		   * Note that it returns ArrayList of a custom class type Credentials. So the reference variable, calling this function to get all desired role users, should be an ArrayList of type Credentials.
		   * After getting all the users in your ArrayList, use the statement 'variable_name.get(index).username' without quotes to get username for the provided index and for password just replace 'username' in the statement with 'password'.
		   * 
		   * @param role  role based on User Xls file.
		   * @param numOfUsers number of uers to fetch for the enterd role.
		   * @return   returns ArrayList of type Credentials if expected number of users are present for the provided role else returns null in any other condition.
		   * @see    Credentials 
		   */  
		  public ArrayList<Credentials> getUsers(String role, int numOfUsers)
		  {
			  int numOfRows;
			  String xlsRole;
			  ArrayList<Credentials> user = new ArrayList<Credentials>();
			  int index =0;
			  String sheetName = "Role";
		   
			  APP_LOGS.debug("Trying to get "+numOfUsers+" user(s) for role "+role);
			  
			  if (!role.isEmpty()) 
			  {
		    
				  numOfRows = userXls.getRowCount(sheetName);
		    
				  for (int i = 2; i <= numOfRows; i++) 
				  {
		     
					  xlsRole = userXls.getCellData(sheetName, "Role", i);
		     
					  if (role.equalsIgnoreCase(xlsRole)) 
					  {
		      
						  user.add(new Credentials());
		      
						  user.get(index).username= userXls.getCellData(sheetName, "Username", i);
		      
						  user.get(index).password= userXls.getCellData(sheetName, "Password", i);
		      
						  index++;
		      
						  if (index==numOfUsers) 
						  {
							  break;
		       
						  }
		      
					  }
		     
				  }	
		    
				  if (user.size()==numOfUsers) 
				  {
		     
					  return user;
		     
				  }
				  else 
				  {
		     
					  return null;
				  }
		    
			  }
			  else 
			  {
				  return null;
			  }
		   
		  }
		  
		  
		  
		  
		  
		  
		  /**
		   * This function will get the first available user credentials based on the role passed to it from User Xls file from User sheet. the user will already have access
		   * Note that it returns object of a custom class Credentials. So the reference variable, calling this function to get all desired role users, should be of class Credentials.
		   * After getting the user in your variable object, use the statement 'variable_name.username' without quotes to get username and for password just replace 'username' in the statement with 'password'.
		   * 
		   * @param role  role based on User Xls file.
		   * @return   returns object of class Credentials if the user is present for the provided role in Users sheet of User Xls file else returns null in any other condition.
		   * @see    Credentials 
		   */  
		  public Credentials getUserWithAccess(String role)
		  {
			  int numOfRows;
			  Credentials user = null;
			  String xlsRole;				  
			  String sheetName = "Users";
		   
			  APP_LOGS.debug("Trying to get user for role "+role+" from "+sheetName+" sheet.");
			  			 
			  try 
			  {
				  
				  numOfRows = userXls.getRowCount(sheetName);
				    
				  for (int i = 2; i <= numOfRows; i++) 
				  {
			     
					  xlsRole = userXls.getCellData(sheetName, "Role", i);
			     
					  if (role.equalsIgnoreCase(xlsRole)) 
					  {
						  user = new Credentials(userXls.getCellData(sheetName, "Username", i), userXls.getCellData(sheetName, "Password", i));
			      
						  break;   						  
			      
					  }
			     
				  }	
				
			  } 
			  catch (Throwable t) 
			  {
				APP_LOGS.debug("Exception in 'getUserToLogin(String role)' function");
				t.getMessage();
			  }
			  
				  
			  return user;				  
		    			  
		   
		  } 		  
		  
		  /****
		     * This function uses javascript to click the element passed to it as parameter and returns the control 
		     * instantly after clicking for the next statement to be executed
		     * 
		     * @param element to click on
		     * @throws NullPointerException
		     */
		  public void click(WebElement element)
		  {
			  if (element != null)
				  eventfiringdriver.executeScript("var el = arguments[0]; el.click();", element);
			  else
				  throw new NullPointerException();
			  
		  }
		  
		  
		  public void waitForBlockUI()
		  {		  
			  
			  try
			  {
				  
				  setImplicitWait(0);
				  wait1.until(ExpectedConditions.presenceOfElementLocated(By.className("blockUI")));
				  wait1.until(ExpectedConditions.visibilityOf(eventfiringdriver.findElement(By.className("blockUI")))).getText();
				  
				  wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.className("blockUI")));
				  Thread.sleep(500);
			  
			  }
			  catch(Throwable t)
			  {
				  //t.printStackTrace();
			  }
			  finally
			  {
				  resetImplicitWait();
			  }
		    
		  }
		  
		  
		  
		  
		  
		  /**
		   * Gets text from the auto hide pop ups (alerts) used after save, update and delete actions. 
		   * Has dynamic waits for visbility and invisiblity of the pop up.
		   * 
		   * @return text from the auto hide pop up and null in any other case
		   */
		  protected String getTextFromAutoHidePopUp()
		  {
			  
			  String text = null;			  
			  
			  try
			  {
				  
				  setImplicitWait(0);				  				  
				  text = wait1.until(ExpectedConditions.visibilityOf(eventfiringdriver.findElement(By.id("autoHideAlert")))).getText();
				  
				  wait1.until(ExpectedConditions.invisibilityOfElementLocated(By.id("autoHideAlert")));
				  Thread.sleep(500);
			  
			  }
			  catch(Throwable t)
			  {
				  //t.printStackTrace();
			  }
			  finally
			  {
				  resetImplicitWait();
			  }
		    
			  return text;
			  
		  }		  
		  
		  
		  
		  //a dynamic wait that will wait for element's visibility located by the config key and will return element on visibility
		  /**
		   * 
		   * @param key of the element from the object repository to wait for visibility
		   * @param timeOutInSeconds if the element doesn't show up for specified time
		   * @return element after it becomes visible else returns null after the time out
		   * @throws InterruptedException
		   */
		  public WebElement waitForElementVisibility(By locator, long timeOutInSeconds)
		  {
			  
			  
			  WebElement element = null;			  
			  
			  try
			  {				  
				  WebDriverWait w = new WebDriverWait(eventfiringdriver, timeOutInSeconds);
				  
				  setImplicitWait(0);					
				  
				  w.until(ExpectedConditions.presenceOfElementLocated(locator));
				  element = w.until(ExpectedConditions.visibilityOfElementLocated(locator));
				  
			  }
			  catch(Throwable t)
			  {
				  t.printStackTrace();
			  }
			  finally
			  {
				  resetImplicitWait();
			  }			 
			  
			  return element;
			  
		  }
		  
		  
		  /**
		   * Used to set implicit wait when required. After its usage resetImplicitWait function should be called compulsorily for default timeout
		   * 
		   * @param time to set implicit wait for
		   */
		  public void setImplicitWait(long time)
		  {
			  eventfiringdriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		  }
		  
		  
		  /**
		   * Used to reset implicit wait as per the default time in the config file
		   */
		  public void resetImplicitWait()
		  {
			  eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(CONFIG.getProperty("default_implicitWait")), TimeUnit.SECONDS);
		  }
	
	

}
