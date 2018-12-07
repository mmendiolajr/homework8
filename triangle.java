package trianglepackage;
import java.util.*;

public class TriangleBoard {
    boolean[][] peg;
    int pegsLeft;
    int dimension;

    public static class Point {
      int row;
      int column;

      public Point(int row,int column) {
         this.row=row;
         this.column=column;
      }
      public boolean isValid(int dimension) {
         return row>=0 && row<dimension && column>=0 && column<=row;
      }
      public void set(int row,int column) {
         this.row=row; 
         this.column=column;
      }
    }
    public class Move {
       TriangleBoard.Point from;
       TriangleBoard.Point to;
   
       public Move(TriangleBoard.Point from,TriangleBoard.Point to) {
           this.from=from;
           this.to=to;
       }
       public boolean isValid(TriangleBoard board) {
           if (!from.isValid(board.dimension) || !to.isValid(board.dimension)) {return false;}
           if (!board.peg[from.row][from.column]) {return false;}
           if (board.peg[to.row][to.column]) {return false;}
           int rowJump=Math.abs(from.row-to.row);
           int colJump=Math.abs(from.column-to.column);
           if (rowJump==0) {
               if (colJump!=2) {return false;}
           } else if (rowJump==2) {
               if (colJump!=0 && colJump!=2) {return false;}
           } else {return false;}
           return board.peg[(from.row+to.row)/2][(from.column+to.column)/2];
       }
    }

    public void setup(int dim, TriangleBoard.Point hole) {
       dimension=dim;
       peg=new boolean[dim][dim];
       pegsLeft=-1;
       for (int i=0;i<dim;i++) {
          for (int j=0;j<=i;j++) {peg[i][j]=true; pegsLeft++;}
       }
       peg[hole.row][hole.column]=false;
    }
    public boolean move(Move move) {
        if (!move.isValid(this))  {
           System.out.println("Invalid move");
           return false;
        }
        peg[move.from.row][move.from.column]=false;
        peg[move.to.row][move.to.column]=true;
        peg[(move.from.row+move.to.row)/2][(move.from.column+move.to.column)/2]=false;
        pegsLeft--;
        return true;        
    }
    public void undoMove(Move move) {
        peg[move.from.row][move.from.column]=true;
        peg[move.to.row][move.to.column]=false;
        peg[(move.from.row+move.to.row)/2][(move.from.column+move.to.column)/2]=true;
        pegsLeft++;
    }
    public ArrayList validMovesFromPoint(Point from) {
        ArrayList moves=new ArrayList();
        if (!from.isValid(dimension)) {return moves;}
        if (!peg[from.row][from.column]) {return moves;}
        Move move=new Move(from,new Point(from.row-2,from.column));
        if (move.isValid(this)) {moves.add(move);}
        move=new Move(from,new Point(from.row-2,from.column-2));
        if (move.isValid(this)) {moves.add(move);}
        move=new Move(from,new Point(from.row,from.column-2));
        if (move.isValid(this)) {moves.add(move);}
        move=new Move(from,new Point(from.row,from.column+2));
        if (move.isValid(this)) {moves.add(move);}
        move=new Move(from,new Point(from.row+2,from.column));
        if (move.isValid(this)) {moves.add(move);}
        move=new Move(from,new Point(from.row+2,from.column+2));
        if (move.isValid(this)) {moves.add(move);}
        return moves;
    }
    public ArrayList validMoves() {
        Point point;
        ArrayList moves=new ArrayList();
        ArrayList pointMoves;
        for (int i=0;i<dimension;i++) {
           for (int j=0;j<=i;j++) {
               point=new Point(i,j);
               pointMoves=this.validMovesFromPoint(point);
               moves.addAll(pointMoves);
           }
        }
        return moves;
    }
    public ArrayList bestSequence() {
        ArrayList sequence=new ArrayList();
        ArrayList<Move> moves=this.validMoves();
        if (moves.isEmpty()) {return sequence;}
        for (int i=0;i<moves.size();i++) {
            this.move(moves.get(i));
            if (pegsLeft==1) {
                sequence.add(moves.get(i));
                this.undoMove(moves.get(i));            
                return sequence;
            }
            ArrayList moveSequence=this.bestSequence();
            if (moveSequence.size()+1>sequence.size()) {
               sequence.clear();
               sequence.add(moves.get(i));
               sequence.addAll(moveSequence);
            }
            this.undoMove(moves.get(i));            
        }
        return sequence;
    }
}
