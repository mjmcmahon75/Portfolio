/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bstmcmahon;

/**
 *
 * @author Max McMahon Whitley CSCI 333
 * @version 11/6/15 Project: Binary Search Tree: Class: Binary Search Tree:
 * implements a binary search tree and the associated methods, including three
 * modes of traversal, and the standard ADT library functions with added
 * wrappers, and a populate function
 * Edited comments, parameters
 */
public class BinarySearchTree {

    private BSTNode root;
    private int size;

    //constructor
    BinarySearchTree() {
        root = null;
        size = 0;
    }

    //pre-order traversal
    //pre-order Traversal setup
    public void preTrav() {
        BSTNode T = this.root;
        preTrav(T);
    }

    //pre-order recursive
    //recursive section/overloaded function for subtrees
    //parameters: T: BSTNode: node in tree currently being examined
    private void preTrav(BSTNode T) {
        if (T != null) {
            BSTNode L = T.getLeft();
            BSTNode R = T.getRight();
            System.out.print(T.getKey() + ", ");
            preTrav(L);
            preTrav(R);
        }
    }

    //in-order traversal
    //in-order Traversal setup
    public void inTrav() {
        BSTNode T = this.root;
        inTrav(T);
    }

    //in-order recursive
    //also overloaded operator for subtree use
    //parameters: T: BSTNode: node in tree currently being examined
    public void inTrav(BSTNode T) {
        if (T != null) {
            BSTNode L = T.getLeft();
            BSTNode R = T.getRight();

            inTrav(L);
            System.out.print(T.getKey() + ", ");
            inTrav(R);
        }
    }

    //post-order traversal
    //post-order Traversal setup
    public void postTrav() {
        BSTNode T = this.root;
        postTrav(T);
    }

    //in-order recursive
    //overloaded for subtree use
    //parameters: T: BSTNode: node in tree currently being examined
    public void postTrav(BSTNode T) {
        if (T != null) {
            BSTNode L = T.getLeft();
            BSTNode R = T.getRight();

            postTrav(L);
            postTrav(R);
            System.out.print(T.getKey() + ", ");
        }
    }

    //ADT operations
    //Search
    //Search Wrapper/overloaded operator
    public BSTNode search(int k) {
        BSTNode searched;
        searched = search(this.root, k);
        if (searched == null) {
            System.out.println("not found.");
        }
        return searched;
    }

    BSTNode search(BSTNode x, int k) {
        if (x == null) {
            return null;
        }
        if (k == x.getKey()) {
            return x;
        }
        if (k < x.getKey()) {
            return search(x.getLeft(), k);
        } else {
            return search(x.getRight(), k);
        }

    }

    //Minimum
    //Overloaded operator, no parameter version used for entire tree, parmater version used recursively, and for subtrees
    public BSTNode minimum() {
        BSTNode min;
        min = minimum(this.root);
        return min;
    }

    //min
    public BSTNode minimum(BSTNode x) {
        while (x.getLeft() != null) {
            x = x.getLeft();
        }
        return x;
    }

    //Maximum
    //Overloaded, no parameter setup and node parameter recursive section
    public BSTNode maximum() {
        BSTNode max;
        max = maximum(this.root);
        return max;
    }

    //max recursive
    public BSTNode maximum(BSTNode x) {
        while (x.getRight() != null) {
            x = x.getRight();
        }
        return x;
    }
    //Succcessor

    //overloaded method allows either integer or node input, int version finds corresponding node, and plugs it into node version
    public BSTNode successor(int x) {
        BSTNode xN = search(x);
        if (xN == null) {
            System.out.println("node does not exisit");
            return null;
        }
        return successor(xN);
    }

    public BSTNode successor(BSTNode x) {
        if (x.getRight() != null) {
            return minimum(x.getRight());
        }
        while ((x.getP() != null) && (x == x.getP().getRight())) {
            x = x.getP();
        }
        return x.getP();
    }

    //Predecessor
    //overloaded
    //this version uses int x, and searches for node xN to plug into node version
    public BSTNode predecessor(int x) {
        BSTNode xN = search(x);
        if (xN == null) {
            System.out.println("node does not exisit");
            return null;
        }
        return predecessor(xN);
    }

    //parameters: x: node which we are trying to find predecessor for
    public BSTNode predecessor(BSTNode x) {
        if (x.getLeft() != null) {
            return maximum(x.getLeft());
        }
        while ((x.getP() != null) && (x == x.getP().getLeft())) {
            x = x.getP();
        }
        return x.getP();
    }

    //Insert
    //(slight variation of pseudocode, with an integer parameter rather than a  removing necessity of declaring a new node in testing)
    //T is not needed, as insert applies to the current tree.
    //parameters: zn(key of new node)
    public void insert(int zn) {
        //z: the new node
        BSTNode z;
        z = new BSTNode(zn);

        //y: previously visited node
        //x: node we are currently visiting
        BSTNode y = null;
        BSTNode x = this.root;

        while (x != null) {
            y = x;
            if (zn < x.getKey()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        z.setP(y);
        if (y == null) {
            this.root = z;
        } else if (z.getKey() < y.getKey()) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }

        this.size++;

    }

    //Delete
    //Tree of origin not needed!
    //z node to be deleted
    public void delete(BSTNode z) {
        //case 1
        if (z.getLeft() == null) {
            transplant(z, z.getRight());
            //case 2
        } else if (z.getRight() == null) {
            transplant(z, z.getLeft());
        } else {
            //case 3b
            BSTNode y = minimum(z.getRight());
            if (y.getP() != z) {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setP(y);
            }
            //case 3
            transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setP(y);
        }
        size--;
    }

    //Populate
    //Extra function, allows insertion of multiple values into a tree simultaneously
    //int[] A: array that we populate new tree with
    public void populate(int[] A) {
        for (int i = 0; i < A.length; i++) {
            int zn = A[i];
            insert(zn);
        }
    }

    //Transplant helper method, replaces node u, with node v
    private void transplant(BSTNode u, BSTNode v) {
        //u's predecessor
        BSTNode uP = u.getP();
        
        //left cild of uP
        BSTNode uPL = uP.getLeft();

        if (uP == null) {
            this.root = v;
        } else if (u == uPL) {
            uP.setLeft(v);
        } else {
            uP.setRight(v);
        }
        if (v != null) {
            v.setP(uP);
        }
    }

    //size getter
    public int getSize() {
        return this.size;
    }

}
