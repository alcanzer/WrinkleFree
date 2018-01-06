package biz.wrinklefree.wrinklefree;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.relex.circleindicator.CircleIndicator;

public class SignUpActivity extends FragmentActivity {

    public static CustomViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pager = (CustomViewPager) findViewById(R.id.viewpager);
        CircleIndicator indicator = findViewById(R.id.circleindicator);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        indicator.setViewPager(pager);

    }

    public class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: return UserRegOne.newInstance("ABC", ":");
                case 1: return UserRegTwo.newInstance("ABC", ":");
                case 2: return UserRegThree.newInstance("ABC", ":");
                case 3: return UserRegFour.newInstance("ABC", ";");
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
