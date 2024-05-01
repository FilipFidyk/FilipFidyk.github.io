package structures;

import java.time.LocalDateTime;

//A binary search tree which will store all ratings in relation to one movie
public class BinarySearchTree{
    private RatingsNode root;
    private int size;

    public BinarySearchTree(){
        root = null;
        size = 0;
    }

    public BinarySearchTree(RatingsNode inRoot){
        root = inRoot;
        size = 1;
    }

    //Private adding function that maintains a total order between userIDs
    //Taken from lab5 and altered
    private boolean addToSubTree(RatingsNode n, int newUserID, int newFilmID, float rating, LocalDateTime timestamp){
        if (n!=null) 
        {
            int nValue = n.getUserID();
            if (newUserID == nValue){
                return false;
            }
            else if (newUserID < nValue) {
                if (n.getLeft()==null){
                    n.setLeft(new RatingsNode(newUserID, newFilmID, rating, timestamp, n));
                    size++;
                    return true;
                }
                else
                    addToSubTree(n.getLeft(), newUserID, newFilmID, rating, timestamp);
            }
            else {
                if (n.getRight()==null){
                    n.setRight(new RatingsNode(newUserID, newFilmID, rating, timestamp, n));
                    size++;
                    return true;
                }
                else
                    addToSubTree(n.getRight(), newUserID, newFilmID, rating, timestamp);
            }
        }
        return false;
    }

    //public add function either set the root or uses the private function to add to the correct subtree
    public boolean add(int newUserID, int newFilmID, float rating, LocalDateTime timestamp){
        if (root==null) {
            root = new RatingsNode(newUserID, newFilmID, rating, timestamp);
            return true;
        }
        else
            return addToSubTree(root, newUserID, newFilmID, rating, timestamp);
    }

    public RatingsNode getRoot(){
        return root;
    }

    //A simple public facing find method that calls the private one which requires a node to start at
    public RatingsNode find(int user){
        return find(root, user);
    }
    
    //Recursive find method, uses pre-Order traversal 
    private RatingsNode find(RatingsNode currentNode, int user){
        
        if (currentNode != null){
            if (currentNode.getUserID() == user) {
                return currentNode;
            }
            else if (currentNode.getUserID() < user) {
                find(currentNode.getLeft(), user);
            }
            else {
                find(currentNode.getRight(), user);
            }
        }

        return null;
    }

    //Same public-private dynamic as with add and find
    public boolean remove(int user){
        //Empty tree
        if (root == null){
            return false;
        }
            return removeFromSubTree(root, user);
        
    }   
    
