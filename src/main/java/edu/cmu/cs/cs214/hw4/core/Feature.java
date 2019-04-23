package edu.cmu.cs.cs214.hw4.core;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * abstract class for feature, implemented some methods that work the same across diff features
 */
public abstract class Feature {
    private FeatureType type;
    private Set<Tile> tiles;
    private boolean isScored;
    private boolean hasFollower;
    private ArrayList<Player> followers;

    /**
     * constructor
     * @param tile the tile it first belongs on
     */
    Feature(Tile tile){
        tiles = new HashSet<>();
        tiles.add(tile);
        isScored = false;
        hasFollower = false;
        followers = new ArrayList<>();
    }

    /**
     * sets the type of this feature (uses the enum FeatureType)
     * @param newType the enum FeatureType of this feat
     */
    void setType(FeatureType newType){
        type = newType;
    }

    public void addTile(Tile newTile){
        tiles.add(newTile);
    }

    /**
     * getter methods
     * @return the thing it was getting
     */
    public FeatureType getType(){ return type;}

    Set<Tile> getTiles(){ return tiles;}

    public boolean isScored(){ return isScored;}
    void scored(){isScored = true;}
    public boolean isHasFollower(){return hasFollower;}

    /**
     * merges two features after a new tile is put down on the board
     * @param other the feature on the adjacent tile
     * @return a new feature that represents the two features combined
     */
    public Feature merge(Feature other){
        //set other.tiles to the unioned set of this and other
        //make other's fields match to this's fields
        //return a new, combined Feature, and make other and this both point to the new Feature
        assert(!other.isScored);
        assert(other.type==this.type);
        if(tiles!=other.tiles) {
            tiles.addAll(other.tiles);
            if (other.hasFollower && !this.hasFollower) this.hasFollower = true;
            this.followers.addAll(other.followers);
        }
        return this;
    }

    /**
     * returns the player(s) who owns this feature
     * @param type the enum type of the feature
     * @return an ArrayList of players that have the most followers on this feature
     */
    ArrayList<Player> owners(FeatureType type){
        Map<Player,Integer> result = new HashMap<>();
        ArrayList<Player> players = new ArrayList<>();
        for(Player p: followers){
            result.put(p,result.getOrDefault(p,0)+1);
            players.add(p);
        }

        int max=0;
        for(Player p:players){
            if(result.get(p)>max) max = result.get(p);
        }
        ArrayList<Player> owners = new ArrayList<>();
        for(Player p:players){
            if(result.get(p)==max) owners.add(p);
        }
        return owners;
    }

    /**
     * method to place a follower onto the feature
     * @param player player who is putting follower down
     * @return if follower can be placed
     */
    public boolean placeFollower(Player player){
        if(!hasFollower){
            if(player.putFollower()) {
                hasFollower=true;
                followers.add(player);
                return true;
            }
            return false;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(tiles,type);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Feature && other.hashCode() == this.hashCode();
    }
    //abstract methods

    @Override
    public String toString(){return ""+tiles.size();}

    /**
     * scores the board after a new tile is placed down
     * @param board the current game board state
     * @return how many points were scored
     */
    abstract public int score(Tile[][] board);

    /**
     * scores the board after the game has finished
     * @return how many points were scored
     */
    abstract public int scoreIncomplete();


}
