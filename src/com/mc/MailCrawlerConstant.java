package com.mc;

import java.util.HashMap;

/**
 *Contains static constants which are widely being used by MailCrawler
 * 
 * @author Abhi Andhariya
 *
 */
public class MailCrawlerConstant {

	/**
	 *  contains value of fake USER_AGENT, so the web server thinks the robot is a normal web browser. 
	 */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";



	// constants for property file keys
	public static final String FILE_SEPARATOR_STR = "fileSeparator";
	public static final String MAILING_LIST = "mailingList";
	public static final String YEAR = "year";
	public static final String TARGET_DIR = "targetDir";
	public static final String OVERWRITE_IF_DIR_EXIT = "overwriteIfDirExist";
	
	public static final String FILE_SEPARATOR_STR_DEFAULT = "\\";
	public static final String MAILING_LIST_DEFAULT = "maven-users";
	public static final String YEAR_DEFAULT = "2015";
	public static final String TARGET_DIR_DEFAULT = "D:\\Mails\\";
	public static final String OVERWRITE_IF_DIR_EXIT_DEFAULT = "No";
	
	public static final String YES = "YES";
	
	public static final HashMap<Integer,String> monthMap = new HashMap<Integer,String>();
	
	static{
		monthMap.put(1,"January");
		monthMap.put(2,"February");
		monthMap.put(3,"March");
		monthMap.put(4,"April");
		monthMap.put(5,"May");
		monthMap.put(6,"June");
		monthMap.put(7,"July");
		monthMap.put(8,"August");
		monthMap.put(9,"September");
		monthMap.put(10,"October");
		monthMap.put(11,"November");
		monthMap.put(12,"December");
	}
	

	public static final String CSS_FILE_NAME = "style.css";

	public static final String CSS_FILE_CONTENT = "body	"
			+ "{"
			+	  	"font-family : verdana;"
			+ 		"font-size : 10px;"
			+ "}"
			+ "td"
			+ "{"
			+ 		"border:1px solid;"
			+ 		"padding:20px;"
			+ "}"
			+ ".left"
			+ "{"
			+ 		"background-color : #eee;"
			+ 		"font-weight : bold;"
			+ "}";

	public static final String MAIL_BODY_OPENING_TAGS = "<html>"
			+ "<head>"
			+ "<link rel='stylesheet' type='text/css' href='../../style.css' />"
			+ "</head>"
			+ "<body>"
			+ "<center>"
			+ "<table>"
			+ "<tr>";

	public static final String MAIL_BODY_ENDING_TAGS = "</tr>"
			+ "</table>"
			+ "</center>"
			+ "</body>"
			+ "</html>";
}
