package rhg.com.sample.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    CustomLoginButton customLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customLoginButton = (CustomLoginButton) findViewById(R.id.clb);
        customLoginButton.setText("登  录");
        customLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoginButton.doLogin();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customLoginButton.finishLogin();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        customLoginButton.stopRunningAnimation();
        super.onDestroy();
    }
}
