import jdk.nashorn.api.tree.Tree;

import java.util.ArrayList;

public class AVLTree implements AVLTreeADT {
    private int size;
    private AVLTreeNode root;

    public AVLTree(){


        // A constructor which returns a new AVLTree object, it sets size to zero
        //and the root to a new leaf node (use one of the constructors for AVLTreeNode).
        this.size = 0;
        this.root = new AVLTreeNode();
    }

    public void setRoot(AVLTreeNode node){
        //Given node, set it as the root of the AVL Tree.
        this.root =node;
    }

    public AVLTreeNode root(){return root;}

    public boolean isRoot(AVLTreeNode node){
        // Given node, is node the root of the AVL
        // Tree? Return true if node is the root, and return false otherwise.
        return true;
    }

    public int getSize(){ return size;}

    public AVLTreeNode get(AVLTreeNode node, int key){
        //Given the root of a binary search
        //tree node and a key, return the node containing key as its key; otherwise return the leaf node
        //where k should have been in the AVL Tree

        if (node.isLeaf()){
            return node;
        }
        else{
            if (node.getKey() == key){
                return node;
            }
            else if (key < node.getKey()) {
                return get(node.getLeft(),key);
            }
            else{
                return get(node.getRight(),key);
            }
        }
    }

    public AVLTreeNode smallest(AVLTreeNode node){
        // Given the root of a binary search tree
        //node, return the node containing the smallest key; return null if the AVLTree has no data
        //stored in it.
        AVLTreeNode p;
        if (node.isLeaf()){
            return null;
        }
        else{
            p = node;
            while (p.isInternal()){
                p = p.getLeft();
            }
        }

        return p.getParent();
    }

    public AVLTreeNode put(AVLTreeNode node, int key, int data) throws TreeException{
        // Put method for a binary search tree (will be used by another method for properly inserting
        // data into an AVL Tree). Given the root of a binary search tree node and a key-value pair key
        // and data, return the node storing the the new node containing record (key, data); this method
        // must throw the TreeException if a record with a duplicate key is attempted to be inserted
        // into the tree (do not overwrite any data). Again, there is a constructor for AVLTreeNode you
        // might find useful here.

        AVLTreeNode p = get(node, key);


        if (p.isInternal()) throw new TreeException("A node with this key already exists! Choose another key for your data");
        else{
            p.setKey(key);
            p.setData(data);


            AVLTreeNode ltree = new AVLTreeNode(p);
            p.setLeft(ltree);
            AVLTreeNode rtree = new AVLTreeNode(p);
            p.setRight(rtree);
            size++;
            return p;
        }


    }

    public AVLTreeNode remove(AVLTreeNode node, int key) throws TreeException{
        // Remove method for a binary search tree (will be used by another method for properly removing
        //data from an AVL Tree). Given the root of a binary search tree node and a key, remove the
        //record with key from the tree. The method must return the node where the removed node
        //used to be (this is c 'in our notes). If there is no node storing a record with key, throw the
        //TreeException.


        /*
        *
        * p <- get (r,k)
        * if p is a leaf then return false;
        * else
        *       if p has a child c that is a leaf then
        *           p' <- parent of p
        *           c' <- other child of p
        *           if p is the root then make c' the new root
        *           else make c' the child of p'
    *           else
    *               s <- smallest(right child child of p)
    *               copy key & data from s into p
    *               remove(s,key in s)
        *
        *
        * */



        AVLTreeNode ret_node;
        AVLTreeNode parent_node;

        AVLTreeNode j = get(node,key);

        // Case 1: it's a leaf

        if (j.isLeaf()) {
            throw new TreeException("No such key in this tree.");
        } else {

            // Case 2: Has one child -> set parent's child to node's child
            if (j.getRight().isLeaf()) {
                // it's only child is on the left
                parent_node = j.getParent();
                ret_node = j.getLeft();

                if (j.isRoot()){
                    // make ret_node the root
                    ret_node.setParent(null);
                    setRoot(ret_node);
                } else {
                    ret_node.setParent(parent_node);
                    if (parent_node.getRight() == j){parent_node.setRight(ret_node);}
                    else{parent_node.setLeft(ret_node);}
                }
                size --;

            } else if (j.getLeft().isLeaf()) {
                parent_node = j.getParent();
                ret_node = j.getRight();

                if (j.isRoot()){
                    // make ret_node the root
                    ret_node.setParent(null);
                    setRoot(ret_node);
                } else {
                    ret_node.setParent(parent_node);
                    if (parent_node.getRight() == j){parent_node.setRight(ret_node);}
                    else{parent_node.setLeft(ret_node);}
                }
                size --;
                // Case 3: has two children
            } else {

                AVLTreeNode succ = j.getRight();

                while (succ.getLeft().isInternal()) {
                    succ = succ.getLeft();
                }

                // save succ's data (probably not neccesary)
                int temp_key = succ.getKey();
                int temp_data = succ.getData();

                // set j's key,data to that of succ's
                j.setData(temp_data);
                j.setKey(temp_key);
                ret_node = j;

                ret_node = remove(succ,succ.getKey());



            }
        }

        return ret_node;
    }

