import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

class TermFrequency {
    private HashMap<String, Integer> words;
    private String[] stopWords;
    private int totalWordRead = 0;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        this.words = new HashMap<>();
        this.stopWords = Arrays.copyOf(stopWords, stopWords.length);

        readWords(inputStream);
    }

    public int countTotal() {
        return totalWordRead;
    }

    public int countDistinct() {
        return (int)words.keySet().stream().distinct().count();
    }

    public List<String> mostOften(int k) {

        HashMap<String, Integer> copy = (HashMap<String, Integer>) words.clone();
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(copy.entrySet());
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> stringIntegerEntry, Entry<String, Integer> t1) {
                //sortiraj spored string ako ima poveke isti max vrednosti
                if(stringIntegerEntry.getValue() == t1.getValue()){
                    return stringIntegerEntry.getKey().compareTo(t1.getKey());
                }
                return t1.getValue().compareTo(stringIntegerEntry.getValue());
            }
        });

        return list.stream().map(x -> x.getKey()).collect(Collectors.toList()).subList(0, k);
        /*
        List<String> result = copy
                .entrySet()
                .stream()
                .filter(x -> x.getValue() == max)
                .map(x -> x.getKey())
                .collect(Collectors.toList())
                .subList(0, k);
        if(result.size() > 1){
            Collections.sort(result);
        }
        return result;

         */
    }

    private void readWords(InputStream inputStream) {
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        while(scanner.hasNext()) {
            String input = scanner.next();
            String formatted = input.replaceAll("[.,]*", "");
            String word = formatted.trim().toLowerCase();
            if(!isStopWord(word)&&!word.isEmpty()) {
                //check if there is a value for that word in the dictionary
                int count = words.computeIfAbsent(word, key -> 0);
                words.put(word, count + 1);
                ++totalWordRead;
            }
        }
    }

    private boolean isStopWord(String word) {
        for(String stopWord : stopWords) {
            if(stopWord.equals(word)){
                return true;
            }
        }
        return false;
    }
}