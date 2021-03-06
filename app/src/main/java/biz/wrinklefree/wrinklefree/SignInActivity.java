package biz.wrinklefree.wrinklefree;

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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
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

import biz.wrinklefree.wrinklefree.ResponseObjects.LoginResponse;

import static biz.wrinklefree.wrinklefree.SignUpActivity.pager;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button mSignIn;
    TextView mSignUp;
    GoogleApiClient mClient;
    LocationManager mLocationManager;
    Criteria mCriteria;
    boolean locationEnabled;
    Location mCurrentLatLng;
    Context ctx;
    JSONObject demoObj;
    LoginResponse mUser = null;
    public static int userId;
    public static String[] name;
    public static String lat, lng;
    public static final String BASE_URL = "http://dev-api.wrinklefree.biz:8080/tidykart-ws/";
    private UserInfoPref userInfoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userInfoPref = new UserInfoPref(this);

        mSignIn = findViewById(R.id.signin);
        mSignUp = findViewById(R.id.signUp);

        //Criteria for the Location listener( to consume less power)
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

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
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
            }
        });


        checkPermissions();
        ctx = this;

        //Setting entry animation to the linear layouts in the activity.

        //Instantiate Location Manager
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //Build a googlesignin Options object.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Google ApiClient object.
        mClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Setting properties of the sign-in button

        //Checks if the user is already logged in.
        silentLogin();

        //Sign in funtion
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
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
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Opens the google login box if the user is not signed in.
    void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        startActivityForResult(signInIntent, 9);
    }

    //Creates a json object that is posted to the backend and fetches the response.
    void getData(GoogleSignInResult result) {

        demoObj = new JSONObject();
        try {
            demoObj.put("emailAddress", result.getSignInAccount().getEmail());
            demoObj.put("mobileNumber", userInfoPref.getKey("MobileNumber"));
            demoObj.put("firstName", userInfoPref.getKey("FirstName"));
            demoObj.put("lastName", userInfoPref.getKey("LastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(BASE_URL + "login", demoObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                mUser = gson.fromJson(response.toString(), LoginResponse.class);
                userId = mUser.getUserInfo().get(0).getUserId();
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                if (mUser.getIsFirstTimeUser()) {
                    userInfoPref.putKey("userID", String.valueOf(mUser.getUserInfo().get(0).getUserId()));
                    userInfoPref.putKey("Email", mUser.getUserInfo().get(0).getEmailAddress());
                    //mUser.getUserInfo().get(0).getFirstName());
                    startActivity(intent);
                    finish();
                } else if(mUser.getUserInfo().get(0).getAddressUpdated() == false){

                    intent.putExtra("FormUpdate", "Address");
                    startActivity(intent);
                    finish();
                }
                else if(mUser.getUserInfo().get(0).getMobileVerified() == false){
                    intent.putExtra("FormUpdate", "Mobile");
                    startActivity(intent);
                    finish();
                }else{
                    userInfoPref.putKey("FirstName", mUser.getUserInfo().get(0).getFirstName() == null ? "" : mUser.getUserInfo().get(0).getFirstName());
                    userInfoPref.putKey("LastName", mUser.getUserInfo().get(0).getLastName() == null ? "" : mUser.getUserInfo().get(0).getLastName());
                    userInfoPref.putKey("MobileNumber", String.valueOf(mUser.getUserInfo().get(0).getMobileNumber()).isEmpty() ? "" : String.valueOf(mUser.getUserInfo().get(0).getMobileNumber()));
                    userInfoPref.putKey("Email", mUser.getUserInfo().get(0).getEmailAddress());
                    userInfoPref.putKey("userID", String.valueOf(mUser.getUserInfo().get(0).getUserId()));
                    Log.d("Response", response.toString());
                    Intent mIntent = new Intent(getApplicationContext(), Homepage.class);
                    intent.putExtra("username", mUser.getUserInfo().get(0).getFirstName());
                    startActivity(mIntent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }));

        /*VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, BASE_URL + "getappversion", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9 && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if(resultCode == RESULT_CANCELED){
        }
    }


    private void silentLogin() {

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
            getData(result);
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

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
                getApplicationContext(), new GeocoderHandler());
        lat = String.format("%.5f", location.getLatitude());
        lng = String.format("%.5f", location.getLongitude());
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
        }
    }
}
