package iit.musicrecognizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

public class Login extends AsyncTask<String, Void, String> {

	private TextView resultField;
	private Context context;
	String email;
	String password;
	
	public Login(Context context, TextView result) {
		this.context = context;
		resultField = result;
	}

	@Override
	protected String doInBackground(String... params) {
		email = params[0];
		password = params[1];
		
		String link = "http://192.168.1.2/humzearch/" + "login.php";
		System.out.println(link);
		StringBuilder sb = new StringBuilder();
		
		try {
			String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
		    wr.write( data ); 
		    wr.flush(); 
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = null;
		    
		    //=== Get JSON Object from server ===
		    while((line = reader.readLine()) != null){
		    	sb.append(line);
		    	break;
		    }
		    //=== Get JSON Object from server ===
		    
		    //=== Read JSON object data ===
		    String savedEmail = "", savedPass = null, salt = null;
		    try {
				JSONArray jsonResponse;
				jsonResponse = new JSONArray(sb.toString());
				
				for(int i=0; i<jsonResponse.length(); i++){
					JSONObject e = jsonResponse.getJSONObject(i);
					savedEmail = e.getString("email");
					savedPass  = e.getString("password");
					salt = e.getString("salt");
				}
				
			} catch(Exception e){
				e.printStackTrace();
			}
		    //=== Read JSON object data ===
		    
		    //=== Validate username and Password ==
		    if(savedEmail.equals("")){
		    	return new String("0");
		    } else {
		    	return validateLogin(savedEmail, savedPass, salt);
		    }
			
			
		} catch (Exception e) {
			return new String("Exception: " + e.getMessage());
		}
		
	}
	
	protected void onPostExecute(String result){
		if(result.equals("0")){
			this.resultField.setText("This email is not registered with humzearch.");
		} else if(result.equalsIgnoreCase("failed")){
			this.resultField.setText("Wrong email/password combination.");
		} else {
			Intent mainMenuScreen = new Intent(context, MainMenu.class);
			context.startActivity(mainMenuScreen);
		}
	}
	
	//=== Validates username and Password
	public String validateLogin(String savedEmail, String savedPass, String salt){
		String encryptedPass = get_SHA_256_SecurePassword(password, salt);
		if(savedEmail.equals(email) &&  savedPass.equals(encryptedPass)){
			return new String("success");
		} else {
			return new String("failed");
		}
	}
    
	//=== 256 hash ===
    public String get_SHA_256_SecurePassword(String enteredPass, String salt){
		
		String generatedPass = null;
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(enteredPass.getBytes());
			StringBuilder sb = new StringBuilder();
			
			for(int i=0;i<bytes.length;i++)
				sb.append(Integer.toString( (bytes[i] & 0xff) + 0x100, 16).substring(1) );
			
			generatedPass = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		return generatedPass;
	}
	
	public static String getURL(){
		Properties prop = new Properties();
		InputStream input = null;
		try{
			input = new FileInputStream("config.properties");
			prop.load(input);
			String url = (prop.getProperty("url"));
			return url;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
