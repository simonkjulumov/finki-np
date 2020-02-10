import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    private static Map<String, ArrayList<String>> anagrams;

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        anagrams = new HashMap<String, ArrayList<String>>();
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        while(scanner.hasNextLine()) {
            String word = scanner.nextLine();
            anagrams.put(word.trim(), new ArrayList<String>() {{ add(word.trim()); }});
        }
        scanner.close();

        for(String word : anagrams.keySet()) {
            for(String w : anagrams.keySet()){
                ArrayList<String> list = anagrams.get(w);
                if(list.size() >= 5)
                {
                    Collections.sort(list);
                    continue;
                }
                if(!word.equals(w) && isAnagram(word, w) && !wordAlreadyExistsInValidAnagramList(w)){
                    anagrams.get(word).add(w);
                }
            }
        }

        List<ArrayList<String>> anagramWordList = anagrams.values().stream().filter(x -> x.size() >= 5).collect(Collectors.toList());
        Collections.sort(anagramWordList, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> l1, ArrayList<String> l2) {
                return l1.get(0).compareTo(l2.get(0));
            }
        });

        for(ArrayList<String> anagramWords : anagramWordList) {
            StringJoiner sj = new StringJoiner(" ", "", "\n");
            for(String s : anagramWords.stream().collect(Collectors.toList())) {
                sj.add(s);
            }
            System.out.print(sj);
        }
    }

    private static boolean wordAlreadyExistsInValidAnagramList(String word) {
        List<ArrayList<String>> anagramWordList = anagrams.values().stream().filter(x -> x.size() >= 5).collect(Collectors.toList());
        for(ArrayList<String> anagramWords :  anagramWordList) {
            for(String anagramWord : anagramWords)
                if(anagramWord.equals(word))
                    return true;
        }
        return false;
    }

    private static boolean isAnagram(String wordA, String wordB) {
        if(wordA.length() != wordB.length()) return false;

        char wordACharArray[] = wordA.toCharArray();
        Arrays.sort(wordACharArray);
        char wordBCharArray[] = wordB.toCharArray();
        Arrays.sort(wordBCharArray);

        return new String(wordACharArray).equals(new String(wordBCharArray));
    }
}
