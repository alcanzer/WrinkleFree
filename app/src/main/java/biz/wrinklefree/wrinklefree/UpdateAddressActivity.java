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

public class UpdateAddressActivity extends AppCompatActivity {

    EditText mAdd1, mAdd2, mPincode;
    Button mSubmit;
    String BASE_URL = "http://dev-api.wrinklefree.biz:8080/tidykart-ws/";
    Boolean callingAct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        

        mAdd1 = (EditText) findViewById(R.id.addText1);
        mAdd2 = (EditText) findViewById(R.id.addText2);
        mPincode = (EditText) findViewById(R.id.pincodeText);
        mSubmit = (Button) findViewById(R.id.submitBtn);
        int calAct = getIntent().getIntExtra("CallingAct", 0);

        if(calAct == 1){
            callingAct = true;
        }



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mAdd1.getText().toString().trim().equals("") || mAdd2.getText().toString().trim().equals("") || mPincode.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter the required details", Toast.LENGTH_SHORT).show();
                }

                else {

                    JSONObject addObj = new JSONObject();

                    try {
                        addObj.put("userId", SignInActivity.userId);
                        addObj.put("addressLine1", mAdd1.getText());
                        addObj.put("addressLine2", mAdd2.getText());
                        addObj.put("pincode", mPincode.getText());
                        addObj.put("latlong", SignInActivity.lat+","+SignInActivity.lng);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(BASE_URL + "updateaddress", addObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.getBoolean("iSServiceAvailable")) {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateAddressActivity.this, HomepageActivity.class);
                                        intent.putExtra("username", SignInActivity.name[0]);
                                        startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }));
                }
            }
        });

    }
}
