package TestPackaging.Conversions.PaintUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Inf3cti0us
 * Date: 16/07/13
 * Project: Powerbot
 */
public class PaintUtils {
    /**
     *
     * @param number  Input number
     * @return  Returns a number formatted into Thousands("<bold>K</bold>"),Millions("<bold>M</bold>")
     */

    public static String formatNumber(int number)
    {
        DecimalFormat format = new DecimalFormat("#0.0");
        if (number >= 10000) {
            return String.valueOf(format.format(number / 1000.0F)) +
                    "K";
        }
        if(number >= 1000000){
            return String.valueOf(format.format(number / 1000000.0F)) +
                    "M";
        }
        return String.valueOf(number);
    }

    /**
     *
     * @param text  String to paint
     * @param font  Font type of string
     * @param rect  Rectangle string will be in
     * @param color Color of the string
     * @param g  Graphics which will paint string
     */
    public static void drawStringCentered(String text, Font font, Rectangle rect, Color color, Graphics g)
    {
        g.setFont(font);
        g.setColor(color);

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D stringRect = fm.getStringBounds(text, g);

        int x = (int)(rect.getCenterX() - stringRect.getWidth() / 2.0D);
        int y = (int)(rect.getCenterY() + rect.getHeight() / 3.0D);

        g.drawString(text, x, y);
    }

    /**
     *
     * @param g Graphics to paint grid
     * @param x Coords x
     * @param y Coords y
     * @param cellWidth Width of each cell
     * @param cellHeight Height of each cell
     * @param rows Amount of cells to draw
     * @param columns Amount of columns to draw
     */
    public static void drawGrid(Graphics2D g, int x, int y, int cellWidth, int cellHeight, int rows, int columns)
    {
        for (int i = 0; i <= columns; i++) {
            g.draw(new Line2D.Double(x + cellWidth * i, y, x + cellWidth * i, y + rows * cellHeight));
        }
        for (int i = 0; i <= rows; i++)
            g.draw(new Line2D.Double(x, y + cellHeight * i, x + columns * cellWidth, y + cellHeight * i));
    }

    /**
     *
     * @param g Graphics that paint text
     * @param text String to be painted
     * @param w Width of string
     * @param h Height of string
     */
    public static void paintText(Graphics g, String text, int w, int h)
    {
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(text);
        g.drawString(text, (w - stringWidth) / 2, (h - fm.getAscent() - fm.getDescent()) / 2 + fm.getAscent());
    }

    /**
     *
     * @param g Graphics that will paint text
     * @param text String to be written
     * @param kernelSize Size of the Kernel
     * @param blurFactor Amount of Blur
     * @param shadowColor Color of the shadow
     * @param shiftX Coords x shift factor
     * @param shiftY Coords y shift factor
     */
    public static void paintDropShadowedText(Graphics g, String text, int kernelSize, float blurFactor, Color shadowColor, int shiftX, int shiftY)
    {
        Color startColor = g.getColor();

        float[] kernelData = new float[kernelSize * kernelSize];
        for (int i = 0; i < kernelData.length; i++) {
            kernelData[i] = blurFactor;
        }
        int padding = 10;
        ConvolveOp blur = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernelData));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent() + fm.getDescent();
        int imageWidth = textWidth + padding;
        int imageHeight = textHeight + padding;

        BufferedImage textImage = new BufferedImage(imageWidth, imageHeight, 2);
        BufferedImage dropShadowImage = new BufferedImage(imageWidth, imageHeight, 2);

        Graphics2D textImageG = textImage.createGraphics();
        textImageG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        textImageG.setFont(g.getFont());
        textImageG.setColor(shadowColor);
        paintText(textImageG, text, imageWidth, imageHeight);
        textImageG.dispose();

        Graphics2D blurryImageG = dropShadowImage.createGraphics();
        blurryImageG.drawImage(textImage, blur, shiftX, shiftY);
        blurryImageG.setColor(startColor);
        blurryImageG.setFont(g.getFont());

        blurryImageG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintText(blurryImageG, text, imageWidth, imageHeight);
        blurryImageG.dispose();

        g.drawImage(dropShadowImage, 0, 0, null);
    }


    public static void outlineText(Graphics g, String s, float x, float y, Color c)
    {
        Graphics2D g2 = (Graphics2D)g;
        Color origColor = g.getColor();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(c);
        int t = 1;
        g2.drawString(s, x + t, y + t);
        g2.drawString(s, x + t, y - t);
        g2.drawString(s, x - t, y + t);
        g2.drawString(s, x - t, y - t);

        g2.drawString(s, x + t, y);
        g2.drawString(s, x, y + t);
        g2.drawString(s, x - t, y);
        g2.drawString(s, x, y - t);

        g2.setColor(origColor);
        g2.drawString(s, x, y);
    }

    public static void outlineText(Graphics2D g, String s, int x, int y) {
        outlineText(g, s, x, y, 1, Color.black);
    }

    /**
     *
     * @param g  Graphics which will draw image. Onrepaint() ?
     * @param s  String which will be written
     * @param x  Coords x
     * @param y  Coords y
     * @param thick Thickness of the string
     * @param c  Color of the text
     */
    public static void outlineText(Graphics2D g, String s, int x, int y, int thick, Color c)
    {
        Color orig = g.getColor();
        g.setColor(c);
        for (int i = -thick; i <= thick; i++) {
            for (int j = -thick; j <= thick; j++) {
                g.drawString(s, x + i, y + j);
            }
        }
        g.setColor(orig);
        g.drawString(s, x, y);
    }


}
