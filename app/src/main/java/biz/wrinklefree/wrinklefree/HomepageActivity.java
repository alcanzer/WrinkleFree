package biz.wrinklefree.wrinklefree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

public class HomepageActivity extends AppCompatActivity {

    TextView mBanner;
    CardView mNewOrder;
    FloatingActionButton mLogout;
    GoogleApiClient mClient;
    CardView mWelcome, mProfile, mOrders;
    LinearLayout mLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mBanner = (TextView) findViewById(R.id.banner);
        mNewOrder = (CardView) findViewById(R.id.neworder);
        mLogout = (FloatingActionButton) findViewById(R.id.logout);
        mWelcome = (CardView) findViewById(R.id.welcomecard);
        mLinear = (LinearLayout) findViewById(R.id.homelinear);
        mOrders = (CardView) findViewById(R.id.orders);
        mProfile = (CardView) findViewById(R.id.profile);

        Animation cardAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lefttoright);
        Animation layoutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottomtotop);

        mWelcome.setAnimation(cardAnim);
        mLinear.setAnimation(layoutAnim);

        Intent intent = getIntent();

        mBanner.setText("Welcome, " + intent.getStringExtra("username"));

        mNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PickupActivity.class);
                startActivity(intent);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateAddressActivity.class);
                startActivity(intent);
            }
        });

        mOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
                startActivity(intent);
            }
        });


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(mClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            Intent intent = new Intent(HomepageActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }
}
