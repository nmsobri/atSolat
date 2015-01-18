package com.slier.atsolat.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.Spinner;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.slier.atsolat.R;
import com.slier.atsolat.dialog.About;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.widget.AdapterView.OnItemSelectedListener;

public class Setting extends ActionBarActivity{

    protected static final String PREFS_NAME = "Peferences";
    protected String selectedState = null;
    protected String selectedDistrict = null;
    protected String[] listOfDistrictCode = null;
    protected String selectedDistrictCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        String startingState = settings.getString("state", "Johor");
        final String startingDistrict = settings.getString("district", "Batu Pahat");

        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setContentView(R.layout.setting);

        Spinner stateSpinner = (Spinner) this.findViewById(R.id.spinnerState);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, R.layout.spinner);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setSelection(stateAdapter.getPosition(startingState));

        final Spinner districtSpinner = (Spinner) this.findViewById(R.id.spinnerDistrict);

        stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {

                int[] districts = {
                        R.array.district_johor,R.array.district_kedah,R.array.district_kelantan,
                        R.array.district_melaka,R.array.district_negeri_sembilan,R.array.district_pahang,
                        R.array.district_perak,R.array.district_perlis,R.array.district_pulau_pinang,
                        R.array.district_sabah,R.array.district_sarawak,R.array.district_selangor,
                        R.array.district_terengganu,R.array.district_wilayah_persekutuan
                };

                int[] districtCode = {
                        R.array.johor_code,R.array.kedah_code,R.array.kelantan_code,
                        R.array.melaka_code,R.array.negeri_sembilan_code,R.array.pahang_code,
                        R.array.perak_code,R.array.perlis_code,R.array.pulau_pinang_code,
                        R.array.sabah_code,R.array.sarawak_code,R.array.selangor_code,
                        R.array.terengganu_code,R.array.wilayah_persekutuan_code
                };

                listOfDistrictCode = Setting.this.getResources().getStringArray(districtCode[position]);

                ArrayAdapter<CharSequence> districtaAdapter = new ArrayAdapter<CharSequence>(Setting.this,
                        R.layout.spinner,
                        Setting.this.getResources().getStringArray(districts[position])
                );

                districtaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                districtaAdapter.notifyDataSetChanged();
                districtSpinner.setAdapter(districtaAdapter);
                districtSpinner.setSelection(districtaAdapter.getPosition(startingDistrict));
                Setting.this.selectedState = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        districtSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Setting.this.selectedDistrictCode = Setting.this.listOfDistrictCode[position];
                Setting.this.selectedDistrict = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button buttonSave = (Button) this.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("state", Setting.this.selectedState);
                editor.putString("district", Setting.this.selectedDistrict);
                editor.putString("districtCode", Setting.this.selectedDistrictCode);
                editor.commit();
                Toast.makeText(Setting.this, "Preferences saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                this.showAboutAlert();
                break;
            case R.id.exit:
                this.exitApp();
                break;
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    protected void showAboutAlert() {
        About about = new About();
        about.show(getFragmentManager(), "about alert");
    }

    protected void exitApp() {
        Setting.this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static String getPrefsName() {
        return PREFS_NAME;
    }
}