package main;

import java.util.HashMap;
import java.util.regex.Pattern;

public class PatternContainer {
    private HashMap<String, Pattern> mp = new HashMap<>();
    public PatternContainer(){};
    public void addPattern(String name, String pattern) {
        Pattern th = Pattern.compile(pattern);
        mp.put(name, th);
    }
    public Pattern getPattern(String name){
        return mp.get(name);
    }

}
