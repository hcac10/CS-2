package edu.caltech.cs2.lab04;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullStringTree {
    public StringNode root;

    protected static class StringNode {
        public final String data;
        public StringNode left;
        public StringNode right;

        public StringNode(String data) {
            this(data, null, null);
        }

        public StringNode(String data, StringNode left, StringNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
            // Ensures that the StringNode is either a leaf or has two child nodes.
            if ((this.left == null || this.right == null) && !this.isLeaf()) {
                throw new IllegalArgumentException("StringNodes must represent nodes in a full binary tree");
            }
        }

        // Returns true if the StringNode has no child nodes.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    protected FullStringTree() {
        root = null;
    }

    public FullStringTree(Scanner in) {
        root = deserialize(in);
    }

    private StringNode deserialize(Scanner in) {
        if(!in.hasNextLine())
            return null;
        else if(root == null){
            root = new StringNode(in.nextLine().substring(3));
            root.left = deserialize(in);
            root.right = deserialize(in);
            return root;
        }
        else{
            String temp = in.nextLine();
            StringNode ans;
            if(temp.charAt(0) == 'I'){
                ans = new StringNode(temp.substring(3));
                ans.left = deserialize(in);
                ans.right = deserialize(in);
            }
            else{
                ans = new StringNode(temp.substring(3));
            }
            return ans;
        }
    }

    public List<String> explore() {
        List<String> ans = new ArrayList<String>();
        ans.addAll(help(root));
        return ans;
    }

    public List<String> help(StringNode inp){
        List<String> ans = new ArrayList<>();
        if(inp.isLeaf()) {
            ans.add("L: " + inp.data);
            return ans;
        }
        else{
            ans.add("I: " + inp.data);
            ans.addAll(help(inp.left));
            ans.addAll(help(inp.right));
            return ans;
        }
    }

    public void serialize(PrintStream output) {
        List<String> ans = new ArrayList<>();
        ans = explore();
        for(String c: ans)
            output.println(c);
    }
}