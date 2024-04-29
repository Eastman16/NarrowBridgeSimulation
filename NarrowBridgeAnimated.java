import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class NarrowBridgeAnimated extends JPanel implements Runnable{
    private static final String title = "Bridge program - animated";
    LinkedList<Bus> busesList;

    NarrowBridgeAnimated(GUI gui){
        this.busesList= (LinkedList<Bus>) gui.allBuses;
        JFrame frame = new JFrame(title);
        frame.setSize(515,650);
        frame.setContentPane(this);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        new Thread(this).start();
        repaint();
    }

    @Override
    public void run() {
        while(true){
            this.repaint();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*Stages of the route:
        1. Boarding passengers
        2. Going to the bridge
        3. Waiting to enter the bridge
        4. Riding on the bridge
        5. Getting off the bridge
        6. Going to the parking
        7. Unboarding passengers
        */

        //white fields stage 1/7 of the route
        g.setColor(Color.WHITE);
        g.fillRect(0,0,50,650);
        g.fillRect(450,0,50,650);
        //gray fields 2/6 stage of the route
        g.setColor(Color.GRAY);
        g.fillRect(50,0,80,650);
        g.fillRect(380,0,70,650);
        //yellow fields 3/5 stage of the route
        g.setColor(Color.yellow);
        g.fillRect(120,0,40,650);
        g.fillRect(340,0,40,650);
        //green field stage 4
        g.setColor(Color.GREEN);
        g.fillRect(160,0,180,650);

        int yPos = 50;

        //conditions for bus movement
        synchronized (busesList){
            try{
                for(Bus bus: busesList){
                    if(bus.busMovement.equals(BusMovement.GOING_TO__THE_BRIDGE) || bus.busMovement.equals(BusMovement.GOING_TO_THE_PARKING)){
                        bus.xPos+=20.0*160.0/500.0;
                    }
                    if(bus.busMovement.equals(BusMovement.RIDING_ON_THE_BRIDGE)){
                        bus.xPos+=20.0*225.0/3000.0;
                    }
                    if (busesList.size() <= 15)
                        yPos += 30;
                    else
                        yPos += 15 * 30 / busesList.size();

                    bus.draw(g, (int)bus.xPos, yPos);

                }
            }catch (Exception e){}
        }
    }
}
