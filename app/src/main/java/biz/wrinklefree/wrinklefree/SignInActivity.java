package biz.wrinklefree.wrinklefree;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    SignInButton mSignIn;
    TextView mTextView, mLocationTv;
    GoogleApiClient mClient;
    LocationManager mLocationManager;
    Criteria mCriteria;
    boolean locationEnabled;
    Location mCurrentLatLng;
    Context ctx;
    JSONObject demoObj;
    LoginResponse mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        checkPermissions();
        ctx = this;

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String BASE_URL = "http://dev-api.wrinklefree.biz:8080/tidykart-ws/";

        demoObj = new JSONObject();
        try {
            demoObj.put("emailAddress", "abc@gmail.com");
            demoObj.put("mobileNumber", "989534823");
            demoObj.put("firstName", "ABC");
            demoObj.put("lastName", "CDE");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mSignIn = (SignInButton) findViewById(R.id.signin);
        mTextView = (TextView) findViewById(R.id.usertag);
        mLocationTv = (TextView) findViewById(R.id.locatadd);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignIn.setSize(SignInButton.SIZE_STANDARD);
        mSignIn.setScopes(gso.getScopeArray());

        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
        mCriteria.setAltitudeRequired(false);
        mCriteria.setBearingRequired(false);
        mCriteria.setSpeedRequired(false);
        mCriteria.setCostAllowed(true);
        mCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        mCriteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        final Looper looper = null;


        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                getData();
                if (ActivityCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestSingleUpdate(mCriteria, SignInActivity.this, looper);
                Intent intent = new Intent(getApplicationContext(), UpdateAddressActivity.class);
                startActivity(intent);
            }

            private void getData() {
                VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(BASE_URL + "login", demoObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        mUser = gson.fromJson(response.toString(), LoginResponse.class);
                        Toast.makeText(getApplicationContext(), mUser.getUserInfo().get(0).getEmailAddress(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }));

                VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, BASE_URL + "getappversion", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(ctx, response.toString()
                                , Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        startActivityForResult(signInIntent, 9);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mTextView.setText(result.getSignInAccount().getEmail());
        }
    }


    void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();

                    if (!locationEnabled) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLatLng = location;
        Toast.makeText(ctx, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
                getApplicationContext(), new GeocoderHandler());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            mLocationTv.setText(locationAddress);
        }
    }

}
