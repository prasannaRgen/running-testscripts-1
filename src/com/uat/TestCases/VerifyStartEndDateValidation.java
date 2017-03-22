package com.uat.TestCases;


import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.TestBase;
import com.uat.pages.Login;
import com.uat.pages.Projects;
import com.uat.util.TestUtil;


public class VerifyStartEndDateValidation extends TestBase {
	
	
	private boolean testSkipped = false;
	String runmodes[]=null;
	int count=-1;
	private boolean testPassed = false;
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkAndInitialize() throws Exception
	{
		initialize();
		APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
		
		if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			testSkipped = true;
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
		
				

	}
	
	/*@BeforeMethod
	public void initializeEnv()
	{		
		openBrowser();
	}*/
	
	@Test(dataProvider="getTestData")
	public void verifyStartEndDateValidation(String role, String groupName, String portfolio, String projectName, String version, 
			String startDate, String endDate, String versionLead, String expectedMessage) throws InterruptedException
	{		
		
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
			
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		openBrowser();
		Login login = new Login(eventfiringdriver);
		
		if (login.CloudLogin(role))
		{
			login = null;
			
			Thread.sleep(2000);
			
			Projects project = new Projects(eventfiringdriver);
			
			project.goToTestManagement();
			
			waitForBlockUI();
			
			project.createNewProject(groupName, portfolio, projectName, version, startDate, endDate, versionLead);
			
			String actualMessage = project.getValidationMessage();
			
			Assert.assertEquals(actualMessage, expectedMessage);
		}
		else
			throw new SkipException("Login Unsuccessful");	
		
		
	}
	
	
	@AfterMethod
	public void afterMethod(ITestResult result)
	{
	    try
		 {
	    	
		    if(result.getStatus() == ITestResult.SUCCESS)
		    {	
		    	TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
		    }
	
		    else if(result.getStatus() == ITestResult.FAILURE)
		    {
		    	testPassed = false;
		    	TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, result.getThrowable().getMessage());
				System.out.println("Failed - Taking Screenshot");
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), this.getClass().getSimpleName());
	
		    }
		    
		    else if(result.getStatus() == ITestResult.SKIP ){	
		    	 
		    	 TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		    	 TestUtil.printComments(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, result.getThrowable().getMessage());
	
		    }
		    closeBrowser();
			
		}
	   catch(Exception e)
	   {
	     e.printStackTrace();
	   }

	}
	
	@AfterTest
	public void reportTestResult()
	{
		if(testSkipped)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "SKIP");
		else if (!testPassed)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
		else
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
		
		
		
	}
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}
	

}
