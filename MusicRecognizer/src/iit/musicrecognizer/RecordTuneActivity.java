package iit.musicrecognizer;

import iit.musicrecognizer.model.Tune;
import iit.musicrecognizer.util.AndroidMultiPartEntity;
import iit.musicrecognizer.util.AndroidMultiPartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class RecordTuneActivity extends Activity {
	
	private static final String TAG = RecordTuneActivity.class.getSimpleName();
	private MediaRecorder myAudioRecorder;
	private String outputFile = null;
	private Button start,stop,play, upload;
	private Spinner languageSpinner, countrySpinner;
	private ProgressBar progressBar;
	private TextView txtPercentage, txtArtist, txtYear;
	private Tune tune;
	long totalSize = 0;
	
	 
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordtune_layout);
        
        start = (Button)findViewById(R.id.btnStartRecord);
        stop = (Button)findViewById(R.id.btnStopRecord);
        play = (Button)findViewById(R.id.btnPlayRecord);
        upload = (Button) findViewById(R.id.btnUploadTune);
        
        languageSpinner = (Spinner) findViewById(R.id.languagespinner);
        countrySpinner = (Spinner) findViewById(R.id.spinnerRecordTuneCountry);
        
        txtArtist = (TextView) findViewById(R.id.txtRecordTuneArtist);
        txtYear = (TextView) findViewById(R.id.txtRecordTuneYear);
        txtPercentage = (TextView) findViewById(R.id.txtUploadPercentage);
        
        progressBar = (ProgressBar) findViewById(R.id.uploadprogressBar);

        stop.setEnabled(false);
        play.setEnabled(false);


    }
	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 
	 
	 public void record(View view){
		 	outputFile = getOutputMediaFileUri();
	        myAudioRecorder = new MediaRecorder();
	        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	        myAudioRecorder.setOutputFile(outputFile);
	        
	        try {
	            myAudioRecorder.prepare();
	            myAudioRecorder.start();
	        } catch (IllegalStateException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        start.setEnabled(false);
	        stop.setEnabled(true);
	        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

	 }
	 
    public void stop(View view){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;
        stop.setEnabled(false);
        play.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                Toast.LENGTH_LONG).show();
    }
    
    public void play(View view) throws IllegalArgumentException,
	    SecurityException, IllegalStateException, IOException{
	
		MediaPlayer m = new MediaPlayer();
		m.setDataSource(outputFile);
		m.prepare();
		m.start();
		Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
	}
    
	public void upload(View view){
		tune = new Tune();
		tune.setArtist(String.valueOf(txtArtist.getText()));
		tune.setLanguage(String.valueOf(languageSpinner.getSelectedItem()));
		tune.setCountry(String.valueOf(countrySpinner.getSelectedItem()));
		tune.setYear(String.valueOf(txtYear.getText()));
		
		if(outputFile == null) {
			Toast.makeText(this, "No recorded audio found.", Toast.LENGTH_SHORT).show();;
		} else {
			new uploadFileToServer().execute();
		}
	}
	
	public String getOutputMediaFileUri() {
        return (getOutputMediaFile()).getAbsolutePath();
    }
	
	private static File getOutputMediaFile(){
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "/humzearch");
		
		// Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + "humzearch" + " directory");
                return null;
            }
        }
        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        
        File mediaFile;
        mediaFile = new File(mediaStorageDir + File.separator + "audio_" + timeStamp + ".3gp");
        try {
			mediaFile.createNewFile();
			Log.d(TAG, "file created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return mediaFile;
	}
	
	
	
	//upload file to server inner class
	private class uploadFileToServer extends AsyncTask<Void, Integer, String>{

		@Override
		protected void onPreExecute() {
			progressBar.setProgress(0);
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(values[0]);
			txtPercentage.setText(String.valueOf(values[0]) + "%");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}


		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;
			int urlID = R.string.url;
			String host = RecordTuneActivity.this.getResources().getString(urlID);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(host + "uploadtune.php");
			
			try{
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {
					
					@Override
					public void transferred(long num) {
						publishProgress((int) ((num / (float) totalSize) * 100));						
					}
				});
				
				File sourceFile = new File(outputFile);
				
				entity.addPart("tune", new FileBody(sourceFile));
				entity.addPart("userID", new StringBody("2"));
				entity.addPart("artist", new StringBody(tune.getArtist()));
				entity.addPart("language", new StringBody(tune.getLanguage()));
				entity.addPart("country", new StringBody(tune.getCountry()));
				entity.addPart("year", new StringBody( String.valueOf(tune.getYear() ) ) );
				Log.d(TAG, outputFile);
				
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				
				int statusCode = response.getStatusLine().getStatusCode();
				
				if(statusCode == 200){
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: " + statusCode;
				}
				
			} catch (ClientProtocolException e){
				responseString = e.toString();
			} catch (Exception e){
				responseString = e.toString();
				e.printStackTrace();
			}
			outputFile = null;
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);
			showAlert(result);
			super.onPostExecute(result);
		}

		private void showAlert(String result) {
			Toast.makeText(RecordTuneActivity.this, result, Toast.LENGTH_LONG).show();
			Log.d(TAG, result);		
		}
		
	}

}


