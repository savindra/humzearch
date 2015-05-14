package iit.musicrecognizer.adapter;

import iit.musicrecognizer.R;
import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Tune;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TunesListAdapter extends BaseAdapter {
	
	private Activity activity;
	private LayoutInflater inflater;
	private List<Tune> tuneItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	

	public TunesListAdapter(Activity activity, List<Tune> tuneItems) {
		super();
		this.activity = activity;
		this.tuneItems = tuneItems;
	}

	@Override
	public int getCount() {
		return tuneItems.size();
	}

	@Override
	public Object getItem(int position) {
		return tuneItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null)
			convertView = inflater.inflate(R.layout.viewtunes_row_layout, null);
		
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.viewTunesThumbnail);
		
		TextView name = (TextView) convertView.findViewById(R.id.viewTunesName);
		TextView tuneID = (TextView) convertView.findViewById(R.id.viewTunesID);
		TextView tuneDetails = (TextView) convertView.findViewById(R.id.viewTunesDetails);
		TextView dateAdded = (TextView) convertView.findViewById(R.id.viewTunesDate);
		
		Tune t = tuneItems.get(position);
		
		thumbNail.setImageUrl(t.getAuthorImgUrl(), imageLoader);
		
		name.setText(t.getAuthor());
		
		tuneID.setText(t.getTuneID());
		
		tuneDetails.setText(t.getArtist() + ", " + t.getYear() + ", " + t.getLanguage() + ", " + t.getCountry());
		
		
		return convertView;
	}

}
