package com.example.kmg91.airquality;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;

public class detailview extends AppCompatActivity {
    TextView txt_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);
        this.setFinishOnTouchOutside(false);

        txt_detail = (TextView)findViewById(R.id.txt_content);

        HashMap<String, String> airinfo = new HashMap<String, String>();
        Intent intent = getIntent();
        airinfo = (HashMap<String,String>)intent.getSerializableExtra("detail_value");

        txt_detail.setTextSize(17.0f);
        txt_detail.setText("측정일시 : \t\t\t\t\t" + airinfo.get("air_date"));
        txt_detail.append("\n측정소 : \t\t\t\t\t\t" + airinfo.get("air_stename"));
        txt_detail.append("\n미세먼지 수치 : \t\t" + airinfo.get("air_maxindex") + "(" + airinfo.get("air_grade") + ")");
        txt_detail.append("\n이산화질소 : \t\t\t" + airinfo.get("air_nitro") + " ppm");
        txt_detail.append("\n오존 : \t\t\t\t\t\t\t" + airinfo.get("air_ozone") + " ppm");
        txt_detail.append("\n일산화탄소 : \t\t\t" + airinfo.get("air_carbon") + " ppm");
        txt_detail.append("\n아황산가스 : \t\t\t" + airinfo.get("air_sulf") + " ppm");
        txt_detail.append("\n미세먼지 : \t\t\t\t\t" + airinfo.get("air_pm10") + " ㎍/㎡");
        txt_detail.append("\n초미세먼지 : \t\t\t" + airinfo.get("air_pm25") + " ㎍/㎡");

    }

    public void OnClickClose(View view){
        finish();
    }
}
