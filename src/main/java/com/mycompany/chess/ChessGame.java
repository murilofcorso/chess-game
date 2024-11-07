package com.mycompany.chess;

import com.mycompany.chess.board.Board;
import com.mycompany.chess.pieces.Piece;
import javax.swing.JFrame;

public class ChessGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        Board board = new Board();
        ChessBoard canvas = new ChessBoard(board.board) {}; 
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        for(int i = 0; i < board.board.length; i++) {
            for(int j = 0; j < board.board.length; j++) { 
                Piece p = (Piece) board.board[i][j].getPiece();
                if(p != null) {
                    System.out.print(p.squareNotation);  
                }
            }
            System.out.println();
        }
    }
}
    