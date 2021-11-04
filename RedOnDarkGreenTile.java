import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.*;
public class RedOnDarkGreenTile extends Tile
{
    public RedOnDarkGreenTile(int row, int col, boolean isKing)
    {
        super(new ImageIcon(makeURL(isKing)), row, col, isKing);
    }
    
    public static String makeURL(boolean isKing)
    {
        if (!isKing)
        {
            return "images/redOnDarkGreen.jpg";
        }
        else
        {
            return "images/redKingOnDarkGreen.jpg";
        }
    }
}
