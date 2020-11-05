package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.border.AbstractBorder;
import net.projectdf.GUI.Sprites.BorderSprites;

public class PerforatedBorder extends AbstractBorder {

    public static int BLUE = 0;
    public static int GREEN = 1;
    public static int GREY = 2;

    private Image topLeft;
    private Image topRight;
    private Image bottomLeft;
    private Image bottomRight;
    private Image vertical;
    private Image horizontal;

    private final int INSETS = 4;

    public PerforatedBorder(int color) {
        if (color == BLUE) {
            topLeft = BorderSprites.getSprite(BorderSprites.B_TOP_LEFT);
            topRight = BorderSprites.getSprite(BorderSprites.B_TOP_RIGHT);
            bottomLeft = BorderSprites.getSprite(BorderSprites.B_BOTTOM_LEFT);
            bottomRight = BorderSprites.getSprite(BorderSprites.B_BOTTOM_RIGHT);
            vertical = BorderSprites.getSprite(BorderSprites.B_VERTICAL);
            horizontal = BorderSprites.getSprite(BorderSprites.B_HORIZONTAL);
        } else if (color == GREEN) {
            topLeft = BorderSprites.getSprite(BorderSprites.GR_TOP_LEFT);
            topRight = BorderSprites.getSprite(BorderSprites.GR_TOP_RIGHT);
            bottomLeft = BorderSprites.getSprite(BorderSprites.GR_BOTTOM_LEFT);
            bottomRight = BorderSprites.getSprite(BorderSprites.GR_BOTTOM_RIGHT);
            vertical = BorderSprites.getSprite(BorderSprites.GR_VERTICAL);
            horizontal = BorderSprites.getSprite(BorderSprites.GR_HORIZONTAL);
        } else if (color == GREY) {
            topLeft = BorderSprites.getSprite(BorderSprites.G_TOP_LEFT);
            topRight = BorderSprites.getSprite(BorderSprites.G_TOP_RIGHT);
            bottomLeft = BorderSprites.getSprite(BorderSprites.G_BOTTOM_LEFT);
            bottomRight = BorderSprites.getSprite(BorderSprites.G_BOTTOM_RIGHT);
            vertical = BorderSprites.getSprite(BorderSprites.G_VERTICAL);
            horizontal = BorderSprites.getSprite(BorderSprites.G_HORIZONTAL);
        } else {
            throw new IllegalArgumentException("must use BLUE, GREEN, or GREY");
        }
    }

    @Override
    public Insets getBorderInsets(Component component) {
        return getBorderInsets(component, new Insets(INSETS, INSETS, INSETS, INSETS));
    }

    @Override
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = INSETS;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(x, y, width, height);

        Graphics2D g2 = (Graphics2D) g;

        int tlw = topLeft.getWidth(null);
        int tlh = topLeft.getHeight(null);
        int tcw = horizontal.getWidth(null);
        int tch = horizontal.getHeight(null);
        int trw = topRight.getWidth(null);
        int trh = topRight.getHeight(null);

        int lcw = vertical.getWidth(null);
        int lch = vertical.getHeight(null);
        int rcw = vertical.getWidth(null);
        int rch = vertical.getHeight(null);

        int blw = bottomLeft.getWidth(null);
        int blh = bottomLeft.getHeight(null);
        int bcw = horizontal.getWidth(null);
        int bch = horizontal.getHeight(null);
        int brw = bottomRight.getWidth(null);
        int brh = bottomRight.getHeight(null);

        // Top
        fillTexture(g2, horizontal, x + tlw, y, width - tlw - trw, tch);

        // Sides
        fillTexture(g2, vertical, x, y + tlh, lcw, height - tlh - blh);
        fillTexture(g2, vertical, x + width - rcw, y + trh, rcw, height - trh - brh);

        // Bottom
        fillTexture(g2, horizontal, x + blw, y + height - bch, width - blw - brw, bch);

        // Corners
        fillTexture(g2, topLeft, x, y, tlw, tlh);
        fillTexture(g2, topRight, x + width - trw, y, trw, trh);
        fillTexture(g2, bottomLeft, x, y + height - blh, blw, blh);
        fillTexture(g2, bottomRight, x + width - brw, y + height - brh, brw, brh);
    }

    private void fillTexture(Graphics2D g2, Image img, int x, int y, int w, int h) {
        BufferedImage buff = createBufferedImage(img);
        Rectangle anchor = new Rectangle(x, y, img.getWidth(null), img.getHeight(null));
        TexturePaint paint = new TexturePaint(buff, anchor);
        g2.setPaint(paint);
        g2.fillRect(x, y, w, h);
    }

    private BufferedImage createBufferedImage(Image img) {
        BufferedImage buff = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics gfx = buff.createGraphics();
        gfx.drawImage(img, 0, 0, null);
        gfx.dispose();
        return buff;
    }
}
