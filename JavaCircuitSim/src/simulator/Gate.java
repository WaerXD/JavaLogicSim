package simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Gate extends GateComponents implements MouseListener, MouseMotionListener {

    private  GatesVisual gateVisual;

    int gateWidth = 90;
    int gateHeight = 55;

    boolean draggable = true; // можно ли перемещать или нет

    AffineTransform affineTransform = new AffineTransform();
    transient FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
    Font textFont = new Font("Comic Sans", Font.BOLD,20);

    int textWidth;
    int textHeight;

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int compX = 0;
    private volatile int compY = 0;

    boolean selectionGate = false;

    Point prevPoint;
    Point compCorner;

    NodeAttachment input1;
    NodeAttachment input2;
    NodeAttachment output;
    NodeAttachment NOTInput; //???

    public boolean inputA = false;
    public boolean inputB = false;

    private transient ArrayList<ActionListener> listeners = new ArrayList<>();

    public Gate(GatesVisual gate){
        compCorner = new Point(0,0);
        setSize(gateWidth, gateHeight);

        enableInputMethods(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setVisible(true);

        this.gateVisual = gate;

        repaint();

        if(this.gateVisual == GatesVisual.NOT){
            NOTInput = new NodeAttachment(this, INOUT.INPUT,false);
            scene.inNodes.add(NOTInput);
            scene.attachedNodes.add(NOTInput);

            NOTInput.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (NOTInput.getSelected()){
                        NOTInput.setSelected(false);
                    }
                    else {
                        for (NodeAttachment node : scene.inNodes){
                            node.setSelected(false);
                        }

                        if (scene.firstNodeSelect != null){

                            if(NOTInput.getConnectedWires().size() < 1){
                                NOTInput.setSelected(true);
                                Wire wire = new Wire(scene.firstNodeSelect, NOTInput);
                                scene.wires.add(wire);
                                scene.firstNodeSelect.addConnection(wire);
                                scene.firstNodeSelect = null;

                                for(int i = 0; i < scene.attachedNodes.size(); i++){
                                    scene.attachedNodes.get(i).setSelected(false);
                                }

                                for(NodeConnector nodes : scene.nodeConnectors){
                                    nodes.setSelected(false);
                                }

                                inputA = NOTInput.getConnectedWires().get(0).isActivated();
                                calculateOut(inputA,true);
                            }
                        }
                    }
                }
            });

            scene.mainScene.add(NOTInput);
        } else {
            input1 = new NodeAttachment(this, INOUT.INPUT, true);
            scene.inNodes.add(input1);
            scene.attachedNodes.add(input1);

            input1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (scene.firstNodeSelect != null) {
                        if (input1.getConnectedWires().size() < 1) {
                            Wire wire = new Wire(scene.firstNodeSelect, input1);
                            scene.wires.add(wire);
                            scene.firstNodeSelect = null;

                            for (int i = 0; i < scene.attachedNodes.size(); i++) {
                                scene.attachedNodes.get(i).setSelected(false);
                            }
                            for (NodeConnector nodes : scene.nodeConnectors) {
                                nodes.setSelected(false);
                            }
                            ArrayList<Wire> thisIConnectedWires = input1.getConnectedWires();
                            ArrayList<Wire> otherIConnectedWires = input2.getConnectedWires();
                            if (otherIConnectedWires.size() != 0) {
                                inputA = thisIConnectedWires.get(0).isActivated();
                                inputB = otherIConnectedWires.get(0).isActivated();
                                calculateOut(inputA, inputB);
                            }
                        }
                    }
                }
            });
            input2 = new NodeAttachment(this, INOUT.INPUT, false);
            scene.inNodes.add(input2);
            scene.attachedNodes.add(input2);
            input2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (scene.firstNodeSelect != null) {
                        if (input2.getConnectedWires().size() < 1) {
                            scene.wires.add(new Wire(scene.firstNodeSelect, input2));
                            scene.firstNodeSelect = null;
                            for (JComponent node2 : scene.attachedNodes) {
                                ((NodeAttachment) node2).setSelected(false);
                            }
                            for (NodeConnector nodes : scene.nodeConnectors) {
                                nodes.setSelected(false);
                            }
                            ArrayList<Wire> thisIConnectedWires = input2.getConnectedWires();
                            ArrayList<Wire> otherIConnectedWires = input1.getConnectedWires();
                            if (otherIConnectedWires.size() != 0) {
                                inputA = thisIConnectedWires.get(0).isActivated();
                                inputB = otherIConnectedWires.get(0).isActivated();
                                calculateOut(inputA, inputB);
                            }
                        }
                    }
                }
            });
            scene.mainScene.add(input1);
            scene.mainScene.add(input2);
        }
        output = new NodeAttachment(this, INOUT.INPUT, false);
        scene.outNodes.add(output);
        scene.attachedNodes.add(output);

        output.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (output.getSelected()) {
                    output.setSelected(false);
                    scene.firstNodeSelect = null;
                } else {
                    for (NodeAttachment node : scene.outNodes) {
                        node.setSelected(false);
                    }
                    output.setSelected(true);
                    scene.firstNodeSelect = output;
                }
            }
        });
        scene.mainScene.add(output);
        scene.mainScene.add(this);
    }
    @Override
    public void paint (Graphics graphics){
        super.paint(graphics);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(gateVisual.getColor());
        graphics.fillRoundRect(0, 0, gateWidth, gateHeight, 15, 15);

        graphics.setColor(Color.white);
        graphics.setFont(textFont);

        String label = gateVisual.toString();
        textWidth = (int) (textFont.getStringBounds(label, fontRenderContext).getWidth());
        textHeight = (int) (textFont.getStringBounds(label, fontRenderContext).getHeight());

        graphics.drawString(label, gateWidth / 2 -  (textWidth / 2), gateHeight / 2 + (textHeight / 2) - 3);
    }

    public void calculateOut(boolean A, boolean B){
        switch (this.gateVisual){
            case NOT:
                if(A){
                    this.activated = false;
                    output.activated = false;
                }
                else{
                    this.activated = true;
                    output.activated = true;
                }
                break;

            case AND:
                if(A && B) {

                    this.activated = true;
                    output.activated = true;

                } else {

                    this.activated = false;
                    output.activated = false;

                }
                break;
            case OR:
                if(A || B) {

                    this.activated = true;
                    output.activated = true;

                } else {

                    this.activated = false;
                    output.activated = false;

                }
                break;
            case XOR:
                if(A && B || !A && !B) {

                    this.activated = false;
                    output.activated = false;

                } else {

                    this.activated = true;
                    output.activated = true;

                }
                break;
        }

        sendOutput();
    }

    public void sendOutput(){
        ArrayList<Wire> outConnectedWires = output.getConnectedWires();

        for(Wire wire : outConnectedWires){
            if(this.activated){
                wire.turnWireOn();
            }
            else{
                wire.turnWireOff();
            }
        }
    }

    @Override
    public void setLocation(int x, int y) {

        super.setLocation(x, y);

        if(this.gateVisual == GatesVisual.NOT) {

            NOTInput.setLocation(this.getX() - (NOTInput.getWidth() / 2), this.getY() + (this.getHeight() / 2) - (output.getHeight() / 2));
            output.setLocation(this.getX() + this.getWidth() - (output.getWidth() / 2), this.getY() + (this.getHeight() / 2) - (output.getHeight() / 2));

        } else {

            input1.setLocation(this.getX() - (input1.getWidth() / 2), this.getY() + (this.getHeight() / 4) - (input1.getHeight() / 2));
            input2.setLocation(this.getX() - (input2.getWidth() / 2), this.getY() + this.getHeight() - (this.getHeight() / 4) - (input2.getHeight() / 2));
            output.setLocation(this.getX() + this.getWidth() - (output.getWidth() / 2), this.getY() + (this.getHeight() / 2) - (output.getHeight() / 2));

        }

    }

    public void setSelectionGate(){
        this.draggable = false;
        this.selectionGate = true;
        this.listeners.clear();

        if(this.gateVisual == GatesVisual.NOT){
            NOTInput.removeAllActionListeners();
        }
        else{
            input1.removeAllActionListeners();
            input2.removeAllActionListeners();
        }
    }

    public GatesVisual getGateVisual(){
        return this.gateVisual;
    }
    
    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    public void notifyListeners(MouseEvent e) {

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", e.getWhen(), e.getModifiersEx());
        synchronized(listeners) {

            for(int i = 0; i < listeners.size(); i++) {

                ActionListener temp = listeners.get(i);
                temp.actionPerformed(event);

            }

        }

    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {

            if (this.gateVisual == GatesVisual.NOT) {

                NOTInput.mouseClicked(e);
                NOTInput.setVisible(false);
                scene.mainScene.remove(NOTInput);
                NOTInput.invalidate();

            } else {

                input1.mouseClicked(e);
                input1.setVisible(false);
                scene.mainScene.remove(input1);
                input1.invalidate();

                input2.mouseClicked(e);
                input2.setVisible(false);
                scene.mainScene.remove(input2);
                input2.invalidate();

            }

            output.mouseClicked(e);
            output.setVisible(false);
            scene.mainScene.remove(output);
            output.invalidate();

            this.setVisible(false);
            scene.mainScene.remove(this);
            this.invalidate();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        compX = getX();
        compY = getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.selectionGate){
            notifyListeners(e);
        }
        else{
            scene.dragged = false;
            for(Wire wire : scene.wires){
                wire.repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // автоматический скопированный метод
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // автоматический скопированный метод
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(draggable){
            int dX = e.getXOnScreen() - screenX;
            int dY = e.getYOnScreen() - screenY;

            setLocation(compX + dX, compY + dY);

            scene.dragged = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    // автоматичеккий скопированный метод
    }
}
