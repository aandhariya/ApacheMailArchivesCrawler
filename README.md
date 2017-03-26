# Apache Mail Archives Crawler
A simple Java crawler to download all mails from any mailing list for a particular year (say 2015) from http://mail-archives.apache.org/mod_mbox/

You can configure following values in config.properties file:

<b>fileSeparator</b> - Decide File Separator based on your operating system i.e. for Windows -> \\ , Linux -> //
<br><b>targetDir</b> - Name of the local directory where you want to keep downloaded mails.
<br><b>mailingList</b> - Mailing List used by default constructor of MailCrawler. You can overwrite this value by calling parameterized constructor.
<br><b>year</b> - Year used by default constructor of MailCrawler. You can overwrite this value by calling parameterized constructor.
<br><b>overwriteIfDirExist</b>  Overwrites files in directory, if its value is set to YES. skip the entire process, if its value is set to NO.
