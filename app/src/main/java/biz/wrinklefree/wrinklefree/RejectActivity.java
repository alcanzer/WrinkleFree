package biz.wrinklefree.wrinklefree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static biz.wrinklefree.wrinklefree.SignUpActivity.pager;

public class RejectActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject);

        btn = findViewById(R.id.resendBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
                finish();
            }
        });
    }
}
