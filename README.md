This system is a third-party mail client connected with the standard mailbox server, which realizes the functions of editing, sending, viewing, deleting and downloading mails. The client can also choose the sending protocol, receiving protocol, sending server, and receiving server with the mailbox standard server. Because the email authorization code is complex, the client is connected to the SQLserver database, and the email account password that has been logged in is recorded. After logging in, you can use the remember password function to automatically fill in the password. Considering that users may need to write and view emails at the same time, the email editing and viewing interface adopts the method of pop-up window, that is, it supports writing and viewing multiple emails at the same time.
Development environment:
JDK1.8
depend:
	javax.mail.jar
	jsoup-1.14.jar
	mssql-jdbc-7.00.jre8.jar
