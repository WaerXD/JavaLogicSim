package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Node extends GateComponents implements MouseListener, MouseMotionListener {
    INOUT type;

    final int nodeWidth = 40;
    final int nodeHeight = nodeWidth;
    final int offset = 10;

    volatile int screenX = 0;
    volatile int screenY = 0;
    volatile int inFrameX = 0;
    volatile int inFrameY = 0;

    Node object = this;
    NodeConnector connectorNode;

    boolean draggable = true;
    boolean selectionNode = false;

    private transient ArrayList<ActionListener> listeners = new ArrayList<>();

    public Node(INOUT type){

        this.type = type;
        activated = false;

        setSize(nodeWidth + offset, nodeHeight + offset);

        enableInputMethods(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setVisible(true);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activated = !activated;
                object.setActivated(activated);
                connectorNode.setActivated(activated);

                ArrayList<Wire> childConnectedWires = connectorNode.getConnectedWires();

                for(Wire wire : childConnectedWires){

                    if(activated){
                        wire.turnWireOn();
                    }
                    else{
                        wire.turnWireOff();
                    }
                }
            }
        });

        repaint();

        connectorNode = new NodeConnector(type, this);
        scene.mainScene.add(connectorNode);
        scene.mainScene.add(this);


    }

    @Override
    public void setLocation(int x, int y){
        super.setLocation(x,y);

        if(type == INOUT.INPUT){
            connectorNode.setLocation(this.getX() + nodeWidth, this.getY() + nodeWidth - (connectorNode.getWidth()/2) - (NodeConnector.offset / 2));
        }
        else {
            connectorNode.setLocation(this.getX() - connectorNode.getWidth() + NodeConnector.offset + 3, this.getY() + nodeWidth - (connectorNode.getWidth() / 2) - (NodeConnector.offset / 2));
        }
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);

        Graphics2D graphics2D =(Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(this.isActivated()){
            graphics.setColor(Color.green);
        }
        else{
            graphics.setColor(new Color(74,74,74));
        }

        graphics.fillOval(offset/2,offset/2,nodeWidth,nodeHeight);
    }//рисковка узла

    public void addActionListener(ActionListener listener) {

        listeners.add(listener);

    }

    public void notifyListeners(MouseEvent e) {

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", e.getWhen(), e.getModifiersEx());
        synchronized(listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                ActionListener temp = listeners.get(i);
                temp.actionPerformed(event);
            }

        }

    }

    public void setSelectionNode(){
        this.draggable = false;
        this.selectionNode = true;
        this.listeners.clear();
        this.connectorNode.removeAllActionListeners();
    }

    public boolean isSelectionNode(){
        return this.selectionNode;
    }

    public NodeConnector getConnectorNode(){
        return this.connectorNode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!this.selectionNode){
            if(type == INOUT.INPUT){
                notifyListeners(e);
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        inFrameX = getX();
        inFrameY = getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.selectionNode){
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
        //автоматически скопированный метод
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //автоматически скопированный метод
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(draggable){
            int dX = e.getXOnScreen()-screenX;
            int dY = e.getYOnScreen()-screenY;

            setLocation(inFrameX + dX, inFrameY + dY);
            scene.dragged = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //автоматически скопированный метод
    }
}
