package common;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.MimeUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.convirza.constants.FileConstants;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

public class HelperClass {
	public static JsonNode rootNode;
	public static URI uri;
	public static String params;

	// Get the access_token through oauth/token api method
	public static String get_oauth_token(String username, String password) throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		URI uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath("/oauth/token")
				.build();
		HttpPost httppost = new HttpPost(uri);
		JSONObject json = new JSONObject();
		json.put("grant_type", "password");
		json.put("client_id", "system");
		json.put("client_secret", "f558ba166258089b2ef322c340554c");
		json.put("username", username);
		json.put("password", password);	

		StringEntity input = new StringEntity(json.toString());
		input.setContentType("application/json");
		httppost.setEntity(input);
		httppost.addHeader("Content-Type" , "application/json");
		HttpResponse response = httpclient.execute(httppost);

		// Return null if API returns error while fetching access token
		if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201)
			return null;

		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		JSONParser parser = new JSONParser();
		Object resultObject = parser.parse(result);
		JSONObject obj =(JSONObject)resultObject;

		System.out.println(obj.get("access_token"));
		String access_token = obj.get("access_token").toString();
		return access_token;
	}

	public static CloseableHttpResponse make_get_request(String path, String access_token, List nvps) throws ClientProtocolException, IOException, URISyntaxException{
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.addParameters(nvps)
				.build();
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("Content-Type", "application/json");
		httpget.addHeader("Authorization", "bearer " + access_token);

		params = nvps.toString();
		uri = httpget.getURI();
		CloseableHttpResponse response = httpclient.execute(httpget);
		return response;
	}

	public static CloseableHttpResponse make_post_request(String path, String access_token, Object nvps) throws ClientProtocolException, IOException, URISyntaxException{
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);       
		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
		try {
			JSONArray request = (JSONArray)nvps;
			httppost.setEntity(new StringEntity(request.toJSONString()));
		}
		catch(ClassCastException e){
			JSONObject request = (JSONObject)nvps;
			httppost.setEntity(new StringEntity(request.toJSONString()));
		}

		params = nvps.toString();
		uri = httppost.getURI();

		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}

	public static CloseableHttpResponse make_post_request_call_upload(String path, String access_token, JSONObject nvps) throws ClientProtocolException, IOException, URISyntaxException{
		
		String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);       
//		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
		
		try {

			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			for(String fieldName:fields) {
					
					if(fieldName.equals("file")) {
						File file = new File(nvps.get("file").toString());
						builder.addBinaryBody(fieldName, new FileInputStream(file),
							    ContentType.MULTIPART_FORM_DATA,
							    file.getName());	
					}
					
					else {
						try {
							builder.addTextBody(fieldName, nvps.get(fieldName).toString(),ContentType.TEXT_PLAIN);							
						}catch(NullPointerException e) {
							builder.addTextBody(fieldName, "",ContentType.TEXT_PLAIN);
						}

					}			
				
				}
					

			HttpEntity multipart = builder.build();
			httppost.setEntity(multipart);
	
		
		}
		catch(ClassCastException e){
		
		}
		
		params = nvps.toJSONString();
		uri = httppost.getURI();

		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	public static CloseableHttpResponse make_post_request_without_field(String path, String access_token, JSONObject nvps,String field) throws ClientProtocolException, IOException, URISyntaxException{
		
		String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);       
//		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
			
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			for(String fieldName:fields) {
				if(!fieldName.equals(field)) {
					
					if(fieldName.equals("file")) {
						File file = new File(nvps.get("file").toString());
						builder.addBinaryBody(fieldName, new FileInputStream(file),
							    ContentType.MULTIPART_FORM_DATA,
							    file.getName());	
					}
					
					else {
						builder.addTextBody(fieldName, nvps.get(fieldName).toString(),ContentType.TEXT_PLAIN);	
					}			
				}
				else
					continue;
				
			}
   
			HttpEntity multipart = builder.build();
			httppost.setEntity(multipart);
		}
		catch(ClassCastException e){			
		}
		
		params = nvps.toJSONString();
		uri = httppost.getURI();

		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	public static CloseableHttpResponse make_post_request_with_null_field(String path, String access_token, JSONObject nvps,String field) throws ClientProtocolException, IOException, URISyntaxException{
		
		String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);       
