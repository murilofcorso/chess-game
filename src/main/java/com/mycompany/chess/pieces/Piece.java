package com.mycompany.chess.pieces;

import com.mycompany.chess.utils.Utils;
import java.awt.Color;

public class Piece {

    public final String squareNotation;
    public final Color color;
    public final int id;
    private int row;
    private int col;
    
    public Piece(String squareNotation, Color color, int id) {
        this.squareNotation = squareNotation;
        int[] pos = Utils.convertFromChessNotation(squareNotation);
        this.row = pos[0];
        this.col = pos[1];
        this.color = color;
        this.id = id;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public void move(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public boolean isWhite() {
        return color == Color.WHITE;
    }
}
