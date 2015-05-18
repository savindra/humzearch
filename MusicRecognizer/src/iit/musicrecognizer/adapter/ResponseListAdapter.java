package iit.musicrecognizer.adapter;

import iit.musicrecognizer.R;
import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Responses;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResponseListAdapter extends BaseAdapter {
	
	private Activity activity;
	private LayoutInflater inflater;
	private List<Responses> responseItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ResponseListAdapter(Activity activity, List<Responses> responseItems) {
		super();
		this.activity = activity;
		this.responseItems = responseItems;
	}

	@Override
	public int getCount() {
		return responseItems.size();
	}

	@Override
	public Object getItem(int position) {
		return responseItems.get(position);
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
			convertView = inflater.inflate(R.layout.responses_row_layout, null);
		
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.responsesThumbnail);
		
		TextView message = (TextView) convertView.findViewById(R.id.responsesmsg);
		TextView date = (TextView) convertView.findViewById(R.id.responsesDate);
		
		Responses r = responseItems.get(position);
		
		thumbNail.setImageUrl(r.getUser().getImage(), imageLoader);
		message.setText(r.getUser().getName() + " added response to your tune. (Tune ID: " + r.getT().getTuneID() + ")");
		date.setText(r.getDate());
		
		return convertView;
	}

}
