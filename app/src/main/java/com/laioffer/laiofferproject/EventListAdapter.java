package com.laioffer.laiofferproject;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by weiguang on 9/13/17.
 */

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList;
    private DatabaseReference databaseReference;
    private LayoutInflater inflater;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"; //Sample Ad given by Google
    private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544-3347511713";     // Should be change in Public Application
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ADS = 1;
    private static final int TYPE_MAX_COUNT = TYPE_ADS + 1;
    private AdLoader.Builder builder;
    private TreeSet mSperatorSet = new TreeSet();

    public EventListAdapter(Context context) {
        this.context = context;
        eventList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //get the instance of layout

        int count = 0;
        for (int i = 0; i < eventList.size(); i++) {
            if (i % 6 == 1) {
                this.eventList.add(new Event());
                mSperatorSet.add(count + i); //check whether ADs showed in the right place;
                count++;
            }
            this.eventList.add(eventList.get(i));
        }

        //initial Ads
        MobileAds.initialize(context, ADMOB_APP_ID);
        builder = new AdLoader.Builder(context, ADMOB_AD_UNIT_ID);
    }
    @Override
    public int getItemViewType(int position) {
        return mSperatorSet.contains(position) ? TYPE_ADS : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Event getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView title;
        TextView location;
        TextView description;
        TextView time;
        ImageView imageView;

        ImageView img_view_good;
        ImageView img_view_comments;
        //ImageView img_view_repost;

        TextView good_number;
        TextView comments_number;
        //TextView repost_number;

        FrameLayout frameLayout; //ADs Item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        int type = getItemViewType(position);
        if (rowView == null) {
            ViewHolder viewholder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    rowView = inflater.inflate(R.layout.event_list_item, parent, false); //load layout
                    viewholder.title = (TextView) rowView.findViewById(R.id.event_item_title);
                    viewholder.location = (TextView) rowView.findViewById(R.id.event_item_location);
                    viewholder.description = (TextView) rowView.findViewById(R.id.event_item_description);
                    viewholder.time = (TextView) rowView.findViewById(R.id.event_item_time);
                    viewholder.imageView = (ImageView) rowView.findViewById(R.id.event_item_img);
                    viewholder.img_view_good = (ImageView) rowView.findViewById(R.id.event_good_img);
                    viewholder.img_view_comments = (ImageView) rowView.findViewById(R.id.event_comment_img);
                   // viewholder.img_view_repost = (ImageView) rowView.findViewById(R.id.event_repost_img);
                    viewholder.good_number = (TextView) rowView.findViewById(R.id.event_good_num);
                    viewholder.comments_number = (TextView) rowView.findViewById(R.id.event_comment_num);
                    //viewholder.repost_number = (TextView) rowView.findViewById(R.id.event_repost_num);
                    break;
                case TYPE_ADS:
                    rowView = inflater.inflate(R.layout.ads_container_layout, parent, false);
                    viewholder.frameLayout = (FrameLayout) rowView.findViewById(R.id.native_ads_container);
                    break;
            }
                rowView.setTag(viewholder); // connect View object and Class
        }
        final ViewHolder holder = (ViewHolder) rowView.getTag(); //

        if(type == TYPE_ADS) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
               @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                   NativeContentAdView adView = (NativeContentAdView) inflater.inflate(R.layout.ads_content, null);
                   addContentView(ad, adView);
                   holder.frameLayout.removeAllViews();
                   holder.frameLayout.addView(adView);
               }
            });
            NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
            builder.withNativeAdOptions(adOptions);

            //builder loader and load advertisements
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build()); // add AD into local view;
        } else {
            final Event event = eventList.get(position);


            holder.title.setText(event.getTitle());
            String[] locations = event.getLocation().split(",");
            try {
                holder.location.setText(locations[1] + ',' + locations[2]);
            } catch (Exception e) {
                holder.location.setText("Wrong Location Type");
            }
            holder.description.setText(event.getDescription());
            holder.time.setText(Utilities.timeTransformer(event.getTime()));

            if (event.getImgUri() != null) {
                final String uri = event.getImgUri();
                holder.imageView.setVisibility(View.VISIBLE);
                new AsyncTask<Void, Void, Bitmap>() {
                    // get the image from BG thread
                    protected Bitmap doInBackground(Void... params) {
                        return Utilities.getBitmapFromURL(uri);
                    }

                    // show image got from BG in UI
                    protected void onPostExecute(Bitmap bitmap) {
                        holder.imageView.setImageBitmap(bitmap);
                    }
                }.execute();
            }

            holder.good_number.setText(String.valueOf(event.getGood()));
            holder.comments_number.setText(String.valueOf(event.getCommendNumber()));
            //holder.repost_number.setText(String.valueOf(event.getRepost()));

            holder.img_view_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Event recordEvent = snapshot.getValue(Event.class); // get one event
                                if (recordEvent.getId().equals(event.getId())) {
                                    int number = recordEvent.getGood();
                                    holder.good_number.setText(String.valueOf(number + 1));
                                    snapshot.getRef().child("good").setValue(number + 1);
                                    event.setGood(number+1);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }
        return rowView;
    }

    // add AD's content into AdView
    private void addContentView(NativeContentAd ad, NativeContentAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.ads_headline));
        adView.setImageView(adView.findViewById(R.id.ads_image));
        adView.setBodyView(adView.findViewById(R.id.ads_body));
        adView.setAdvertiserView(adView.findViewById(R.id.ads_advertiser));

        ((TextView)adView.getHeadlineView()).setText(ad.getHeadline());
        ((TextView)adView.getBodyView()).setText(ad.getBody());
        ((TextView)adView.getAdvertiserView()).setText(ad.getAdvertiser());

        List<NativeAd.Image> images = ad.getImages();
        if (images.size() > 0) {
            ((ImageView)adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }
        adView.setNativeAd(ad);
    }

}
