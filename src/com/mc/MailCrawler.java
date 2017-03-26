package com.mc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mc.util.MCUtility;

/**
 * Contains prime logic and methods of MailCrawler Application.
 * 
 * @author Abhi Andhariya
 *
 */
public class MailCrawler implements Runnable{

	/**
	 * Stores Application Properties
	 */
	private static final Properties appProp = MCUtility.getProperties();

	/**
	 * Stores file separator 
	 */
	private static final String FILE_SEPARATOR = appProp.getProperty(MailCrawlerConstant.FILE_SEPARATOR_STR, MailCrawlerConstant.FILE_SEPARATOR_STR_DEFAULT);



	/**
	 * It stores mail URLs for one month in LinkedList data structure.
	 */
	private List<String> mails = new LinkedList<String>(); 

	/**
	 * This field is used for grabbing HTML page as an JSoup document.
	 */
	private Document htmlDocument; 
	
	
	/**
	 *  Name of mailingList
	 */
	private String mailingList;

	/**
	 *  Year of mailing list archieve
	 */
	private int year;

	/**
	 *  Month of mailing list archieve
	 */
	private int month;




	/**
	 * Constructs a MailCrawler object using mailing_list 
	 * and year mentioned in config.properties file
	 */
	public MailCrawler()
	{
		this(appProp.getProperty(MailCrawlerConstant.MAILING_LIST, MailCrawlerConstant.MAILING_LIST_DEFAULT), Integer.parseInt(appProp.getProperty(MailCrawlerConstant.YEAR,MailCrawlerConstant.YEAR_DEFAULT)));
	}

	/**
	 * Constructs a MailCrawler object using mailing_list 
	 * and year passed in parameter
	 *
	 * @param mailingList   the {@code String} representing the name of mailing list
	 * @param year   {@code int} value representing the year of mailing list
	 */
	public MailCrawler(String mailingList, int year)
	{
		this.mailingList = mailingList;
		this.year = year;
	}
	
	/**
	 * This private constructor is used for creating thread 
	 * for each month. It constructs a MailCrawler object 
	 * using mailing_list, year and month passed in parameter. 
	 * 
	 * @param mailingList {@code String} value representing the name of mailing list
	 * @param year {@code int} value representing the year of mailing list
	 * @param month {@code int} value representing the month of mailing list
	 */
	private MailCrawler(String mailingList, int year, int month)
	{
		this.mailingList = mailingList;
		this.year = year;
		this.month = month;
	}

