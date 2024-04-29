import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
public class Bus implements Runnable{
    double xPos=0.0;
    int yPos=0;
    public static final int MIN_BOARDING_TIME = 1000;
    public static final int MAX_BOARDING_TIME = 10000;
    public static final int GETTING_TO_BRIDGE_TIME = 500;
    public static final int CROSSING_BRIDGE_TIME = 3000;
    public static final int GETTING_PARKING_TIME = 500;
    public static final int UNLOADING_TIME = 500;


    public BusMovement getBusMovement() {
        return busMovement;
    }
    public void setBusMovement(BusMovement busMovement) {
        this.busMovement = busMovement;
    }
    BusMovement busMovement = BusMovement.BOARDING;
    private static int numberOfBuses = 0;
    int id;
    BusDirection dir;
    GUI bridge;

    //setting sleep time
    public static void sleep(int millis){
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){

        }
    }
    //setting random sleep time
    public static void sleep(int min_millis, int max_milis){
        sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
    }

    //class contructor
    Bus(GUI bridge){
        this.bridge=bridge;
        this.id=++numberOfBuses;
        if(ThreadLocalRandom.current().nextInt(0,2)==0){
            this.dir=BusDirection.EAST;
            this.xPos=10.0;
        }
        else {
            this.dir=BusDirection.WEST;
            this.xPos=10.0;
        }
    }

    //printing methods
    void printBusInfo(String message){
        String messageFinal = "Bus[" + id + "->"+dir+"]: " + message +"\n";
        bridge.textArea.insert(messageFinal,0);
    }
    void boarding(){
        printBusInfo("Waiting for people");
        sleep(MIN_BOARDING_TIME,MAX_BOARDING_TIME);
    }
    void goToTheBridge(){
        busMovement = BusMovement.GOING_TO__THE_BRIDGE;
        printBusInfo("Going to the bridge");
        sleep(GETTING_TO_BRIDGE_TIME);
    }
    void rideTheBridge(){
        busMovement = BusMovement.RIDING_ON_THE_BRIDGE;
        printBusInfo("Ridsing on te bridge");
        sleep(CROSSING_BRIDGE_TIME);
    }
    void goToTheParking(){
        busMovement = BusMovement.GOING_TO_THE_PARKING;
        printBusInfo("Going to the parking");
        sleep(GETTING_PARKING_TIME+150);
    }
    void unloading(){
        busMovement = BusMovement.UNLOADING;
        printBusInfo("Passengers unboarding");
        sleep(UNLOADING_TIME);
    }
    //bus cycle
    public void run(){
        busMovement = BusMovement.BOARDING;
        bridge.allBuses.add(this);
        boarding();

        goToTheBridge();
        bridge.getOnTheBridge(this);

        rideTheBridge();
        busMovement = BusMovement.GOING_TO_THE_PARKING;
        bridge.getOffTheBridge(this);

        busMovement = BusMovement.GOING_TO_THE_PARKING;
        goToTheParking();

        unloading();
        bridge.allBuses.remove(this);
    }

    //drawing bus
    public void draw(Graphics g, int x, int y){
        this.yPos=y;
        g.setColor(Color.BLACK);
        if(this.dir.equals(BusDirection.EAST)){
            g.drawOval(x,y,10,10);
            g.drawOval(x+10,y,10,10);
            g.fillRect(x-4,y-12,27,17);
            g.setColor(Color.WHITE);
            g.drawString(this.id+"E",x,y);
        }else{
            x = 482 - x;
            g.drawOval(x,y,10,10);
            g.drawOval(x+10,y,10,10);
            g.fillRect(x-4,y-12,27,17);
            g.setColor(Color.WHITE);
            g.drawString(this.id+"W",x,y);
        }
    }
}
