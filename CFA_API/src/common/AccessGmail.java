package common;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AccessGmail {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Client for cfa-api";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/gmail-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(GmailScopes.GMAIL_LABELS,GmailScopes.MAIL_GOOGLE_COM);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize2() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(new File(System.getProperty("user.dir")+"/src/client_secret.json"));
//      Quickstart.class.getResourceAsStream(System.getProperty("user.dir")+"/src/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//        Credential credential = new AuthorizationCodeInstalledApp(
//            flow, new LocalServerReceiver()).authorize("user");
////        System.out.println(
////                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
//        return credential;
    }
    
    
    	private static Credential authorize() throws Exception {
    	  GoogleCredential credential = new GoogleCredential.Builder()
    	  .setTransport(HTTP_TRANSPORT)
    	  .setJsonFactory(JSON_FACTORY)
    	  .setServiceAccountId("cfa-api@cfa-api-1549784880976.iam.gserviceaccount.com")
    	  .setServiceAccountPrivateKeyFromP12File(new File("/home/ankur/Downloads/CFA-API-146ea20d5827.p12"))
    	  .setServiceAccountScopes(Collections.singleton(GmailScopes.MAIL_GOOGLE_COM))
    	  .setServiceAccountUser("cfa-api@cfa-api-1549784880976.iam.gserviceaccount.com")
    	  .build();
    	  return credential;
    	}

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws Exception 
     */
    public static Gmail getGmailService() throws Exception {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

/*  Get call labels
    Read latest email from gmail account
    Get the message and email header of first email*/    
    public static String[] read_latest_email() throws IOException, MessagingException {
        // Build a new authorized API client service.
        Gmail service = null;
		try {
			service = getGmailService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        // Print the labels in the user's account.
        String user = "automation_support@moentek.com";
        ListLabelsResponse listResponse =
            service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            for (Label label : labels) {
//                System.out.printf("- %s\n", label.getName());
            }
        }
        
        // Read message from INBOX
        ArrayList<String> label = new ArrayList<String>();
        label.add("INBOX");
        List messages = AccessGmail.listMessagesWithLabels(service, "me", label);
 
        Message first_email=null; Message message = null;
        // Get the message of first email of Leviett
        for(int i=0; i<messages.size(); i++){
        	first_email = (Message)messages.get(i);
        	message = AccessGmail.getMessage(service, "me", first_email.get("id").toString());
        	if(message!=null)
        		break;
        }
        
        String email_data = AccessGmail.get_email_body(message);
        // Get email header of first email
        String[] email_header = AccessGmail.read_message_header(message);
        
        // Put the email header and message into String array and return to calling method
        String[] email_data_with_header = new String[5];
        for(int i=0; i<email_header.length; i++){
        	email_data_with_header[i] = email_header[i];
        }
        email_data_with_header[4] = email_data;
        return email_data_with_header;
    }
    
    // Return the message id list associated with labels
    public static List<Message> listMessagesWithLabels(Gmail service, String userId,
    												   List<String> labelIds) throws IOException {
	    ListMessagesResponse response = service.users().messages().list(userId).setLabelIds(labelIds).execute();
	    List<Message> messages = new ArrayList<Message>();
	    while (response.getMessages() != null) {
	      messages.addAll(response.getMessages());
	      if (response.getNextPageToken() != null) {
	          String pageToken = response.getNextPageToken();
	          response = service.users().messages().list(userId).setLabelIds(labelIds)
	        		     .setPageToken(pageToken).execute();
	      } else {
	        break;
	      }
	    }

//	    for (Message message : messages) {
//	      System.out.println(message.toPrettyString());
//	    }
	    
	    return messages;
	 }
    
    // Get the message using message id
    public static Message getMessage(Gmail service, String userId, String messageId)
									throws IOException {
	    Message message = service.users().messages().get(userId, messageId).execute();
	    for(MessagePartHeader header : message.getPayload().getHeaders()){
	    	if(header.getName().equals("Subject")){
	    		System.out.println(header.getValue());
	    		if(header.getValue().equals("ADF Lead"))
	    			return message;
	    	}	    	
	    }
	    return null;
    }
    
    // Get the email message headers
    public static String[] read_message_header(Message message){
    	String[] mail_header_info = new String[4];
    	for(MessagePartHeader header: message.getPayload().getHeaders()){
    		String from = "", to = "", subject = "", date = "";
    		
    		if(header.getName().equals("From")){
    			from = header.getValue();
    			mail_header_info[0] = from;
    		}
    		else if(header.getName().equals("To")){
    			to = header.getValue();
    			mail_header_info[1] = to;
    		}
    		else if(header.getName().equals("Subject")){
    			subject = header.getValue();
    			mail_header_info[2] = subject;
    		}
    		else if(header.getName().equals("Date")){
    			date = header.getValue();
    			mail_header_info[3] = date;
    		}
	    }
    	// Return To, From, Subject and Date header associated with particular email
    	return mail_header_info;
    }
    
    // Get the decoded email body of triggered email
    public static String get_email_body(Message message) throws UnsupportedEncodingException{
    	String encoded_message = "";
    	
    	// For small message data are not in parts, 
    	if(message.getPayload().getParts() == null)
    	    encoded_message = message.getPayload().getBody().getData();
    	
    	// For long message data are in parts, Read the data from part one.
    	else
    	    encoded_message = message.getPayload().getParts().get(0).getBody().getData();
    	
    	// Decode the data and return to the calling function
	    byte[] decoded_message = Base64.decodeBase64(encoded_message);
	    String email_body = new String(decoded_message, "UTF-8");
	    return email_body;
    }
    
    public static void main(String a[]) throws IOException, MessagingException, ParserConfigurationException, SAXException{
    	String[] email_data_with_header = AccessGmail.read_latest_email();
    	AccessGmail.retrieve_call_info(email_data_with_header[4]);
    }
    
    
    // Retrieve information from triggered email of email/calldata API    
    public static String[] retrieve_call_info(String xml_doc) throws ParserConfigurationException, SAXException, IOException{
    	String id = "", tracking_num = "", audio = "", duration = "", disposition = "", repeat_call = "";
    	
		// Parse the XML data from triggered email of email/calldata API 
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(xml_doc);
		ByteArrayInputStream input =  new ByteArrayInputStream(
		   xmlStringBuilder.toString().getBytes("UTF-8"));
		Document doc = null;
		try{
			doc = builder.parse(input);
			doc.getDocumentElement().normalize();
		}
		catch(Exception e){
			Assert.assertTrue(false,"Error is email parsing looks like email body is not is XML format. Error raised - "+e.getMessage());
		}
		NodeList node_list = doc.getElementsByTagName("prospect");
		 for (int temp = 0; temp < node_list.getLength(); temp++) {
			 Node nNode = node_list.item(temp);
			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				 Element eElement = (Element) nNode;				 
				 // Read the ID, tracking number and audio file from triggered email
				 id = eElement.getElementsByTagName("id").item(0).getTextContent();
				 // Get the data from comment tag
				 String comment_data = eElement.getElementsByTagName("comments").item(0).getTextContent();
				 tracking_num = comment_data.substring(comment_data.indexOf("Toll Free Number:")+18, comment_data.indexOf("Toll Free Number:")+28);
				 duration = comment_data.substring(comment_data.indexOf("Call Duration:")+15, comment_data.indexOf("Call Duration:")+17);
				 disposition = comment_data.substring(comment_data.indexOf("Call Result:")+13, comment_data.indexOf("Call Result:")+22);
				 repeat_call = comment_data.substring(comment_data.indexOf("Repeat call:")+13, comment_data.indexOf("Repeat call:")+18);
				 audio = comment_data.substring(comment_data.indexOf("Audio location:")+15, comment_data.indexOf("%3D ;")+3);
			 }
		 }
			
		String[] call_info = {id,tracking_num,audio,duration,disposition,repeat_call};
		return call_info;
    }    
}