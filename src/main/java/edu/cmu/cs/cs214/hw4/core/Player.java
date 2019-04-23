package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

public class Player {
    private int score;
    private int followerCount;
    private String name;

    public Player(String newName){
        score=0;
        followerCount=7;
        name=newName;

    }

    public void addScore(int score){
        this.score += score;
    }

    public int getScore(){ return score;}

    public int followersLeft(){return followerCount;}

    public void returnFollower(int i){followerCount+=i;}

    public boolean putFollower(){
        if(followerCount==0) return false;
        else {
            followerCount--;
            return true;
        }
    }

    @Override
    public String toString(){
        return name;
    }
}
