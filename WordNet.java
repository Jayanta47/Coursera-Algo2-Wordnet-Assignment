/* *****************************************************************************
 *  Name: Wordnet executive class
 *  Date: 5/4/2020
 *  Description: coursera algorithm II course assignment 1
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Digraph g;
    private Map<Integer, String> synsetIdtoNounMap = new HashMap<Integer, String>();
    private Map<String, Set<Integer>> nounToSynIdMap = new HashMap<String, Set<Integer>>();
    private final SAP sp;
    /**
     * Constructor takes name of files
     * @param synsets Name of the file where synsets exist
     * @param hypernyms Name of the file where hypernyms exist
     */
    public WordNet(String synsets, String hypernyms) {
        checkNull(synsets, "Synsets name cannot be null");
        checkNull(hypernyms, "Hypernyms name canot be null");

        // long start = System.currentTimeMillis();
        int nVertices = parseSynset(synsets);
        g = parseHypernym(hypernyms, nVertices);

        checkCycle();
        checkRooted();

        sp = new SAP(g);

        // long fin = System.currentTimeMillis();
        // System.out.println(g.E());
        // System.out.println(nVertices);
        // System.out.println("total execution time: "+ (fin-start));
    }


    private int parseSynset(String filename) {
        In in = null;
        try {
            in = new In(filename);
            return parseSynsetUtil(in);
        } finally {
            if (in != null) in.close();
        }
    }

    private int parseSynsetUtil(In in) {
        int nVertices = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int ID = Integer.parseInt(parts[0]);
            String literal = parts[1];
            synsetIdtoNounMap.put(ID, literal);
            // mapping noun to ID
            String [] noun = literal.split(" ");
            for (String n: noun) {
                Set<Integer> set = nounToSynIdMap.get(n);
                if (set == null) {
                    set = new HashSet<Integer>();
                }
                set.add(ID);
                nounToSynIdMap.put(n, set);
            }
            nVertices++;
        }
        return nVertices;
    }

    private Digraph parseHypernym(String filename, int nVertices) {
        In in = null;
        try {
            in = new In(filename);
            return parseHypernymUtil(in, nVertices);
        } finally {
            if (in != null)in.close();
        }
    }

    private Digraph parseHypernymUtil(In in, int nVertices) {
        Digraph gt = new Digraph(nVertices);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int hypoID = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int hyperID = Integer.parseInt(parts[i]);
                gt.addEdge(hypoID, hyperID);
            }
        }
        return gt;
    }

    public Iterable<String> nouns() {
        return Collections.unmodifiableSet(nounToSynIdMap.keySet());
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToSynIdMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        checkNull(nounA, null);
        checkNull(nounB, null);
        Set<Integer> v = nounToSynIdMap.get(nounA);
        Set<Integer> w = nounToSynIdMap.get(nounB);
        int dist = sp.length(v, w);
        return dist;
    }

    public String sap(String nounA, String nounB) {
        checkNull(nounA, null);
        checkNull(nounB, null);

        Set<Integer> v = nounToSynIdMap.get(nounA);
        Set<Integer> w = nounToSynIdMap.get(nounB);

        int a = sp.ancestor(v, w);
        String anc = synsetIdtoNounMap.get(a);
        return anc;
    }

    private <T> void checkNull(T name, String msg) {
        if (name == null) {
            if (msg != null)throw new IllegalArgumentException(msg);
            else throw new IllegalArgumentException();
        }
    }

    private void checkCycle() {
        DirectedCycle dc = new DirectedCycle(g);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("The given graph is not a DAG");
        }
    }

    private void checkRooted() {
        int rootCnt = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                rootCnt++;
            }
        }

        if (rootCnt != 1) {
            throw new IllegalArgumentException("The given graph is not a rooted DAG");
        }
    }

    public static void main(String[] args) {

        String syntxt = "Test files/synsets6.txt";
        String hyptxt = "Test files/hypernyms6InvalidCycle+Path.txt";
        WordNet w = new WordNet(syntxt, hyptxt);
        // System.out.println(w.nouns().getClass());
        // Iterator<String> its = w.nouns().iterator();
        // ArrayList<String> arr = new ArrayList<String>();
        // while (its.hasNext()) {
        //     arr.add(its.next());
        // }
        // Collections.sort(arr);
        // for (String n: arr) {
        //     System.out.println(n);
        // }
    }
}
