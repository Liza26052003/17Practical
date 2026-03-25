import java.util.Arrays;
public class tryBST{
// The GOOGLE AI Mode and VS Code Agent was consulted on 
// the duration of the creation this practical
// Testing the sync

 tNode root;

    void insert(int key) {
        root = insertRec(root, key);
    }

    tNode insertRec(tNode root, int key) {
        if (root == null) return new tNode(key);
        if (key < root.data) root.left = insertRec(root.left, key);
        else if (key > root.data) root.right = insertRec(root.right, key);
        return root;
    }

    void removeEvens() {
        root = removeEvensRec(root);
    }

    tNode removeEvensRec(tNode node) {
        if (node == null) return null;

        node.left = removeEvensRec(node.left);
        node.right = removeEvensRec(node.right);

        if (node.data % 2 == 0) {
            return deleteNode(node, node.data);
        }
        return node;
    }

    tNode deleteNode(tNode root, int key) {
        if (root == null) return null;
        if (key < root.data) root.left = deleteNode(root.left, key);
        else if (key > root.data) root.right = deleteNode(root.right, key);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            root.data = minValue(root.right);
            root.right = deleteNode(root.right, root.data);
        }
        return root;
    }

    int minValue(tNode root) {
        int minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }

    boolean isBST() {
        return isBSTUtil(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    boolean isBSTUtil(tNode node, int min, int max) {
        if (node == null) return true;
        if (node.data < min || node.data > max) return false;
        return (isBSTUtil(node.left, min, node.data - 1) &&
                isBSTUtil(node.right, node.data + 1, max));
    }

    void buildBalanced(int low, int high) {
        if (low > high) return;
        int mid = low + (high - low) / 2;
        insert(mid);
        buildBalanced(low, mid - 1);
        buildBalanced(mid + 1, high);
    }

    public static void main(String[] args) {
        int n = 20;
        int limit = (int) Math.pow(2, n) - 1;
        int repetitions = 30;

        long[] populateTimes = new long[repetitions];
        long[] removeTimes = new long[repetitions];

        for (int i = 0; i < repetitions; i++) {
            tryBST tree = new tryBST();

            long start = System.currentTimeMillis();
            tree.buildBalanced(1, limit);
            populateTimes[i] = System.currentTimeMillis() - start;

            if (i == 0 && !tree.isBST()) System.out.println("BST Error!");

            start = System.currentTimeMillis();
            tree.removeEvens();
            removeTimes[i] = System.currentTimeMillis() - start;
        }

        printStats("Populate tree", limit, populateTimes);
        printStats("Remove evens", limit, removeTimes);
    }

    static void printStats(String method, int n, long[] times) {
        double avg = Arrays.stream(times).average().orElse(0);
        double sd = 0;
        for (long t : times) sd += Math.pow(t - avg, 2);
        sd = Math.sqrt(sd / times.length);

        System.out.printf("%-25s %-10d %-15.2f %.2f\n", method, n, avg, sd);
    }




}
class tNode {
    int data;
    tNode left, right;

    public tNode(int item) {
        data = item;
        left = right = null;
    }
}

