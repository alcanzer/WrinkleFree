package biz.wrinklefree.wrinklefree;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class PickupActivity extends AppCompatActivity {

    //DatePicker mDatePicker;
    Date mDate;
    Spinner mSlot, mPickDates, mServiceType;
    Button mReq;
    int mTimeSlot, mSType;
    Calendar c;
    EditText mCoupon;
    CircularProgressButton mCouponCheck;
    private List<SlotList> mOptions;
    private List<ServiceTypeList> mList;
    private ArrayList<String> serviceList, slotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        final UserInfoPref pref = new UserInfoPref(getApplication());

        //mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        serviceList = new ArrayList<>();
        slotList = new ArrayList<>();
        mServiceType = findViewById(R.id.servicetype);
        mSlot = (Spinner) findViewById(R.id.spinner);
        mPickDates = findViewById(R.id.pickdates);
        mReq = (Button) findViewById(R.id.pickreq);
        mCoupon = (EditText) findViewById(R.id.couponET);
        mCouponCheck = findViewById(R.id.checkcoupon);

        mOptions = new ArrayList<>(Arrays.asList(Homepage.config.getSlotList()));
        mList = new ArrayList<>(Arrays.asList(Homepage.config.getServiceTypeList()));

        fillData();

        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        final List<String> dateString = new ArrayList<>();
        dateString.add(format.format(today));
        final List<Date> dates = new ArrayList<>();
        dates.add(today);
        c = Calendar.getInstance();
        c.setTime(today);
        for(int i = 1; i <= 2; i++){
            c.add(Calendar.DATE, i);
            Date  newDate = c.getTime();
            dates.add(newDate);
            dateString.add(format.format(newDate));
        }
        //mDatePicker.setMinDate(System.currentTimeMillis());
        //mDatePicker.setMaxDate(System.currentTimeMillis() + (1000*60*60*24*2));

        ArrayAdapter<String> adapterDate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dateString);


        mPickDates.setAdapter(adapterDate);

        mPickDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDate = dates.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mDate = dates.get(0);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, slotList);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSlot.setAdapter(adapter);

        mSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeSlot = Integer.parseInt(mOptions.get(i).getSlotId());
                Log.d("ItemSelected", mTimeSlot+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mTimeSlot = Integer.parseInt(mOptions.get(0).getSlotId());
                Log.d("ItemSelected", mTimeSlot+"");
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceList);

        mServiceType.setAdapter(adapter1);

        mServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSType = Integer.parseInt(mList.get(i).getServiceTypeId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSType = Integer.parseInt(mList.get(0).getServiceTypeId());
            }
        });

        mCouponCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCouponCheck.startAnimation();

                Toast.makeText(PickupActivity.this, mSType+" "+mCoupon.getText(), Toast.LENGTH_SHORT).show();

                JSONObject jObj = new JSONObject();

                try {
                    jObj.put("userId", pref.getKey("userID"));
                    jObj.put("couponCode", mCoupon.getText().toString().trim());
                    jObj.put("serviceTypeId", mSType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(SignInActivity.BASE_URL + "isvalidcoupon", jObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("validCoupon")){
                                mCouponCheck.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                        R.drawable.ic_action_name));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCouponCheck.revertAnimation();
                                    }
                                }, 2000);
                            }
                            else{
                                mCouponCheck.doneLoadingAnimation(Color.RED, BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                        R.drawable.ic_error));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCouponCheck.revertAnimation();
                                    }
                                }, 2000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mCouponCheck.revertAnimation();
                    }
                }));
            }
        });

        mReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setTime(mDate);
                long time = c.getTimeInMillis();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("userId", Integer.parseInt(pref.getKey("userID")));
                    obj.put("addressId", Integer.parseInt(pref.getKey("AddressId")));
                    obj.put("pickupSlotId", mTimeSlot);
                    obj.put("serviceTypeId", mSType);
                    obj.put("pickupDate", time);
                    obj.put("couponCode", mCoupon.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(SignInActivity.BASE_URL + "pickuprequest", obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(PickupActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PickupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });

    }

    public void fillData(){

        for(ServiceTypeList e : mList){
            serviceList.add(e.getServiceName());
        }

        for(SlotList e: mOptions){
            slotList.add(e.getSlotTiming());
        }

    }
}
