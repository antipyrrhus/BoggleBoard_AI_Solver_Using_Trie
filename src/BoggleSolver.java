import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Yury Park
 * This class - BoggleSolver. Automated solver for the Boggle game (For reference, see https://en.wikipedia.org/wiki/Boggle)
 * Given a boggle board and a dictionary of valid words, finds ALL valid words of length > 2 on that board.
 * This effectively results a list of words comprising a perfect solution to any boggle board.
 * NOTE: the letter 'Q' is automatically treated as 'Qu' for purposes of this game.
 */
public class BoggleSolver {
    private BoggleTrieST<Integer> dict; //custom inner class

    /**
     * BoggleTrieST
     * @author Yury Park
     * An inner Trie class. Used for fast String search of valid words.
     * @param <Value> Value stands for something arbitrary, similar to parameterizing <T>.
     */
    private static class BoggleTrieST<Value> {
        private static final int R = 26; // 26 A-Z letters
        private static final int OFFSET = 65; // Offset of letter A in ASCII table, because 'A' translates to 65 on the Ascii Table.

        //The root of this trie class contains the starting character of every valid possible word.
        private Node root = new Node();

        /**
         * Node. Inner class.
         * @author Yury Park
         * Each Node in this Trie class stands for some character of the English alphabet in some sequence of letters
         * that comprise part of a valid English word. Each Node contains two attributes:
         * 1) a generic value (Object val), which is either null or non-null. If the value is non-null,
         *    then the node represents the last character of a valid word.
         * 2) An array of other Nodes (Node[] next), which indicates the list of possible next characters in the sequence.
         *    Some of the array elements may be null, in which case it means the character pertaining to that array position
         *    does not exist.
         */
        private static class Node {
            private Object val;
            private Node[] next = new Node[R];	//Since R = 26 (see above), every Node will have up to 26 children.
        }

        /* Not needed for this game, but just in case. */
//        public enum NodeType {
//            PREFIX, MATCH, NON_MATCH
//        }

        /*****************************************
         * Is the given word in the trie or not? *
         *****************************************/
        public boolean contains(String key) {
        	//Return true IFF the value associated with this String is not null.
        	//Otherwise, it means this trie does not contain the given String key.
            return get(key) != null;	//invoke custom method get()
        }

        /**
         * Method: get. Invoked by contains() method.
         * @param key given String
         * @return the value (if any) associated with the given String, if the String is found in this Trie.
         *         Otherwise, if the String doesn't exist, or if the String is not a valid word (no value associated with it),
         *         return null.
         */
        public Value get(String key) {
            Node x = this.get(root, key, 0);	//custom overloaded method
            if (x == null)
                return null;
            return (Value) x.val;  //unavoidable cast as long as we're using generic Value
        }

        /**
         * Method: get. Recursive helper method for get().
         * @param x current Node
         * @param key given String to search for
         * @param d the current index no. of the given String's char position.
         * @return the Node associated with the given String, if it exists in this Trie. Otherwise return null.
         */
        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;  //If the current index equals the length of the given String, we're done. Return this node.
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d + 1);	//Must offset the index no. by OFFSET because 'A' translates to 25 on the ASCII table.
        }

        /**
         * Method: get_NonRecur
         *         Non-recursive version of the get() method directly above. Should be slightly faster.
         * @param x current Node
         * @param key given String to search for
         * @param d the current index no. of the given String's char position.
         * @return the Node associated with the given String, if it exists in Trie. Otherwise return null.
         */
        private Node get_NonRecur(Node x, StringBuilder key, int d) {
            Node currentX = x;
            int  currentD = d;

            while (currentX != null && currentD < key.length()) {
            	char c = key.charAt(currentD);
            	currentX = currentX.next[c - OFFSET];
            	currentD++;
            }
            //end while
            return currentX;
        }

        /****************************************************
         * Insert key-value pair into the Trie.
         ****************************************************/
        /**
         * Method: put
         * @param key a valid word String to insert into this Trie.
         * @param val some Generic Value (e.g. the number 1) that, as long as it is non-null and
         *        associated with a Node object, indicates that that Node is the last character of a valid String.
         */
        public void put(String key, Value val) {
            root = put(root, key, val, 0);  //custom recursive method
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {  //This indicates we've reached the end of the given String key.
                x.val = val;          //Set the value to this Node to indicate end of the String.
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, val, d + 1);  //recurse by putting the next character
            return x;
        }

        /************************************************************************************************************
         * MOST OF THE METHODS BELOW ARE NOT NEEDED FOR THIS PROGRAM. THEY'RE LEFT IN JUST AS AN ACADEMIC EXERCISE. *
        *************************************************************************************************************/

//        // find the key that is the longest prefix of s
//        public String longestPrefixOf(String query) {
//            int length = longestPrefixOf(root, query, 0, 0);
//            return query.substring(0, length);
//        }
//
//        // find the key in the subtrie rooted at x that is the longest
//        // prefix of the query string, starting at the dth character
//        private int longestPrefixOf(Node x, String query, int d, int length) {
//            if (x == null)
//                return length;
//            if (x.val != null)
//                length = d;
//            if (d == query.length())
//                return length;
//            char c = query.charAt(d);
//            return longestPrefixOf(x.next[c - OFFSET], query, d + 1, length);
//        }
//
//        public Iterable<String> keys() {
//            return keysWithPrefix("");
//        }
//
//        public Iterable<String> keysWithPrefix(String prefix) {
//            Queue<String> queue = new Queue<String>();
//            Node x = get(root, prefix, 0);
//            collect(x, prefix, queue);
//            return queue;
//        }
//
//        public boolean isPrefix(String prefix) {
//            return get(root, prefix, 0) != null;
//        }
//
//        public NodeType getNodeType(String key) {
//            Node x = get(root, key, 0);
//            if (x == null)
//                return NodeType.NON_MATCH;
//            else if (x.val == null)
//                return NodeType.PREFIX;
//            else
//                return NodeType.MATCH;
//        }
//
//        private void collect(Node x, String key, Queue<String> queue) {
//            if (x == null)
//                return;
//            if (x.val != null)
//                queue.enqueue(key);
//            for (int c = 0; c < R; c++)
//                collect(x.next[c - OFFSET], key + (char) c, queue);
//        }
//
//        public Iterable<String> keysThatMatch(String pat) {
//            Queue<String> q = new Queue<String>();
//            collect(root, "", pat, q);
//            return q;
//        }
//
//        public void collect(Node x, String prefix, String pat, Queue<String> q) {
//            if (x == null)
//                return;
//            if (prefix.length() == pat.length() && x.val != null)
//                q.enqueue(prefix);
//            if (prefix.length() == pat.length())
//                return;
//            char next = pat.charAt(prefix.length());
//            for (int c = 0; c < R; c++)
//                if (next == '.' || next == c)
//                    collect(x.next[c - OFFSET], prefix + (char) c, pat, q);
//        }
//
//        public void delete(String key) {
//            root = delete(root, key, 0);
//        }
//
//        private Node delete(Node x, String key, int d) {
//            if (x == null)
//                return null;
//            if (d == key.length())
//                x.val = null;
//            else {
//                char c = key.charAt(d);
//                x.next[c - OFFSET] = delete(x.next[c - OFFSET], key, d + 1);
//            }
//            if (x.val != null)
//                return x;
//            for (int c = 0; c < R; c++)
//                if (x.next[c - OFFSET] != null)
//                    return x;
//            return null;
//        }
    }
    //end private static class BoggleTrieST<Value>

    /**
     * Constructor
     * Initializes the Trie data structure using the given array of strings as the dictionary.
     * (Assumes each word in the dictionary contains only the uppercase letters A through Z.)
     *
     * @param dictionary An Array of valid words that make up a dictionary.
     */
    public BoggleSolver(String[] dictionary) {
    	this.dict = new BoggleTrieST<>();
        for (String s : dictionary) {
        	//1 is just some arbitrary generic Value we're going to associate with the last character of a valid word in the dictionary.
            dict.put(s, 1);  //see custom put() method in BoggleTrieST class.
        }
    }

    /**
     * Returns the set of all valid words of length > 2 in the given Boggle board, as an Iterable.
     *
     * @param board
     * @return The set of all valid words of length > 2 in the given Boggle board, as an Iterable.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> words = new HashSet<>();

        /* Go thru every square in the board and search for all words that can be formed starting at that square */
        StringBuilder word = new StringBuilder(); //New empty StringBuilder for faster String manipulation
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                searchWords(board, i, j, words, word); //custom method
            }
        }
        return words;	//return the Set of words found.
    }

    /**
     * Method: searchWords. Helper method invoked by getAllValidWords().
     * @param board BoggleBoard object.
     * @param i board's row index
     * @param j board's col index
     * @param words The HashSet which we want to populate with all valid words on this board starting at the given (i,j) location.
     * @param word A StringBuilder object to help us look for valid words character by character
     */
    private void searchWords(BoggleBoard board, int i, int j, HashSet<String> words, StringBuilder word) {
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        word.setLength(0);	//reset the StringBuilder to contain empty String before running dfs() method.
        dfs(board, i, j, words, visited, word, this.dict.root, 0); //custom method call
    }

    /**
     * Method: dfs. Helper method invoked by searchWords().
     * @param board BoggleBoard object
     * @param i board's row index
     * @param j board's col index
     * @param words The HashSet which we want to populate with all valid words on this board starting at the given (i,j) location.
     * @param visited 2D array. visited[i][j] = true IFF (i,j) has already been visited during this particular dfs method call.
     * @param word A StringBuilder object to help us look for valid words character by character
     * @param root the current Node of the Trie from where to begin the word search
     * @param d the index no. pertaining to a character position of a word String (used for the get_Nonrecur() method below)
     */
    private void dfs(BoggleBoard board, int i, int j, HashSet<String> words, boolean[][] visited, StringBuilder word,
    		         BoggleSolver.BoggleTrieST.Node root, int d) {

        char letter = board.getLetter(i, j);  //Get the letter at the (i,j) location of the board.
        word.append(letter == 'Q' ? "QU" : letter);  //append to the current word String using StringBuilder.

        /* After the above append, see if the resulting word String (which may be an INCOMPLETE or INVALID word)
         * exists in the Trie (represented by this.dict), starting at the given root (which is not necessarily THE original root.)
         * We update and pass on new root recursively as a parameter to save time.
         * If the Trie contains the (possibly incomplete or invalid) word, newRoot will be a non-null node, and we can move on. */
        BoggleSolver.BoggleTrieST.Node newRoot = this.dict.get_NonRecur(root, word, d);

        /* if newRoot is not null, it means the given (possibly incomplete or invalid) word exists in the Trie,
         * and we should explore whether the word is an actual word or not, and also whether
         * further characters may be found on the board that can be appended.
         *
         * (Otherwise, if newRoot is null, we do nothing, and simply roll back the letter that had been appended to the StringBuilder,
         * and return immediately.) */
        if (newRoot == null) {
        	word.setLength(letter == 'Q' ? word.length() - 2 : word.length() - 1); // 'Q' is treated as 'Qu' in this game, so roll back 2 chars.
        	return;
        }

        /* If we get this far, newRoot != null.
         * NOTE: newRoot.val will be NON-null if the word is itself a valid word
         *       (as opposed to just a prefix of a possibly valid longer word).
         *       In this case, as long as the word length > 2, we'll add this valid word to the HashSet
         *       before continuing on. */
        if (word.length() > 2 && newRoot.val != null) words.add(word.toString());
        visited[i][j] = true;

        /* If a given letter on the board square at (i, j) happens to be 'Q', then
         * this automatically counts as "QU" instead (because, for virtually all English words,
         * a 'Q' is followed by a 'U'.) So in this case we'll increment the index by 2.
         * Otherwise, just increment by 1. */
        int newD = (letter == 'Q' ? d + 2 : d + 1);

        // Do a recursive DFS for all adjacent, unvisited neighboring squares (including diagonal neighbors).
        boolean firstRow = i == 0;
        boolean firstCol = j == 0;
        boolean lastRow = i == board.rows() - 1;
        boolean lastCol = j == board.cols() - 1;
        if (!firstRow && !firstCol && !visited[i-1][j-1]) dfs(board, i - 1, j - 1, words, visited, word, newRoot, newD);
        if (!firstRow              && !visited[i-1][j])   dfs(board, i - 1, j,     words, visited, word, newRoot, newD);
        if (!firstRow && !lastCol  && !visited[i-1][j+1]) dfs(board, i - 1, j + 1, words, visited, word, newRoot, newD);
        if (             !firstCol && !visited[i][j-1])   dfs(board, i,     j - 1, words, visited, word, newRoot, newD);
        if (             !lastCol  && !visited[i][j+1])   dfs(board, i,     j + 1, words, visited, word, newRoot, newD);
        if (!lastRow  && !firstCol && !visited[i+1][j-1]) dfs(board, i + 1, j - 1, words, visited, word, newRoot, newD);
        if (!lastRow               && !visited[i+1][j])   dfs(board, i + 1, j,     words, visited, word, newRoot, newD);
        if (!lastRow  && !lastCol  && !visited[i+1][j+1]) dfs(board, i + 1, j + 1, words, visited, word, newRoot, newD);

        //After all done, revert the visited[i][j] back to false, and
        //roll back the letter that had been appended to the StringBuilder
        visited[i][j] = false;
        word.setLength(letter == 'Q' ? word.length() - 2 : word.length() - 1);
    }

    /**
     * Method: scoreOf
     *
     * @param word a word String. May or may not be a valid word.
     * @return The score of the given word if it is in the Trie (dict), zero otherwise.
     *         (You can assume the word contains only the uppercase letters A through Z.)
     */
    public int scoreOf(String word) {
    	// TODO consider using constants instead of literal numbers here
        if (dict.contains(word)) {
            if (word.length() < 3) {
                return 0;
            } else if (word.length() < 5) {
                return 1;
            } else if (word.length() < 6) {
                return 2;
            } else if (word.length() < 7) {
                return 3;
            } else if (word.length() < 8) {
                return 5;
            } else {	//word length is 8 or more, then score is 11.
                return 11;
            }
        }
        return 0;
    }

    /* Optional testing. End user must input something like the following:
     * dictionary-common.txt board4x4.txt */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        System.out.println(Arrays.toString(dictionary));
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}