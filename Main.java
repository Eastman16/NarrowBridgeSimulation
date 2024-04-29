public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();

        while (true) {
            Bus bus = new Bus(gui);
            (new Thread(bus)).start();
            try {
                Thread.sleep((5000 - gui.traffic));
            } catch (InterruptedException e) {
            }
        }
    }
}
