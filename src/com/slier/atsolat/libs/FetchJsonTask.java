package com.slier.atsolat.libs;

import java.sql.Date;
import java.util.List;
import java.util.Locale;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import android.os.Handler;
import org.json.JSONObject;
import com.slier.atsolat.R;
import java.util.ArrayList;
import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONException;
import android.content.Context;
import android.app.ProgressDialog;
import java.text.SimpleDateFormat;

public class FetchJsonTask extends AsyncTask<String, Void, JSONObject>{

    private ProgressDialog dialog = null;
    private Context context = null;
    private long startTime = 0;
    private long endTime = 0;
    private int delayTime = 1200;

    public FetchJsonTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
        dialog = ProgressDialog.show(this.context, "Fetching Data", "Please Wait");
    };

    @Override
    protected JSONObject doInBackground(String... params) {
        JsonUrl jsonUrl = new JsonUrl();
        return jsonUrl.getJsonFromUrl(params[0]);
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {

        LinearLayout layoutContainer = (LinearLayout) ((Activity) this.context).findViewById(R.id.layoutBlockContainer);
        LinearLayout layoutNoConnection = (LinearLayout) ((Activity) this.context).findViewById(R.id.layouotBlockNoConnection);
        LinearLayout layoutContent = (LinearLayout) ((Activity) this.context).findViewById(R.id.layoutContent);
        ListView prayListView = (ListView) ((Activity) this.context).findViewById(R.id.listPrayTime);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)layoutContainer.getLayoutParams();

        if(jsonData == null) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    FetchJsonTask.this.dialog.dismiss();
                }
            };
            handler.postDelayed(runnable, FetchJsonTask.this.delayTime);

            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutContent.setVisibility(View.GONE);
            layoutNoConnection.setVisibility(View.VISIBLE);
            return;
        }else {
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutContent.setVisibility(View.VISIBLE);
            layoutNoConnection.setVisibility(View.GONE);
        }

        JSONArray times = null;
        List<String> prayTimes = new ArrayList<String>();
        String[] prayZones = this.context.getResources().getStringArray(R.array.pray_zone);
        try {
            times = jsonData.getJSONObject("response").getJSONArray("times");
            for (int i = 0; i < times.length(); i++ ) {
                Date date = new Date( Long.parseLong(times.getString(i)) * 1000L);
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
                prayTimes.add(prayZones[i] + " : " + formatter.format(date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> prayAdapter = new ArrayAdapter<String>( this.context,
                android.R.layout.simple_list_item_1,prayTimes
        );

        closeDialog(prayListView, prayAdapter);
    }

    protected void closeDialog(final ListView v, final ArrayAdapter a) {
        endTime = System.currentTimeMillis();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FetchJsonTask.this.dialog.dismiss();
                v.setAdapter(a);
            }
        };

        if(endTime - startTime < delayTime) {
            handler.postDelayed(runnable, delayTime - (endTime - startTime));
        }
        else {
            dialog.dismiss();
            v.setAdapter(a);
        }
    }
}