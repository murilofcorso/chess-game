package com.mycompany.chess.board;

import com.mycompany.chess.pieces.Bishop;
import com.mycompany.chess.pieces.King;
import com.mycompany.chess.pieces.Knight;
import com.mycompany.chess.pieces.Pawn;
import com.mycompany.chess.pieces.Piece;
import com.mycompany.chess.pieces.Queen;
import com.mycompany.chess.pieces.Rook;
import com.mycompany.chess.utils.Utils;
import java.awt.Color;

public final class Board{
    public Square[][] board;
    
    private Color black = Color.BLACK;
    private Color white = Color.WHITE;

    public Board() {
        board = new Square[8][8];
        this.createBoard();
        this.createPieces();
    }
    
    private void createBoard() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }
    
    private void createPawns() {
        for(int i = 0; i < 8; i++) {
            int id = 6;
            String notationBlack = Utils.convertToChessNotation(1, i);
            String notationWhite = Utils.convertToChessNotation(6, i);
            board[1][i].setPiece(new Pawn(notationBlack, black, id));
            board[6][i].setPiece(new Pawn(notationWhite, white, id));
        }
    }
    
    private void createRooks() {
        int id = 3;
        board[0][0].setPiece(new Rook("a8", black, id));
        board[0][7].setPiece(new Rook("h8", black, id));
        board[7][0].setPiece(new Rook("a1", white, id));
        board[7][7].setPiece(new Rook("h1", white, id));
    }
    
    private void createBishops() {
        int id = 4;
        board[0][2].setPiece(new Bishop("c8", black, id));
        board[0][5].setPiece(new Bishop("f8", black, id));
        board[7][2].setPiece(new Bishop("c1", white, id));
        board[7][5].setPiece(new Bishop("f1", white, id));
    }
    
    private void createKnights() {
        int id = 5;
        board[0][1].setPiece(new Knight("b8", black, id));
        board[0][6].setPiece(new Knight("g8", black, id));
        board[7][1].setPiece(new Knight("b1", white, id));
        board[7][6].setPiece(new Knight("g1", white, id));
    }
    
    private void createPieces() {
//        createPawns();
//        createBishops();
//        createKnights();
        createRooks();
        board[0][3].setPiece(new Queen("d8", black, 2));
        board[7][3].setPiece(new Queen("d1", white, 2));
        
        board[0][4].setPiece(new King("e8", black, 1));
        board[7][4].setPiece(new King("e1", white, 1));
    }
}
