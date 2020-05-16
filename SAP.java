/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Collections;

public class SAP {

    private final Digraph G;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.G = G;

    }

    public int length(int v, int w) {
        checkNotNull(Collections.singletonList(v), "v cannot be null");
        checkNotNull(Collections.singletonList(w), "w cannot be null");
        validateRange(Collections.singletonList(v));
        validateRange(Collections.singletonList(w));
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    public int ancestor(int v, int w) {
        checkNotNull(Collections.singletonList(v), "v cannot be null");
        checkNotNull(Collections.singletonList(w), "w cannot be null");
        validateRange(Collections.singletonList(v));
        validateRange(Collections.singletonList(w));
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // checkNotNull(v, "vertex/vertices cannot be null");
        // checkNotNull(w, "vertex/vertices cannot be null");

        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        validateRange(v);
        validateRange(w);

        return new SapBFS(G, v, w).length();
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        validateRange(v);
        validateRange(w);
        return new SapBFS(G, v, w).ancestor();
    }

    private <T> void checkNotNull(Iterable<T> items, String msg) {
        for (T item : items) {
            if (item == null) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    private void validateRange(Iterable<Integer> v) {
        // System.out.print(v+" ");
        for (int vi : v) {
            if (vi < 0 || vi >= G.V()) {
                throw new IllegalArgumentException("given vertex " + vi + " is out of range");
            }
        }
    }

    private static class SapBFS {
         private final Digraph dg;
        private final BreadthFirstDirectedPaths v;
        private final BreadthFirstDirectedPaths w;
        private int anc;
        private int len;
        private SapBFS(Digraph dg, Iterable<Integer> v, Iterable<Integer> w) {
            this.dg = dg;
            this.v = new BreadthFirstDirectedPaths(dg, v);
            this.w = new BreadthFirstDirectedPaths(dg, w);
            anc = -1;
            len = -1;
            evaluateAnc();
        }

        private void evaluateAnc() {
            for (int a = 0; a < dg.V(); a++) {
                if (isAnc(a)) {
                    int templen = totalDist(a);
                    if (len == -1 || len > templen) {
                        len = templen;
                        anc = a;
                    }
                }
            }
        }

        private boolean isAnc(int a) {
            return v.hasPathTo(a) && w.hasPathTo(a);
        }

        private int totalDist(int a) {
            return v.distTo(a)+w.distTo(a);
        }

        public int ancestor() { return anc; }

        public int length() { return len; }
    }

    public static void main(String[] args) {
       //  In in = new In("Test files/digraph1.txt");
       //  Digraph dg = new Digraph(in);
       //  SAP sp = new SAP(dg);
       //
       // while (!StdIn.isEmpty()) {
       //     int v = StdIn.readInt();
       //     int w = StdIn.readInt();
       //     int len = sp.length(v, w);
       //     int anc = sp.ancestor(v, w);
       //     System.out.println(v + " "+w+": length= "+len+" ancestor= "+anc);
       // }
    }
}
