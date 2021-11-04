import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.*;
import javax.swing.border.*;
public class CheckersLogicWithoutTimer implements ActionListener
{
    private Tile[][] tiles = new Tile[8][8];
    private JFrame frame;
    private JPanel buttonsPanel;
    private JPanel labelPanel;
    private JSeparator separator;
    private JLabel label;
    private JLabel timerLabel;
    private String labelText;
    private GridLayout layout;
    private boolean redTurn;
    private Tile clickedTile;
    private int numRedTiles, numWhiteTiles;

    public void run()
    {
        createBoard();
        setDefaultPieces();

        Tile firstTile = null, secondTile = null;
        
        while(numRedTiles>0 && numWhiteTiles>0)
        {
            System.out.print("");
            if (firstTile==null && clickedTile!=null && isEligible(clickedTile)) //if first click is eligible
            {
                firstTile = clickedTile;
                firstTile.setBorder(new LineBorder(Color.BLACK, 3, true));
                labelText = "" + firstTile;
            }
            else if (firstTile!=null && isMoveEligible(firstTile, clickedTile)) //if second click is eligible
            {
                secondTile = clickedTile;
                labelText += "-" + secondTile;
                Tile piece = move(firstTile, secondTile);
                while (piece!=null && canJump(piece))
                {
                    firstTile.setBorder(BorderFactory.createEmptyBorder());
                    piece.setBorder(new LineBorder(Color.BLACK, 3, true));
                    firstTile = piece;
                    secondTile = clickedTile;
                    if (isMoveEligible(firstTile, secondTile))
                    {
                        piece = move(firstTile, secondTile);
                        labelText += "-" + secondTile;
                    }
                }
                firstTile.setBorder(BorderFactory.createEmptyBorder());
                firstTile = null;
                redTurn = !redTurn;
                reconfigureLabelPanel();
            }
            else
            {
                if (firstTile!=clickedTile && firstTile!=null)
                {
                    firstTile.setBorder(BorderFactory.createEmptyBorder());
                    firstTile = null;
                }
                secondTile = null;
            }
        }
        
        redTurn = !redTurn; //redTurn stores the winner
        if (redTurn)
        {
            reconfigureLabelPanel("RED WINS!");
        }
        else    //if (whiteTurn)
        {
            reconfigureLabelPanel("WHITE WINS!");
        }
    }
    
