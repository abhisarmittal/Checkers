import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.*;
public class LightGreenTile extends Tile
{
    public LightGreenTile(int row, int col)
    {
        super(new ImageIcon("images/lightGreen.jpg"), row, col);
    }
}
