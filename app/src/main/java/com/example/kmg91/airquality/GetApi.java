package com.example.kmg91.airquality;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by kmg91 on 2016-11-16.
 */

public class GetApi {
    final String API_URL = "http://openAPI.seoul.go.kr:8088/526a5752456b6d67313034614a587464/xml/ListAirQualityByDistrictService/1/5/";
    HashMap<Integer, String> station = new LinkedHashMap<Integer, String>();
    HashMap<String, String> air_info = new HashMap<String, String>();

    public void initHashmap(){
        /*
        강남구, 강동구, 강북구, 강서구, 관악구, 광진구, 구로구, 금천구, 노원구,
                도봉구, 동대문구, 동작구, 마포구, 서대문구, 서초구, 성동구, 성북구, 송파구,
                양천구, 영등포구, 용산구, 은평구, 종로구, 중구, 중랑구
                */
        //station.put(0, "선택하세요");
        station.put(111261, "강남구");
        station.put(111251, "관악구");
        station.put(111274, "강동구");
        station.put(111212, "강서구");
        station.put(111141, "광진구");
        station.put(111221, "구로구");
        station.put(111281, "금천구");
        station.put(111311, "노원구");
        station.put(111171, "도봉구");
        station.put(111152, "동대문구");
        station.put(111241, "동작구");
        station.put(111201, "마포구");
        station.put(111191, "서대문구");
        station.put(111262, "서초구");
        station.put(111142, "성동구");
        station.put(111161, "성북구");
        station.put(111273, "송파구");
        station.put(111301, "양천구");
        station.put(111231, "영등포구");
        station.put(111131, "용산구");
        station.put(111181, "은평구");
        station.put(111123, "종로구");
        station.put(111121, "중구");
        station.put(111151, "중랑구");
    }

    // 정보 가져오는 영역
    public HashMap<String, String> getAirInfo(int code) {
        InputStream is = null;
        try {
                String tagName = "";
                URL url = new URL(API_URL + code);
                is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(is, "utf-8");
                int eventType = parser.getEventType();
                boolean isItemTag = false;


                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //eventtype : 2, start_tag (태그의 시작)
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();

                        if (tagName.equals("MSRDATE") || tagName.equals("MSRSTENAME")
                            || tagName.equals("MAXINDEX") || tagName.equals("GRADE")
                            || tagName.equals("NITROGEN") || tagName.equals("OZONE")
                            || tagName.equals("CARBON") || tagName.equals("SULFUROUS")
                            || tagName.equals("PM10") || tagName.equals("PM25"))
                            isItemTag = true;
                    }

                    // 텍스트로 이루어진 내용, evt : 3 (태그 사이의 텍스트)
                    else if (eventType == XmlPullParser.TEXT && isItemTag) {
                        if (tagName.equals("MSRDATE")) {
                            SimpleDateFormat formatToDate = new SimpleDateFormat("yyyyMMddHHmm");
                            SimpleDateFormat formatToString = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
                            try {
                                Date da = formatToDate.parse(parser.getText());
                                String da_str = formatToString.format(da);
                                air_info.put("air_date", da_str);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (tagName.equals("MSRSTENAME"))
                            air_info.put("air_stename", parser.getText());

                        if(tagName.equals("MAXINDEX"))
                            air_info.put("air_maxindex", parser.getText());

                        if(tagName.equals("GRADE"))
                            air_info.put("air_grade", parser.getText());

                        if(tagName.equals("NITROGEN"))
                            air_info.put("air_nitro", parser.getText());

                        if(tagName.equals("OZONE"))
                            air_info.put("air_ozone", parser.getText());

                        if(tagName.equals("CARBON"))
                            air_info.put("air_carbon", parser.getText());

                        if(tagName.equals("SULFUROUS"))
                            air_info.put("air_sulf", parser.getText());

                        if(tagName.equals("PM10"))
                            air_info.put("air_pm10", parser.getText());

                        if(tagName.equals("PM25"))
                            air_info.put("air_pm25", parser.getText());

                        isItemTag = false;
                    }

                    // event type : 2 (태그의 끝)
                    else if (eventType == XmlPullParser.END_TAG) {
                            isItemTag = false;
                    }
                    eventType = parser.next();  // evt 4
                }

                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                air_info = null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                air_info = null;
            }
            return air_info;
        }

}
