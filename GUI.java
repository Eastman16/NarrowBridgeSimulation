import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class GUI extends JFrame implements ActionListener{
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JMenuBar menuBar = new JMenuBar();
    JMenu menuOptions = new JMenu("Options");
    JMenuItem menuItemAuthor = new JMenuItem("About author");
    JMenuItem menuItemExit = new JMenuItem("Exit the program");
    JMenuItem menuItemInstruction = new JMenuItem("Instruction");
    private static final String author = "Marcin Sitarz 2640022";
    private static final String title = "Bridge Simulation";
    private static final String instruction = "Program's variants:\n" +
            "1. Unrestricted traffic (vehicles are not stopped before entering the bridge)\n" +
            "2. Two-way traffic, limited number of cars on the bridge (up to 3 only)\n" +
            "3. One-way traffic, no limit on the number of cars\n" +
            "4. Only one car always crosses the bridge at a time";
    static int traffic = 2000;
    Integer[] optionChooser = {1,2,3,4};
    JLabel label_variant = new JLabel("Variant: ");
    JLabel label_trafic_intensity = new JLabel("Traffic intensity: ");
    JLabel label_onTheBridge = new JLabel("On the bridge: ");
    JLabel label_queue = new JLabel("Queue: ");
    JComboBox comboBox = new JComboBox(optionChooser);
    JSlider slider = new JSlider(0, 500, 4500, 1500);
    JTextField field_on_bridge = new JTextField();
    JTextField field_queue = new JTextField();
    JTextArea textArea = new JTextArea();

    GUI() {
        JPanel panel = new JPanel();
        this.setTitle(title);
        this.setDefaultCloseOperation(3);
        this.setSize(500, 700);
        this.add(panel1);
        this.add(panel2);
        this.setJMenuBar(menuBar);
        menuBar.add(menuOptions);
        menuOptions.add(menuItemAuthor);
        menuOptions.add(menuItemInstruction);
        menuOptions.add(menuItemExit);

        field_on_bridge.setEditable(false);
        field_queue.setEditable(false);
        textArea.setEditable(false);

        menuItemAuthor.addActionListener(this);
        menuItemExit.addActionListener(this);
        menuItemInstruction.addActionListener(this);

        panel1.setLayout(new GridLayout(4, 2, 10, 10));
        panel1.add(label_variant);
        panel1.add(comboBox);
        panel1.add(label_trafic_intensity);
        panel1.add(slider);
        panel1.add(label_onTheBridge);
        panel1.add(field_on_bridge);
        panel1.add(label_queue);
        panel1.add(field_queue);
        panel2.setLayout(new GridLayout(1, 1, 10, 10));
        panel2.add(textArea);
        panel.add(panel1);
        panel.add(panel2);

        panel1.setPreferredSize(new Dimension(350, 200));
        panel2.setPreferredSize(new Dimension(450,400));
        label_variant.setHorizontalAlignment(JLabel.CENTER);
        label_trafic_intensity.setHorizontalAlignment(JLabel.CENTER);
        label_onTheBridge.setHorizontalAlignment(JLabel.CENTER);
        label_queue.setHorizontalAlignment(JLabel.CENTER);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(textArea, 22, 30);
        panel2.add(scroll);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                GUI.traffic=slider.getValue();
            }
        });

        new NarrowBridgeAnimated(this);

        this.setContentPane(panel);
        this.setVisible(true);

    }

    List<Bus> allBuses = new LinkedList<Bus>();
    List<Bus> busesWaiting = new LinkedList<Bus>();
    List<Bus> busesOnTheBridge = new LinkedList<Bus>();

    void printBridgeInfo(Bus bus, String message){
        textArea.insert("Bus["+bus.id+"->"+bus.dir+"]" +message+"\n",0);

        Iterator i = busesWaiting.iterator();
        StringBuilder sb = new StringBuilder();

        Bus b;
        while(i.hasNext()) {
            b = (Bus)i.next();
            sb.append(b.id);
            sb.append(" ");
        }

        field_queue.setText(sb.toString());
        sb = new StringBuilder();
        i = busesOnTheBridge.iterator();

        while(i.hasNext()) {
            b = (Bus)i.next();
            sb.append(b.id);
            sb.append(" ");
        }

        field_on_bridge.setText(sb.toString());
    }

    //determinating the way of entering the bridge
    synchronized void getOnTheBridge(Bus bus){

        switch((int)comboBox.getSelectedItem()){
            case 1:

                for(Bus busWaiting: busesWaiting){
                    busWaiting.busMovement=BusMovement.RIDING_ON_THE_BRIDGE;
                }
                busesWaiting.clear();
                busesOnTheBridge.add(bus);
                printBridgeInfo(bus,"Entering the bridge");
                repaint();
                break;

            case 2:
                while(busesOnTheBridge.size()>=3){
                    busesWaiting.add(bus);
                    bus.busMovement = BusMovement.WAITING_BEFORE_ENTERING;
                    printBridgeInfo(bus, "Waiting to enter");
                    try{
                        wait();
                    } catch (InterruptedException e){}
                    busesWaiting.remove(bus);

                }
                busesOnTheBridge.add(bus);
                bus.busMovement = BusMovement.RIDING_ON_THE_BRIDGE;
                printBridgeInfo(bus,"Entering the bridge");
                repaint();
                break;
            case 3:
                Random rand = new Random();
                int direction = rand.nextInt(2);
                //EAST = 0
                //WEST = 1
                if(direction==0 && bus.dir.toString().equals("E")) {
                    busesOnTheBridge.add(bus);
                    printBridgeInfo(bus,"Entering the bridge");
                }
                else if(direction==1 && bus.dir.toString().equals("W")){
                    busesOnTheBridge.add(bus);
                    printBridgeInfo(bus,"Entering the bridge");
                }
                else{
                    bus.busMovement = BusMovement.WAITING_BEFORE_ENTERING;
                    busesWaiting.add(bus);
                    printBridgeInfo(bus, "Waiting to enter");
                    try{
                        wait();
                    } catch (InterruptedException e){}
                    busesWaiting.remove(bus);
                    busesOnTheBridge.add(bus);
                    bus.busMovement = BusMovement.RIDING_ON_THE_BRIDGE;
                    printBridgeInfo(bus, "Entering the bridge");
                }
                repaint();
                break;
            case 4:
                while(!busesOnTheBridge.isEmpty()){
                    busesWaiting.add(bus);
                    bus.busMovement = BusMovement.WAITING_BEFORE_ENTERING;
                    printBridgeInfo(bus, "Waiting to enter");
                    try{
                        wait();
                    } catch (InterruptedException e){}
                    busesWaiting.remove(bus);
                }
                busesOnTheBridge.add(bus);
                bus.busMovement = BusMovement.RIDING_ON_THE_BRIDGE;
                printBridgeInfo(bus,"Entering the bridge");
                repaint();
                break;
        }
    }
    //singnalizing the end of the ride on the bridge
    synchronized void getOffTheBridge(Bus bus){
        busesOnTheBridge.remove(bus);
        printBridgeInfo(bus, "Leaving the bridge");
        notify();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==menuItemAuthor){
            JOptionPane.showMessageDialog(this,author,"About author",JOptionPane.INFORMATION_MESSAGE);
        }
        if(e.getSource()==menuItemInstruction){
            JOptionPane.showMessageDialog(this,instruction,"Instruction",JOptionPane.INFORMATION_MESSAGE);
        }
        if(e.getSource()==menuItemExit){
            System.exit(0);
        }
    }
}
