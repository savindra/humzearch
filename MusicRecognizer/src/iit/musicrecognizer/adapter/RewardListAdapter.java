package iit.musicrecognizer.adapter;

import iit.musicrecognizer.R;
import iit.musicrecognizer.RewardsActivity;
import iit.musicrecognizer.app.AppController;
import iit.musicrecognizer.model.Reward;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RewardListAdapter extends BaseAdapter {
	
	private Activity activity;
	private LayoutInflater inflater;
	private List<Reward> rewardItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	

	public RewardListAdapter(Activity activity, List<Reward> rewardItems) {
		super();
		this.activity = activity;
		this.rewardItems = rewardItems;
	}

	@Override
	public int getCount() {
		return rewardItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rewardItems.get(position);
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
			convertView = inflater.inflate(R.layout.rewards_row_layout, null);
		
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.rewardsThumbnail);
		
		TextView name = (TextView) convertView.findViewById(R.id.txtRewardName);
		TextView description = (TextView) convertView.findViewById(R.id.txtRewardDesc);
		TextView value = (TextView) convertView.findViewById(R.id.txtRewardValue);
		Button redeem = (Button) convertView.findViewById(R.id.btnRewardRedeem);
		
		final Reward r = rewardItems.get(position);
		
		thumbNail.setImageUrl(r.getImage(), imageLoader);
		name.setText(r.getName());
		description.setText(r.getDesc());
		value.setText(r.getValue() + " Points");
		
		redeem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(r.getName());
			}
		});
		
		return convertView;
	}
	

}
