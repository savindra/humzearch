package iit.musicrecognizer;

import iit.musicrecognizer.adapter.TunesListAdapter;
import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Tune;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTunes extends Activity{
	
	private static final String TAG  = ViewTunes.class.getSimpleName();
	
	int urlID;
	String host;
	private String url;
	private ProgressDialog pDialog;
	private List<Tune> tuneList = new ArrayList<Tune>();
	private ListView listView;
	private TunesListAdapter adapter;
	private SeekBar seekBar;
	private MediaPlayer mediaPlayer;
	Handler seekHandler = new Handler();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtunes_layout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        urlID = R.string.url;
        host = this.getResources().getString(urlID);
        url = host + "viewtunes.php";
        
        listView = (ListView)findViewById(R.id.viewTunesListView);
        adapter = new TunesListAdapter(this, tuneList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView t = (TextView) view.findViewById(R.id.viewTunesName);
				String s = t.getText().toString();
				
				final Dialog dialog = new Dialog(ViewTunes.this);
				final MediaPlayer player = new MediaPlayer();
				
				dialog.setContentView(R.layout.viewtunes_dialog);
				
				dialog.setTitle("View Tune");
				
				seekBar = (SeekBar) findViewById(R.id.viewTunesseekBar);
				Uri myUri = Uri.parse(host + "monster.mp3"); 
				try{
					mediaPlayer = new MediaPlayer();
				    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				    mediaPlayer = MediaPlayer.create(ViewTunes.this, myUri);
				    seekBar.setMax(mediaPlayer.getDuration());
				    seekUpdate();
				    //mediaPlayer.prepare();
				} catch (Exception e ){
					e.printStackTrace();
				}
				
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						mediaPlayer.stop();
						
					}
				});
				
				
				Button play = (Button) dialog.findViewById(R.id.viewTunesPlay);
				
				play.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mediaPlayer.start();
					}
				});				
				
			}
		});
        
        pDialog = new ProgressDialog(this);
        
        pDialog.setMessage("Loading...");
        pDialog.show();
        
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
        
        JsonArrayRequest tuneReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){

			@Override
			public void onResponse(JSONArray response) {
				Log.d(TAG, response.toString());
				hidePDialog();
				
				for(int i=0; i<response.length(); i++){
					try{
						JSONObject obj = response.getJSONObject(i);
						Tune tune = new Tune();
						tune.setArtist(obj.getString("artist"));
						tune.setAuthor(obj.getString("name"));
						tune.setAuthorImgUrl(host + obj.getString("img"));
						tune.setCountry(obj.getString("country"));
						tune.setDate_added(obj.getString("date_added"));
						tune.setLanguage(obj.getString("language"));
						tune.setTuneID(obj.getString("tuneID"));
						tune.setYear(obj.getInt("year"));
						
						tuneList.add(tune);
						
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}
        }, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();	
					}
       });
        
        AppController.getInstance().addToRequestQueue(tuneReq);
        
	}
	
	Runnable run = new Runnable(){
		@Override
		public void run() {
			seekUpdate();
			
		}
	};
	
	public void seekUpdate(){
		seekBar.setProgress(mediaPlayer.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}

	
	private void hidePDialog() {
		if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
		
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
        
        switch (item.getItemId()) {
        	case android.R.id.home:
        		NavUtils.navigateUpFromSameTask(this);
        			return true;
        }	
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
       moveTaskToBack(true);
       ViewTunes.this.finish();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}
    
}


