package edu.cmu.cs.cs214.hw4.core;

/**
 * class that represents the game board
 */
public class GameBoard {
    private Tile[][] board;
    private int xBound,yBound;
    private boolean firstTile;

    GameBoard(){
        board = new Tile[72][72]; //35,35 is starting tile
        xBound =71; //bounds are inclusive
        yBound=71;
        firstTile=true;
    }

    private boolean checkPlace(Tile tile,int x,int y){
        //check all adjacent tiles, if none, its ok
        if(y-1>=0 && !tileCorrect(board[y][x-1],tile)) return false;
        if(y+1<=yBound && !tileCorrect(board[y][x+1],tile)) return false;
        if(x-1>=0 && !tileCorrect(board[y-1][x],tile)) return false;
        if(x+1<=xBound && !tileCorrect(board[y+1][x],tile)) return false;
        if(firstTile) {
            firstTile=false;
            return true;
        }
        return(board[y][x-1]!=null||board[y][x+1]!=null||board[y-1][x]!=null||board[y+1][x]!=null);
    }

    /**
     * method that checks if the current tile has any possible spot
     * on the board
     * @param tile current tile
     * @return whether this tile can be placed somewhere or not
     */
    public boolean isPossiblePlacement(Tile tile){//should pass in deepcopy of tile
        for(int j=0;j<72;j++){
            for(int i=0;i<72;i++){
                if(board[j][i]==null) continue;

                if(board[j-1][i]==null){
                    for(int z=0;z<4;z++) {
                        if (checkPlace(tile, i, j)) return true;
                        tile.rotate();
                    }
                }
                if(board[j+1][i]==null){
                    for(int z=0;z<4;z++) {
                        if (checkPlace(tile, i, j)) return true;
                        tile.rotate();
                    }
                }
                if(board[j][i-1]==null){
                    for(int z=0;z<4;z++) {
                        if (checkPlace(tile, i, j)) return true;
                        tile.rotate();
                    }
                }
                if(board[j][i+1]==null){
                    for(int z=0;z<4;z++) {
                        if (checkPlace(tile, i, j)) return true;
                        tile.rotate();
                    }
                }
            }
        }
        return false;
    }

    private boolean tileCorrect(Tile one, Tile two){
        if(one==null) return true;
        int[] pos1 = one.getPos();
        int[] pos2 = two.getPos();
        boolean[] walls1 = one.getWallSides();
        boolean[] walls2 = two.getWallSides();
        boolean[] field1 = one.getFieldSides();
        boolean[] field2 = two.getFieldSides();
        boolean[] road1 = one.getRoadSides();
        boolean[] road2 = two.getRoadSides();
        boolean adjSides=false;

        if(pos1[0]==pos2[0]-1){ //if two is below one
            if(pos1[1]==pos2[1]){//if two is directly below one
                if(!(walls2[0]==walls1[2] && field2[0]==field1[2]
                        && road2[0]==road1[2])) return false;
                else adjSides=true;
            }
        }
        if(pos1[0]==pos2[0]) {// same row
            if(pos2[1]==pos1[1]+1) {//2 is right of 1
                if(!(walls2[3]==walls1[1]&&field2[3]==field1[1]
                        && road2[3]==road1[1])) return false;
                else adjSides=true;
            }
            if(pos2[1]==pos1[1]-1) {//2 is left of 1
                if(!(walls2[1]==walls1[3]&&field2[1]==field1[3]
                        && road2[1]==road1[3])) return false;
                else adjSides=true;
            }
        }
        if(pos1[0]==pos2[0]+1) {//if 2 is above 1
            if(pos1[1]==pos2[1]){
                if(!(walls2[2]==walls1[0] && field2[2]==field1[0]
                    && road2[2]==road1[0])) return false;
                else adjSides=true;
            }
        }
        return adjSides;
    }

    /**
     * the main method used to place a new tile down on the board,
     * will update scores and merge features on different tiles.
     * @param newTile the tile being placed down
     * @param x x coordinate
     * @param y y coordinate
     * @return whether the tile has been placed successfully
     */
    public boolean placeTile(Tile newTile, int x, int y){
        assert(x<=xBound && x>=0);
        assert(y<=yBound&& y>=0);
        newTile.setPos(x,y);
        if(checkPlace(newTile,x,y)) {
            board[y][x] = newTile;
            mergeFeatures(newTile);
            return true;
        }
        else{
            //smth cant merge!

            return false;
        }
    }

    private void mergeFeatures(Tile tile){
        int[] pos = tile.getPos(); //[0] is y, [1] is x
        //merge w top tile
        if(pos[0]-1>=0){
            Tile topTile = board[pos[0]-1][pos[1]];
            if(topTile!=null) {
                tile.mergeTile(topTile,0);
            }
        }
        //merge w bottom tile
        if(pos[0]+1<=yBound){
            Tile botTile = board[pos[0]+1][pos[1]];
            if(botTile!=null){
                tile.mergeTile(botTile,1);
            }
        }
        //merge w right tile
        if(pos[1]+1<=xBound){
            Tile rightTile = board[pos[0]][pos[1]+1];
            if(rightTile!=null){
                tile.mergeTile(rightTile,2);
            }
        }

        //merge w left tile
        if(pos[1]-1>=0){
            Tile leftTile = board[pos[0]][pos[1]-1];
            if(leftTile!=null){
                tile.mergeTile(leftTile,3);
            }
        }

        //merge cloister, if possible
        for(int i=pos[0]-1;i<=pos[0]+1;i++){
            if(i<0||i>yBound) continue;
            for(int j=pos[1]-1;j<=pos[1]+1;j++){
                if(j<0||j>xBound) continue;
                if(board[i][j]!=null) {
                    if (tile.getHasCloister() && !board[i][j].getHasCloister()) {
                        tile.mergeCloister(board[i][j]);
                    }
                    else if (!tile.getHasCloister()&& board[i][j].getHasCloister()) {
                            board[i][j].mergeCloister(tile);
                        }
                    else{
                        tile.merge2Cloister(board[i][j]);
                    }
                }
            }
        }
    }

    void scoreIncomplete(){
        for(int j=0;j<72;j++){
            for(int i=0;i<72;i++){
                if (board[j][i]==null) continue;
                else board[j][i].scoreIncomplete();
            }
        }
    }

    public Tile[][] getBoard(){
        return board;
    }
}
