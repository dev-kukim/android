package com.example.kmg91.airquality;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by kmg91 on 2016-11-17.
 */

public class spinnerAdapter extends ArrayAdapter {

    public spinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = super.getCount();
        return count>0 ? count-1 : count ;

    }


}
