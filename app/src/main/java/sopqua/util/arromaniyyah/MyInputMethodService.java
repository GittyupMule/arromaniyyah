package sopqua.util.arromaniyyah;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

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
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;
            case returnCode:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case 68:
                composingText += "`";
                inputConnection.setComposingText(composingText, 1);
                break;
            case 75:
                composingText += "'";
                inputConnection.setComposingText(composingText, 1);
                break;
            case 55:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                inputConnection.commitText(",", 1);
                break;
            case 56:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                inputConnection.commitText(".", 1);
                break;
            case 32:
                inputConnection.commitText(mostLikelyWord(composingText), 1);
                inputConnection.commitText(" ", 1);
                break;
            default:
                char code = (char) primaryCode;
                composingText += String.valueOf(code);
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
    private String composingText = "";

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout_qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }
}
