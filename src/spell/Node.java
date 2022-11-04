package spell;

public class Node implements INode{
    public int count;
    public Node[] children;


    public Node() {
        count = 0;
        children = new Node[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }

    public boolean hasChild(char a) {
        int position = a - 'a';
        if(this.children[position] != null) {
            return true;
        }
        return false;
    }
}
