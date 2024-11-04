package com.mycompany.chess;

import com.mycompany.chess.board.Square;
import com.mycompany.chess.pieces.Bishop;
import com.mycompany.chess.pieces.Pawn;
import com.mycompany.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import com.mycompany.chess.utils.Utils;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener{

    private final Square[][] board;
    private final int tileSize = 100;
    
    private int mouseX, mouseY;
    
    private Piece activePiece;
    private int piecePosX;
    private int piecePosY;
    
    public ChessBoard(Square[][] board) {
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        
        setPreferredSize(new Dimension(tileSize*8, tileSize*8)); // Define o tamanho do canvas
        this.board = board;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
         // Tamanho de cada quadrado do tabuleiro
        boolean isWhite = true;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Define a cor do quadrado
                g.setColor(isWhite ? Color.LIGHT_GRAY : Color.GRAY);
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                // Alterna a cor para o próximo quadrado
                isWhite = !isWhite;
            }
            isWhite = !isWhite; // Alterna no final de cada linha
        }
        drawPieces(g);
    }
    
    
    private void drawPiece(Graphics g, Piece piece, int x, int y) {
        g.setColor(piece.color);
        g.fillOval(x + 10, y + 10, tileSize - 20, tileSize - 20);
    }
    
    private void drawPieces(Graphics g) {
        int x;
        int y;
        for(Square[] line : board) {
            for(Square square : line) {
                Piece p = (Piece) square.getPiece();
                if(p != null) {
                    x = p.getRow()*tileSize;
                    y = p.getCol()*tileSize;
                    if(p.beingDragged == true) {
                        drawPiece(g, p, piecePosX, piecePosY);
                    } else {
                        drawPiece(g, p, y, x);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseY = e.getY();
        mouseX = e.getX();
        
        int row = mouseY / 100;
        int col = mouseX / 100;
        
        Piece p = Utils.getPiece(board, row, col);
        if(p != null) {
            activePiece = p;
            p.beingDragged = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseY = e.getY();
        mouseX = e.getX();
        
        if(activePiece != null) {
            piecePosX = mouseX - tileSize / 2;
            piecePosY = mouseY - tileSize / 2;
        } else {
            piecePosX = -1;
            piecePosY = -1;
        }
        repaint();
    }   
    
    @Override
    public void mouseReleased(MouseEvent e) {
        activePiece.beingDragged = false;
        int oldRow = activePiece.getRow();
        int oldCol = activePiece.getCol();
        String notationOld = Utils.convertToChessNotation(oldRow, oldCol);
        
        int newRow = e.getY()/tileSize;
        int newCol = e.getX()/tileSize;
        String notationNew = Utils.convertToChessNotation(newRow, newCol);
        
        
        if(activePiece instanceof Pawn pawn) {
            if(getPawnMoves(pawn).contains(board[newRow][newCol])) {
                movePiece(activePiece, notationOld, notationNew);
            }
        } 
        else if(activePiece instanceof Bishop bishop) {
            if(getBishopMoves(bishop).contains(board[newRow][newCol])) {
                movePiece(activePiece, notationOld, notationNew);
            }
        }
        
        repaint();
    }
    
    public void movePiece(Piece p, String notationOld, String notationNew) {
        int[] posOld = Utils.convertFromChessNotation(notationOld);
        int[] posNew = Utils.convertFromChessNotation(notationNew);
        
        board[posOld[0]][posOld[1]].setPiece(null);
        board[posNew[0]][posNew[1]].setPiece(activePiece);
        activePiece.move(posNew[0], posNew[1]);    
    }
    
    public ArrayList<Square> getPawnMoves(Pawn pawn) {
        ArrayList<Square> moves = new ArrayList<>();
        int direction = pawn.isWhite() ? -1 : 1;
        int row = pawn.getRow();
        int col = pawn.getCol();
        
        if(board[row + direction][col].isEmpty()) {
            moves.add(board[row + direction][col]);
            
            if(pawn.isWhite() && row == 6 || !pawn.isWhite() && row == 1) {
                if(board[row + 2 * direction][col].isEmpty()) {
                    moves.add(board[row + 2 * direction][col]);
                }
            }
        }
        // Captura diagonal à esquerda
        if (col > 0 && isOpponentPiece(row + direction, col - 1, pawn.isWhite())) {
            moves.add(board[row + direction][col - 1]);
        }

        // Captura diagonal à direita
        if (col < 7 && isOpponentPiece(row + direction, col + 1, pawn.isWhite())) {
            moves.add(board[row + direction][col + 1]);
        }
        
        return moves;
    }
    
    public ArrayList<Square> getBishopMoves(Bishop bishop) {
        ArrayList<Square> moves = new ArrayList<>();
        
        addDiagonal(moves, bishop.getRow(), bishop.getCol(), -1, -1);
        addDiagonal(moves, bishop.getRow(), bishop.getCol(), 1, -1);
        addDiagonal(moves, bishop.getRow(), bishop.getCol(), -1, 1);
        addDiagonal(moves, bishop.getRow(), bishop.getCol(), 1, 1); 
        
        return moves;
    }
    
    private boolean isOpponentPiece(int row, int col, boolean isWhite) {
        // Verifique se a posição contém uma peça adversária
        Piece piece = (Piece) board[row][col].getPiece();
        return piece != null && piece.isWhite() != isWhite;
    }

    private void addDiagonal(ArrayList<Square> list, int row, int col, int rowIncrement, int colIncrement) {
        Piece p = (Piece) board[row][col].getPiece();
        
        int newRow = row + rowIncrement;
        int newCol = col + colIncrement;
        
        while(isWhithinBounds(newRow, newCol)) {
            if(board[newRow][newCol].isEmpty()) {
                list.add(board[newRow][newCol]);
            } else {
                if(isOpponentPiece(newRow, newCol, p.isWhite())) {
                    list.add(board[newRow][newCol]);
                }
                break;
            }
            newRow += rowIncrement;
            newCol += colIncrement;
        }
    }
    
    private boolean isWhithinBounds(int newRow, int newCol) {
        return (newRow >= 0 && newRow < 8) && (newCol >= 0 && newCol < 8);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
}