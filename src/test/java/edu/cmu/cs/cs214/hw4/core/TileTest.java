package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TileTest {

    private ArrayList<Tile> testPile;
    private GameBoard gBoard;
    @Before
    public void init(){
        testPile=TilePile.makeTileArray();
        gBoard = new GameBoard();
        //
    }

    @Test
    public void randoTest1(){
        testPile=TilePile.makeTileArray();
        Tile Q = testPile.get(43);
    }

    @Test
    public void tilePlacementTest(){
        gBoard.placeTile(testPile.get(7),35,35);
        Tile allCity = testPile.get(6);
        assert(!gBoard.placeTile(allCity,34,35));

        assert(gBoard.placeTile(allCity,36,35));
        gBoard.getBoard()[35][36]=null;
        allCity.rotate();
        assert(gBoard.placeTile(allCity,36,35));
        gBoard.getBoard()[35][36]=null;

        Tile cloisterWithRoad = testPile.get(0);
        assert(!gBoard.placeTile(cloisterWithRoad,35,36));
        cloisterWithRoad.rotate();
        cloisterWithRoad.rotate();
        //System.out.println(testPile.get(7));
        assert(gBoard.placeTile(cloisterWithRoad,35,36));
        gBoard.getBoard()[36][35]=null;
    }

    @Test
    public void randomTest(){
        testPile=TilePile.makeTileArray();
        Tile J = testPile.get(24);
        Tile E = testPile.get(11);
        assert(gBoard.placeTile(J,35,35));
        assert(gBoard.placeTile(E,34,35));
    }

    @Test
    public void placeAbove(){
        testPile=TilePile.makeTileArray();
        Tile allCity = testPile.get(6);
        assert(gBoard.placeTile(allCity,35,35));
        Tile anotherD = testPile.get(8);
        anotherD.rotate();
        assert(gBoard.placeTile(anotherD,35,34));

        Tile E = testPile.get(11);
        assert(gBoard.placeTile(E,35,33));
    }

    @Test
    public void testRoad(){
        testPile=TilePile.makeTileArray();
        gBoard.placeTile(testPile.get(7),35,35);
        assert (gBoard.placeTile(testPile.get(71),35,34));
        Tile secondRoad = testPile.get(70);
        secondRoad.rotate();
        assert (gBoard.placeTile(secondRoad,35,36));
    }

    @Test
    public void testCityComplete(){
        testPile=TilePile.makeTileArray();
        assert (gBoard.placeTile(testPile.get(7),35,35));
        assert (gBoard.getBoard()[35][35]!=null);
        Tile cityEdge = testPile.get(11);//tile E
        cityEdge.rotate();
        cityEdge.rotate();
        cityEdge.rotate();
        assert(gBoard.placeTile(cityEdge,36,35));
        cityEdge.score(gBoard.getBoard());
        assert(cityEdge.getFeatures()[6].getFeat().isScored());
    }

    @Test
    public void testComplexCityComplete(){
        testPile=TilePile.makeTileArray();
        assert (gBoard.placeTile(testPile.get(7),35,35));
        Tile allCity = testPile.get(6);
        assert(gBoard.placeTile(allCity,36,35));
        Tile anotherD = testPile.get(8);
        anotherD.rotate();
        assert(gBoard.placeTile(anotherD,36,34));
        Tile botD = testPile.get(11);
        assert(gBoard.placeTile(botD,36,36));
        Tile rightD = testPile.get(12);
        rightD.rotate();
        rightD.rotate();
        rightD.rotate();
        assert(gBoard.placeTile(rightD,37,35));
    }
}
