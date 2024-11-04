package com.mycompany.chess.board;

import com.mycompany.chess.utils.Utils;

public class Square {
    final public int col;
    final public int row;
    final public String notation;
    private Object piece;
    

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.notation = Utils.convertToChessNotation(row, col);
        this.piece = null;
    }
    
    public void setPiece(Object piece) {
        this.piece = piece;
    }
    
    public Object getPiece() {
        return piece;
    }
    
    public boolean isEmpty() {
        return piece == null;
    }
}
