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
        System.out.println(getPossibleMoves((Pawn) activePiece));
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
        
        int row = e.getY()/tileSize;
        int col = e.getX()/tileSize;
        
        int oldRow = activePiece.getRow();
        int oldCol = activePiece.getCol();
        
        if(getPossibleMoves((Pawn) activePiece).contains(board[row][col])) {
            board[row][col].setPiece(activePiece);
            activePiece.move(row, col);
            board[oldRow][oldCol].setPiece(null);
        }
        
        if(getPossibleMoves((Bishop) activePiece).contains(board[row][col])) {
            board[row][col].setPiece(activePiece);
            activePiece.move(row, col);
            board[oldRow][oldCol].setPiece(null);
        }

        repaint();
    }
    
    public ArrayList<Square> getPossibleMoves(Pawn pawn) {
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
    
    public ArrayList<Square> getPossibleMoves(Bishop bishop) {
        ArrayList<Square> moves = new ArrayList<>();
        
        
        
        return moves;
    }
    
    private boolean isOpponentPiece(int row, int col, boolean isWhite) {
        // Verifique se a posição contém uma peça adversária
        Piece piece = (Piece) board[row][col].getPiece();
        return piece != null && piece.isWhite() != isWhite;
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