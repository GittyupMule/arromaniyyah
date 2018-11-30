package sopqua.util.arromaniyyah;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
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

    private String mostLikelyWord(String composingText) {
        return mostLikelyWords(composingText)[0];
    }

    private String[] mostLikelyWords(String composingText) {
        //TODO: Arabic word guessing logic goes here
        return new String[]{ composingText };
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
        candidatesView = getLayoutInflater().inflate(R.layout.candidates_view, null);
        updateSuggestions();
        TextView[] candidates = new TextView[]{
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion0),
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
                        .findViewById(R.id.suggestion9)
        };
        for (int i = 0; i < 10; i++) {
            candidates[0].setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                getCurrentInputConnection().commitText(mostLikelyWords(composingText)[0], 1);
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
        Log.d("ジョジョ", "I'm in the method");
        String[] suggestions = mostLikelyWords(composingText);
        View[] borders = new View[]{getLayoutByRes(R.layout.candidates_view, null)
                .findViewById(R.id.border0),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border1),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border2),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border3),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border4),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border5),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border6),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border7),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border8),
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.border9)};
        TextView[] candidates = new TextView[]{
                getLayoutByRes(R.layout.candidates_view, null)
                        .findViewById(R.id.suggestion0),
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
                        .findViewById(R.id.suggestion9)
        };
        TextView textView = getLayoutByRes(R.layout.candidates_view, null)
                .findViewById(R.id.composingTextView);
        textView.setText(composingText);
        Log.d("ジョジョ", "Composing text set");
        int len = suggestions.length;
        for (int i = 0; i < len; i++) {
            candidates[i].clearAnimation();
            candidates[i].setVisibility(View.VISIBLE);
            borders[i].clearAnimation();
            borders[i].setVisibility(View.VISIBLE);
            candidates[i].setText(suggestions[i]);
        }
        for (int i = len; i < 10; i++) {
            candidates[i].clearAnimation();
            candidates[i].setVisibility(View.GONE);
            borders[i].clearAnimation();
            borders[i].setVisibility(View.GONE);
        }
        Log.d("ジョジョ", "Candidates visibility updated");
    }
}