	/**
     * Creates base directory with name of mailing list and year. 
     * 
     * <p> Generates common CSS file which will be used by all the mail files.
     *
     * <p> For improving performance, it generates 12 threads, one thread 
     * per mails of each month.
     * 
     * <p> It will overwrite directory or skip the entire process based on the 
     * value  of <b>overwriteIfDirExist</b> property in <b>config.properties</b> file.
     * 
     * <p> overwrites if its value is YES. Skips processing if value is NO.
     * */
	public void startCrawling()
	{
		int m = 0;  //month
		Thread[] t = new Thread[12];
		
		String dirPath = getBaseDirPath();
		
		File dir = new File(dirPath);

		if(!(dir.exists()) 
				|| MailCrawlerConstant.YES.equalsIgnoreCase(appProp.getProperty(MailCrawlerConstant.OVERWRITE_IF_DIR_EXIT, MailCrawlerConstant.OVERWRITE_IF_DIR_EXIT_DEFAULT)))
		{
			dir.mkdirs();
			generateCSSFile(dirPath);
			while(m<12)
			{
				Runnable r = new MailCrawler(this.mailingList, this.year, m+1);
				t[m] = new Thread(r);
				t[m].start();
				m++;
			}
			m=0;
			while(m<12)
			{
				try {
					t[m].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m++;
			}

		}

	}

	
	/**
	 * Creates LinkedList to store mail links for each month. 
     *
     * <p> Traverse through all the pages and add mail links to the list. 
	 * 
	 * @throws IOException if link for entered mailing is list not found
	 */
	private void addMailLinks() throws IOException
	{
		try
		{
			int page = 0;
			String url = null;
			Connection connection = null;
			Elements mailLinksOnPage = null;

			mails = new LinkedList<String>();
			while(true)
			{
				url = String.format("http://mail-archives.apache.org/mod_mbox/%s/%4d%02d.mbox/date?%d", mailingList, year, month, page);
				connection = Jsoup.connect(url).userAgent(MailCrawlerConstant.USER_AGENT);
				htmlDocument = connection.timeout(0).get();

				mailLinksOnPage =  htmlDocument.select("td[class=subject] a");

				if(mailLinksOnPage.isEmpty())
					break;


				for(Element mail : mailLinksOnPage)
				{
					mails.add(mail.absUrl("href"));
				}
				page++;

			}
			System.out.println("addMailLinks :: Total " + mails.size() + " mails found for " + MailCrawlerConstant.monthMap.get(month) + ", " + year);
		}
		catch(IOException io)
		{
			System.err.println("addMailLinks :: Error in out HTTP request for " + MailCrawlerConstant.monthMap.get(month) + ", " + year + "\n" + io);
			throw io;
		}
	}

	/**
	 * Fetches mail links from Linked list created by addMailLinks() method
     * and create Mail files.
     * 
     * <p> Generates mail file name combining underscore separated subject 
     * and timestamp of the mail.
     * 
     * <p> It uses generateMailBody() method to generate fileContent.
	 */
	private void downloadMails()
	{
		Connection connection = null;
		String fileName = null;
		String fileContent = null;

		String dirPath = getBaseDirPath()
				+ year 	+ FILE_SEPARATOR
				+ String.format("%02d", month) + FILE_SEPARATOR;

		File dir = new File(dirPath);

		dir.mkdirs();
		
		for(String url : this.mails)
		{
			try
			{
				connection = Jsoup.connect(url).userAgent(MailCrawlerConstant.USER_AGENT);
				htmlDocument = connection.timeout(0).get();

				fileName = dirPath 
						+ (htmlDocument.select("tr[class=subject] td[class=right]").text()
						+ htmlDocument.select("tr[class=date] td[class=right]").text()).replaceAll("\\W+", "_") 
						+ ".html";

				fileContent = generateMailBody();
				
				MCUtility.createFile(fileName, fileContent);
				
			}
			catch(IOException io)
			{
				System.err.println("downloadMails :: Error in out HTTP request for " + url + "\n" + io);
			}
		}
		System.out.println("downloadMails :: Download completed for " + MailCrawlerConstant.monthMap.get(month) + ", " + year);
	}

	/**
	 * Generates html mail body.
     * 
	 * @return String with mail body in html format
	 */
	private String generateMailBody()
	{

		StringBuilder mailBody = new StringBuilder(MailCrawlerConstant.MAIL_BODY_OPENING_TAGS);


		mailBody.append(htmlDocument.getElementsByClass("from"));
		mailBody.append(htmlDocument.getElementsByClass("subject"));
		mailBody.append(htmlDocument.getElementsByClass("date"));
		mailBody.append(htmlDocument.getElementsByClass("contents"));

		mailBody.append(MailCrawlerConstant.MAIL_BODY_ENDING_TAGS); 


		return mailBody.toString();

	}
	
	/**
     * Returns base directory path using mailinglist.
     * e.g. mailing_list_xyz\2015\
     * 
     * <p>It uses fileSeparator defined in config.properties file.
     * 
     * 
	 * @return String containing base directory path 
	 */
	public String getBaseDirPath()
	{
		return appProp.getProperty(MailCrawlerConstant.TARGET_DIR, MailCrawlerConstant.TARGET_DIR_DEFAULT) 
				+ mailingList + FILE_SEPARATOR;
	}

	/**
	 * Generates common CSS file which will be used by all the mail files.
	 *  
	 * @param dirPath CSS file path
	 */
	private void generateCSSFile(String dirPath)
	{
		MCUtility.createFile(dirPath + MailCrawlerConstant.CSS_FILE_NAME, MailCrawlerConstant.CSS_FILE_CONTENT);
	}

	/**
	 * Calls addMailLinks() and downloadMails() for one month
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			addMailLinks();
			downloadMails();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter correct mailing list and year");
		}
		
	}
	
}
