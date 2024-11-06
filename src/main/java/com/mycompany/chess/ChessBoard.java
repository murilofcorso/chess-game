package com.mycompany.chess;

import com.mycompany.chess.board.Square;
import com.mycompany.chess.pieces.Bishop;
import com.mycompany.chess.pieces.King;
import com.mycompany.chess.pieces.Knight;
import com.mycompany.chess.pieces.Pawn;
import com.mycompany.chess.pieces.Piece;
import com.mycompany.chess.pieces.Queen;
import com.mycompany.chess.pieces.Rook;
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
    private Color playingColor = Color.WHITE;
    
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
                    if(p == activePiece) {
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
        if(p != null && p.color == playingColor) {
            activePiece = p;
        }
        System.out.println(isSquareUnderAttack(row, col));
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
        if(activePiece != null) {
            checkMoves(e, activePiece);
        }
        
        
        activePiece = null;
    }
    
    private void checkMoves(MouseEvent e, Piece p) {
        int oldRow = p.getRow();
        int oldCol = p.getCol();
        String notationOld = Utils.convertToChessNotation(oldRow, oldCol);
        
        int newRow = e.getY()/tileSize;
        int newCol = e.getX()/tileSize;
        String notationNew = Utils.convertToChessNotation(newRow, newCol);
        
        
        switch (p) {
            case Pawn pawn -> {
                if(getPawnMoves(pawn).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            case Bishop bishop -> {
                if(getBishopMoves(bishop).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            case Knight knight -> {
                if(getKnightMoves(knight).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            case Rook rook -> {
                if(getRookMoves(rook).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            case Queen queen -> {
                if(getQueenMoves(queen).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            case King king -> {
                if(getKingMoves(king).contains(board[newRow][newCol])) {
                    movePiece(p, notationOld, notationNew);
                }
            }
            default -> {   
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

        playingColor = activePiece.isWhite() ? Color.BLACK: Color.WHITE;     
        p.addMove(); 
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
        
        addLinearMoves(moves, bishop.getRow(), bishop.getCol(), -1, -1);
        addLinearMoves(moves, bishop.getRow(), bishop.getCol(), 1, -1);
        addLinearMoves(moves, bishop.getRow(), bishop.getCol(), -1, 1);
        addLinearMoves(moves, bishop.getRow(), bishop.getCol(), 1, 1); 
        
        return moves;
    }
    
    public ArrayList<Square> getRookMoves(Rook rook) {
        ArrayList<Square> moves = new ArrayList<>();
        
        addLinearMoves(moves, rook.getRow(), rook.getCol(), -1, 0);
        addLinearMoves(moves, rook.getRow(), rook.getCol(), 0, -1);
        addLinearMoves(moves, rook.getRow(), rook.getCol(), 0, 1);
        addLinearMoves(moves, rook.getRow(), rook.getCol(), 1, 0); 
        
        return moves;
    }
    
    public ArrayList<Square> getQueenMoves(Queen queen) {
        ArrayList<Square> moves = new ArrayList<>();
        
        addLinearMoves(moves, queen.getRow(), queen.getCol(), -1, 0);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), 0, -1);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), 0, 1);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), 1, 0);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), -1, -1);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), 1, -1);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), -1, 1);
        addLinearMoves(moves, queen.getRow(), queen.getCol(), 1, 1); 
        
        return moves;
    }
    
    private ArrayList<Square> getKingMoves(King king) {
        ArrayList<Square> moves = new ArrayList<>();
        int row = king.getRow();
        int col = king.getCol();
        Piece p = Utils.getPiece(board, row, col);
        
        int[][] kingMoves = {
            {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, 
            {0, 1}, {1, 1}, {1, 0}, {1, -1}
        };
        
        for(int[] move: kingMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            if(isWithinBounds(newRow, newCol)) {
                if(board[newRow][newCol].isEmpty() || isOpponentPiece(newRow, newCol, p.isWhite())) {
                    moves.add(board[newRow][newCol]);
                }
            }
        }
        
        return moves;
    }
    
    private ArrayList<Square> getKnightMoves(Knight knight) {
        ArrayList<Square> moves = new ArrayList<>();
        int row = knight.getRow();
        int col = knight.getCol();
        Piece p = Utils.getPiece(board, row, col);
        
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        
        for(int[] move: knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            if(isWithinBounds(newRow, newCol)) {
                if(board[newRow][newCol].isEmpty() || isOpponentPiece(newRow, newCol, p.isWhite())) {
                    moves.add(board[newRow][newCol]);
                }
            }
        }
        
        return moves;
    }
    
    private boolean isOpponentPiece(int row, int col, boolean isWhite) {
        // Verifique se a posição contém uma peça adversária
        Piece piece = (Piece) board[row][col].getPiece();
        return piece != null && piece.isWhite() != isWhite;
    }

    private void addLinearMoves(ArrayList<Square> list, int row, int col, int rowIncrement, int colIncrement) {
        Piece p = Utils.getPiece(board, row, col);
        
        int newRow = row + rowIncrement;
        int newCol = col + colIncrement;
        
        while(isWithinBounds(newRow, newCol)) {
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
    
    private boolean isWithinBounds(int newRow, int newCol) {
        return (newRow >= 0 && newRow < 8) && (newCol >= 0 && newCol < 8);
    }
    
    public boolean canCastleKingSide(King king) {
        // rei já se moveu?       
        // tem a torre do rei?
        // torre já se moveu?
        if(king.hasMoved() || !canCastleKingSideRook(king)) {
            return false;
        }      
        // tem peças no caminho?
        for(int i = 1; i < 3; i++) {
            if(!board[king.getRow()][king.getCol()+i].isEmpty()) {
                return false;
            }
        }
        return true;
        // trajetória do roque está sendo vigiada
        // rei está em cheque?
    }

    public boolean canCastleQueenSide(King king) {
        // rei já se moveu?       
        // tem a torre do rei?
        // torre já se moveu?
        if(king.hasMoved() || !canCastleQueenSideRook(king)) {
            return false;
        }
        // tem peças no caminho?
        for(int i = 1; i < 4; i++) {
            if(!board[king.getRow()][king.getCol()-i].isEmpty()) {
                return false;
            }
        }
        return true;     
    }
    
    public boolean canCastleKingSideRook(King king) {
        Piece p = Utils.getPiece(board, king.getRow(), king.getCol()+3);
        return p instanceof Rook && !p.hasMoved();
    }
    
    public boolean canCastleQueenSideRook(King king) {
        Piece p = Utils.getPiece(board, king.getRow(), king.getCol()-4);
        return p instanceof Rook && !p.hasMoved();
    }
    
    public boolean isSquareUnderAttack(int row, int col) {
        // OBS: considerar cor do "atacante"
        // is attacked by rook or queen (lines)
        if(isAttackedByRookOrQueen(row, col)) return true;
        if(isAttackedByBishopOrQueen(row, col)) return true;
        if(isAttackedByKnight(row, col)) return true;
        if(isAttackedByPawn(row, col)) return true;
        if(isAttackedByKing(row, col)) return true;
        // is attacked by bishop or queen (diagonals)
        // is attacked by knight (L moves)
        // is attacked by pawn (one square diagonaly)
        // is attacked by king (one square any direction)
        
        
        return false;
    }

    private boolean isAttackedByRookOrQueen(int row, int col) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};      
        for(int[] direction: directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            while(isWithinBounds(newRow, newCol)) {
                Piece p = (Piece) board[newRow][newCol].getPiece();
                if(p != null) {                  
                    if(playingColor != p.color && (p instanceof Rook || p instanceof Queen)) {
                        return true;
                    }
                    break;
                }
                newRow += direction[0];
                newCol += direction[1];
            }
        }      
        return false;
    }
    
    private boolean isAttackedByBishopOrQueen(int row, int col) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};        
        for(int[] direction: directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            while(isWithinBounds(newRow, newCol)) {
                Piece p = (Piece) board[newRow][newCol].getPiece();
                if(p != null) {                   
                    if(playingColor != p.color && (p instanceof Bishop || p instanceof Queen)) {
                        return true;
                    }
                    break;
                }
                newRow += direction[0];
                newCol += direction[1];
            }
        } 
        return false;
    }
    
    private boolean isAttackedByKnight(int row, int col) {
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };      
        for(int[] move: knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];         
            if(isWithinBounds(newRow, newCol)) {
                Piece p = (Piece) board[newRow][newCol].getPiece();
                if(p != null && playingColor != p.color && p instanceof Knight) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAttackedByPawn(int row, int col) {
        int direction = playingColor == Color.WHITE ? -1: 1;
        
        int[][] pawnAttacks = {{direction, -1}, {direction, 1}};
        for(int[] attack: pawnAttacks) {
            int newRow = row + attack[0];
            int newCol = col + attack[1];
            
            if(isWithinBounds(newRow, newCol)) {
                Piece p = (Piece) board[newRow][newCol].getPiece();
                if(p != null && playingColor != p.color && p instanceof Pawn) {
                    return true;
                }
            }
        }
        return false;
    }   
    
    private boolean isAttackedByKing(int row, int col) {
        int[][] kingMoves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] move : kingMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            if (isWithinBounds(newRow, newCol)) {
                Piece p = (Piece) board[newRow][newCol].getPiece();
                if (p != null && playingColor != p.color && p instanceof King) {
                    return true;
                }
            }
        }
        return false;
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