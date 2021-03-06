package iit.musicrecognizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        getActionBar().hide();
        
        Button loginButton = (Button) findViewById(R.id.welcomeLoginBtn);
        Button signupButton = (Button) findViewById(R.id.welcomeSignBtn);
        
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginScreen);
				
			}
		});
		
		signupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent signupScreen = new Intent(getApplicationContext(), SignupActivity.class);
				startActivity(signupScreen);
				
			}
		});
        
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
}
