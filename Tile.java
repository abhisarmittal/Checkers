import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.*;
public class Tile extends JButton
{
    private int row, col;
    private boolean isKing;
    
    public Tile(ImageIcon i, int row, int col, boolean isKing)
    {
        super(i);
        this.row=row;
        this.col=col;
        this.isKing = isKing;
    }
    
    public Tile(ImageIcon i, int row, int col)
    {
        super(i);
        this.row=row;
        this.col=col;
    }
    
    public int getRow()
    {
        return row;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public boolean isKing()
    {
        return isKing;
    }
    
    public String toString()
    {
        return createTileNumber();
    }
    
    public String createTileNumber()
    {
        return "" + ((8-this.row)*4 - (this.col/2));
    }
}
