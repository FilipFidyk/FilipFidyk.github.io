package structures;

import java.time.LocalDateTime;

//The class that stores all the relevant information to a rating, also a parent and child nodes
public class RatingsNode{
    private int userID;
    private int tmdbID;
    private float rating;
    private LocalDateTime timestamp;

    private RatingsNode parent;
    private RatingsNode right;
    private RatingsNode left;

    //Only use for the root node, since there is no parent initialisation
    public RatingsNode(int inUserID, int imdbID, float inRating, LocalDateTime inTimestamp){
        this.userID = inUserID;
        this.tmdbID = imdbID;
        this.rating = inRating;
        this.timestamp = inTimestamp;

        this.parent = null;
        this.right = null;
        this.left = null;
    }

    //Use for any other node
    public RatingsNode(int inUserID, int imdbID, float inRating, LocalDateTime inTimestamp, RatingsNode parentNode){
        this.userID = inUserID;
        this.tmdbID = imdbID;
        this.rating = inRating;
        this.timestamp = inTimestamp;

        this.parent = parentNode;
        this.right = null;
        this.left = null;
    }

    public int getUserID(){
        return this.userID;
    }

    public int getTmdbID(){
        return this.tmdbID;
    }

    public float getRating(){
        return this.rating;
    }

    public void setRating(float inRating){
        this.rating = inRating;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime inTimestamp){
        this.timestamp = inTimestamp;
    }

    public RatingsNode getParent(){
        return this.parent;
    }

    public void setParent(RatingsNode parentNode){
        this.parent = parentNode;
    }

    public RatingsNode getRight(){
        return this.right;
    }

    public void setRight(RatingsNode rightNode){
        this.right = rightNode;
    }

    public RatingsNode getLeft(){
        return this.left;
    }

    public void setLeft(RatingsNode leftNode){
        this.left = leftNode;
    }
}