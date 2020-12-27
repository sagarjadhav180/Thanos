package common;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import com.convirza.constants.Directory;

import java.util.*;

public class SendEmail

{
public static void execute(String reportFileName, int[] test_status_count) throws Exception

{
String pie_chart_link = HelperClass.createpiechart(test_status_count[0],test_status_count[1], test_status_count[2]);

String[] to = {"lsnow@convirza.com"};
String[] cc = {"rakesh.m@moentek.com","snehal.d@moentek.com","ganesh.g@moentek.com","sagar.j@moentek.com"};
String[] bcc = {""};
String api_url = HelperClass.get_api_url();
String build_status = test_status_count[1] > 0? "<h1 style='color:red;font-family:sans-serif'> BUILD FAILURE </h1>" : "<h1 style='color:blue;font-family:sans-serif'> BUILD SUCCESS </h1>";
String writer = "<!DOCTYPE html> <html> <head></head> <body> <p style='color:black;font-size:13px;font-family:sans-serif'>Hi All,<br><br> This is automated mail from QA Team. <br> Please find the attachment for the log result.</p>" + build_status + "<span style='color:black;font-size:13px;font-family:sans-serif'><b>Project:</b> CONVIRZA FOR ADVERTISERS</span><br> <span style='color:black;font-size:13px;font-family:sans-serif'><b>Date of build: </b>"+ new java.util.Date() +"<br> <span style='color:black;font-size:13px;font-family:sans-serif'><b>API url: </b>"+ api_url +"</span><br> <b><span style='color:black;font-size:13px;font-family:sans-serif'> Number of Test Cases Failed: </span></b>" + test_status_count[1] + "<br> <b><span style='color:black;font-size:13px;font-family:sans-serif'> Number of Test Cases Passed: </span></b>" + test_status_count[0] + "<br> <b><span style='color:black;font-size:13px;font-family:sans-serif'> Number of Test Cases Skipped: </span></b>"+ test_status_count[2]+ "<br><br><br> <img src ="+ pie_chart_link +"/> <br/><br/> <b style='font-size:13.5px;font-family:open sans-serif;padding-left:5px'>Thanks,</b><br/> <b style='font-size:13.5px;font-family:open sans-serif;padding-left:5px'>QA Team</b><br/></body> </html>";
String[] attachment = {System.getProperty("user.dir") + "/Result/cfa_api_report.html", Directory.ExcelResult.getFailedTestResultFile(), Directory.ExcelResult.getPassedTestResultFile()};
String[] attachmentName = {reportFileName, "FailedTestCasesStatistics.xlsx","PassedTestCasesStatistics.xlsx"};
SendEmail.sendMail("CFAAppAutomation@gmail.com",
"CFAAppAutomation@123",
"smtp.gmail.com",
"465",
"true",
"true",
true,
"javax.net.ssl.SSLSocketFactory",
"false",
to,
cc,
bcc,
"CFA API Automation Result",
writer,
attachment,
attachmentName
);
}

public static boolean sendMail(String userName,
String passWord,
String host,
String port,
String starttls,
String auth,
boolean debug,
String socketFactoryClass,
String fallback,
String[] to,
String[] cc,
String[] bcc,
String subject,
String text,
String[] attachmentPath,
String[] attachmentName
){

//Object Instantiation of a properties file.
Properties props = new Properties();

props.put("mail.smtp.user", userName);

props.put("mail.smtp.host", host);

if(!"".equals(port)){
props.put("mail.smtp.port", port);
}

if(!"".equals(starttls)){
props.put("mail.smtp.starttls.enable",starttls);
props.put("mail.smtp.auth", auth);
}

if(debug){

props.put("mail.smtp.debug", "true");

}else{

props.put("mail.smtp.debug", "false");

}

if(!"".equals(port)){
props.put("mail.smtp.socketFactory.port", port);
}
if(!"".equals(socketFactoryClass)){
props.put("mail.smtp.socketFactory.class",socketFactoryClass);
}
if(!"".equals(fallback)){
props.put("mail.smtp.socketFactory.fallback", fallback);
}

try{

Session session = Session.getDefaultInstance(props, null);

session.setDebug(debug);

MimeMessage msg = new MimeMessage(session);

msg.setText(text.toString(),"text/html");
msg.setSubject(subject);

Multipart mp = new MimeMultipart();
MimeBodyPart messageBodyPart = new MimeBodyPart();
messageBodyPart.setContent(text.toString(), "text/html");
for (int i=0; i<attachmentPath.length; i++) {
	MimeBodyPart messageBodyPart1 = new MimeBodyPart();
	DataSource source = new FileDataSource(attachmentPath[i]);
	messageBodyPart1.setDataHandler(new DataHandler(source));
	messageBodyPart1.setFileName(attachmentName[i]);
	mp.addBodyPart(messageBodyPart1);	
}

mp.addBodyPart(messageBodyPart);
msg.setContent(mp);
msg.setFrom(new InternetAddress(userName));

for(int i=0;i<to.length;i++){
msg.addRecipient(Message.RecipientType.TO, new
InternetAddress(to[i]));
}

for(int i=0;i<cc.length;i++){
msg.addRecipient(Message.RecipientType.CC, new
InternetAddress(cc[i]));
}

//for(int i=0;i<bcc.length;i++){
//msg.addRecipient(Message.RecipientType.BCC, new
//InternetAddress(bcc[i]));
//}

msg.saveChanges();

Transport transport = session.getTransport("smtp");

transport.connect(host, userName, passWord);

transport.sendMessage(msg, msg.getAllRecipients());

transport.close();

return true;

} catch (Exception mex){
mex.printStackTrace();
return false;
}
}
}













	
