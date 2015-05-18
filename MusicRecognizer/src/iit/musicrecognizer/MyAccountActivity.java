package iit.musicrecognizer;

import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Address;
import iit.musicrecognizer.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccountActivity extends Activity {
	
	private TextView name, email, dateJoined, points, address, device;
	private Button addressAddEdit;
	private NetworkImageView img;
	private String userID;
	User user;
	Address objAddress;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private Dialog dialog;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_layout);
        
        name = (TextView) findViewById(R.id.txtMyAccountName);
        email = (TextView) findViewById(R.id.txtMyAccountEmail);
        dateJoined = (TextView) findViewById(R.id.txtMyAccountJoinDate);
        address = (TextView) findViewById(R.id.txtMyAccountAddress);
        device = (TextView) findViewById(R.id.txtMyAcountDevice);
        img = (NetworkImageView) findViewById(R.id.myAccountImg);
        points = (TextView) findViewById(R.id.txtMyAccountPoints);
        addressAddEdit = (Button) findViewById(R.id.btnMyAccountAddress);
         
        new UserDetails(MyAccountActivity.this).execute();
        
    }
	
	public void addEdit(View view){
		
		dialog = new Dialog(MyAccountActivity.this);
		dialog.setContentView(R.layout.address_dialog);
		dialog.setTitle("Add/Edit Address");
		
		TextView address1, address2, city, postcode;
		Spinner country;
		Button addEdit;
		
		address1 = (TextView) dialog.findViewById(R.id.txtMyAccountDialogAddress1);
		address2 = (TextView) dialog.findViewById(R.id.txtMyAccountDialogAddress2);
		city = (TextView) dialog.findViewById(R.id.txtMyAccountDialogCity);
		postcode = (TextView) dialog.findViewById(R.id.txtMyAccountDialogPostcode);
		country = (Spinner) dialog.findViewById(R.id.spinnerMyAccountDialogCounty);
		
		address1.setText(objAddress.getAddress1());
		address2.setText(objAddress.getAddress2());
		city.setText(objAddress.getCity());
		postcode.setText(objAddress.getPostcode());
		
		dialog.show();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class UserDetails extends AsyncTask<String, Long, String>{
    	
    	private Context context;
		private ProgressDialog progressDialog;

		public UserDetails(Context context) {
			super();
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
		    try {
		        progressDialog = ProgressDialog.show(context, "Response", "Loading...", true);
		    } catch (final Throwable th) {
		        //TODO
		    }
		}

		@Override
		protected String doInBackground(String... params) {
			
			String userID = Runtime.getUserID();
			
			int urlID = R.string.url;
			String host = context.getResources().getString(urlID);
			
			String link = host + "getuser.php";
			StringBuilder sb = new StringBuilder();
			
			try{
				String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
				
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
			    
			    try {
					JSONArray jsonResponse;
					jsonResponse = new JSONArray(sb.toString());
					String dateString;
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					user = new User();
					objAddress = new Address();
					for(int i=0; i<jsonResponse.length(); i++){
						JSONObject e = jsonResponse.getJSONObject(i);
						user.setName(e.getString("name"));
						user.setEmail(e.getString("email"));
						
						dateString = e.getString("date_added");
						date = formatter.parse(dateString);
						user.setDate_added(date);
						user.setImage(e.getString("image"));
						user.setPoints(e.getInt("points"));
						
						objAddress.setAddressID(e.getString("addressID"));
						objAddress.setAddress1(e.getString("address1"));
						objAddress.setAddress2(e.getString("address2"));
						objAddress.setCity(e.getString("city"));
						objAddress.setCountry(e.getString("country"));
						objAddress.setPostcode(e.getString("postcode"));
						
					}
					
				} catch(Exception e){
					e.printStackTrace();
				}
			    
			    
			} catch (Exception e) {
				return new String("Exception: " + e.getMessage());
			}
			
			return null;
		}
		
		protected void onProgressUpdate(Long... progress) {
		    //do something
		    super.onProgressUpdate(progress);
		}
		
		protected void onPostExecute(String result){
			int urlID = R.string.url;
			String host = context.getResources().getString(urlID);
			
			name.setText(user.getName());
			email.setText(user.getEmail());
			dateJoined.setText(user.getDate_added().toString());
			img.setImageUrl(host + user.getImage(), imageLoader);
			points.setText(user.getPoints() + " Points");
			
			String address1 = objAddress.getAddress1();
			String address2 = objAddress.getAddress2();
			String city = objAddress.getCity();
			String country = objAddress.getCountry();
			String postcode = objAddress.getPostcode();
			address.setText(address1 + ",\n" + address2 + ",\n" + city + ",\n" + country + " " + postcode);
			
			if(address1.equals("")){
				address.setText("Address not entered.");
			}
			
			device.setText(Build.MODEL);
			progressDialog.dismiss();
		}
    	
    }

}
