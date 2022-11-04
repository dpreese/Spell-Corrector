package spell;

import java.util.Locale;

public class Trie implements ITrie {
    private Node root;
    private int wordCount;
    private int nodeCount;

    public Trie() {
        root = new Node();
        wordCount = 0;
        nodeCount = 1;
    }

    @Override
    public void add(String word) {
        Node n = this.root;
        char currentLetter;
        int position;
        //word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            currentLetter = word.charAt(i);
            position = currentLetter - 'a';
            if(!(n.hasChild(currentLetter))){ //possibly might have to use position instead of currentletter
                n.children[position] = new Node();
                this.nodeCount++;
                //n = (Node)n.getChildren()[position];
            }
            n = n.children[position];
        }
        if(n.getValue() == 0) {
            this.wordCount++;
        }
        n.incrementValue();
    }


    @Override
    public INode find(String word) {
        Node n = this.root;
        char currentLetter;
        int position;
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            currentLetter = word.charAt(i);
            position = currentLetter - 'a';
            if (n.hasChild(currentLetter)) {
                n = n.children[position];
            }
            else {
                return null;
            }
        }
        if (n == null || n.getValue() == 0) return null;

        return n;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toString_Helper(root, curWord, output);

        return output.toString();
    }

    private void toString_Helper(Node n, StringBuilder curWord, StringBuilder output) {

        if(n.count > 0) {
            output.append(curWord.toString() + "\n");
        }
        for (int i = 0; i < root.getChildren().length; ++i) {
            Node child = n.children[i];
            if(child!= null) {
                char childChar = (char)('a' + i);
                curWord.append(childChar);
                toString_Helper(child, curWord, output);
                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }
    @Override
    public int hashCode() {
        int hashnum = wordCount * nodeCount;
        for(int i = 0; i < root.getChildren().length; i++) {
            if(root.getChildren()[i] != null) {
                hashnum += i + 13; //uses a random prime number of my choosing (TA recommended) to help generate the
            }                       //hashnum
        }
        return hashnum;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }

        Trie t = (Trie)obj;
        if(this.nodeCount != t.nodeCount) {
            return false;
        }
        if(this.wordCount != t.wordCount) {
            return false;
        }

        return equals_Helper(root, t.root);
    }


    private boolean equals_Helper(Node n1, Node n2) {

        if(n1.count != n2.count) {
            return false;
        }
        for(int i = 0; i < n1.getChildren().length; i++) {
            if((n1.getChildren()[i] != null) && (n2.getChildren()[i] != null)) {
                if(!(equals_Helper((Node)n1.getChildren()[i], (Node)n2.getChildren()[i]))){
                    return false;
                }
            }
            else if((n1.getChildren()[i] == null) && (n2.getChildren()[i] == null)) {
                continue;
            }
            else {
                continue;
            }
        }

        return true;
    }
}
