package com.mycompany.chess;

import com.mycompany.chess.board.Square;
import com.mycompany.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import com.mycompany.chess.utils.Utils;
import java.awt.Dimension;
import javax.swing.JPanel;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener{

    private final Square[][] board;
    private final int tileSize = 100;
    
    private int mouseX, mouseY;
    private boolean dragging = false;
    
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
                        drawPiece(g, p, x, y);
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
        dragging = true;
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
        dragging = false;
        activePiece.beingDragged = false;
        repaint();
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