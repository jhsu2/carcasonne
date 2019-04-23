package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * class that represents Tiles in the game
 */
public class Tile {
    private int[] pos;
    private Follower follower;
    private final boolean hasCloister;
    private final boolean hasPennant;
    private final boolean hasIntersect;
    private boolean[] wallCorners; //topleft is index 0, index 1 is topright, move ccw
    private boolean[] wallSides;   //top is index 0, index 1 is right, etc.
    private boolean[] fieldSides;
    private boolean[] roadSides;
    private FeatureWrap[] features;    //left side of topside is index 0, right side of top is 1, etc
    private FeatureWrap[] roadFeatures;//top is index 0, right is index 1, etc
    private Feature cloisterFeature;
    private String id;

    private ArrayList<Feature> tileFeatures;

    /**
     * constructor for tiles
     * @param in the tile's respective TileBean object, used to load yaml file into tile data
     */
    public Tile(TileBean in){
        pos =new int[] {0,0}; //[y,x] positions
        follower = null;
        hasCloister = in.getCloister();
        hasPennant = in.getPennant();
        hasIntersect = in.getIntersection();
        wallCorners = in.getWallCorners();
        wallSides = in.getWallSides();
        fieldSides = in.getFieldSides();
        roadSides = in.getRoadSides();
        id = in.getId();
        features = new FeatureWrap[8];
        tileFeatures = new ArrayList<>();
        setFeatures(in.getFeatures());
        roadFeatures = new FeatureWrap[4];
        setRoadFeatures();
        cloisterFeature = new CloisterFeature(this);
        if(id.equals("K")){
            rotate();
        }
    }

    /**
     * sets the position of a tile
     * @param x x pos, 0 on the left
     * @param y y pos, 0 at the top
     */
    public void setPos(int x, int y){
        pos[0] = y;
        pos[1] = x;
    }

    private void setRoadFeatures(){
        if(!hasIntersect){ //if no intersection,its all the same road
            FeatureWrap feat =new FeatureWrap(new RoadFeature(this));
            for(int i=0;i<4;i++){
                if(roadSides[i]) roadFeatures[i]=feat;
            }
            tileFeatures.add(feat.getFeat());
        }
        else{
            for(int i=0;i<4;i++){
                Feature feat = new RoadFeature(this);
                if(roadSides[i]) roadFeatures[i] = new FeatureWrap(feat);
                tileFeatures.add(feat);
            }
        }
    }

    public FeatureWrap[] getRoadFeatures(){ return roadFeatures;}

    private void setFeatures(int[][] input){ //index 0 represents the type of feature
        for(int[] sides:input) {
            Feature newFeat;
            switch (sides[0]) { //create new feature
                case 0:
                    newFeat = new CityFeature(this); //0 is city
                    break;
                case 1:
                    newFeat = new FarmFeature(this); //1 is farm
                    break;
                case 2:
                    newFeat = new RoadFeature(this); //2 is road
                    break;
                default:
                    newFeat = new CloisterFeature(this); //3 is cloister
                    break;
            }
            FeatureWrap wrapper = new FeatureWrap(newFeat);
            for (int i = 1; i < sides.length; i++) {
                features[sides[i]] = wrapper;
            }
            tileFeatures.add(newFeat);
        }
    }

    /**
     * getter methods
     * @return the respective field that each method is getting
     */
    public boolean getHasIntersect(){return hasIntersect;}

    public boolean getHasCloister(){ return hasCloister;}

    public boolean getHasPennant(){ return hasPennant;}

    public FeatureWrap[] getFeatures(){
        return features;
    }

    public ArrayList<Feature> getTileFeatures() {
        return tileFeatures;
    }

    public boolean[] getWallSides(){ return wallSides;}

    public boolean[] getFieldSides(){ return fieldSides;}

    public boolean[] getRoadSides(){return roadSides;}

    public int[] getPos(){
        return pos;
    }
    public Follower follower(){
        return follower;
    }

    public String getId(){return id;}

    //methods for game system

    /**
     * merges the features of a newly placed tile with its neighbors
     * @param other the tile being merged with
     * @param dir the direction of other (ie. top,down,right,left)
     */
    public void mergeTile(Tile other, int dir){//dir=0 is other top, 1 is bottom, 2 right, 3 left
        int[] featIndex = new int[4];
        assert(dir>=0 && dir<=4);
        switch (dir){
            case 0:
                featIndex[0]=0; featIndex[1]=5;featIndex[2]=1;featIndex[3]=4;
                break;
            case 1:
                featIndex[0]=5; featIndex[1]=0;featIndex[2]=4;featIndex[3]=1;
                break;
            case 2:
                featIndex[0]=2; featIndex[1]=7;featIndex[2]=3;featIndex[3]=6;
                break;
            default:
                featIndex[0]=7; featIndex[1]=2;featIndex[2]=6;featIndex[3]=3;
                break;
        }
        //merging features
        FeatureWrap temp = features[featIndex[0]].merge(other.features[featIndex[1]]);
        changeFeatures(featIndex[0], temp);
        other.changeFeatures(featIndex[1], temp);

        temp = features[featIndex[2]].merge(other.features[featIndex[3]]);
        changeFeatures(featIndex[2], temp);
        other.changeFeatures(featIndex[3], temp);

        //merging roads
        if(roadFeatures[(featIndex[0]/2)]!=null) {
            FeatureWrap temp1 = roadFeatures[(featIndex[0] / 2)].merge(other.roadFeatures[(featIndex[1] / 2)]);
            System.out.println(temp1.getFeat());
            changeRoadFeatures(featIndex[0] / 2, temp1);
            other.changeRoadFeatures(featIndex[1] / 2, temp1);
        }
    }

