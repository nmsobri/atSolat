package com.slier.atsolat.activity;

import java.util.Date;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.slier.atsolat.R;
import java.text.DateFormat;
import android.widget.Button;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import com.slier.atsolat.dialog.About;
import android.content.SharedPreferences;
import com.slier.atsolat.libs.FetchJsonTask;
import android.support.v7.app.ActionBarActivity;

public class Main extends ActionBarActivity{

    final private String apiUrl = "http://mpt.i906.my/mpt.json?code=%s&filter=%s";

    protected String districtCode = null;

    protected int apiFilter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        SharedPreferences settings = getSharedPreferences(Setting.getPrefsName(), 0);

        TextView textState = (TextView) this.findViewById(R.id.textStateValue);
        textState.setText(settings.getString("state", "Johor"));

        TextView textDistrict = (TextView) this.findViewById(R.id.textDistrictValue);
        textDistrict.setText(settings.getString("district", "Batu Pahat"));

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        TextView textDate = (TextView) this.findViewById(R.id.TextDateValue);
        textDate.setText(dateFormat.format(date));

        Button buttonRetry = (Button) this.findViewById(R.id.btnRetryConnection);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main.this.fetchJson();
            }
        });

        this.districtCode = settings.getString("districtCode", "jhr-0");
        fetchJson();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getSharedPreferences(Setting.getPrefsName(), 0);
        String districtCode = settings.getString("districtCode", "jhr-0");

        if(this.districtCode != districtCode) {
            TextView textState = (TextView) this.findViewById(R.id.textStateValue);
            textState.setText(settings.getString("state", "Unknown"));

            TextView textDistrict = (TextView) this.findViewById(R.id.textDistrictValue);
            textDistrict.setText(settings.getString("district", "Unknown"));

            FetchJsonTask jsonTask = new FetchJsonTask(this);
            jsonTask.execute(String.format(apiUrl, districtCode, apiFilter));
            this.districtCode = districtCode;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.setting:
                this.startActivity(new Intent("com.slier.atsolat.SETTING"));
                break;
            case R.id.about:
                this.showAboutAlert();
                break;
            case R.id.exit:
                this.exitApp();
                break;
        }
        return true;
    }

    protected void showAboutAlert() {
        About about = new About();
        about.show(getFragmentManager(), "about");
    }

    protected void exitApp() {
        Main.this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void fetchJson() {
        FetchJsonTask jsonTask = new FetchJsonTask(this);
        jsonTask.execute(String.format(apiUrl, districtCode, apiFilter));
    }

}
