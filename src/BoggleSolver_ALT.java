/**
 * @author Yury Park
 * This one uses Ternary Trie (less space used, but slightly slower than the R-way Trie used in BoggleSolver class)
 * Minimal comments provided for this class as it is no longer used. For academic reference only.
 */
import java.util.HashSet;

/**
 * @author Yury Park
 */
public class BoggleSolver_ALT {
    private dictionaryST<Integer> diction = new dictionaryST<>();
    private HashSet<String> words;
    private boolean[][] visited;
    private BoggleBoard board;

    private class dictionaryST<Value> {
        private Node root;
        private int size;

        private class Node {
            private Value val;
            private char c;
            private Node left, right, mid;

            public Node () { }

            public Node(char c) {
                this.c = c;
            }

            public void setVal(Value val) {
                this.val = val;
            }

            public void setLeft(Node l) {
                left = l;
            }

            public void setRight(Node r) {
                right = r;
            }

            public void setMid(Node m) {
                mid = m;
            }

            public Node getLeft() {
                return left;
            }

            public Node getRight() {
                return right;
            }

            public Node getMid() {
                return mid;
            }

            public Value getVal() {
                return val;
            }

            public char getChar() {
                return c;
            }
        }

        public dictionaryST() {
        }

        public boolean contains(String s) {
            if (get(s) != null) return true;
            return false;
        }

        /*
         *  Puts the string s into the dictionary.
         *  Ensures that subsequent calls to get(s) return v.
         *
         * @param s the String to be put into the dictionary
         */
        public void put(String s, Value v) {
            if (!contains(s))
                size++;
            root = put(root, s, v, 0);
        }

        private Node put(Node x, String st, Value val, int d) {
            char c = st.charAt(d);
            if (x == null)
                x = new Node(c);

            if (x.getChar() < c) {
                x.setLeft(put(x.getLeft(), st, val, d));
            } else if (x.getChar() > c) {
                x.setRight(put(x.getRight(), st, val, d));
            } else if (d < st.length() - 1) {
                x.setMid(put(x.getMid(), st, val, d + 1));
            } else {
                x.setVal(val);
            }
            return x;
        }
        /*
         * Returns the a positive Value if the string exists in the dictionary
         * and null if not.
         *
         * @param s String to find in dictionary
         */
        public Value get(String s) {
            Node x = get(root, s, 0);
            if (x == null) return null;
            return x.getVal();
        }

        private Node get(Node x, String st, int d) {
            char c = st.charAt(d);
            if (x == null)
                return null;

            if (x.getChar() < c) {
                return get(x.getLeft(), st, d);
            } else if (x.getChar() > c) {
                return get(x.getRight(), st, d);
            } else if (d < st.length() - 1) {
                return get(x.getMid(), st, d + 1);
            } else {
                return x;
            }
        }

        private Node get_NonRecur(Node x, String st, int d) {
            if (x == null)
                return null;

            Node currentX = x;
            int  currentD = d;

            while (currentX != null) {
            	char c = st.charAt(currentD);
            	if (currentX.getChar() < c) {
            		currentX = currentX.getLeft();
            	} else if (currentX.getChar() > c) {
            		currentX = currentX.getRight();
                } else if (currentD < st.length() - 1) {
                    currentX = currentX.getMid();
                    currentD++;
                } else {
                	return currentX;
                }
            }
            //end while
            return currentX;
        }

        /*
         * Returns all the words in the dictionary
         *
         * @return Returns an Iterable Object which is empty iff there are no
         *          words in the dictionary
         */
        public Iterable<String> keys() {
            Queue<String> queue = new Queue<String>();
            collect(root, "", queue);
            return queue;
        }

        private void collect(Node x, String pref, Queue<String> queue) {
            if (x == null)
                return;
            collect(x.getLeft(), pref, queue);
            String repPref = pref + x.getChar();
            if (x.getVal() != null)
                queue.enqueue(repPref);
            collect(x.getMid(), repPref, queue);
            collect(x.getRight(), pref, queue);
        }

        public boolean isAPrefix(String s) {
            return (get(root, s, 0) != null);
        }

