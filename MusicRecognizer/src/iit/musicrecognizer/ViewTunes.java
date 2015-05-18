package iit.musicrecognizer;

import iit.musicrecognizer.adapter.TunesListAdapter;
import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Tune;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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
	private String tuneID;

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
				
				tuneID = tuneList.get(position).getTuneID();
				System.out.println(tuneID);
				
				final Dialog dialog = new Dialog(ViewTunes.this);
				final MediaPlayer player = new MediaPlayer();
				
				dialog.setContentView(R.layout.viewtunes_dialog);
				
				dialog.setTitle("View Tune");
				
				seekBar = (SeekBar) findViewById(R.id.viewTunesseekBar);
				Uri myUri = Uri.parse(host + tuneList.get(position).getFileurl()); 
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
				
				final TextView tuneTitle = (TextView) dialog.findViewById(R.id.txtViewTunesAddTitle);
				final TextView tuneArtist = (TextView) dialog.findViewById(R.id.txtViewTunesAddArtist);
				Button addResponse = (Button) dialog.findViewById(R.id.btnAddResponse);
				
				addResponse.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String title = tuneTitle.getText().toString();
						String artist = tuneArtist.getText().toString();
						new AddResponse(ViewTunes.this).execute(title, artist, tuneID);
						
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
						tune.setYear(obj.getString("year"));
						tune.setFileurl(obj.getString("url"));
						
						tuneList.add(tune);
						
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				adapter.notifyDataSetChanged();
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
	
	//inner class to add response
	private class AddResponse extends AsyncTask<String, Long, String>{
		
		private Context context;
		private ProgressDialog progressDialog;
		
		public AddResponse(Context context) {
			super();
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
		    try {
		        progressDialog = ProgressDialog.show(context, "Response", "Please Wait...Adding response", true);
		    } catch (final Throwable th) {
		        //TODO
		    }
		}

		@Override
		protected String doInBackground(String... params) {
			
			String tune_title = params[0];
			String artist = params[1];
			String userID = Runtime.getUserID();
			
			int urlID = R.string.url;
			String host = context.getResources().getString(urlID);
			
			String link = host + "addresponse.php";
			StringBuilder sb = new StringBuilder();
			
			try{
				String data = URLEncoder.encode("tune_title", "UTF-8") + "=" + URLEncoder.encode(tune_title, "UTF-8");
				data += "&" + URLEncoder.encode("artist", "UTF-8") + "=" + URLEncoder.encode(artist, "UTF-8");
				data += "&" + URLEncoder.encode("tuneID", "UTF-8") + "=" + URLEncoder.encode(tuneID, "UTF-8");
				data += "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
				
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
			    
			} catch (Exception e){
				return new String("Exception: " + e.getMessage());
			}
			
			return sb.toString();
		}
		
		protected void onProgressUpdate(Long... progress) {
		    //do something
		    super.onProgressUpdate(progress);
		}
		
		protected void onPostExecute(String result){
			progressDialog.dismiss();
			Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}
		
		
		
	}
    
}


