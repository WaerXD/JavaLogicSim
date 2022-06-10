package simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NodeAttachment extends GateComponents implements MouseListener, MouseMotionListener {

    Gate attachedGate;
    INOUT type;

     int atNodeWidth = 20;
     int atNodeHeight = atNodeWidth;
     int offset = 10;

    boolean A;

    boolean selected = false;

    private transient ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

    public NodeAttachment(Gate gate, INOUT type, boolean A){
        activated = false;
        this.A = A;
        attachedGate = gate;
        this.type = type;

        setSize(atNodeWidth + offset, atNodeHeight + offset);
        enableInputMethods(true);
        addMouseListener(this);
        setFocusable(true);
        setVisible(true);

        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(this.selected){
            graphics.setColor(Color.green);
        }
        else {
            graphics.setColor(Color.black);
        }

        graphics.fillOval(offset/2, offset/2,atNodeWidth, atNodeHeight);

    }



    public void addActionListener(ActionListener listener) {

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

    public void removeAllActionListeners() {

        listeners.clear();

    }

    public void setSelected(boolean selected){
        this.selected = selected;
        repaint();
    }

    public boolean getSelected(){
        return this.selected;
    }

    @Override
    public void setActivated(boolean activated){

     if(attachedGate.getGateVisual() == GatesVisual.NOT){

         attachedGate.inputA = activated;
         attachedGate.calculateOut(attachedGate.inputA,true);
     }
     else{
         if(this.A){
             attachedGate.inputA = activated;
         }
         else{
             attachedGate.inputB = activated;
         }


         attachedGate.calculateOut(attachedGate.inputA, attachedGate.inputB);
     }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2){

            for(Wire wire : attachedGate.getConnectedWires()){
                wire.turnWireOff();
            }

            for(Wire wire : connectedWires){
                wire.deleteWire(type,true);
            }
            connectedWires.clear();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        notifyListeners(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //автоматически скопированный метод
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
        //автоматически скопированный метод
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //автоматически скопированный метод
    }
}
