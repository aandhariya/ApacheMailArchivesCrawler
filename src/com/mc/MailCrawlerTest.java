package com.mc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is for testing the MailCrawler Application.
 * It contains only main method which calls startCrawling() method to start the process.
 * 
 * @author Abhi Andhariya
 *
 */
public class MailCrawlerTest
{
    
	/**
	 * Takes mailingList and year from user.
	 * <p>Calls startCrawling() method to start the process.
	 * <p> Displays start and completion time.
	 * @param args
	 */
    public static void main(String[] args)
    {
    	
    	try(BufferedReader br = new BufferedReader( new InputStreamReader(System.in));)
    	{
    		System.out.println("Enter mailing list name"); // maven-users
        	String mailingList = br.readLine();
        	System.out.println("Enter year");  // e.g. 2015
        	int year = 0;
        	while(year==0)
        	{
        		try
            	{
            		year = Integer.parseInt(br.readLine());
            	}
            	catch(NumberFormatException e)
            	{
            		System.out.println("Invalid Number Format. Please enter correct number format.");
            	}
        	}
        	
        	
        	
	    	DateFormat df1 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	        Date dateobj1 = new Date();
	        System.out.println("Downlaod started at " + df1.format(dateobj1));
	    	
	        
	    	MailCrawler mc = new MailCrawler(mailingList,year);
	        mc.startCrawling();
	        
	        
	        DateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	        Date dateobj2 = new Date();
	        System.out.println("Process Completed at " + df2.format(dateobj2));
    	}
    	catch(IOException io)
    	{
    		System.out.println("Error occured " + io);
    		io.printStackTrace();
    	}
    }
}