    /**
     * method for adding tiles onto cloister features
     * @param other the tile being added to
     */
    public void mergeCloister(Tile other){
        assert(hasCloister);
        Feature temp = cloisterFeature.merge(other.cloisterFeature);
        changeCloisterFeature(temp);
        other.changeCloisterFeature(temp);
    }

    public void merge2Cloister(Tile other){
        cloisterFeature.addTile(other);
        other.cloisterFeature.addTile(this);
    }

    private void changeFeatures(int index, FeatureWrap newFeat){
        features[index].setFeat(newFeat.getFeat());
        //holder = newFeat;
    }

    private void changeRoadFeatures(int index, FeatureWrap newFeat){
        roadFeatures[index].setFeat(newFeat.getFeat());
    }

    private void changeCloisterFeature(Feature newFeat){
        cloisterFeature = newFeat;
    }

    /**
     * method for rotating a tile
     */
    public void rotate(){ //clockwise rotate
        boolean wallCornerHolder = wallCorners[3];
        boolean wallSideHolder = wallSides[3];
        boolean fieldSideHolder = fieldSides[3];
        boolean roadSideHolder = roadSides[3];
        FeatureWrap featureHolder1 = features[6];
        FeatureWrap featureHolder2 = features[7];
        FeatureWrap roadFeatureHolder = roadFeatures[3];

        for(int i=3;i>=1;i--){
            wallCorners[i] = wallCorners[i-1];
            wallSides[i] = wallSides[i-1];
            fieldSides[i] = fieldSides[i-1];
            roadSides[i] = roadSides[i-1];
            roadFeatures[i] = roadFeatures[i-1];
        }

        wallCorners[0] = wallCornerHolder;
        wallSides[0] = wallSideHolder;
        fieldSides[0] = fieldSideHolder;
        roadSides[0] = roadSideHolder;
        roadFeatures[0] = roadFeatureHolder;

        for(int i=7;i>=2;i-=2){
            features[i] = features[i-2];
            features[i-1] = features[i-3];
        }
        features[0] = featureHolder1;
        features[1] = featureHolder2;
    }

    /**
     * scores tiles by looping through all its features
     * @param board current game board state
     * @return total points gotten
     */
    void score(Tile[][] board){
//        System.out.println(Arrays.toString(getPos()));
        for(FeatureWrap feat:features){
            feat.getFeat().score(board);
        }

        for(FeatureWrap roadFeat:roadFeatures){
            if(roadFeat!=null) {
                roadFeat.getFeat().score(board);
            }
        }

        cloisterFeature.score(board);
    }

    void scoreIncomplete(){
        for(FeatureWrap feat:features){
            feat.getFeat().scoreIncomplete();
        }

        for(FeatureWrap roadFeat:roadFeatures){
            if(roadFeat!=null) {
                roadFeat.getFeat().scoreIncomplete();
            }
        }


        cloisterFeature.scoreIncomplete();
    }

    /**
     * checks if this tile has roads or not
     * @return if the tile has roads
     */
    public boolean noRoads(){
        for(boolean b:roadSides){
            if(b) return true;
        }
        return false;
    }

    /**
     * places a follower on the indexed features
     * @param index which feature to put follower on
     * @param player the player who placed the follower
     * @return if follower was placed successfully
     */
    public boolean placeFollower(int index, Player player){
        if(features[index].getFeat().placeFollower(player)){
            follower = new Follower(player,features[index]);
            return true;
        }
        return false;
    }

    /**
     * places follower on indexed road feature
     * @param index which road to put follower on
     * @param player player who placed the follower
     * @return if follower was placed successfully
     */
    public boolean placeRoadFollower(int index, Player player){
        if(roadFeatures[index].getFeat().placeFollower(player)){
            follower = new Follower(player,roadFeatures[index]);
            return true;
        }
        return false;
    }

    /**
     * place follower on cloister
     * @param player player who places follower
     * @return if follower was placed successfully
     */
    public boolean placeCloisterFollower(Player player){
        if(getHasCloister()){
            if(cloisterFeature.placeFollower(player)){
                follower = new Follower(player,new FeatureWrap(cloisterFeature));
                return true;
            }
        }
        return false;
    }

    public void returnFollower(){
        follower = null;
    }

    @Override
    public int hashCode(){
        return pos[0]*31+pos[1]; //since all tiles have diff positions, hashing pos should be enough
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Tile){
            return ((Tile)other).hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public String toString(){
        String feats="";
        for(FeatureWrap f:features){feats+=f.getFeat().toString()+" ";}
        return feats;
    }
}
