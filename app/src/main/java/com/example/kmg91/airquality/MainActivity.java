package com.example.kmg91.airquality;
//import android.app.AlertDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemSelectedListener {
    final String bad = "장시간 또는 무리한 실외활동 제한\n특히 눈이 아픈 증상이 있거나, 기침이나 목의 통증으로 불편한 사람은 실외활동 자제";
    final String normal = "실외활동시 특별히 행동에 제약을 받을 필요는 없지만 몸상태에 따라 유의하여 활동";
    final String good = "춥거나 덥다고 실내에만 계시지 말고\n밖으로 나가서 맑은 공기를 쐬는것도 좋습니다.\n(비오는날 빼고..)";
    private long pressedTime;
    private int code;
    HashMap<String, String> airinfo = new HashMap<String, String>();
    Handler handler = new Handler();
    GetApi getApi = new GetApi();
    TextView txt_status, txt_label, txt_info;
    Button howto, detail;
    Spinner spin_gu;
    ImageView imgview;
    ConnectivityManager conManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Networkchecking();
        spin_gu = (Spinner) findViewById(R.id.spin_Gu);
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_label = (TextView) findViewById(R.id.textView2);
        txt_info = (TextView) findViewById(R.id.txt_info);
        imgview = (ImageView) findViewById(R.id.imageView);
        howto = (Button) findViewById(R.id.btn_howto);
        detail = (Button) findViewById(R.id.btn_detail);
        howto.setTextColor(Color.WHITE);
        detail.setTextColor(Color.WHITE);
        howto.setTextSize(17f);
        detail.setTextSize(17f);
        txt_info.setText("상단에서 조회하실 지역을 선택하세요.");
        txt_info.setTextSize(20.0f);
        txt_info.setGravity(Gravity.CENTER_HORIZONTAL);
        getApi.initHashmap();
        String[] values = getApi.station.values().toArray(new String[0]);
        spinnerAdapter spin = new spinnerAdapter(this, android.R.layout.simple_spinner_item);
        spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.addAll(values);
        spin.add("선택하세요");
        spin_gu.setAdapter(spin);
        spin_gu.setSelection(spin.getCount(), false);
        spin_gu.setOnItemSelectedListener(this);

    }

    public void Networkchecking(){
        conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // network 상태확인
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();

        if (null == netInfo) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("네트워크 오류")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("네트워크 연결 상태를 확인해주세요.\n프로그램을 종료합니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
        Log.e("push", "fin");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void OnClickHowTo(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cleanair.seoul.go.kr/safety_guide.htm?method=dust"));
        startActivity(intent);
    }

    public void OnClickDetail(View view) {
        Intent detail_intent = new Intent(getApplicationContext(), detailview.class);
        detail_intent.putExtra("detail_value", airinfo);
        startActivity(detail_intent);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            long sec = System.currentTimeMillis() - pressedTime;
            if (sec > 2000)
                pressedTime = 0;
            else
                finish();
        }
    }

    @Override
    public void run() {

        airinfo = getApi.getAirInfo(code);
        handler.post(new Runnable() {
            @Override
            public void run() {
                txt_status.setGravity(Gravity.CENTER);
                txt_status.setTextSize(40.0f);
                txt_info.setGravity(Gravity.CENTER);
                howto.setVisibility(View.VISIBLE);
                detail.setVisibility(View.VISIBLE);
                String dust = "\n미세먼지 수치 : " + airinfo.get("air_maxindex");
                SpannableString content = new SpannableString(dust);
                content.setSpan(new RelativeSizeSpan(0.5f), 0, dust.length(), 0);

                if (airinfo.get("air_grade").equals("나쁨")) {

                    txt_status.setTextColor(Color.rgb(255, 94, 0));
                    txt_status.setText("나쁨");
                    txt_status.append(content);

                    txt_info.setText(bad);
                    imgview.setImageResource(R.drawable.bad);
                    imgview.setColorFilter(Color.rgb(255, 94, 0));
                }

                if (airinfo.get("air_grade").equals("")) {
                    txt_status.setTextColor(Color.rgb(237, 210, 0));
                    txt_status.setText("보통");
                    txt_status.append(content);
                    txt_info.setText(normal);
                    imgview.setImageResource(R.drawable.normal);
                    imgview.setColorFilter(Color.rgb(237, 210, 0));
                }

                if (airinfo.get("air_grade").equals("좋음")) {
                    txt_status.setTextColor(Color.rgb(108, 192, 255));
                    txt_status.setText("좋음");
                    txt_status.append(content);
                    txt_info.setText(good);
                    imgview.setImageResource(R.drawable.good);
                    imgview.setColorFilter(Color.rgb(108, 192, 255));
                }
            }
        });
    }
}
