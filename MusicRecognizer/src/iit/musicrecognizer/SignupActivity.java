package iit.musicrecognizer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {
	
	private EditText emailField, nameField, passwordField;
	private TextView result;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        emailField = (EditText) findViewById(R.id.txtSignupEmail);
        nameField = (EditText) findViewById(R.id.txtSignupName);
        passwordField = (EditText) findViewById(R.id.txtSignupPass);
        result = (TextView) findViewById(R.id.txtSignupResult);
        
        
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
    
    public void signup(View view) throws NoSuchAlgorithmException{
    	String email = emailField.getText().toString();
    	String name = nameField.getText().toString();
    	String password = passwordField.getText().toString();
    	String salt = getSalt();
    	String securedPass = get_SHA_256_SecurePassword(password, salt);
    	
    	if(email.equals("") || name.equals("") || password.equals("")){
    		Toast.makeText(this, "Empty Fields.", Toast.LENGTH_SHORT).show();
    	}else {
    		new Signup(this, result).execute(email, name, securedPass, salt);
    	}
    	
    	
    }
    
    
    private String getSalt() throws NoSuchAlgorithmException{
    	SecureRandom sr = new SecureRandom().getInstance("SHA1PRNG");
    	byte[] salt = new byte[16];
    	sr.nextBytes(salt);
    	return salt.toString();
    	
    }
    
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

}
