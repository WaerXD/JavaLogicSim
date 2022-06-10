package gatesInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import simulator.Gate;
import simulator.GatesVisual;
import simulator.INOUT;
import simulator.Node;

public class gateSpawner extends JComponent implements MouseListener {

    JFrame scene = Launcher.scene;

    int gateSpawnerWidth = 150;
    int gateSpawnerHeight = scene.getHeight();

    public gateSpawner(){
        setSize(gateSpawnerWidth, gateSpawnerHeight);
        enableInputMethods(true);
        addMouseListener(this);

        setFocusable(true);
        setVisible(true);

        GatesVisual[] type = GatesVisual.values();

        for(int i = 0; i< type.length; i++){
            Gate gate = new Gate(type[i]);
            gate.setLocation(gateSpawnerWidth/2 - gate.getWidth()/2, 20+(85*i));
            gate.setSelectionGate();

            int k=i;
            gate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Gate addGate = new Gate(type[k]);
                    addGate.setLocation((scene.getWidth()/2) - (addGate.getWidth()/2)+(gateSpawnerWidth/2), (scene.getHeight()/2) - (addGate.getHeight()));
                }
            });


        }

        Node inNode = new Node(INOUT.INPUT);
        inNode.setLocation(gateSpawnerWidth/2 - inNode.getWidth()/2 - inNode.getConnectorNode().getWidth()/2 + 5, 20 +(85 * type.length -1 ));
        inNode.setSelectionNode();
        inNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node addNode = new Node(INOUT.INPUT);
                addNode.setLocation((scene.getWidth()/2) - (addNode.getWidth()/2)+(gateSpawnerWidth/2), (scene.getHeight()/2) - (addNode.getHeight()));
            }
        });

        Node outNode = new Node(INOUT.OUTPUT);
        outNode.setLocation(gateSpawnerWidth / 2 - outNode.getWidth() / 2 + outNode.getConnectorNode().getWidth() / 2 - 5, 20 + (85 * type.length - 1) + 80);
        outNode.setSelectionNode();
        outNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Node addedNode = new Node(INOUT.OUTPUT);
                addedNode.setLocation((scene.getWidth() / 2) - (addedNode.getWidth() / 2) + (gateSpawnerWidth / 2), (scene.getHeight() / 2) - (addedNode.getHeight()));

            }

        });

        repaint();
        scene.add(this);

    }
    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.gray);
        graphics.fillRect(0,0,gateSpawnerWidth,gateSpawnerHeight);
    }



// автоматически скопированные методы на нажатие, удержание, отпускание, наведение и уведение указателя мыши
    @Override
    public void mouseClicked(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // автоматически скопированный метод
    }


}