    public void createBoard()
    {
        //layout
        layout = new GridLayout(8,8,0,0);
        
        //buttonsPanel
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(layout);
        buttonsPanel.setMaximumSize(new Dimension(64*8,64*8));
        for (int row=0; row<8; row++)
        {
            for(int col=0; col<8; col++)
            {
                Tile currBtn;
                if ((row+col)%2==1)
                {
                    currBtn = new DarkGreenTile(row, col);
                }
                else
                {
                    currBtn = new LightGreenTile(row, col);
                }
                currBtn.setPreferredSize(new Dimension(64,64));
                currBtn.addActionListener(this);
                tiles[row][col] = currBtn;
                buttonsPanel.add(currBtn);
            }
        }
        
        //separator
        separator = new JSeparator();
        
        //labelPanel
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel = new JLabel();
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        labelPanel = new JPanel();
        labelPanel.setBackground(new Color(214,214,214));
        labelPanel.setPreferredSize(new Dimension(64*8,64));
        labelPanel.add(label);
        labelPanel.add(timerLabel);
        
        //frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(new Point(200,100));
    
        frame.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
        frame.getContentPane().add(separator, BorderLayout.CENTER);
        frame.getContentPane().add(labelPanel, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void setDefaultPieces()
    {
        //SETTING RED PIECES
        for (int row=5; row<8; row++)
        {
            for (int col=0; col<8; col++)
            {
                if (tiles[row][col] instanceof DarkGreenTile)   //if ((row+col)%2==1)
                {
                    tiles[row][col] = new RedOnDarkGreenTile(row, col, false);
                    numRedTiles++;
                }
            }
        }
        
        //SETTING WHITE PIECES
        for (int row=0; row<3; row++)
        {
            for (int col=0; col<8; col++)
            {
                if (tiles[row][col] instanceof DarkGreenTile)   //if ((row+col)%2==1)
                {
                    tiles[row][col] = new WhiteOnDarkGreenTile(row, col, false);
                    numWhiteTiles++;
                }
            }
        }
        
        redTurn = true;
        reconfigureButtonsPanel();
        reconfigureLabelPanel("RED TO MOVE");
    }
    
    public void reconfigureButtonsPanel()
    {
        buttonsPanel.removeAll();
        for (int row=0; row<tiles.length; row++)
        {
            for (int col=0; col<tiles[row].length; col++)
            {
                tiles[row][col].setPreferredSize(new Dimension(64,64));
                tiles[row][col].addActionListener(this);
                buttonsPanel.add(tiles[row][col]);
            }
        }
    }
    
    public void reconfigureLabelPanel(String text)
    {
        if (text.indexOf("RED")>=0)
        {
            label.setText("<html><div style='text-align:center;'><b>" + text.substring(0,text.indexOf("RED")) + 
                        "<font color='#a80303'>RED</font>" + text.substring(text.indexOf("RED")+3) + "</b></div></html>");
        }
        else if (text.indexOf("WHITE")>=0)
        {
            label.setText("<html><div style='text-align:center;'><b>" + text.substring(0,text.indexOf("WHITE")) + 
                        "<font color='white'>WHITE</font>" + text.substring(text.indexOf("WHITE")+5) + "</b></div></html>");
        }
    }
    
    public void reconfigureLabelPanel()
    {
        if (redTurn)
        {
            reconfigureLabelPanel("<html>RED TO MOVE<br>LAST MOVE: " + labelText + "</html>");
        }
        else    //if(whiteTurn)
        {
            reconfigureLabelPanel("<html>WHITE TO MOVE<br>LAST MOVE: " + labelText + "</html>");
        }
    }
    
    public Tile move(Tile firstTile, Tile secondTile)
    {
        Tile piece = null;
        if (redTurn)
        {
            //if moving diagonally
            if (Math.abs(secondTile.getRow() - firstTile.getRow())==1)
            {
                tiles[firstTile.getRow()][firstTile.getCol()] = new DarkGreenTile(firstTile.getRow(), firstTile.getCol());
                tiles[secondTile.getRow()][secondTile.getCol()] = new RedOnDarkGreenTile(secondTile.getRow(), secondTile.getCol(), firstTile.isKing() || secondTile.getRow()==0);
            }
            //if jumping over
            else if (Math.abs(secondTile.getRow() - firstTile.getRow())==2)
            {
                tiles[firstTile.getRow()][firstTile.getCol()] = new DarkGreenTile(firstTile.getRow(), firstTile.getCol());
                tiles[secondTile.getRow()][secondTile.getCol()] = new RedOnDarkGreenTile(secondTile.getRow(), secondTile.getCol(), firstTile.isKing() || secondTile.getRow()==0);
                int midRow=(firstTile.getRow()+secondTile.getRow())/2;
                int midCol=(firstTile.getCol()+secondTile.getCol())/2;
                tiles[midRow][midCol] = new DarkGreenTile(midRow, midCol);
                numWhiteTiles--;
                piece = tiles[secondTile.getRow()][secondTile.getCol()];
            }
        }
        else    //if (whiteTurn)
        {
            //if moving diagonally
            if (Math.abs(secondTile.getRow() - firstTile.getRow())==1)
            {
                tiles[firstTile.getRow()][firstTile.getCol()] = new DarkGreenTile(firstTile.getRow(), firstTile.getCol());
                tiles[secondTile.getRow()][secondTile.getCol()] = new WhiteOnDarkGreenTile(secondTile.getRow(), secondTile.getCol(), firstTile.isKing() || secondTile.getRow()==7);
            }
            //if jumping over
            else if (Math.abs(secondTile.getRow() - firstTile.getRow())==2)
            {
                tiles[firstTile.getRow()][firstTile.getCol()] = new DarkGreenTile(firstTile.getRow(), firstTile.getCol());
                tiles[secondTile.getRow()][secondTile.getCol()] = new WhiteOnDarkGreenTile(secondTile.getRow(), secondTile.getCol(), firstTile.isKing() || secondTile.getRow()==7);
                int midRow=(firstTile.getRow()+secondTile.getRow())/2;
                int midCol=(firstTile.getCol()+secondTile.getCol())/2;
                tiles[midRow][midCol] = new DarkGreenTile(midRow, midCol);
                numRedTiles--;
                piece = tiles[secondTile.getRow()][secondTile.getCol()];
            }
        }
        reconfigureButtonsPanel();
        return piece;
    }
    
    public boolean isEligible(Tile firstTile)
    {
        if (redTurn && firstTile instanceof RedOnDarkGreenTile)
        {
            if (canJump(firstTile))
            {
                return true;
            }
            for (Tile[] row: tiles)
            {
                for (Tile currBtn: row)
                {
                    if (currBtn instanceof RedOnDarkGreenTile && canJump(currBtn))
                    {
                        return false;
                    }
                }
            }
            return canDiagonallyMove(firstTile);
        }
        else if (!redTurn && firstTile instanceof WhiteOnDarkGreenTile)
        {
            if (canJump(firstTile))
            {
                return true;
            }
            for (Tile[] row: tiles)
            {
                for (Tile currBtn: row)
                {
                    if (currBtn instanceof WhiteOnDarkGreenTile && canJump(currBtn))
                    {
                        return false;
                    }
                }
            }
            return canDiagonallyMove(firstTile);
        }
        return false;
    }
    
    public boolean canDiagonallyMove(Tile currBtn)
    {
        int row = currBtn.getRow();
        int col = currBtn.getCol();
        
        //up-right
        if (row<=6 && col<=6 && isDiagonalMoveEligible(currBtn, tiles[row+1][col+1]))
        {
            return true;
        }
        //up-left
        if (row<=6 && col>=1 && isDiagonalMoveEligible(currBtn, tiles[row+1][col-1]))
        {
            return true;
        }
        //down-right
        if (row>=1 && col<=6 && isDiagonalMoveEligible(currBtn, tiles[row-1][col+1]))
        {
            return true;
        }
        //down-left
        if (row>=1 && col>=1 && isDiagonalMoveEligible(currBtn, tiles[row-1][col-1]))
        {
            return true;
        }
        return false;
    }
    
    public boolean canJump(Tile currBtn)
    {
        int row = currBtn.getRow();
        int col = currBtn.getCol();
        
        //up-right
        if (row<=5 && col<=5 && isJumpEligible(currBtn, tiles[row+2][col+2]))
        {
            return true;
        }
        //up-left
        if (row<=5 && col>=2 && isJumpEligible(currBtn, tiles[row+2][col-2]))
        {
            return true;
        }
        //down-right
        if (row>=2 && col<=5 && isJumpEligible(currBtn, tiles[row-2][col+2]))
        {
            return true;
        }
        //down-left
        if (row>=2 && col>=2 && isJumpEligible(currBtn, tiles[row-2][col-2]))
        {
            return true;
        }
        return false;
    }
    
    public boolean isMoveEligible(Tile firstTile, Tile secondTile)
    {
        return (isJumpEligible(firstTile, secondTile) || (!canJump(firstTile) && isDiagonalMoveEligible(firstTile, secondTile)));
    }
    
    public boolean isDiagonalMoveEligible(Tile firstTile, Tile secondTile)
    {
        if (secondTile instanceof DarkGreenTile && //if no other piece is already on the desired tile
            Math.abs(secondTile.getCol()-firstTile.getCol())==1)    //if moving diagonally
        {
            //RED TURN:
            if (redTurn)
            {
                //if moving diagonally forward
                if (firstTile.getRow()-1 == secondTile.getRow())
                {
                    return true;
                }
                //if moving diagonally backward
                if (firstTile.isKing() && firstTile.getRow()+1 == secondTile.getRow())
                {
                    return true;
                }
            }
            //WHITE TURN:
            else
            {
                //if moving diagonally forward
                if (firstTile.getRow()+1 == secondTile.getRow())
                {
                    return true;
                }
                //if moving diagonally backward
                if (firstTile.isKing() && firstTile.getRow()-1 == secondTile.getRow())
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isJumpEligible(Tile firstTile, Tile secondTile)
    {
        if (secondTile instanceof DarkGreenTile && //if no other piece is already on the desired tile
            Math.abs(secondTile.getCol()-firstTile.getCol())==2)    //if jumping over
        {
            //RED TURN:
            if (redTurn)
            {
                //if jumping over forward
                if (firstTile.getRow()-2 == secondTile.getRow() && 
                    tiles[(firstTile.getRow()+secondTile.getRow())/2][(firstTile.getCol()+secondTile.getCol())/2] instanceof WhiteOnDarkGreenTile)
                {
                    return true;
                }
                //if jumping over once backward
                if (firstTile.isKing() && firstTile.getRow()+2 == secondTile.getRow() && 
                    tiles[(firstTile.getRow()+secondTile.getRow())/2][(firstTile.getCol()+secondTile.getCol())/2] instanceof WhiteOnDarkGreenTile)
                {
                    return true;
                }
            }
            //WHITE TURN:
            else
            {
                //if jumping over once forward
                if (firstTile.getRow()+2 == secondTile.getRow() && 
                    tiles[(firstTile.getRow()+secondTile.getRow())/2][(firstTile.getCol()+secondTile.getCol())/2] instanceof RedOnDarkGreenTile)
                {
                    return true;
                }
                //if jumping over once backward
                if (firstTile.isKing() && firstTile.getRow()-2 == secondTile.getRow() && 
                    tiles[(firstTile.getRow()+secondTile.getRow())/2][(firstTile.getCol()+secondTile.getCol())/2] instanceof RedOnDarkGreenTile)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void actionPerformed(ActionEvent actionEvent)
    {
        clickedTile = (Tile)actionEvent.getSource();
    }
}
