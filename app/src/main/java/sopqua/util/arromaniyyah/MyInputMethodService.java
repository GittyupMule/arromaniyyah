package sopqua.util.arromaniyyah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

public final class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    public MyInputMethodService() {
        super();
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        final int backspaceCode = 67;
        final int returnCode = 66;
        switch (primaryCode) {
            case backspaceCode:
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (composingText.length() > 0) {
                    composingText = composingText.substring(0, composingText.length() - 1);
                    inputConnection.setComposingText(composingText, 1);
                } else if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                    composingText = "";
                }
                updateSuggestions();
                break;
            case returnCode:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                updateSuggestions();
                break;
            case 68:
                composingText += "`";
                updateSuggestions();
                inputConnection.setComposingText(composingText, 1);
                break;
            case 75:
                composingText += "'";
                updateSuggestions();
                inputConnection.setComposingText(composingText, 1);
                break;
            case 55:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                composingText = "";
                updateSuggestions();
                inputConnection.commitText(",", 1);
                break;
            case 56:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                composingText = "";
                updateSuggestions();
                inputConnection.commitText(".", 1);
                break;
            case 32:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                composingText = "";
                updateSuggestions();
                inputConnection.commitText(" ", 1);
                break;
            default:
                char code = (char) primaryCode;
                composingText += String.valueOf(code);
                updateSuggestions();
                inputConnection.setComposingText(composingText, 1);
        }
    }

    static String mostLikelyWord(String composingText) {
        return mostLikelyWords(composingText)[0];
    }

    static String[] mostLikelyWords(String composingText) {
        //TODO: Arabic word guessing logic goes here
        if (composingText == "") {
            return wtfArray("ジョジョ", new String[]{"TEST!!!!"});
        }
        String[] result = new String[]{ composingText };
        return wtfArray("ジョジョ", result);
    }

    static String[] wtfArray(String tag, String[] input) {
        Log.wtf(tag, "[");
        for (String i : input) {
            Log.wtf(tag, "    \"" + i + "\"");
        }
        Log.wtf(tag, "]");
        return input;
    }

    @Override
    public void onText(CharSequence charSequence) {

    }
    @Override
    public void swipeLeft() {

    }
    @Override
    public void swipeRight() {

    }
    @Override
    public void swipeDown() {

    }
    @Override
    public void swipeUp() {

    }

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private View candidatesView;
    private String composingText;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout_qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        composingText = "";
        setCandidatesViewShown(true);
        return keyboardView;
    }

    @Override
    public View onCreateCandidatesView() {
        Log.wtf("ジョジョ", "onCreateCandidatesView");
        candidatesView = getLayoutInflater().inflate(R.layout.candidates_view, null);
        updateSuggestions();
        TextView[] candidates = new TextView[]{
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion0)/*,
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion1),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion2),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion3),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion4),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion5),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion6),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion7),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion8),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion9)*/
        };
        for (int i = 0; i < 1; i++) {
            final int j = i;
            candidates[i].setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                getCurrentInputConnection().commitText(mostLikelyWords(composingText)[j], 1);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // Why are you here? The view should be invisible.
                            }
                        }
                    }
            );
        }
        return candidatesView;
    }

    public View getLayoutByRes(int layoutRes, ViewGroup viewGroup)     {
        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return factory.inflate(layoutRes, viewGroup);
    }

    public void updateSuggestions() {
        Intent intent = new Intent();
        intent.setAction("sopqua.util.arromaniyyah.UPDATE_SUGGESTIONS");
        intent.putExtra("composing",composingText);
        sendBroadcast(intent);
    }
}
