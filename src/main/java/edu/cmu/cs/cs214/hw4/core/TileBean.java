package edu.cmu.cs.cs214.hw4.core;

public class TileBean {
    private boolean intersection;
    public void setIntersection(boolean input){intersection = input;}
    public boolean getIntersection(){return intersection;}

    private boolean cloister;
    public void setCloister(boolean input){cloister = input;}
    public boolean getCloister(){return cloister;}

    private boolean pennant;
    public void setPennant(boolean input){pennant = input;}
    public boolean getPennant(){return pennant;}

    private boolean[] wallCorners;
    public void setWallCorners(boolean[] input){
        wallCorners = input;
    }
    public boolean[] getWallCorners(){return wallCorners;}

    private boolean[] wallSides;
    public void setWallSides(boolean[] input){
        wallSides = input;
    }
    public boolean[] getWallSides(){return wallSides;}

    private boolean[] fieldSides;
    public void setFieldSides(boolean[] input){
        fieldSides = input;
    }
    public boolean[] getFieldSides(){return fieldSides;}

    private boolean[] roadSides;
    public void setRoadSides(boolean[] input){
        roadSides = input;
    }
    public boolean[] getRoadSides(){return roadSides;}

    private int[][] features;
    public void setFeatures(int[][] input){
        features = input;
    }
    public int[][] getFeatures(){return features;}

    private String id;
    public void setId(String input){id = input;}
    public String getId(){return id;}

}
