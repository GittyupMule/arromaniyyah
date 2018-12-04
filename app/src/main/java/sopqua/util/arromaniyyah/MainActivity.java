package sopqua.util.arromaniyyah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import android.widget.Toast;

import static sopqua.util.arromaniyyah.MyInputMethodService.mostLikelyWords;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyBroadcastReceiver.ma = this;
        MyBroadcastReceiver.v = getLayoutInflater().inflate(R.layout.candidates_view, null);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        Log.wtf("ジョジョ", "Activity being destroyed, please help");
        super.onDestroy();
    }

    static void setIc(InputConnection mIc) {
        MyBroadcastReceiver.ic = mIc;
    }

}