    public ArrayList<AVLTreeNode> inorder(AVLTreeNode node){
        /*
        * Return an ArrayList (use
        * Java’s ArrayList class) with AVLTreeNode objects from an inorder traversal. Use the next
        * method to perform the inorder traversal; the nodes in the list have keys of value from
        * smallest to largest (not the data in the nodes).
        *
        * */

        ArrayList<AVLTreeNode> arr_list = new ArrayList<AVLTreeNode>();

        inorderRec(node,arr_list);

        return arr_list;
    }

    public void inorderRec(AVLTreeNode node, ArrayList<AVLTreeNode> list){
        // Given a subtree rooted at node and a list, perform an inorder traversal.
        // The list must contain AVLTreeNode objects in the order delivered by an inorder traversal.
        // Remember that the only nodes you need to explore are internal nodes.
        if (node.isInternal()){
            inorderRec(node.getLeft(),list);
            list.add(node);
            inorderRec(node.getRight(),list);
        }

    }

    public void recomputeHeight(AVLTreeNode node){
        // Recomputes the height of node, recall
        // that this was provided in class.

        node.setHeight(1 + Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));

    }


    public void rebalanceAVL(AVLTreeNode r, AVLTreeNode v){

        // r is root of tree
        // v is a node where an imbalance MAY HAVE occurred

        // This method re-balances
        // the tree and updates the heights of nodes as the method moves up to the root of the tree.
        // This algorithm was given in class. To implement this method, I strongly recommend (not
        // required) implementing the three optional methods at the bottom of this section; the one
        // part where you must identify and apply the appropriate rotation is less tedious if so. Be
        // very careful in how you reassign the references for attributes of nodes. Also make
        // sure that the heights of (only) adjusted nodes are recomputed so that the heights
        // are maintained, it must not recompute the heights for the entire tree.
        /*
        *
        * if v isn't a leaf then recomputeHeight(v)
        * while v is not the root r do{
        *       v <- parent of v
        *       if subtree rooted at v is not AVL then { // |Height of left child - height of right child | > 1
        *           Let y be the tallest child of v
        *           let x be the tallest child of y
        *           // if there's ever a tie when picking x go with the same side as y
        *
        *           Rebalance subtree rooted at v with the appropriate rotation (LL, LR, RR, RL) // make sure to update heights
        *
        *           let v be the root of the resulting subtrees
        *       }
        *       recomputeHeight(v)
        *       }// end while
        *
        * */
        AVLTreeNode y;
        AVLTreeNode x;
        if (v.isInternal()){recomputeHeight(v);};
        while(v != r){
            v = v.getParent();
            int diff = balanceFactor(v);
            if (java.lang.Math.abs(diff) > 1){
                y = taller(v, true);
                x = taller(y, true);
                v = rotation(v,y,x);
                if (v.isRoot()){
                    setRoot(v);
                    recomputeHeight(v.getLeft());
                    recomputeHeight(v.getRight());
                    recomputeHeight(v);
                    break;
                }
                recomputeHeight(v.getLeft());
                recomputeHeight(v.getRight());
                recomputeHeight(v);
            }
            recomputeHeight(v);
        }
    }

    public void putAVL(AVLTreeNode node, int key, int data) throws TreeException {
        // Given the root of an AVL Tree node and a key-value pair key and data, insert this record into
        // the AVL Tree (use your put method), re-balance if necessary. It must call the rebalanceAVL
        // method.
        //TODO implement putAVL()
        /*
        * put (r,k,data)
        *
        * let v be the node where (k,data) was inserted
        * -- side note: might have to modify put to return this or use get or something
        * rebalanceAVL(r,v)
        *
        * */

        AVLTreeNode v = put(node,key,data);

        rebalanceAVL(node, v);
    }

    public void removeAVL(AVLTreeNode node, int key) throws TreeException{
        // Given the root of an AVL Tree node and a key, remove the record with key (use your remove
        // method), rebalance whenever necessary. This method must call the rebalanceAVL method.

        // probably want to write seperate succesor for this

        //TODO implement removeAVL()

        AVLTreeNode v = remove(node,key);

        rebalanceAVL(node,v);

    }

    public AVLTreeNode taller(AVLTreeNode node, boolean onLeft){
        /*
        *
        * Checks the children
        * of a node and returns the one that is taller. That is, return the left or right child of node that
        * has the larger height. On a tie, it must select the direction that is the same as its parent; if
        * onLeft is true, then node was the left (taller) child of its parent, and if f alse it was the right
        * (taller) child of its parent.
        *
        * */

        //TODO see wa-gwan with this onLeft business

        AVLTreeNode return_node;
        AVLTreeNode left = node.getLeft();
        AVLTreeNode right = node.getRight();
        if (left.getHeight() == right.getHeight()){
            if (node.getParent().getKey() > node.getKey()){
                return_node = node.getRight();
            } else {
                return_node = node.getLeft();
            }
        } else if (left.getHeight() > right.getHeight()){
            return_node = node.getLeft();
        }
        else{
            return_node = node.getRight();

        }

        return return_node;
    }

    private AVLTreeNode leftRotate(AVLTreeNode node){
        // This is a simple rotation (a left-rotation
        // or right-rotation) as discussed in class. Recall that this operation promotes node to be the
        // new parent of a subtree and demotes its parent. Return node. You might recompute the
        // heights of the (internal) children in the new subtree rooted at node here.

        //TODO add recompute heights where neccesary

        // Let Q be P's right child.
        AVLTreeNode parent_node = node.getParent();

        // Set P's right child to be Q's left child.
        parent_node.setRight(node.getLeft());

        // [Set Q's left-child's parent to P]
        node.getLeft().setParent(parent_node);

        // Set Q's left child to be P.
        node.setLeft(parent_node);

        //set node's new parent
        node.setParent(parent_node.getParent());
        if (node.getParent() != null) {
            if (node.getKey() > node.getParent().getKey()) {
                node.getParent().setRight(node);
            } else{
                node.getParent().setLeft(node);
            }
        }

        // [Set P's parent to Q]
        parent_node.setParent(node);

        //recomputeHeight(node.getRight());
        //recomputeHeight(node);

        return node;
    }

    private AVLTreeNode rightRotate(AVLTreeNode node){
        // This is a simple rotation (a left-rotation
        // or right-rotation) as discussed in class. Recall that this operation promotes node to be the
        // new parent of a subtree and demotes its parent. Return node. You might recompute the
        // heights of the (internal) children in the new subtree rooted at node here.
        //Right rotation of node Q:

        // Let P be Q's left child.
        //---
        // Let Q be P's Parent, p is bottom left
        AVLTreeNode parent_node = node.getParent();

        // Set Q's left child to be P's right child.
        parent_node.setLeft(node.getRight());

        // [Set P's right-child's parent to Q]
        node.getRight().setParent(parent_node);

        // Set P's right child to be Q.
        node.setRight(parent_node);

        //set node's new parent
        node.setParent(parent_node.getParent());

        if (node.getParent() != null) {
            if (node.getKey() > node.getParent().getKey()) {
                node.getParent().setRight(node);
            } else{
                node.getParent().setLeft(node);
            }
        }


        // [Set Q's parent to P]
        parent_node.setParent(node);

        //recomputeHeight(node.getLeft());
        //recomputeHeight(node);

        return node;
    }

    public AVLTreeNode rotation(AVLTreeNode z, AVLTreeNode y, AVLTreeNode x){
        // Given
        // the three nodes,
        //
        // z is the node where an imbalance occurs,
        // y is the taller of the two children of z, and
        // x is the taller of the two children of y,
        //
        // perform the appropriate rotation. There will be two cases,
        // a single rotation (LL or RR), or a double rotation (LR or RL). Return the
        // root of the resulting subtree the rotation is applied to.
        // Tip: Recall that in a single rotation rotate(y) is used,
        // and in a double rotation rotate(x) is applied twice.



        // this DECIDES whether it needs a RL, LR, RR, LL rotation and then calls

        // Case 1 - Left Left -> Right rotation at the ****

        // if balance factor (left height - right height) is greater than 1, then the left node is the taller child
        // if x ,the tallest child of y, has a key that is less than y's key then it's also left
        // there for left-left case, so right rotation of z?


        int diff = balanceFactor(z);
        if ( diff > 1 && x.getKey() < y.getKey()){
            return rightRotate(y);
        }

        // Case 2 - Right Right -> left rotation at the ****
        // if balance factor (left height - right height) is less than -1, then the right node is the taller child
        // if x ,the tallest child of y, has a key that is greater than y's key then it's also right
        else if ( diff < -1 && x.getKey() > y.getKey()){
            return leftRotate(y);
        }
        // Case 3 - Left Right
        // if balance factor (left height - right height) is greater than 1, then the left node is the taller child
        // if x ,the tallest child of y, has a key that is greater than y's key then it's also right
        else if ( diff > 1 && x.getKey() > y.getKey()){

            x = leftRotate(x);
            return rightRotate(x);
        }
        // Case 4 - Right Left
        // if balance factor (left height - right height) is less than -1, then the right node is the taller child
        // if x ,the tallest child of y, has a key that is less than y's key then it's also left

        else if ( diff < -1 && x.getKey() < y.getKey()){
            x = rightRotate(x);
            return leftRotate(x);
        }
        return z;
    }

    private int balanceFactor(AVLTreeNode v){

        return v.getLeft().getHeight() - v.getRight().getHeight();
    }
}
