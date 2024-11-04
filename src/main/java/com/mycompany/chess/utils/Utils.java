package com.mycompany.chess.utils;

import com.mycompany.chess.board.Board;
import com.mycompany.chess.board.Square;
import com.mycompany.chess.pieces.Piece;

public class Utils {
    public static String convertToChessNotation(int row, int col) {
        // Converte a coluna para a letra correspondente
        char coluna = (char) ('a' + col);
        
        // Converte a linha para o número correspondente
        int linha = 8 - row;
        
        // Retorna a notação de xadrez como uma String
        return "" + coluna + linha;
    }
    
    public static int[] convertFromChessNotation(String notation) {
        int[] pos = new int[2];

        // Coluna: converte a letra (a-h) para um número (0-7)
        pos[1] = notation.charAt(0) - 'a';

        // Linha: converte o número (1-8) para um índice (7-0)
        pos[0] = 8 - Character.getNumericValue(notation.charAt(1));

        return pos;
    }
    
    public static Square getSquare(Board board, String notation) {
        int[] pos = convertFromChessNotation(notation);
        Square s = board.board[pos[0]][pos[1]];      
        return s;
    }
    
    public static Square getSquare(Board board, int row, int col) {
        Square s = board.board[row][col];      
        return s;
    }
    
    public static Piece getPiece(Square[][] board, int row, int col) {
        Square s = board[row][col];
        Piece p = (Piece) s.getPiece();
        return p;
    }
}
