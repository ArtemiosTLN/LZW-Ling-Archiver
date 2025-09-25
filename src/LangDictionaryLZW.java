import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * ENG
 * This is a file that contains language dictionaries and all the stuff needed for handling them.
 * If you want to add another language dictionary you can do it here.
 * EST
 * See fail sisaldab kõike keele sõnastike ja teise vajalike asju nende käsitsemiseks.
 * Kui Te tahate oma sõnastiku lisada, tehke seda siin.
 * RUS
 * Этот файл содержит языковые словари и все необходимые вещи для управления ними.
 * Если вы хотите добавить свой словарь, то это можно сделать здесь.
 */

public class LangDictionaryLZW {
    public LinkedHashMap<String, Integer> Dictionary;
    public HashMap<Character, TreeSet<String>> WordsByFirstLetter;
    public int maximumLength;
    public int code = 0;

    public LangDictionaryLZW() {
        Dictionary = new LinkedHashMap<>();
        WordsByFirstLetter = new HashMap<>();
        maximumLength = 0;
    }

    public void addTextSymbols(HashSet<Character> symbols) {
        for (Character textSymbol : symbols) {
            this.addWord(String.valueOf(textSymbol));
        }
    }
    public void setLang(String lang) {
        StringBuilder result = new StringBuilder();
        try {
            if (Objects.equals(lang, "none")) return;
            InputStreamReader reader = new InputStreamReader(new FileInputStream("Morphemes/" + lang + ".txt"), StandardCharsets.UTF_8);
            int i;
            while ((i = reader.read()) != -1) {
                char c = (char) i;
                if (c == ';') {
                    this.addWord(result.toString());
                    result = new StringBuilder();
                } else result.append(c);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e + "\nFile was not found in specified location.");
        }
    }
    public void addWord(String word) {
        Dictionary.put(word, code);
        code++;
        if (word.length() > 1) {
            if (WordsByFirstLetter.containsKey(word.charAt(0))) {
                WordsByFirstLetter.get(word.charAt(0)).add(word);
            } else WordsByFirstLetter.put(word.charAt(0), new TreeSet<>(Comparator.reverseOrder()){{add(word);}});
            if (maximumLength < word.length()) maximumLength = word.length();
        }
    }
    public Integer getCode(String word) {
        return Dictionary.get(word);
    }
    public String getWord(Integer b) {
        for (String s : Dictionary.keySet()) {
            if (Dictionary.get(s).equals(b)) {
                return s;
            }
        }
        return null;
    }
    public TreeSet<String> getFirstLetterList(Character firstLetter) {
        return WordsByFirstLetter.get(firstLetter);
    }
    public boolean isDictionaryNotFull() {
        return code < Integer.MAX_VALUE;
    }
    public int getSizeOfDictionary() {
        return Dictionary.size();
    }
}
