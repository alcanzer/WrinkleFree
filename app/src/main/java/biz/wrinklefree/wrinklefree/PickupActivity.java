package biz.wrinklefree.wrinklefree;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PickupActivity extends AppCompatActivity {

    DatePicker mDatePicker;
    Date mDate;
    Spinner mSlot;
    Button mReq;
    int mTimeSlot;
    Calendar c;
    EditText mCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mSlot = (Spinner) findViewById(R.id.spinner);
        mReq = (Button) findViewById(R.id.pickreq);
        mCoupon = (EditText) findViewById(R.id.couponET);

        List<String> mOptions = new ArrayList<>();
        mOptions.add("5.00 PM - 7.00 PM");
        mOptions.add("7.00 PM - 9.00 PM");
        mOptions.add("10.00 AM - 1.00 PM");

        mDatePicker.setMinDate(System.currentTimeMillis());
        mDatePicker.setMaxDate(System.currentTimeMillis() + (1000*60*60*24*2));

        c = Calendar.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mOptions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSlot.setAdapter(adapter);

        mSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeSlot = i+1;
                Log.d("ItemSelected", mTimeSlot+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mTimeSlot = 1;
                Log.d("ItemSelected", mTimeSlot+"");
            }
        });

        mReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                long time = c.getTimeInMillis();
                Log.d("Time", time+" "+mTimeSlot);

                JSONObject obj = new JSONObject();

                try {
                    obj.put("userId", SignInActivity.userId);
                    obj.put("addressId", 7);
                    obj.put("pickupSlotId", mTimeSlot);
                    obj.put("couponCode", mCoupon.getText().toString());
                    obj.put("bookingDate", time);
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
}