    //Recursively find the node, removes it and maintains order
    //Special cases if we need to remove the root
    private boolean removeFromSubTree(RatingsNode currentNode, int user){   
        
        //find the node corresponding to the user 
        if (user < currentNode.getUserID()){
            removeFromSubTree(currentNode.getLeft(), user);
        }
        else if(user > currentNode.getUserID()){
            removeFromSubTree(currentNode.getRight(), user);
        }

        //if the node doesn't exist
        if (currentNode == null){
            return false;
        }

        //Utility nodes to help us move around the tree 
        RatingsNode replacingSuccessor = null;
        RatingsNode parent = null;

        //The node for removal has no children so we set the parent's correct child variable to null
        if (currentNode.getLeft() == null && currentNode.getRight() == null){
            if(currentNode != root){
                currentNode = currentNode.getParent();
                if (user < currentNode.getUserID()){
                    currentNode.setLeft(null);
                }
                else{
                    currentNode.setRight(null);
                }
            }
            //if the root has no children, just set to null
            else{
                root = null;
            }
        }
        //The node for removal only has a left child, which we slot into the node position
        else if (currentNode.getLeft() != null && currentNode.getRight() == null){
            //Get the node and go to parent
            replacingSuccessor = currentNode.getLeft();
            
            if (currentNode != root){
                currentNode = currentNode.getParent();

                //As we are in the parent, we have to find our node again, and change it to the successor
                //Copy the changed parent and go to the replaced node
                if (user < currentNode.getUserID()){
                    currentNode.setLeft(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getLeft();
                }
                else{
                    currentNode.setRight(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getRight();
                }
                //and change the parent of this node, to keep integrity
                currentNode.setParent(parent);
            }
            //if it is the root simply set the child's parent as null and set it as root
            else{
                replacingSuccessor.setParent(null);
                root = replacingSuccessor;
            }
        }
        //The node for removal only has a right child, which we slot into the node position
        else if (currentNode.getLeft() == null && currentNode.getRight() != null){
            //Get the node and go to parent
            replacingSuccessor = currentNode.getRight();
            
            if (currentNode != root){
                currentNode = currentNode.getParent();

                //As we are in the parent, we have to find our node again, and change it to the successor
                //Copy the changed parent and go to the replaced node
                if (user < currentNode.getUserID()){
                    currentNode.setLeft(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getLeft();
                }
                else{
                    currentNode.setRight(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getRight();
                }
                //and change the parent of this node, to keep integrity
                currentNode.setParent(parent);
            }
            //if it is the root simply set the child's parent as null and set it as root
            else{
                replacingSuccessor.setParent(null);
                root = replacingSuccessor;
            }
        }
        //Both children present, take the smallest node in the right subtree and use that
        else{
            //Find the smallest child in the right subtree, either a leaf (left or right) or an internal node (only right) with a right child
            //And then delete it which will be handled by the above c
            replacingSuccessor = findMin(currentNode.getRight());
            removeFromSubTree(currentNode.getRight(), replacingSuccessor.getUserID());
            
            //Make copies of currentNodes' children
            RatingsNode leftChildAfterDeletion = currentNode.getLeft();
            RatingsNode rightChildAfterDeletion = currentNode.getRight();
            
            if (currentNode != root){
                //Go to the parent node and change the node we want to delete to the smallest child
                //Make a copy of the new parent and go to the changed child
                currentNode = currentNode.getParent();
                if(user < currentNode.getUserID()){
                    currentNode.setLeft(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getLeft();
                }
                else{
                    currentNode.setRight(replacingSuccessor);
                    parent = currentNode;
                    currentNode = currentNode.getRight();
                }
                
                //Set the new nodes links correctly
                currentNode.setParent(parent);
                currentNode.setLeft(leftChildAfterDeletion);
                currentNode.setRight(rightChildAfterDeletion);
            }
            //if deleting the root simply set root to the new node and link to the altered children  
            else{
                replacingSuccessor.setParent(null);
                root = replacingSuccessor;
                currentNode.setLeft(leftChildAfterDeletion);
                currentNode.setRight(rightChildAfterDeletion);

            }
        }
        //Decrement tree size and return a successful deletion
        size--;
        return true;
    }

    //Find the smallest node recursively by just following the left side of the tree
    private RatingsNode findMin(RatingsNode subTreeRoot){
        if (subTreeRoot.getLeft() == null){
            return subTreeRoot;
        }
        
        return findMin(subTreeRoot.getLeft());
    }

    //Function should only be used when the rating and subsequentially the timestamp need to be changed
    //In theory it is the same user and movie so they don't have to be changed
    public boolean set(int newUserID, float rating, LocalDateTime timestamp){
        RatingsNode currentNode = find(root, newUserID);

        if (currentNode == null){
            return false;
        }

        currentNode.setRating(rating);
        currentNode.setTimestamp(timestamp);
        return true;
    }

    public int size(){
        return size;
    }

    //Public-private dynamic for converting a tree to an array
    //Public method does something interesting, it converts an arraylist into an array so Ratings can receive a usable solution 
    public RatingsNode[] toArray(){
        if (root == null){
            return null;
        }
        
        CustomArrayList<RatingsNode> allNodes = new CustomArrayList();
        toArray(allNodes, root);

        RatingsNode[] output = new RatingsNode[allNodes.size()];
        for (int i = 0; i < allNodes.size(); i++){
            output[i] = allNodes.get(i);
        }
        return output;
    }

    //Private toArray uses an arrayList for simplicity so that a count variable doesn't have to be tracked through recursive function calls
    private void toArray(CustomArrayList<RatingsNode> allNodes, RatingsNode currentNode){
        if (currentNode != null){ 
            toArray(allNodes, currentNode.getLeft());    
            allNodes.add(currentNode);
            toArray(allNodes, currentNode.getRight());
        } 
    }
}