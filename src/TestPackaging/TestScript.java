package TestPackaging;

import TestPackaging.Conversions.PaintUtils.PaintUtils;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

@Manifest(authors = { "Inf3cti0us -bro433-" }, name = "Name Sensor", description = "Sensors Your accounts name!", version = 0.2, topic = 2)
public class TestScript extends ActiveScript implements PaintListener{

    WidgetChild wUsername = new Widget(137).getChild(53);

    /** Meh
    WidgetChild slot1KeyBind = new Widget(640).getChild(70);
    WidgetChild slot2KeyBind = new Widget(640).getChild(75);
    WidgetChild slot3KeyBind = new Widget(640).getChild(80);
    WidgetChild slot4KeyBind = new Widget(640).getChild(85);
    WidgetChild slot5KeyBind = new Widget(640).getChild(90);
    WidgetChild slot6KeyBind = new Widget(640).getChild(95);
    WidgetChild slot7KeyBind = new Widget(640).getChild(100);
    WidgetChild slot8KeyBind = new Widget(640).getChild(105);
    WidgetChild slot9KeyBind = new Widget(640).getChild(110);
    WidgetChild slot10KeyBind = new Widget(640).getChild(115);
    WidgetChild slot11KeyBind = new Widget(640).getChild(120);
    WidgetChild slot12KeyBind = new Widget(640).getChild(125);
    */


    public BufferedImage sensoredUsername;
    public BufferedImage dataBuffer;

    boolean smeared = false;
    Rectangle noUsername = new Rectangle(548, 424, 189, 31);

    @Override
    public int loop() {
        if(Game.isLoggedIn()){
            if(!smeared && wUsername.visible()){
            createBlurredImage(5);
                smeared = true;
            }

            while(smeared){
                sleep((int) 100L);
               }
        }
        return 43; //Best Number In the world..
    }

    /**
     *
     * @param blurFactor How much blur is added to
     */
    public void createBlurredImage(int blurFactor) {

        //Capture Username
        sensoredUsername = Environment.captureScreen().getSubimage(wUsername.getAbsoluteX(), wUsername.getAbsoluteY(),
                wUsername.getWidth()-15, wUsername.getHeight());

        //
        dataBuffer = new BufferedImage(sensoredUsername.getWidth(null),
                sensoredUsername.getHeight(null),
                BufferedImage.TYPE_INT_BGR);

        //Getting the graphics
        Graphics g = dataBuffer.getGraphics();

        //Drawing the sensoredUsername to 500x300
        g.drawImage(sensoredUsername, 500, 300, null);      // g.drawImage(mshi, 455, 255, null);

        //Declare the blurKernel
        float[] blurKernel = {
                1 / 9f, 1 / 9f, 1 / 9f,
                1 / 9f, 1 / 9f, 1 / 9f,
                1 / 9f, 1 / 9f, 1 / 9f
        };

        //Initialize the blur
        BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));     //new Kernel 3,3,blurKernel

        //Blur the username blurFactor times (lol)
        for(int i=0; i < blurFactor; i++){
        sensoredUsername = blur.filter(sensoredUsername, new BufferedImage(sensoredUsername.getWidth()-1,
                sensoredUsername.getHeight()-1, sensoredUsername.getType()));
        }
         //Dispose of the graphics.. We are done blurring!
        g.dispose();
    }


    @Override
    public void onRepaint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        Font meh = g.getFont();
               if(Game.isLoggedIn() && wUsername!=null && wUsername.isOnScreen() && wUsername.visible()){
                  g.drawImage(sensoredUsername, wUsername.getAbsoluteX()-2, wUsername.getAbsoluteY()+1,
                          wUsername.getWidth()-16, wUsername.getHeight()-4,null);

        } else{
                   PaintUtils.drawStringCentered("No username to block!", meh, noUsername, Color.WHITE, g);
                   PaintUtils.drawGrid(g, 550, 257, 45, 20, 4, 12);    //TODO Work on drawing a grid..

               }

    }
}
