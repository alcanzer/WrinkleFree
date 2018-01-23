package biz.wrinklefree.wrinklefree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PickupsActivity extends AppCompatActivity {

    TextView mOrderDate, mStatus, mService, mCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickups);

        mOrderDate = findViewById(R.id.orderdatetext);
        mStatus = findViewById(R.id.statustext);
        mService = findViewById(R.id.servicetypetextpickup);
        mCoupon = findViewById(R.id.coupontext);

        //ToDO, Make a request for pickups.
    }
}
