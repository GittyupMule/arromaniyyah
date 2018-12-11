package sopqua.util.arromaniyyah;

/**
 * Used for converting Latin text to Arabic, because the normal functionality was breaking
 * and we were running out of time.
 */
public abstract class SemiticizationSchema {
    public abstract String convert(String input);
}
