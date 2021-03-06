package sopqua.util.arromaniyyah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

import static sopqua.util.arromaniyyah.MyInputMethodService.mostLikelyWords;

public class MyBroadcastReceiver extends BroadcastReceiver {
    static View v;
    static MyBroadcastReceiver mbr;
    static InputConnection ic;
    static MainActivity ma;
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("sopqua.util.arromaniyyah.UPDATE_SUGGESTIONS")) {
            Log.wtf("ジョジョ", "UPDATE_SUGGESTIONS");
            String composingText = intent.getStringExtra("composing");
            Log.wtf("ジョジョ", "I'm in the method");
            String[] suggestions = mostLikelyWords(composingText);
            View[] borders = new View[]{
                    v.findViewById(R.id.border0)//,
                    /*v.findViewById(R.id.border1),
                    v.findViewById(R.id.border2),
                    v.findViewById(R.id.border3),
                    v.findViewById(R.id.border4),
                    v.findViewById(R.id.border5),
                    v.findViewById(R.id.border6),
                    v.findViewById(R.id.border7),
                    v.findViewById(R.id.border8),
                    v.findViewById(R.id.border9)*/};
            TextView[] candidates = new TextView[]{
                    v.findViewById(R.id.suggestion0)/*,
                        v.findViewById(R.id.suggestion1),
                        v.findViewById(R.id.suggestion2),
                        v.findViewById(R.id.suggestion3),
                        v.findViewById(R.id.suggestion4),
                        v.findViewById(R.id.suggestion5),
                        v.findViewById(R.id.suggestion6),
                        v.findViewById(R.id.suggestion7),
                        v.findViewById(R.id.suggestion8),
                        v.findViewById(R.id.suggestion9)*/
            };
            TextView textView = v.findViewById(R.id.composingTextView);
            textView.setText(composingText);
            Log.wtf("ジョジョ", "Composing text set to: " + textView.getText());
            int len = suggestions.length;
            for (int i = 0; i < len; i++) {
                candidates[i].clearAnimation();
                candidates[i].setVisibility(View.VISIBLE);
                borders[i].clearAnimation();
                borders[i].setVisibility(View.VISIBLE);
                candidates[i].setText(suggestions[i]);
                Log.wtf("ジョジョ", "Candidate " + i + " text set to: " + candidates[i].getText());
            }
            for (int i = len; i < 1; i++) {
                candidates[i].clearAnimation();
                candidates[i].setVisibility(View.INVISIBLE);
                borders[i].clearAnimation();
                borders[i].setVisibility(View.INVISIBLE);
            }
            Log.wtf("ジョジョ", "Candidates visibility updated");
        } else if (intent.getAction().equals("sopqua.util.arromaniyyah.ON_CREATE")) {
            Log.wtf("ジョジョ", "ON_CREATE");
            TextView[] candidates = new TextView[]{
                    v.findViewById(R.id.suggestion0)/*,
                        v.findViewById(R.id.suggestion1),
                        v.findViewById(R.id.suggestion2),
                        v.findViewById(R.id.suggestion3),
                        v.findViewById(R.id.suggestion4),
                        v.findViewById(R.id.suggestion5),
                        v.findViewById(R.id.suggestion6),
                        v.findViewById(R.id.suggestion7),
                        v.findViewById(R.id.suggestion8),
                        v.findViewById(R.id.suggestion9)*/
            };
            for (int i = 0; i < 1; i++) {
                final int j = i;
                candidates[i].setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ic.commitText(
                                            mostLikelyWords(
                                                    (String) ((TextView) v
                                                            .findViewById(
                                                                    R.id.composingTextView
                                                            )
                                                    ).getText())[j],
                                            1
                                    );
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    // Why are you here? The view should be invisible.
                                }
                            }
                        }
                );
            }
        }
    }
}
