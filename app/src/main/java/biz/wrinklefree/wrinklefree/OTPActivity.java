package biz.wrinklefree.wrinklefree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {

    Button mReqOTP, mVerify;
    EditText phNum, mOTPCode, mCountryCode;
    JSONObject OTPResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        phNum = (EditText) findViewById(R.id.phnumber);
        mCountryCode = (EditText) findViewById(R.id.countrycode);
        mOTPCode = (EditText) findViewById(R.id.verify);
        mReqOTP = (Button) findViewById(R.id.reqotp);
        mVerify = (Button) findViewById(R.id.proceed);

        mReqOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!phNum.getText().equals(null) && !mCountryCode.getText().equals(null)){
                    JSONObject mOTPReq = new JSONObject();
                    try {
                        mOTPReq.put("userId", SignInActivity.userId);
                        mOTPReq.put("countryMobileCode", mCountryCode.getText());
                        mOTPReq.put("mobileNumber", phNum.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(SignInActivity.BASE_URL + "requestotp", mOTPReq, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            OTPResp = response;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }));
                }
            }
        });

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int otp = 0;
                int mUserEnteredCode = Integer.parseInt(mOTPCode.getText().toString());
                try {
                    otp = OTPResp.getInt("otp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mUserEnteredCode == otp){
                    Intent intent = new Intent(OTPActivity.this, UpdateAddressActivity.class);
                    intent.putExtra("CallingActivity", 1);
                    startActivity(intent);
                    finish();
                }

                else {
                    Toast.makeText(OTPActivity.this, otp, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
