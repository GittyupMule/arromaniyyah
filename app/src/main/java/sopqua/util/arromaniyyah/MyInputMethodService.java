package sopqua.util.arromaniyyah;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static sopqua.util.arromaniyyah.MyBroadcastReceiver.v;


public final class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private static HashMap<String, Integer> map;
    private static ArrayList<RomanizationSchema> schemae = new ArrayList<>();

    public MyInputMethodService() {
        super();
        InputStream ins = getResources().openRawResource(
                getResources().getIdentifier("frequency_onegrams.json",
                        "raw", getPackageName()));

        java.util.Scanner s = new java.util.Scanner(ins).useDelimiter("\\A");
        String json = s.hasNext() ? s.next() : "";
        map = new Gson().fromJson(json, new TypeToken<HashMap<String, Integer>>(){}.getType());
        if (!schemae.contains(untypeable)) {
            schemae.add(untypeable);
        }
        if (!schemae.contains(typeable)) {
            schemae.add(typeable);
        }
        if (!schemae.contains(asciiIsOkay)) {
            schemae.add(asciiIsOkay);
        }
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
            case 204:
                if (Build.VERSION.SDK_INT >= 28) {
                    switchToNextInputMethod(false);
                } else if (Build.VERSION.SDK_INT >= 16) {
                    try {
                        ((InputMethodManager) getLayoutInflater()
                                .inflate(R.layout.candidates_view, null)
                                .getContext()
                                .getSystemService(INPUT_METHOD_SERVICE))
                                .switchToNextInputMethod(
                                        getWindow()
                                                .getWindow()
                                                .getAttributes()
                                                .token,
                                        false
                                );
                    } catch (NullPointerException e) {
                        ((InputMethodManager) getLayoutInflater()
                                .inflate(R.layout.candidates_view, null)
                                .getContext()
                                .getSystemService(INPUT_METHOD_SERVICE))
                                .switchToNextInputMethod(
                                        null,
                                        false
                                );
                    }
                }
                break;
            default:
                char code = (char) primaryCode;
                composingText += String.valueOf(code);
                updateSuggestions();
                inputConnection.setComposingText(composingText, 1);
        }
    }

    public static RomanizationSchema typeable = new RomanizationSchema() {
        @Override
        public String firstPass(ArabicGrapheme input) {
            switch (input) {
                case FATHAH:
                    return "a";
                case KASRAH:
                    return "i";
                case DAMMAH:
                    return "u";
                case HANJARIYYAH:
                    return "a";
                case ALIF_MADDAH:
                    return "'a";
                case FATHAH_TANWIN:
                    return "an";
                case KASRAH_TANWIN:
                    return "in";
                case DAMMAH_TANWIN:
                    return "un";
                case SHADDAH:
                    return "ɔ";
                case YA:
                    return "y";
                case MAQSURAH:
                    return "á";
                case WAW:
                    return "w";
                case HA:
                    return "h";
                case NUN:
                    return "n";
                case MIM:
                    return "m";
                case LAM:
                    return "l";
                case KAF:
                    return "k";
                case QAF:
                    return "q";
                case FA:
                    return "f";
                case GHAYN:
                    return "`";
                case AYN:
                    return "`";
                case ZA:
                    return "z";
                case TTA:
                    return "t";
                case DAD:
                    return "d";
                case SAD:
                    return "s";
                case SHIN:
                    return "s";
                case SIN:
                    return "s";
                case ZAY:
                    return "z";
                case RA:
                    return "r";
                case DHAL:
                    return "d";
                case DAL:
                    return "d";
                case KHA:
                    return "h";
                case HHA:
                    return "h";
                case JIM:
                    return "g";
                case THA:
                    return "t";
                case TA:
                    return "t";
                case BA:
                    return "b";
                case ALIF:
                    return "a";
                case HAMZAH:
                    return "'";
                case MARBUTAH:
                    return "h";
                case MEDIAL_IY_HAMZAH:
                    return "i'";
                case MEDIAL_AY_HAMZAH:
                    return "ay'";
                case AL:
                    return "aɫ";
                case MEDIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_U:
                    return "'u";
                case INITIAL_HAMZAH_A:
                    return "'a";
                case MEDIAL_HAMZAH_U_ON_YA:
                    return "'u";
                case MEDIAL_HAMZAH_A_ON_YA:
                    return "'a";
                case MEDIAL_HAMZAH_U_ON_WAW:
                    return "'u";
                case MEDIAL_HAMZAH_A_ON_WAW:
                    return "'a";
                case MEDIAL_HAMZAH_A_ON_ALIF:
                    return "'a";
                case FINAL_HAMZAH_ON_YA:
                    return "'";
                case FINAL_HAMZAH_ON_WAW:
                    return "'";
                case FINAL_HAMZAH_ABOVE_ALIF:
                    return "'";
                case FINAL_HAMZAH_BELOW_ALIF:
                    return "'";
                default:
                    return "";
            }
        }

        @Override
        public String postProcessing(String firstPassInput) {
            if (firstPassInput.contains("ɫ")) { // Solar letters.
                if (firstPassInput.contains("ɫn")) {
                    firstPassInput = firstPassInput.replace("ɫn", "nn");
                } else if (firstPassInput.contains("ɫz")) {
                    firstPassInput = firstPassInput.replace("ɫz", "zz");
                } else if (firstPassInput.contains("ɫt")) {
                    firstPassInput = firstPassInput.replace("ɫt", "tt");
                } else if (firstPassInput.contains("ɫd")) {
                    firstPassInput = firstPassInput.replace("ɫd", "dd");
                } else if (firstPassInput.contains("ɫs")) {
                    firstPassInput = firstPassInput.replace("ɫs", "ss");
                } else if (firstPassInput.contains("ɫr")) {
                    firstPassInput = firstPassInput.replace("ɫr", "rr");
                } else {
                    firstPassInput = firstPassInput.replace("ɫ", "l");
                }
            }
            while (firstPassInput.contains("aa")) {
                firstPassInput = firstPassInput.replace("aa", "a");
            }
            while (firstPassInput.contains("iy")) {
                firstPassInput = firstPassInput.replace("iy", "i");
            }
            while (firstPassInput.contains("uw")) {
                firstPassInput = firstPassInput.replace("uw", "u");
            }
            while (firstPassInput.contains("ɔ")) {
                firstPassInput =
                        firstPassInput.substring(
                                0,
                                firstPassInput.indexOf("ɔ")
                        )
                                + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1,
                                firstPassInput.indexOf("ɔ") + 2
                        )
                                + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1
                        );
            }
            return firstPassInput;
        }
    };

    public static RomanizationSchema untypeable = new RomanizationSchema() {
        @Override
        public String firstPass(ArabicGrapheme input) {
            switch (input) {
                case FATHAH:
                    return "a";
                case KASRAH:
                    return "i";
                case DAMMAH:
                    return "u";
                case HANJARIYYAH:
                    return "ā";
                case ALIF_MADDAH:
                    return "'ā";
                case FATHAH_TANWIN:
                    return "aⁿ";
                case KASRAH_TANWIN:
                    return "iⁿ";
                case DAMMAH_TANWIN:
                    return "uⁿ";
                case SHADDAH:
                    return "ɔ";
                case YA:
                    return "y";
                case MAQSURAH:
                    return "á";
                case WAW:
                    return "w";
                case HA:
                    return "h";
                case NUN:
                    return "n";
                case MIM:
                    return "m";
                case LAM:
                    return "l";
                case KAF:
                    return "k";
                case QAF:
                    return "q";
                case FA:
                    return "f";
                case GHAYN:
                    return "ḡ";
                case AYN:
                    return "`";
                case ZA:
                    return "ẓ";
                case TTA:
                    return "ṭ";
                case DAD:
                    return "ḍ";
                case SAD:
                    return "ṣ";
                case SHIN:
                    return "š";
                case SIN:
                    return "s";
                case ZAY:
                    return "z";
                case RA:
                    return "r";
                case DHAL:
                    return "ḏ";
                case DAL:
                    return "d";
                case KHA:
                    return "ẖ";
                case HHA:
                    return "ḥ";
                case JIM:
                    return "ǧ";
                case THA:
                    return "ṯ";
                case TA:
                    return "t";
                case BA:
                    return "b";
                case ALIF:
                    return "â";
                case HAMZAH:
                    return "'";
                case MARBUTAH:
                    return "ḧ";
                case MEDIAL_IY_HAMZAH:
                    return "iy'";
                case MEDIAL_AY_HAMZAH:
                    return "ay'";
                case AL:
                    return "aɫ";
                case MEDIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_U:
                    return "'u";
                case INITIAL_HAMZAH_A:
                    return "'a";
                case MEDIAL_HAMZAH_U_ON_YA:
                    return "'u";
                case MEDIAL_HAMZAH_A_ON_YA:
                    return "'a";
                case MEDIAL_HAMZAH_U_ON_WAW:
                    return "'u";
                case MEDIAL_HAMZAH_A_ON_WAW:
                    return "'a";
                case MEDIAL_HAMZAH_A_ON_ALIF:
                    return "'a";
                case FINAL_HAMZAH_ON_YA:
                    return "'";
                case FINAL_HAMZAH_ON_WAW:
                    return "'";
                case FINAL_HAMZAH_ABOVE_ALIF:
                    return "'";
                case FINAL_HAMZAH_BELOW_ALIF:
                    return "'";
                default:
                    return "";
            }
        }

        @Override
        public String postProcessing(String firstPassInput) {
            if (firstPassInput.contains("ɫ")) { // Solar letters.
                if (firstPassInput.contains("ɫn")) {
                    firstPassInput = firstPassInput.replace("ɫn", "nn");
                } else if (firstPassInput.contains("ɫẓ")) {
                    firstPassInput = firstPassInput.replace("ɫẓ", "ẓẓ");
                } else if (firstPassInput.contains("ɫṭ")) {
                    firstPassInput = firstPassInput.replace("ɫṭ", "ṭṭ");
                } else if (firstPassInput.contains("ɫḍ")) {
                    firstPassInput = firstPassInput.replace("ɫḍ", "ḍḍ");
                } else if (firstPassInput.contains("ɫṣ")) {
                    firstPassInput = firstPassInput.replace("ɫṣ", "ṣṣ");
                } else if (firstPassInput.contains("ɫš")) {
                    firstPassInput = firstPassInput.replace("ɫš", "šš");
                } else if (firstPassInput.contains("ɫs")) {
                    firstPassInput = firstPassInput.replace("ɫs", "ss");
                } else if (firstPassInput.contains("ɫz")) {
                    firstPassInput = firstPassInput.replace("ɫz", "zz");
                } else if (firstPassInput.contains("ɫr")) {
                    firstPassInput = firstPassInput.replace("ɫr", "rr");
                } else if (firstPassInput.contains("ɫd")) {
                    firstPassInput = firstPassInput.replace("ɫd", "dd");
                } else if (firstPassInput.contains("ɫt")) {
                    firstPassInput = firstPassInput.replace("ɫt", "tt");
                } else if (firstPassInput.contains("ɫḏ")) {
                    firstPassInput = firstPassInput.replace("ɫḏ", "ḏḏ");
                } else if (firstPassInput.contains("ɫṯ")) {
                    firstPassInput = firstPassInput.replace("ɫṯ", "ṯṯ");
                } else {
                    firstPassInput = firstPassInput.replace("ɫ", "l");
                }
            }
            while (firstPassInput.contains("âa")) {
                firstPassInput = firstPassInput.replace("âa", "a");
            }
            while (firstPassInput.contains("aâ")) {
                firstPassInput = firstPassInput.replace("aâ", "â");
            }
            while (firstPassInput.contains("ɔ")) {
                firstPassInput =
                        firstPassInput.substring(
                                0,
                                firstPassInput.indexOf("ɔ")
                        )
                        + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1,
                                firstPassInput.indexOf("ɔ") + 2
                        )
                        + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1
                        );
            }
            return firstPassInput;
        }
    };

    public static MyBiFunction<String, String, Integer> distance = new MyBiFunction<String, String, Integer>() {
        @Override
        public Integer run(String input1, String input2) {
            if (input1.equals(input2)) {
                return 0;
            }
            if (input1.startsWith(input2)) {
                return 0;
            }
            int length = longestPrefix(input1, input2).length();
            if (length == 0) {
                String suffix = longestSuffix(input1, input2);
                int lengthOfSharedSuffix = suffix.length();
                if (lengthOfSharedSuffix < input2.length()) {
                    return input2.length() - lengthOfSharedSuffix;
                } else {
                    return input1.indexOf(suffix);
                }
            } else {
                return distance.run(input1.substring(length), input2.substring(length));
            }
        }
    };

    public static MyBiFunction<String, String, Integer> arDistance = new MyBiFunction<String, String, Integer>() {
        @Override
        public Integer run(String input1, String input2) {
            int result = Integer.MAX_VALUE;
            for (RomanizationSchema r : schemae) {
                int dist = distance.run(r.parseWord(input1), input2);
                if (dist == 0) {
                    return 0;
                }
                if (dist < result) {
                    result = dist;
                }
            }
            return result;
        }
    };

    public static <T, U> List<T> filterLowest10(Iterable<T> input, U compare,
                                                  MyBiFunction<T, U, Integer> filter) {
        ArrayList<T> result = new ArrayList<>();
        int maxVal = Integer.MIN_VALUE;
        int indexOfMax = -1;
        for (final T key : input) {
            if (result.size() < 10) {
                result.add(key);
                if (filter.run(key, compare) > maxVal) {
                    maxVal = filter.run(key, compare);
                    indexOfMax = result.size() - 1;
                }
            } else {
                if (filter.run(key, compare) < maxVal) {
                    result.set(indexOfMax, key);
                    int maxLoc = filter.run(result.get(0), compare);
                    int indexOfMaxLoc = 0;
                    for (int i = 1; i < result.size(); i++) {
                        if (filter.run(result.get(i), compare) > maxLoc) {
                            maxLoc = filter.run(result.get(i), compare);
                            indexOfMaxLoc = i;
                        }
                    }
                    maxVal = maxLoc;
                    indexOfMax = indexOfMaxLoc;
                }
            }
        }
        return result;
    }

    public static String longestPrefix(String input1, String input2) {
        for (int i = 1; i < Math.min(input2.length(), input1.length()); i++) {
            if (!input1.substring(0, i).equals(input2.substring(0, i))) {
                return input1.substring(0, i - 1);
            }
        }
        return "";
    }

    public static String longestSuffix(String input1, String input2) {
        for (int i = 0; i < -1 + Math.min(input2.length(), input1.length()); i++) {
            if (input1.contains(input2.substring(i))) {
                return input2.substring(i);
            }
        }
        return "";
    }

    public static RomanizationSchema asciiIsOkay = new RomanizationSchema() {
        @Override
        public String firstPass(ArabicGrapheme input) {
            switch (input) {
                case FATHAH:
                    return "e";
                case KASRAH:
                    return "i";
                case DAMMAH:
                    return "o";
                case HANJARIYYAH:
                    return "a";
                case ALIF_MADDAH:
                    return "'a";
                case FATHAH_TANWIN:
                    return "en";
                case KASRAH_TANWIN:
                    return "in";
                case DAMMAH_TANWIN:
                    return "on";
                case SHADDAH:
                    return "ɔ";
                case YA:
                    return "y";
                case MAQSURAH:
                    return "a";
                case WAW:
                    return "w";
                case HA:
                    return "h";
                case NUN:
                    return "n";
                case MIM:
                    return "m";
                case LAM:
                    return "l";
                case KAF:
                    return "k";
                case QAF:
                    return "q";
                case FA:
                    return "f";
                case GHAYN:
                    return "gh";
                case AYN:
                    return "`";
                case ZA:
                    return "dh";
                case TTA:
                    return "t";
                case DAD:
                    return "d";
                case SAD:
                    return "s";
                case SHIN:
                    return "sh";
                case SIN:
                    return "s";
                case ZAY:
                    return "z";
                case RA:
                    return "r";
                case DHAL:
                    return "dh";
                case DAL:
                    return "d";
                case KHA:
                    return "h";
                case HHA:
                    return "h";
                case JIM:
                    return "j";
                case THA:
                    return "th";
                case TA:
                    return "t";
                case BA:
                    return "b";
                case ALIF:
                    return "a";
                case HAMZAH:
                    return "'";
                case MARBUTAH:
                    return "h";
                case MEDIAL_IY_HAMZAH:
                    return "i'";
                case MEDIAL_AY_HAMZAH:
                    return "ay'";
                case AL:
                    return "aɫ";
                case MEDIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_I:
                    return "'i";
                case INITIAL_HAMZAH_U:
                    return "'o";
                case INITIAL_HAMZAH_A:
                    return "'e";
                case MEDIAL_HAMZAH_U_ON_YA:
                    return "'o";
                case MEDIAL_HAMZAH_A_ON_YA:
                    return "'e";
                case MEDIAL_HAMZAH_U_ON_WAW:
                    return "'o";
                case MEDIAL_HAMZAH_A_ON_WAW:
                    return "'o";
                case MEDIAL_HAMZAH_A_ON_ALIF:
                    return "'o";
                case FINAL_HAMZAH_ON_YA:
                    return "'";
                case FINAL_HAMZAH_ON_WAW:
                    return "'";
                case FINAL_HAMZAH_ABOVE_ALIF:
                    return "'";
                case FINAL_HAMZAH_BELOW_ALIF:
                    return "'";
                default:
                    return "";
            }
        }

        @Override
        public String postProcessing(String firstPassInput) {
            if (firstPassInput.contains("ɫ")) { // No solar letter logic, al- stays al-.
                firstPassInput = firstPassInput.replace("ɫ", "l");
            }
            while (firstPassInput.contains("ae")) {
                firstPassInput = firstPassInput.replace("ae", "a");
            }
            while (firstPassInput.contains("ea")) {
                firstPassInput = firstPassInput.replace("ea", "a");
            }
            while (firstPassInput.contains("ey")) {
                firstPassInput = firstPassInput.replace("ey", "ay");
            }
            while (firstPassInput.contains("iy")) {
                firstPassInput = firstPassInput.replace("iy", "i");
            }
            while (firstPassInput.contains("ow")) {
                firstPassInput = firstPassInput.replace("ow", "u");
            }
            while (firstPassInput.contains("ɔ")) {
                firstPassInput =
                        firstPassInput.substring(
                                0,
                                firstPassInput.indexOf("ɔ")
                        )
                                + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1,
                                firstPassInput.indexOf("ɔ") + 2
                        )
                                + firstPassInput.substring(
                                firstPassInput.indexOf("ɔ") + 1
                        );
            }
            return firstPassInput;
        }
    };

    static String mostLikelyWord(String composingText) {
        return mostLikelyWords(composingText)[0];
    }

    static String[] mostLikelyWords(String composingText) {
        if (composingText.equals("")) {
            return wtfArray("ジョジョ", new String[]{"TEST!!!!"});
        }
        String[] result = filterLowest10(map.keySet(), composingText, arDistance).toArray(new String[10]);
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
        InputConnection ic = getCurrentInputConnection();
        MainActivity.setIc(ic);
        Log.wtf("ジョジョ", "ON_CREATE");
        TextView[] candidates = new TextView[]{
                v.findViewById(R.id.suggestion0),
                v.findViewById(R.id.suggestion1),
                v.findViewById(R.id.suggestion2),
                v.findViewById(R.id.suggestion3),
                v.findViewById(R.id.suggestion4),
                v.findViewById(R.id.suggestion5),
                v.findViewById(R.id.suggestion6),
                v.findViewById(R.id.suggestion7),
                v.findViewById(R.id.suggestion8),
                v.findViewById(R.id.suggestion9)
        };
        for (int i = 0; i < 10; i++) {
            final int j = i;
            candidates[i].setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                getCurrentInputConnection().commitText(
                                        mostLikelyWords(
                                                composingText
                                        )[j],
                                        1
                                );
                                composingText = "";
                                updateSuggestions();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // Why are you here? The view should be invisible.
                            }
                        }
                    }
            );
        }
        updateSuggestions();
        return candidatesView;
    }

    @Override
    public void onStartCandidatesView(EditorInfo info, boolean restarting) {
        super.onStartCandidatesView(info, restarting);
        setCandidatesView(v);
    }

    public View getLayoutByRes(int layoutRes, ViewGroup viewGroup)     {
        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return factory.inflate(layoutRes, viewGroup);
    }

    public void updateSuggestions() {
        /*Log.wtf("ジョジョ", "Sending UPDATE_SUGGESTIONS");
        Intent intent = new Intent();
        intent.setAction("sopqua.util.arromaniyyah.UPDATE_SUGGESTIONS");
        intent.putExtra("composing",composingText);
        sendBroadcast(intent);
        Log.wtf("ジョジョ", "UPDATE_SUGGESTIONS sent");*/
        Log.wtf("ジョジョ", "I'm in the method");
        String[] suggestions = mostLikelyWords(composingText);
        View[] borders = new View[]{
                v.findViewById(R.id.border0),
                v.findViewById(R.id.border1),
                v.findViewById(R.id.border2),
                v.findViewById(R.id.border3),
                v.findViewById(R.id.border4),
                v.findViewById(R.id.border5),
                v.findViewById(R.id.border6),
                v.findViewById(R.id.border7),
                v.findViewById(R.id.border8),
                v.findViewById(R.id.border9)};
        TextView[] candidates = new TextView[]{
                v.findViewById(R.id.suggestion0),
                v.findViewById(R.id.suggestion1),
                v.findViewById(R.id.suggestion2),
                v.findViewById(R.id.suggestion3),
                v.findViewById(R.id.suggestion4),
                v.findViewById(R.id.suggestion5),
                v.findViewById(R.id.suggestion6),
                v.findViewById(R.id.suggestion7),
                v.findViewById(R.id.suggestion8),
                v.findViewById(R.id.suggestion9)
        };
        TextView textView = v.findViewById(R.id.composingTextView);
        textView.setText(composingText);
        Log.wtf("ジョジョ", "Composing text set to: " + textView.getText());
        int len = suggestions.length;
        for (int i = 0; i < len && i < 10; i++) {
            candidates[i].clearAnimation();
            candidates[i].setVisibility(View.VISIBLE);
            borders[i].clearAnimation();
            borders[i].setVisibility(View.VISIBLE);
            candidates[i].setText(suggestions[i]);
            Log.wtf("ジョジョ", "Candidate " + i + " text set to: " + candidates[i].getText());
        }
        for (int i = len; i < 10; i++) {
            candidates[i].clearAnimation();
            candidates[i].setVisibility(View.INVISIBLE);
            borders[i].clearAnimation();
            borders[i].setVisibility(View.INVISIBLE);
        }
        Log.wtf("ジョジョ", "Candidates visibility updated");
        setCandidatesView(v);
    }
}