        /*
         * Returns all keys in the dictionary that have prefix 'prefix'
         */
        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == "") return keys();
            Queue<String> queue = new Queue<String>();
            Node x  = get(root, prefix, 0);
            if (x == null) return queue;
            if (x.getVal() != null) queue.enqueue(prefix);
            collect(x.getMid(), prefix, queue);
            return queue;
        }

        /*
         * Finds all keys in the dictionary that match a string with wildcards
         *
         * @param pat A string with '.' characters representing 'wildcards'
         * @return    Returns any Keys that match 'pat' with '.' characters replaced
         *            by any possible letter
         */
        public Iterable<String> keysThatMatch(String pat) {
            Queue<String> queue = new Queue<String>();
            collect(root, "", pat, 0, queue);
            return queue;
        }

        private void collect(Node x, String pref, String pat, int place, Queue<String> queue) {
            if (x == null) return;
            char c = pat.charAt(place);
            if (c == '.' || x.getChar() < c)
                collect(x.getLeft(), pref, pat, place, queue);
            if (c == '.' || c == x.getChar()) {
                if (place == pat.length() - 1 && x.getVal() != null)
                    queue.enqueue(pref + x.getChar());
                if (place < pat.length() - 1)
                    collect(x.getMid(), pref + x.getChar(), pat, place + 1, queue);
            }
            if (c == '.' || x.getChar() > c)
                collect(x.getRight(), pref, pat, place, queue);
        }
    }



    public BoggleSolver_ALT (String[] dictionary) {
        for (String s : dictionary) {
            diction.put(s, 1);
        }
    }

    // returns a set of all the valid words in a boggle board as an iterable
    public Iterable<String> getAllValidWords(BoggleBoard board) {
//        HashSet<String> words = new HashSet<String>();
    	words = new HashSet<String>();
    	this.board = board;
    	int R = board.rows();
    	int C = board.cols();

        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++) {
//                boolean[][] visited = new boolean[board.rows()][board.cols()];
            	visited = new boolean[R][C];
                String word = "";
                boggleDFS(i, j, word, diction.root, 0);
            }

        return words;
    }

    private void boggleDFS(int i, int j, String word, dictionaryST<Integer>.Node root, int d) {
//        if (visited[i][j]) return;

        char letter = board.getLetter(i,j);
        word += (letter == 'Q' ? "QU" : letter);
//        System.out.println("Testing: " + word.toString());

//        dictionaryST<Integer>.Node newRoot = this.diction.get(root, word, d);
        dictionaryST<Integer>.Node newRoot = this.diction.get_NonRecur(root, word, d);
//        System.out.println("Testing:" + newRoot);

        if (newRoot != null) {
        	if (word.length() > 2 && newRoot.getVal() != null) words.add(word);
            visited[i][j] = true;

            newRoot = newRoot.getMid();
            int newD = (letter == 'Q' ? d + 2 : d + 1);

            boolean firstRow = i == 0;
            boolean firstCol = j == 0;
            boolean lastRow = i == board.rows() - 1;
            boolean lastCol = j == board.cols() - 1;
            if (!firstRow && !firstCol && !visited[i-1][j-1]) boggleDFS(i - 1, j - 1, word, newRoot, newD);
            if (!firstRow              && !visited[i-1][j])   boggleDFS(i - 1, j    , word, newRoot, newD);
            if (!firstRow && !lastCol  && !visited[i-1][j+1]) boggleDFS(i - 1, j + 1, word, newRoot, newD);
            if (             !firstCol && !visited[i][j-1])   boggleDFS(i    , j - 1, word, newRoot, newD);
            if (             !lastCol  && !visited[i][j+1])   boggleDFS(i    , j + 1, word, newRoot, newD);
            if (!lastRow &&  !firstCol && !visited[i+1][j-1]) boggleDFS(i + 1, j - 1, word, newRoot, newD);
            if (!lastRow               && !visited[i+1][j])   boggleDFS(i + 1, j    , word, newRoot, newD);
            if (!lastRow &&  !lastCol  && !visited[i+1][j+1]) boggleDFS(i + 1, j + 1, word, newRoot, newD);

            visited[i][j] = false;
        }
    }

    // returns the score of a given word
    public int scoreOf(String word) {
        if (this.diction.keysThatMatch(word).iterator().hasNext()) {
            int length = word.length();
            if (length < 3) return 0;
            if (length < 5) return 1;
            if (length == 5) return 2;
            if (length == 6) return 3;
            if (length == 7) return 5;
            return 11;
        }
    return 0;
    }


    public static void main(String[] args) {
    	In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver_ALT solver = new BoggleSolver_ALT(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}