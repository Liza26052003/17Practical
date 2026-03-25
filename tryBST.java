import java.util.*;

/**
 * This program implements a binary search tree (BST) with the following features
 * Consulted Gemini (Google AI) for recursion optimization and VS code Agent
 * iterative balanced tree construction.
 */
public class tryBST {
    tNode root;

    void buildBalanced(int n) {
        int limit = (int) Math.pow(2, n) - 1;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{1, limit});

        while (!queue.isEmpty()) {
            int[] range = queue.poll();
            int low = range[0];
            int high = range[1];

            if (low <= high) {
                int mid = low + (high - low) / 2;
                insert(mid);
                queue.add(new int[]{low, mid - 1});
                queue.add(new int[]{mid + 1, high});
            }
        }
    }

    void insert(int key) {
        root = insertRec(root, key);
    }

    private tNode insertRec(tNode root, int key) {
        if (root == null) return new tNode(key);
        if (key < root.data) root.left = insertRec(root.left, key);
        else if (key > root.data) root.right = insertRec(root.right, key);
        return root;
    }

    void removeEvens() {
        root = removeEvensRec(root);
    }

    private tNode removeEvensRec(tNode node) {
        if (node == null) return null;

        node.left = removeEvensRec(node.left);
        node.right = removeEvensRec(node.right);

        if (node.data % 2 == 0) {
           
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            tNode temp = node.right;
            while (temp.left != null) temp = temp.left;
            node.data = temp.data;
            node.right = deleteNode(node.right, temp.data);
        }
        return node;
    }

    private tNode deleteNode(tNode root, int key) {
        if (root == null) return null;
        if (key < root.data) root.left = deleteNode(root.left, key);
        else if (key > root.data) root.right = deleteNode(root.right, key);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            tNode temp = root.right;
            while (temp.left != null) temp = temp.left;
            root.data = temp.data;
            root.right = deleteNode(root.right, temp.data);
        }
        return root;
    }

    boolean isBST() {
        return isBSTUtil(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBSTUtil(tNode node, int min, int max) {
        if (node == null) return true;
        if (node.data < min || node.data > max) return false;
        return isBSTUtil(node.left, min, node.data - 1) && 
               isBSTUtil(node.right, node.data + 1, max);
    }

    public static void main(String[] args) {
        int n = 20; //
        int repetitions = 30;
        long[] pTimes = new long[repetitions];
        long[] rTimes = new long[repetitions];

        for (int i = 0; i < repetitions; i++) {
            tryBST tree = new tryBST();
            
            long start = System.currentTimeMillis();
            tree.buildBalanced(n);
            pTimes[i] = System.currentTimeMillis() - start;

            if (i == 0 && !tree.isBST()) System.out.println("Error: Not a BST");

            start = System.currentTimeMillis();
            tree.removeEvens();
            rTimes[i] = System.currentTimeMillis() - start;
            
         
        }

        System.out.printf("%-25s %-10s %-15s %-10s\n", "Method", "Keys n", "Avg Time(ms)", "SD");
        printStats("Populated tree", n, pTimes);
        printStats("Removed even", n, rTimes);
    }

    static void printStats(String method, int n, long[] times) {
        double avg = Arrays.stream(times).average().orElse(0);
        double var = 0;
        for (long t : times) var += Math.pow(t - avg, 2);
        double sd = Math.sqrt(var / times.length);
        System.out.printf("%-25s %-10d %-15.2f %.2f\n", method, n, avg, sd);
    }
}

class tNode {
    int data;
    tNode left, right;
    public tNode(int item) { data = item; }
}

