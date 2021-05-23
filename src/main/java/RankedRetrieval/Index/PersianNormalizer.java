package RankedRetrieval.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersianNormalizer {

    public static final char ARABIC_YEH = '\u064A';
    /**
     * ي
     */
    public static final char Kashmiri_Yeh = '\u0620';
    /**
     * ؠ
     */
    public static final char Yeh_With_Hamza_Above = '\u0626';
    /**
     * ئ
     */
    public static final char Farsi_Yeh_With_Inverted_V = '\u063D';
    /**
     * ؽ
     */
    public static final char Farsi_Yeh_With_Two_Dots_Above = '\u063E';
    /**
     * ؾ
     */
    public static final char Farsi_Yeh_With_Three_Dots_Above = '\u063F';
    /**
     * ؿ
     */
    public static final char Arabic_Letter_Alef_Maksura = '\u063F';
    /**
     * ى
     */
    public static final char Arabic_Letter_Yeh_With_Tail = '\u06CD';
    /**
     * ۍ
     */
    public static final char Arabic_Letter_Yeh_With_Small_V = '\u06CD';
    /**
     * ێ
     */
    public static final char Arabic_Letter_E = '\u06D0';
    /**
     * ې
     */
    public static final char Arabic_Letter_Yeh_With_Three_Dots_Below = '\u06D1';
    /**
     * ۑ
     */
    public static final char ARABIC_YEH_BARREE = '\u06D2';
    ;
    /**
     * ے
     */
    public static final char ARABIC_YEH_BARREE_With_Hamza_Above = '\u06D3';
    /**
     * ۓ
     */
    public static final char FARSI_YEH = '\u06CC';

    public static final char FARSI_KEHEH = '\u06A9';
    /**
     * ک
     */
    public static final char ARABIC_KAF = '\u0643';
    /**
     * ك
     */
    public static final char Arabic_Letter_Keheh_With_Two_Dots_Above = '\u063B';
    /**
     * ػ
     */
    public static final char Arabic_Letter_Keheh_With_Tree_Dots_Above = '\u063C';
    /**
     * ؼ
     */
    public static final char Arabic_Letter_Swash_Kaf = '\u06AA';
    /**
     * ڪ
     */
    public static final char Arabic_Letter_Kaf_With_Ring = '\u06AB';
    /**
     * ګ
     */
    public static final char Arabic_Letter_Kaf_With_Dot_Above = '\u06AC';
    /**
     * ڬ
     */
    public static final char Arabic_Letter_NG = '\u06AD';
    /**
     * ڭ
     */
    public static final char Arabic_Letter_Kaf_With_Three_Dots_Below = '\u06AE';
    /**
     * ڮ
     */

    public static final char HEH = '\u0647';
    /**
     * ه
     */
    public static final char HAMZA_ABOVE = '\u0654';
    public static final char HEH_YEH = '\u06C0';
    /**
     * ۀ
     */
    public static final char HEH_GOAL = '\u06C1';
    /**
     * ہ
     */
    public static final char Arabic_Letter_Heh_Goal_With_Hamza_Above = '\u06C2';
    /**
     * ۂ
     */
    public static final char Arabic_Letter_Teh_Marbuta_Goal = '\u06C3';
    /**
     * ۃ
     */
    public static final char Arabic_Letter_Ae = '\u06d5';
    /**
     * ە
     */

    public static final char Arabic_Letter_Hamza = '\u0621';
    /**
     * ء
     */
    public static final char Arabic_Fathatan = '\u064B';
    /**
     * tanvinha
     */
    public static final char Arabic_Dammatan = '\u064C';
    /**
     * tanvinha
     */
    public static final char Arabic_Kasratan = '\u064D';
    /**
     * tanvinha
     */
    public static final char Arabic_Fatha = '\u064E';
    public static final char Arabic_Damma = '\u064F';
    public static final char Arabic_Kasra = '\u0650';
    public static final char Arabic_Shadda = '\u0651';
    public static final char Arabic_Sukun = '\u0652';
    public static final char Arabic_Hamza_Above = '\u0654';
    public static final char Arabic_Hamza_Below = '\u0655';
    public static final char Arabic_Wavy_Hamza_Below = '\u065F';
    public static final char Arabic_Letter_High_Hamza = '\u0674';
    public static final char Arabic_Small_Waw = '\u06E5';
    public static final char shaddah = '\u0651';
    public static final char Hamze_Alef_Above = 'أ';
    public static final char Hamze_Alef_Below = 'إ';
    public static final char Heh = '\u0629';


    public static final Pattern RTL_CHARACTERS =
            Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]");
    public static final Pattern ENGLISH_NUMBER = Pattern.compile("[۱۲۳٤٥٦۷۸۹۰0-9]");
    public static List<String> total = new ArrayList();

    public String[] nomallize(String[] tokenizedtext) {
        int size = tokenizedtext.length;
        for (int i = 0; i < size; i++) {
            Matcher matcher = RTL_CHARACTERS.matcher(tokenizedtext[i]);
            Matcher matcher2 = ENGLISH_NUMBER.matcher(tokenizedtext[i]);
            if (matcher.find()) {
                char[] s = tokenizedtext[i].toCharArray();
                s = farsiNormalizer(s);
                String string = new String(s);
                if (!(string.equals(tokenizedtext[i]))) {
                    tokenizedtext[i] = string;
                }
            }
            if (matcher2.find()) {
                char[] s = tokenizedtext[i].toCharArray();
                s = convertNum(s);
                String string = new String(s);
                if (!(string.equals(tokenizedtext[i]))) {
                    tokenizedtext[i] = string;
                }
            } else {
                tokenizedtext[i] = tokenizedtext[i].toLowerCase();
            }
        }
        return tokenizedtext;
    }

    public char[] farsiNormalizer(char s[]) {

        for (int i = 0; i < s.length; i++) {
            if (s[i] == ARABIC_YEH || s[i] == Kashmiri_Yeh || s[i] == Yeh_With_Hamza_Above || s[i] == Farsi_Yeh_With_Inverted_V || s[i] == Farsi_Yeh_With_Two_Dots_Above
                    || s[i] == Farsi_Yeh_With_Three_Dots_Above || s[i] == Arabic_Letter_Alef_Maksura || s[i] == Arabic_Letter_Yeh_With_Tail || s[i] == Arabic_Letter_Yeh_With_Small_V ||
                    s[i] == Arabic_Letter_E || s[i] == Arabic_Letter_Yeh_With_Three_Dots_Below || s[i] == ARABIC_YEH_BARREE || s[i] == ARABIC_YEH_BARREE_With_Hamza_Above) {
                s[i] = FARSI_YEH;
                break;
            } else if (s[i] == ARABIC_KAF || s[i] == Arabic_Letter_Keheh_With_Two_Dots_Above || s[i] == Arabic_Letter_Keheh_With_Tree_Dots_Above || s[i] == Arabic_Letter_Swash_Kaf
                    || s[i] == Arabic_Letter_Kaf_With_Ring || s[i] == Arabic_Letter_Kaf_With_Dot_Above || s[i] == Arabic_Letter_NG || s[i] == Arabic_Letter_Kaf_With_Three_Dots_Below) {
                s[i] = FARSI_KEHEH;
                break;
            } else if (s[i] == HAMZA_ABOVE || s[i] == HEH_YEH || s[i] == HEH_GOAL || s[i] == Arabic_Letter_Heh_Goal_With_Hamza_Above
                    || s[i] == Arabic_Letter_Teh_Marbuta_Goal || s[i] == Arabic_Letter_Ae || s[i] == Heh) {
                s[i] = HEH;
                break;
            } else if (s[i] == Arabic_Letter_Hamza || s[i] == Arabic_Fathatan || s[i] == Arabic_Dammatan || s[i] == Arabic_Kasratan || s[i] == Arabic_Fatha ||
                    s[i] == Arabic_Damma || s[i] == Arabic_Kasra || s[i] == Arabic_Shadda || s[i] == Arabic_Sukun || s[i] == Arabic_Hamza_Above || s[i] == Arabic_Hamza_Below ||
                    s[i] == Arabic_Wavy_Hamza_Below || s[i] == Arabic_Letter_High_Hamza || s[i] == Arabic_Small_Waw || s[i] == shaddah) {
                s = delete(s, s[i], s.length);
            } else if (s[i] == Hamze_Alef_Below || s[i] == Hamze_Alef_Above)
                s[i] = 'ا';

        }
        return s;
    }

    public char[] delete(char[] s, char c, int lenght) {
        String str = "";
        for (int i = 0; i < lenght; i++) {
            if (s[i] != c) {
                str += s[i];
            }
        }
        char[] r = str.toCharArray();
        return r;

    }

    public char[] convertNum(char[] c) {
        for (int i = 0; i < c.length; i++) {
            if (Character.isDigit(c[i])) {
                if (c[i] == '0' || c[i] == '٠') {
                    c[i] = '۰';
                } else if (c[i] == '1' || c[i] == '١') {
                    c[i] = '۱';
                } else if (c[i] == '2' || c[i] == '٢') {
                    c[i] = '۲';
                } else if (c[i] == '3' || c[i] == '٣') {
                    c[i] = '۳';
                } else if (c[i] == '4' || c[i] == '٤') {
                    c[i] = '۴';
                } else if (c[i] == '5' || c[i] == '٥') {
                    c[i] = '۵';
                } else if (c[i] == '6' || c[i] == '٦') {
                    c[i] = '۶';
                } else if (c[i] == '7' || c[i] == '٧') {
                    c[i] = '۷';
                } else if (c[i] == '8' || c[i] == '٨') {
                    c[i] = '۸';
                } else if (c[i] == '9' || c[i] == '٩') {
                    c[i] = '۹';
                }
            }
        }
        return c;
    }

}
