package iit.musicrecognizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class Signup extends AsyncTask<String, Void, String> {

	private TextView resultField;
	private Context context;
	
	public Signup(Context context, TextView result) {
		this.context = context;
		this.resultField = result;
	}
	
	protected void onPreExecute(){

	}

	@Override
	protected String doInBackground(String... params) {
		try{
			String email = params[0];
			String name = params[1];
			String password = params[2];
			String salt = params[3];
			
			String link = "http://192.168.1.2/humzearch/signup.php";
			String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
			data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
			data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
			data += "&" + URLEncoder.encode("salt", "UTF-8") + "=" + URLEncoder.encode(salt, "UTF-8");
			
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while((line = reader.readLine()) != null){
				sb.append(line);
				break;
			}
			return sb.toString();
	
		} catch (Exception e){
			return new String("Exception: " + e.getMessage());
		}
	}
	
	protected void onPostExecute(String result){
		if(result.equalsIgnoreCase("true") ){
			this.resultField.setText("Registration Completed.You may login now.");
		} else{
			this.resultField.setText("Registration Failed");
		}
	}

}
