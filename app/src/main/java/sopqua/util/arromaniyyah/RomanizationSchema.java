package sopqua.util.arromaniyyah;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class RomanizationSchema {

    public static HashMap<char[], ArabicGrapheme> table;
    static {
        table = new HashMap<>();
        table.put(new char[]{'\u0671'}, ArabicGrapheme.ALIF_WASLAH);
        table.put(new char[]{'\u0670'}, ArabicGrapheme.HANJARIYYAH);
        table.put(new char[]{'\u0652'}, ArabicGrapheme.SUKUN);
        table.put(new char[]{'\u0651'}, ArabicGrapheme.SHADDAH);
        table.put(new char[]{'\u0650'}, ArabicGrapheme.KASRAH);
        table.put(new char[]{'\u064f'}, ArabicGrapheme.DAMMAH);
        table.put(new char[]{'\u064e'}, ArabicGrapheme.FATHAH);
        table.put(new char[]{'\u064d'}, ArabicGrapheme.KASRAH_TANWIN);
        table.put(new char[]{'\u064c'}, ArabicGrapheme.DAMMAH_TANWIN);
        table.put(new char[]{'\u064b'}, ArabicGrapheme.FATHAH_TANWIN);
        table.put(new char[]{'\u064a'}, ArabicGrapheme.YA);
        table.put(new char[]{'\u0649'}, ArabicGrapheme.MAQSURAH);
        table.put(new char[]{'\u0648'}, ArabicGrapheme.WAW);
        table.put(new char[]{'\u0647'}, ArabicGrapheme.HA);
        table.put(new char[]{'\u0646'}, ArabicGrapheme.NUN);
        table.put(new char[]{'\u0645'}, ArabicGrapheme.MIM);
        table.put(new char[]{'\u0644'}, ArabicGrapheme.LAM);
        table.put(new char[]{'\u0643'}, ArabicGrapheme.KAF);
        table.put(new char[]{'\u0642'}, ArabicGrapheme.QAF);
        table.put(new char[]{'\u0641'}, ArabicGrapheme.FA);
        table.put(new char[]{'\u063A'}, ArabicGrapheme.GHAYN);
        table.put(new char[]{'\u0639'}, ArabicGrapheme.AYN);
        table.put(new char[]{'\u0638'}, ArabicGrapheme.ZA);
        table.put(new char[]{'\u0637'}, ArabicGrapheme.TTA);
        table.put(new char[]{'\u0636'}, ArabicGrapheme.DAD);
        table.put(new char[]{'\u0635'}, ArabicGrapheme.SAD);
        table.put(new char[]{'\u0634'}, ArabicGrapheme.SHIN);
        table.put(new char[]{'\u0633'}, ArabicGrapheme.SIN);
        table.put(new char[]{'\u0632'}, ArabicGrapheme.ZAY);
        table.put(new char[]{'\u0631'}, ArabicGrapheme.RA);
        table.put(new char[]{'\u0630'}, ArabicGrapheme.DHAL);
        table.put(new char[]{'\u062f'}, ArabicGrapheme.DAL);
        table.put(new char[]{'\u062e'}, ArabicGrapheme.KHA);
        table.put(new char[]{'\u062d'}, ArabicGrapheme.HHA);
        table.put(new char[]{'\u062c'}, ArabicGrapheme.JIM);
        table.put(new char[]{'\u062b'}, ArabicGrapheme.THA);
        table.put(new char[]{'\u062a'}, ArabicGrapheme.TA);
        table.put(new char[]{'\u0629'}, ArabicGrapheme.MARBUTAH);
        table.put(new char[]{'\u0628'}, ArabicGrapheme.BA);
        table.put(new char[]{'\u30f4', '\u0627', '\u064e', '\u0644'}, ArabicGrapheme.AL);
        table.put(new char[]{'\u0627'}, ArabicGrapheme.ALIF);
        table.put(new char[]{'\u0622'}, ArabicGrapheme.ALIF_MADDAH);
        table.put(new char[]{'\u0621'}, ArabicGrapheme.HAMZAH);
        table.put(new char[]{'\u30f4', '\u0625', '\u0650'}, ArabicGrapheme.INITIAL_HAMZAH_I);
        table.put(new char[]{'\u30f4', '\u0625', '\u064f'}, ArabicGrapheme.INITIAL_HAMZAH_U);
        table.put(new char[]{'\u30f4', '\u0625', '\u064e'}, ArabicGrapheme.INITIAL_HAMZAH_A);
        table.put(new char[]{'\u0650', '\u0626', '\u0652'}, ArabicGrapheme.MEDIAL_IY_HAMZAH);
        table.put(new char[]{'\u064e', '\u0626', '\u0652'}, ArabicGrapheme.MEDIAL_AY_HAMZAH);
        table.put(new char[]{'\u0626', '\u0650'}, ArabicGrapheme.MEDIAL_HAMZAH_I);
        table.put(new char[]{'\u0626', '\u064f'}, ArabicGrapheme.MEDIAL_HAMZAH_U_ON_YA);
        table.put(new char[]{'\u0626', '\u064e'}, ArabicGrapheme.MEDIAL_HAMZAH_A_ON_YA);
        table.put(new char[]{'\u0624', '\u064f'}, ArabicGrapheme.MEDIAL_HAMZAH_U_ON_WAW);
        table.put(new char[]{'\u0624', '\u064e'}, ArabicGrapheme.MEDIAL_HAMZAH_A_ON_WAW);
        table.put(new char[]{'\u0623', '\u064e'}, ArabicGrapheme.MEDIAL_HAMZAH_A_ON_ALIF);
        table.put(new char[]{'\u0626', '\u30f4'}, ArabicGrapheme.FINAL_HAMZAH_ON_YA);
        table.put(new char[]{'\u0625', '\u30f4'}, ArabicGrapheme.FINAL_HAMZAH_BELOW_ALIF);
        table.put(new char[]{'\u0624', '\u30f4'}, ArabicGrapheme.FINAL_HAMZAH_ON_WAW);
        table.put(new char[]{'\u0623', '\u30f4'}, ArabicGrapheme.FINAL_HAMZAH_ABOVE_ALIF);
    }

    public static List<ArabicGrapheme> processWord(String word) {
        String newWord = '\u30f4' + word + '\u30f4';
        Set<char[]> keySet = table.keySet();
        char[][] keyArray = keySet.toArray(new char[][]{});
        Arrays.sort(keyArray, new Comparator<char[]>() {
            @Override
            public int compare(char[] o1, char[] o2) {
                return Character.compare(o2[0], o1[0]);
            }
        });
        ArrayList<ArabicGrapheme> result = new ArrayList<>();
        outer: while (newWord.length() > 0) {
            for (char[] key : table.keySet()) {
                if (newWord.startsWith(new String(key))) {
                    result.add(table.get(key));
                    newWord = newWord.substring(key.length);
                    continue outer;
                }
            }
            newWord = newWord.substring(1);
        }
        return result;
    }

    public abstract String firstPass(ArabicGrapheme input);

    public String firstPass(List<ArabicGrapheme> input) {
        StringBuilder result = new StringBuilder();
        for (ArabicGrapheme a : input) {
            result.append(firstPass(a));
        }
        return result.toString();
    }

    public abstract String postProcessing(String firstPassInput);

    public String parseWord(String input) {
        return postProcessing(firstPass(processWord(input)));
    }

}
