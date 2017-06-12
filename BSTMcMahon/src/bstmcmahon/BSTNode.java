package bstmcmahon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Max McMahon
 * Whitley
 * CSCI 333
 * @version 11/6/15
 * Project: Binary Search Tree:
 * Class: BSTNode: operates the nodes used by the BST, includes constructor, and setters/getters
 */

//BST Node
//Parameters key, left child (left) right child (right) parent (p) subtree size (size)
public class BSTNode {
    
    private int key;
    private BSTNode left;
    private BSTNode right;
    private BSTNode p;
    
    BSTNode(int key)
    {
        this.key=key;
        left=null;
        right=null;
        p=null;
       
    }
    
    
    
    //setters
    public void setLeft(BSTNode nL)
    {
        left=nL;
    }
    
        public void setRight(BSTNode nR)
    {
        right=nR;
    }
        
        public void setP(BSTNode nP)
    {
        p=nP;
    }
    
    //getters
    public int getKey()
    {
        return this.key;
    }
    
    public BSTNode getLeft()
    {
        return this.left;
    }
    
    public BSTNode getRight()
    {
        return this.right;
    }
    
    public BSTNode getP()
    {
        return this.p;
    }
    
}
