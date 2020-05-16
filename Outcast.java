/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String [] nouns) {
        int maxDist = -1;
        String outcastNoun = "";
        for (String nounA : nouns) {

            int dist = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB))
                    dist = dist + wordNet.distance(nounA, nounB);
            }

            if (maxDist < dist) {
                outcastNoun = nounA;
                maxDist = dist;
            }
        }
        return outcastNoun;
    }
    public static void main(String[] args) {
        WordNet w = new WordNet("Test files/synsets.txt", "Test files/hypernyms.txt");
        Outcast ot = new Outcast(w);
        In in = new In("Test files/outcast5.txt");
        String noun = "";
        while (in.hasNextLine()) {
            noun = noun + in.readLine()+ " ";
        }
        String [] nouns = noun.split(" ");
        for (String n: nouns) {
            System.out.println(n);
        }

        System.out.println("outcast = " + ot.outcast(nouns));
    }
}
