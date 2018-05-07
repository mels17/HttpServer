import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
/**
 * This class is an extended version of Java HashMap
 * and includes pattern-value lists which are used to
 * evaluate regular expression values. If given item
 * is a regular expression, it is saved in regexp lists.
 * If requested item matches with a regular expression,
 * its value is get from regexp lists.
 *
 * @author cb
 *
 * @param <K> : Key of the map item.
 * @param <V> : Value of the map item.
 */
public class RegExHashMap<K, V> extends HashMap<K, V> {
    // list of regular expression patterns
    private ArrayList<Pattern> regExPatterns = new ArrayList<Pattern>();
    // list of regular expression values which match patterns
    private ArrayList<V> regExValues = new ArrayList<V>();
    /**
     * Compile regular expression and add it to the regexp list as key.
     */
    @Override
    public V put(K key, V value) {

        regExPatterns.add(Pattern.compile(key.toString()));
        regExValues.add(value);
        return value;
    }
    /**
     * If requested value matches with a regular expression,
     * returns it from regexp lists.
     */
    @Override
    public V get(Object key) {
        CharSequence cs = new String(key.toString());

        for (int i = 0; i < regExPatterns.size(); i++) {
            if (regExPatterns.get(i).matcher(cs).matches()) {

                return regExValues.get(i);
            }
        }

//        return super.get(key);
        return (V) new DefaultResponse();
    }
}