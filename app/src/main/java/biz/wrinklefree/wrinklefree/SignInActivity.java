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

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    SignInButton mSignIn;
    GoogleApiClient mClient;
    LocationManager mLocationManager;
    Criteria mCriteria;
    boolean locationEnabled;
    Location mCurrentLatLng;
    CardView cTop, cBot;
    Context ctx;
    JSONObject demoObj;
    LoginResponse mUser = null;
    boolean done = false;
    ProgressBar mProgressBar;
    public static int userId;
    public static final String BASE_URL = "http://dev-api.wrinklefree.biz:8080/tidykart-ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        checkPermissions();
        ctx = this;

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mSignIn = (SignInButton) findViewById(R.id.signin);
        cTop = (CardView) findViewById(R.id.topCard);
        cBot = (CardView) findViewById(R.id.bottomCard);

        //Setting entry animation to the linear layouts in the activity.
        Animation mRtL = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        Animation mLtR = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        cTop.setAnimation(mRtL);
        cBot.setAnimation(mLtR);

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
        mSignIn.setSize(SignInButton.SIZE_STANDARD);
        mSignIn.setScopes(gso.getScopeArray());

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

        //Checks if the user is already logged in.
        silentLogin();

        //Sign in funtion
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                mProgressBar.setVisibility(View.VISIBLE);
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
        final String[] name = result.getSignInAccount().getDisplayName().split(" ");
        Log.d("NAMERS", name == null ? "A" : name[0]);
        demoObj = new JSONObject();
        try {
            demoObj.put("emailAddress", "gef@gmail.com");
            demoObj.put("mobileNumber", "989534823");
            demoObj.put("firstName", name[0]);
            demoObj.put("lastName", "CDE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(BASE_URL + "login", demoObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                mUser = gson.fromJson(response.toString(), LoginResponse.class);
                userId = mUser.getUserInfo().get(0).getUserId();
                if (mUser.getIsFirstTimeUser()) {
                    Intent intent = new Intent(getApplicationContext(), UpdateAddressActivity.class);
                    intent.putExtra("username", name[0]);//mUser.getUserInfo().get(0).getFirstName());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                    intent.putExtra("username", name[0]);
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE){
                    mProgressBar.setVisibility(View.GONE);
                }
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
            if(mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE){
                mProgressBar.setVisibility(View.GONE);
            }
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
