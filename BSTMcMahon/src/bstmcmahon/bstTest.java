/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bstmcmahon;

/**
 *
 * @author Max McMahon
 * Whitley
 * CSCI 333
 * @version 10/23/15
 * Project: Binary Search Tree:
 * Class: bstTest: includes bstSort, and testers for traversal and ADT functions implemented
 * in the binary search tree class
 */
public class bstTest {
    //bst sorting algorithm, uses populate method I made in BinarySearchTree
    public static void bstSort(BinarySearchTree T, int[] a)
    {
        T.populate(a);
        T.inTrav();
    }
    public static void main(String [ ] args)
    {
        //creation
        BinarySearchTree t1;
        t1=new BinarySearchTree();
        int[] a1=
        {
            99, 9, 8, 7, 5, 4, 3, 2, 91, 85, 34, 43, 21, 67, 80, 90, 100, 122, 15, 16, 17, 18, 19, 20, 23, 22
        };
        t1.populate(a1);
        System.out.println("size");
        System.out.println(t1.getSize());
        System.out.println();
        
        //traversal test
        System.out.println("pre order Traversal:");
        t1.preTrav();
        System.out.println();
        System.out.println("in order Traversal:");
        t1.inTrav();
        System.out.println();
        System.out.println("post order Traversal:");
        t1.postTrav();
        System.out.println();
        System.out.println();
        
        //min/successor & max/predecessor
        BSTNode x1=t1.minimum();
        BSTNode suc=x1;
        BSTNode pre;
        
        System.out.println("min and successor:");
        for(int i=0; i<(t1.getSize()); i++)
        {
            System.out.print(suc.getKey()+" ");
            suc=t1.successor(suc);   
        }
        System.out.println();
        System.out.println();
        System.out.println("max and predecessor:");
        BSTNode x2=t1.maximum();
        pre=x2;
        for(int i=0; i<(t1.getSize()); i++)
        {
            System.out.print(pre.getKey()+" ");
            pre=t1.predecessor(pre);
        }
        System.out.println();
        System.out.println();
        
        BSTNode s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        
        System.out.println("existing key search");
        s1=t1.search(5);
        System.out.println("search: 5");
        System.out.println("found: "+s1.getKey());
        s2=t1.search(21);
        System.out.println("search: 21");
        System.out.println("found: "+s2.getKey());
        s3=t1.search(80);
        System.out.println("search: 80");
        System.out.println("found: "+s3.getKey());
        s4=t1.search(90);
        System.out.println("search: 90");
        System.out.println("found: "+s4.getKey());
        s5=t1.search(7);
        System.out.println("search: 7");
        System.out.println("found: "+s5.getKey());
        
        System.out.println();
        System.out.println("non-existant keys");
        
        System.out.println("search: 777");
        s6=t1.search(777);
        
        System.out.println("search: 999");
        s7=t1.search(999);
         
        System.out.println("search: 1000");
        s8=t1.search(1000);
       
        System.out.println("search: 11111");
        s9=t1.search(11111);
        
        System.out.println("search: 22222");
        s10=t1.search(22222);
        System.out.println();
        System.out.println();
        
        //deletion
        System.out.println("deletion");
        System.out.println("original:");
        System.out.println("size: "+t1.getSize());
        System.out.println("BST:");
        t1.inTrav();
        System.out.println();
        System.out.println();
        
        //copying tree to preserve original, and finding nodes to delete
        BinarySearchTree t2=t1;
        BSTNode d1, d2, d3;
        d1=t2.search(21);
        d2=t2.search(90);
        d3=t2.search(23);
        t2.delete(d1);
        t2.delete(d2);
        t2.delete(d3);
        System.out.println("new:");
        System.out.println("size: "+t2.getSize() );
        System.out.println("BST:");
        t2.inTrav();
        System.out.println();
        System.out.println();
        
        BinarySearchTree t3;
        t3=new BinarySearchTree();
        BinarySearchTree t4;
        t4=new BinarySearchTree();
        BinarySearchTree t5;
        t5=new BinarySearchTree();
        BinarySearchTree t6;
        t6=new BinarySearchTree();
        BinarySearchTree t7;
        t7=new BinarySearchTree();
        
        
        
        System.out.println("BSTSort");
        bstSort(t3, a1);
        System.out.println();
        int[] a2=
        {
            21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1
        };
        bstSort(t4, a2);
        System.out.println();
        
        int[] a3=
        {
            6,9,1,4,5,7,89,56,43,89,100,11,13,198,32,111,16
        };
        bstSort(t5, a3);
        System.out.println();
        
        int[] a4=
        {
            5,8,2,1,4
        };
        bstSort(t6, a4);
        System.out.println();
        
        int [] a5=
        {
            7,9,0,5,4,3,2,1
        };
        bstSort(t7, a5);
        System.out.println();
        
        
        
    }
}


