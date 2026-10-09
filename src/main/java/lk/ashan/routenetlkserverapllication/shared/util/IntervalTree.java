package lk.ashan.routenetlkserverapllication.shared.util;

/**
 * A simple implementation of an Interval Tree for detecting overlaps.
 * Optimized for Time-Intervals (expressed as long values).
 */
public class IntervalTree {
    private Node root;

    public static class Interval {
        public final long low;
        public final long high;

        public Interval(long low, long high) {
            this.low = low;
            this.high = high;
        }
    }

    private static class Node {
        long low, high, max;
        Node left, right;

        Node(long low, long high) {
            this.low = low;
            this.high = high;
            this.max = high;
        }
    }

    public void insert(long low, long high) {
        root = insert(root, low, high);
    }

    private Node insert(Node node, long low, long high) {
        if (node == null) {
            return new Node(low, high);
        }

        if (low < node.low) {
            node.left = insert(node.left, low, high);
        } else {
            node.right = insert(node.right, low, high);
        }

        if (node.max < high) {
            node.max = high;
        }

        return node;
    }

    public boolean overlaps(long low, long high) {
        return findOverlapNode(root, low, high) != null;
    }

    public Interval findOverlapInterval(long low, long high) {
        Node node = findOverlapNode(root, low, high);
        if (node == null) return null;
        return new Interval(node.low, node.high);
    }

    private Node findOverlapNode(Node node, long low, long high) {
        if (node == null) {
            return null;
        }

        // Standard interval overlap condition: low1 < high2 && low2 < high1
        if (low < node.high && node.low < high) {
            return node;
        }

        if (node.left != null && node.left.max > low) {
            return findOverlapNode(node.left, low, high);
        }

        return findOverlapNode(node.right, low, high);
    }
}
