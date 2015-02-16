/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GridJFrame.java
 *
 * Created on May 1, 2009, 12:56:30 PM
 */

package gridworld;

import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author hoho
 */
public class GridJFrame extends javax.swing.JFrame {

    private static final int iCellSize = 50; //50 pixels

    // Dimensions of the grid world
    private int iSizeX, iSizeY;
    
    // Coordinates the food elements
    private int[] iFoodX;
    private int[] iFoodY;
    // Coordinates and orientation of the creature
    int iCreatureX, iCreatureY;
    int iOrientation; //0-north, 1-east, 2-south, 3-west

    // Inset for the System Menu
    int insetX, insetY;

    // Image for the creature

    Image imageCreature;

    InputStream is;
    private static final String absName = "creature.bmp";

    /** Creates new form GridJFrame */
    public GridJFrame() {
        initComponents();
        
        try
        {
            is = this.getClass().getResourceAsStream("creature.bmp");
            imageCreature = ImageIO.read(is);
        }catch(Exception e)
        {
            System.err.println("Couldn't open creature.bmp");
        }

        //for Debug purposes only
        //----------------------

        // Default size of the grid world is 10x10
        this.iSizeX = 10;
        this.iSizeY = 10;
               
        iFoodX = new int[3];
        iFoodY = new int[3];

        for (int i = 0; i < iFoodY.length; i++) {
            iFoodX[i] = i;
            iFoodY[i] = i;
        }

        iCreatureX = 5;
        iCreatureY = 5;
        iOrientation = 3; //creature is facing north


        Insets insets = this.getInsets();
		insetX = insets.left + insets.right;
		insetY = insets.top + insets.bottom;


        setSize(iCellSize*iSizeX, iCellSize*iSizeY + insetY);
    }
    public GridJFrame(int iSizeX, int iSizeY) {
        initComponents();


        try
        {
            is = this.getClass().getResourceAsStream(absName);
            imageCreature = ImageIO.read(is);
        }catch(Exception e)
        {
            System.err.println("Could not open creature.bmp");
        }

        //Resize the JFrame to the needed size
        this.iSizeX = iSizeX;
        this.iSizeY = iSizeY;
        
        // Get into account the system menu when drawing
        Insets insets = this.getInsets();
		insetX = insets.left + insets.right;
		insetY = insets.top + insets.bottom;

        setSize(iCellSize*iSizeX, iCellSize*iSizeY+insetY);

        this.setBackground(Color.white);

        
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        repaint();
    }//GEN-LAST:event_formFocusGained

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        repaint();
    }//GEN-LAST:event_formComponentShown

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GridJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void paint(Graphics g)
    {
         Graphics2D g2d = (Graphics2D)g;

         //g2d.clearRect(0, insetY, iCellSize*iSizeX, iCellSize*iSizeY);
         g2d.setColor(Color.white);
         g2d.fillRect(0, 0, iCellSize*iSizeX, iCellSize*iSizeY+insetY);
         g2d.setColor(Color.red);
         //Draw the vertical lines determining the cells
         for (int i = 1; i < iSizeX; i++)
         {
                g2d.drawLine(iCellSize*i, 0, iCellSize*i, iSizeY*iCellSize+insetY);
         }
         //Draw the horizontal lines determining the cells
         for (int i = 1; i < iSizeY; i++)
         {
                g2d.drawLine(0, iCellSize*i + insetY, iSizeX*iCellSize, iCellSize*i+ insetY);
         }


         // Draw the food elements
         g.setColor(Color.green);
         for (int i = 0; i < iFoodX.length; i++) {
                g.fillRoundRect(iFoodX[i]*iCellSize,
                                iFoodY[i]*iCellSize+insetY,
                                iCellSize,
                                iCellSize,
                                50,
                                50);

            }

         // Draw the creature. Bute first check for the correct orientation

         //AffineTransform at = new AffineTransform();
         //at.setToRotation(-Math.PI/2);

         if(iOrientation == 1) // East
            g2d.rotate(Math.PI/2,iCreatureX*iCellSize+iCellSize/2, iCreatureY*iCellSize+insetY+iCellSize/2 );
         else if(iOrientation == 2) // South
            g2d.rotate(Math.PI,iCreatureX*iCellSize+iCellSize/2, iCreatureY*iCellSize+insetY+iCellSize/2 );
         else if(iOrientation == 3) // West
            g2d.rotate(-Math.PI/2,iCreatureX*iCellSize+iCellSize/2, iCreatureY*iCellSize+insetY+iCellSize/2 );
         g2d.drawImage(imageCreature,
                       iCreatureX*iCellSize+1,
                       iCreatureY*iCellSize+insetY+1,
                       this);

   }




    /**
     * 
     * @param iSizeX
     * @param iSizeY
     * @param iCreatureX
     * @param iCreatureY
     * @param iFoodX
     * @param iFoodY
     */
    public void doDrawing(int iCreatureX, int iCreatureY,
                          int iOrientation,
                          int[] arrayFoodX, int[] arrayFoodY)
    {
        this.iFoodX = arrayFoodX;
        this.iFoodY = arrayFoodY;

        this.iCreatureX = iCreatureX;
        this.iCreatureY = iCreatureY;
        this.iOrientation = iOrientation;

        repaint(0,0, iCellSize*iSizeX, iCellSize*iSizeY+insetY);
    }


}
