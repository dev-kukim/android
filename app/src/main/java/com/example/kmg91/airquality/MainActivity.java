package com.example.kmg91.airquality;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.TextView;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemSelectedListener {
    final String bad = "장시간 또는 무리한 실외활동 제한\n특히 눈이 아픈 증상이 있거나, 기침이나 목의 통증으로 불편한 사람은 실외활동 자제";
    final String normal = "실외활동시 특별히 행동에 제약을 받을 필요는 없지만 몸상태에 따라 유의하여 활동";
    final String good = "";
    int code;
    HashMap<String, String> airinfo = new HashMap<String, String>();
    Handler handler = new Handler();
    GetApi getApi = new GetApi();
    TextView txt_status, txt_label, txt_info;
    Spinner spin_gu;
    ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        spin_gu = (Spinner)findViewById(R.id.spin_Gu);
        txt_status = (TextView)findViewById(R.id.txt_status);
        txt_label = (TextView)findViewById(R.id.textView2);
        txt_info = (TextView)findViewById(R.id.txt_info);
        imgview = (ImageView)findViewById(R.id.imageView);
        //imgview.setVisibility(View.GONE);
       // SpannableString content = new SpannableString("");
      //  content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        txt_info.setText("상단에서 조회하실 지역을 선택하세요.");
        txt_info.setTextSize(20.0f);
        txt_info.setGravity(Gravity.CENTER_HORIZONTAL);

        getApi.initHashmap();

        String[] values = getApi.station.values().toArray(new String[0]);
        spinnerAdapter spin = new spinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, values);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.addAll(values);
        //adapter.add("선택하세요");
        spin.addAll(values);
        spin.add("선택하세요");
        //spin_gu.setAdapter(adapter);
        //spin_gu.setSelection(adapter.getCount());

        spin_gu.setAdapter(spin);
        spin_gu.setSelection(spin.getCount(), false);
        spin_gu.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Thread thread = new Thread(this);
        for (Object o : getApi.station.keySet()) {
            if (getApi.station.get(o).equals(spin_gu.getSelectedItem())) {
                code = Integer.parseInt(o.toString());
            }
        }
        Log.e("push", "pushed");
        thread.start();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void run() {
        airinfo = getApi.getAirInfo(code);
        handler.post(new Runnable() {
             @Override
             public void run() {
                 Log.e("set", "before set text");
                 String dust = "\n미세먼지 수치 : " + airinfo.get("air_maxindex");
                 /*
                 txt_info.setGravity(Gravity.CENTER_VERTICAL);
                 txt_info.setText("측정일시 : " + airinfo.get("air_date"));
                 txt_info.append("\n측정소 : " + airinfo.get("air_stename"));
                 txt_info.append("\n환경지수 : " + airinfo.get("air_maxindex") + "(" + airinfo.get("air_grade") + ")");
                 txt_status.append("\n이산화질소 : " + airinfo.get("air_nitro") + " ppm");
                 txt_status.append("\n오존 : " + airinfo.get("air_ozone") + " ppm");
                 txt_status.append("\n일산화탄소 : " + airinfo.get("air_carbon") + " ppm");
                 txt_status.append("\n아황산가스 : " + airinfo.get("air_sulf") + " ppm");
                 txt_status.append("\n미세먼지 : " + airinfo.get("air_pm10") + " ㎍/㎡");
                 txt_status.append("\n초미세먼지 : " + airinfo.get("air_pm25") + " ㎍/㎡");
                 */
                 Log.e("set", "after set text");

                 //View view = getWindow().getDecorView();
                   if(airinfo.get("air_grade").equals("나쁨")) {
                     //  view.setBackgroundColor(Color.parseColor("#ff0000"));
                       txt_status.setGravity(Gravity.CENTER);
                       txt_status.setTextSize(40.0f);
                       txt_status.setTextColor(Color.rgb(255,94,0));
                       txt_status.setText("나쁨");
                       SpannableString content = new SpannableString(dust);
                       content.setSpan(new RelativeSizeSpan(0.5f),0,dust.length(), 0);
                       txt_status.append(content);
                       txt_info.setGravity(Gravity.CENTER);
                       txt_info.setText(bad);

                       imgview.setImageResource(R.drawable.bad);
                       imgview.setColorFilter(Color.rgb(255,94,0));
                     //  txt_label.setTextColor(Color.WHITE);
                      // txt_status.setTextColor(Color.WHITE);
                   }

                 if(airinfo.get("air_grade").equals("보통")) {
                     txt_status.setGravity(Gravity.CENTER);
                     txt_status.setTextSize(40.0f);
                     txt_status.setTextColor(Color.rgb(237,210,0));
                     txt_status.setText("보통");
                     SpannableString content = new SpannableString(dust);
                     content.setSpan(new RelativeSizeSpan(0.5f),0,dust.length(), 0);
                     txt_status.append(content);
                     txt_info.setGravity(Gravity.CENTER);
                     txt_info.setText(normal);

                     imgview.setImageResource(R.drawable.normal);
                     imgview.setColorFilter(Color.rgb(237,210,0));
                 }

                 if(airinfo.get("air_grade").equals("좋음")) {
                    // view.setBackgroundColor(Color.parseColor("#6FD7FF"));
                     txt_label.setTextColor(Color.BLACK);
                     txt_status.setTextColor(Color.BLACK);
                 }
             }
        });
    }
}