//		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
		
		
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			for(String fieldName:fields) {
				if(!fieldName.equals(field)) {
						if(fieldName.equals("file")) {
						File file = new File(nvps.get("file").toString());
						builder.addBinaryBody(fieldName, new FileInputStream(file),
							    ContentType.MULTIPART_FORM_DATA,
							    file.getName());	
					}
					else {
						builder.addTextBody(fieldName, nvps.get(fieldName).toString(),ContentType.TEXT_PLAIN);	
					}
				}
				else {
					if(fieldName.equals("file")) {
//						File file = new File(nvps.get("file").toString());
						builder.addBinaryBody(fieldName, new File("null"));
					}
					builder.addTextBody(fieldName, "null",ContentType.TEXT_PLAIN);
					
				}
					
				
			}
   
			HttpEntity multipart = builder.build();
			httppost.setEntity(multipart);
		}
		catch(ClassCastException e){
			
		}
		
		params = nvps.toJSONString();
		uri = httppost.getURI();

		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}

	
	public static CloseableHttpResponse make_put_request(String path, String access_token, Object nvps) throws ClientProtocolException, IOException, URISyntaxException{
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPut httppost = new HttpPut(uri);       
		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
		try{
			JSONArray request = (JSONArray)nvps;
			httppost.setEntity(new StringEntity(request.toJSONString()));
		}
		catch(ClassCastException e){
			JSONObject request = (JSONObject)nvps;
			httppost.setEntity(new StringEntity(request.toJSONString()));
		}

		params = nvps.toString();
		uri = httppost.getURI();
		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}    

	public static HttpResponse make_delete_request(String path, String access_token, Object nvps) throws ClientProtocolException, IOException, URISyntaxException{    	
		String api_url = HelperClass.get_api_url();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpDelete httpdelete = new HttpDelete(uri);            
		httpdelete.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httpdelete.addHeader("Authorization", "bearer "+access_token);
		HttpEntity entity;
		try{
			JSONObject request = (JSONObject)nvps;
			entity = new StringEntity(request.toJSONString());
		}
		catch(ClassCastException exception){
			JSONArray request = (JSONArray)nvps;
			entity = new StringEntity(request.toJSONString());
		}

		params = nvps.toString();
		uri = httpdelete.getURI();

		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(uri);
			httpDeleteWithBody.addHeader("Content-Type", "application/json");
			if(!access_token.equals(""))
				httpDeleteWithBody.addHeader("Authorization", "bearer "+access_token);
			httpDeleteWithBody.setEntity(entity);
			response = httpClient.execute(httpDeleteWithBody);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static CloseableHttpResponse make_post_request_with_header(String path, String access_token, String[] header, StringEntity input) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String api_url = HelperClass.get_api_url();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);
		input.setContentType(header[0]);
		httppost.setEntity(input);

		params = input.toString();
		uri = httppost.getURI();

		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}	

	public static CloseableHttpResponse make_get_request_without_content_type(String path, String access_token) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String api_url = HelperClass.get_api_url();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpGet httpget = new HttpGet(uri);

		params = "";
		uri = httpget.getURI();

		httpget.addHeader("Authorization", "bearer "+access_token);
		CloseableHttpResponse response = httpclient.execute(httpget);
		return response;
	}

	public static CloseableHttpResponse make_get_request_without_authorization(String path, String access_token) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String api_url = HelperClass.get_api_url();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("Content-Type", "application/json");

		params = "";
		uri = httpget.getURI();

		CloseableHttpResponse response = httpclient.execute(httpget);
		return response;
	}

	
	public static CloseableHttpResponse make_post_request_message_upload(String path, String access_token, JSONObject nvps) throws Exception{
		
		String api_url = HelperClass.get_api_url();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		uri = new URIBuilder()
				.setScheme("https")
				.setHost(api_url)
				.setPath(path)
				.build();
		HttpPost httppost = new HttpPost(uri);       
//		httppost.addHeader("Content-Type", "application/json");
		if(!access_token.equals(""))
			httppost.addHeader("Authorization", "bearer "+access_token);
		
		try {

			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			if(nvps.size()==1) {
				File file = new File(nvps.get("file").toString());
				builder.addBinaryBody("file", new FileInputStream(file),
				ContentType.MULTIPART_FORM_DATA,
				file.getName());
				
			}else {
				for(int i=0;i<nvps.size();i++) {
					File file = new File(nvps.get("file_"+i).toString());
					builder.addBinaryBody("file_"+i, new FileInputStream(file),
					ContentType.MULTIPART_FORM_DATA,
					file.getName());	
				}
			}
			
			HttpEntity multipart = builder.build();
			httppost.setEntity(multipart);		
			
		}catch(ClassCastException e){
		
		}
		
		params = nvps.toJSONString();
		uri = httppost.getURI();
		CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	
	// check object value is not null or blank
	public static void multiple_assertnotEquals(Object obj, String field){
		Assert.assertNotEquals(obj, "null", field+" is null");
		Assert.assertNotEquals(obj, null, field+" is null");
		Assert.assertNotEquals(obj, "", field+" is blank");
	}

	public static String createpiechart(int passed, int failed, int skipped){
		// Create pie chart
		int total = passed + failed + skipped;
		int passed_percentage = (passed == 0) ? 0 : (passed * 100 / total);
		int failed_percentage = (failed == 0) ? 0 : (failed * 100 / total);
		int skipped_percentage = (skipped == 0) ? 0 : (skipped * 100 / total);
		Slice s1 = Slice.newSlice(passed_percentage, Color.BLUE, "Passed", "Passed");
		Slice s2 = Slice.newSlice(failed_percentage, Color.RED, "Failed", "Failed");
		Slice s3 = Slice.newSlice(skipped_percentage, Color.newColor("CACACA"), "Skipped", "Skipped");
		PieChart chart = GCharts.newPieChart(s1, s2, s3);
		chart.setTitle("CFA API Automation Result", Color.BLACK, 16);
		chart.setSize(500, 200);
		chart.setThreeD(true);
		String url = chart.toURLString();
		System.out.println("Pie chart is created");
		return url;
	}

	public static void writeExcel(int[] dataToWrite) throws IOException{
		String filePath = "/home/user/Desktop", fileName = "automation_status.xls", sheetName = "weekly";
		//Create a object of File class to open xlsx file
		File file =    new File(filePath+"/"+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = null;
		//Find the file extension by spliting file name in substing and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx")){
			//If it is xlsx file then create object of XSSFWorkbook class
			workbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls")){
			//If it is xls file then create object of XSSFWorkbook class
			workbook = new HSSFWorkbook(inputStream);
		}
		//Read excel sheet by sheet name    
		Sheet sheet = workbook.getSheet(sheetName);
		//Get the current count of rows in excel file
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		//Get the first row from the sheet
		Row row = sheet.getRow(0);
		//Create a new row and append it at last of sheet
		Row newRow = sheet.createRow(rowCount+1);
		//Create a loop over the cell of newly created Row
		System.out.println("Last Cell: "+ row.getLastCellNum());
		for(int j = 0; j < row.getLastCellNum(); j++){
			Cell cell = newRow.createCell(j);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if(j==0)
				cell.setCellValue(format.format(new Date()).toString());
			//Fill data in row
			else
				cell.setCellValue(dataToWrite[j-1]);
		}
		//Close input stream
		inputStream.close();
		//Create an object of FileOutputStream class to create write data in excel file
		FileOutputStream outputStream = new FileOutputStream(file);
		//write data in the excel file
		workbook.write(outputStream);
		//close output stream
		outputStream.close();    
	}

	public Object[] readExcel() throws IOException{
		String filePath = "/home/user/Desktop", fileName = "automation_status.xls", sheetName = "weekly";
		//Create a object of File class to open xlsx file
		File file =    new File(filePath+"/"+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = null;
		//Find the file extension by spliting file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx")){
			//If it is xlsx file then create object of XSSFWorkbook class
			workbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls")){
			//If it is xls file then create object of XSSFWorkbook class
			workbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet sheet = workbook.getSheet(sheetName);
		//Find number of rows in excel file
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();

		ArrayList<String> date_array = new ArrayList<String>();
		ArrayList<String> passed_array = new ArrayList<String>();
		ArrayList<String> failed_array = new ArrayList<String>();
		ArrayList<String> skipped_array = new ArrayList<String>();
		System.out.println("Row Count: "+rowCount);
		//Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount+1; i++) {
			Row row = sheet.getRow(i);
			System.out.println("Cell: " +row.getLastCellNum());
			//Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				//Print excel data in console
				DataFormatter formatter = new DataFormatter();
				if(j==0)
					date_array.add(formatter.formatCellValue(row.getCell(j)));
				else if(j==1)
					passed_array.add(formatter.formatCellValue(row.getCell(j)));
				else if(j==2)
					failed_array.add(formatter.formatCellValue(row.getCell(j)));
				else if(j==3)
					skipped_array.add(formatter.formatCellValue(row.getCell(j)));
			}
		}
		Object[] weekly_statistic = new Object[]{date_array,passed_array,failed_array,skipped_array};
		return weekly_statistic;
	}

	public static String weekly_chart() throws IOException{
		HelperClass hc = new HelperClass();	
		Object[] weekly_statistic = hc.readExcel();
		ArrayList<String> date_array = (ArrayList<String>)weekly_statistic[0], passed_array = (ArrayList<String>)weekly_statistic[1], failed_array = (ArrayList<String>)weekly_statistic[2], skipped_array = (ArrayList<String>)weekly_statistic[3];
		final String passed = "PASSED";
		final String failed = "FAILED";
		final String skipped = "SKIPPED";

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(int i=0; i<failed_array.size();i++){
			dataset.addValue(Integer.parseInt(failed_array.get(i)), failed , date_array.get(i));
		}

		for(int i=0; i<passed_array.size();i++){
			dataset.addValue(Integer.parseInt(passed_array.get(i)), passed , date_array.get(i));
		}

		for(int i=0; i<skipped_array.size();i++){
			dataset.addValue(Integer.parseInt(skipped_array.get(i)), skipped , date_array.get(i));
		}

		JFreeChart barChart = ChartFactory.createBarChart3D(
				"Weekly Automation Status", 
				"Date", "Test Cases Count", 
				dataset,PlotOrientation.VERTICAL, 
				true, true, false);

		int width = 940; /* Width of the image */
		int height = 480; /* Height of the image */ 
		File file = new File("BarChart.jpeg"); 
		ChartUtilities.saveChartAsJPEG(file, barChart, width, height);
		String absolute_path = file.getAbsolutePath();
		String weekly_chart = image_upload(absolute_path);
		return weekly_chart;
	}

	public static String image_upload(String image){
		// Give global url to captured screenshot	
		String uploaded_image_link = "";
		String upload_to = "https://api.imgur.com/3/upload.json";
		String API_key = "6ce9f890db1c13bab4076752571ef4861c467151";
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(upload_to);
		httpPost.addHeader("Authorization", "Client-ID " + "d12b61ca3109d70");
		try {
			final MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart("image", new FileBody(new File(image)));
			entity.addPart("key", new StringBody(API_key));
			httpPost.setEntity(entity);
			final HttpResponse response = httpClient.execute(httpPost,
					localContext);
			final String response_string = EntityUtils.toString(response
					.getEntity());
			rootNode = new ObjectMapper().readTree(new StringReader(response_string));
			JsonNode innerNode = rootNode.get("data");
			System.out.println(rootNode);
			JsonNode aField = innerNode.get("link");
			uploaded_image_link = aField.asText();
			System.out.println("Give global url to captured screenshot");
			System.out.println("Uploaded image link: " + uploaded_image_link);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return uploaded_image_link;
	}

	public static ArrayList<String> read_config(ArrayList<String> fields) throws IOException{
		FileReader reader=new FileReader(System.getProperty("user.dir")+"/src/config.properties");
		Properties prop =new Properties();  
		prop.load(reader);  
		ArrayList<String> fields_value = new ArrayList<String>();
		for(String field: fields){
			fields_value.add(prop.getProperty(field));
		}
		return fields_value;
	}
	
	public static void write_config(Map<String,String> properties) {
		FileReader reader;
		try {
			reader = new FileReader(System.getProperty("user.dir")+"/src/config.properties");
			Properties prop =new Properties();  
			prop.load(reader); 
			
			properties.forEach((key,value) -> {
				prop.setProperty(key, value);
			});
			
			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"/src/config.properties");
			prop.store(writer, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static String get_api_url() throws IOException{
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("url");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		return config.get(0);
	}

	public static String current_environment() throws IOException{
		FileReader reader=new FileReader(System.getProperty("user.dir")+"/src/config.properties");
		Properties p=new Properties();  
		p.load(reader);  
		String env = p.getProperty("env");
		return env;
	}

	public static ArrayList<String> readTestData(String class_name, String method_name) throws IOException{
		String fileName = "";
		if (HelperClass.current_environment().contains("staging"))

			fileName = "staging" + FileConstants.FileExtention.XLS;

		else if (HelperClass.current_environment().equals("production"))
			fileName = "Production.xls";
		String filePath = System.getProperty("user.dir")+"/TestData", sheetName = class_name;

		//Create a object of File class to open xlsx file
		File file = new File(filePath+"/"+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = null;
		//Find the file extension by spliting file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if (fileExtensionName.equals(".xlsx")){
			//If it is xlsx file then create object of XSSFWorkbook class
			workbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls")){
			//If it is xls file then create object of XSSFWorkbook class
			workbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet sheet = workbook.getSheet(sheetName);
		//Find number of rows workbook excel file
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();

		ArrayList<String> cell_values = new ArrayList<String>();
		//Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount+1; i++) {
			Row row = sheet.getRow(i);	  
			if (row!=null) {
				if (row.getCell(0).getStringCellValue().equals(method_name)) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						//Print excel data in console
						DataFormatter formatter = new DataFormatter();
						cell_values.add(formatter.formatCellValue(row.getCell(j)));
					}
				} else
					continue;
			}    
			//Create a loop to print cell values in a row
		}
		return cell_values;
	}
}
