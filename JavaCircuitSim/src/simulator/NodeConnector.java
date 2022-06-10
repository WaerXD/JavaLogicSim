package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class NodeConnector extends  GateComponents implements MouseListener, MouseMotionListener {

    int rectWidth = 20; // высота и ширина прямоугольника
    int rectHeight = 5;

    int circleWidth = 20;
    int circleHeight = circleWidth; //высота и ширина круга

    public static int offset = 10;

    boolean sameWire = false;
    NodeConnector object = this;

    INOUT type;
    Node parent;
    boolean selected = false;

    private transient ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();


    public NodeConnector(INOUT type, Node parent){
        this.type = type;
        this.parent = parent;

        setSize(rectWidth + circleWidth + offset, circleHeight + offset); //установка размеров коннектора

        enableInputMethods(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setVisible(true);

        scene.nodeConnectors.add(this); //Добавление на сцену

        addActionListener(new ActionListener(){ // добавление слушателя при вызове конструктора

            @Override
            public void actionPerformed(ActionEvent e) {
                if(object.selected){
                    object.setSelected(false);
                    scene.firstNodeSelect = null;
                }
                else{
                    if(object.type == INOUT.INPUT){
                        for(NodeConnector node : scene.nodeConnectors){
                            node.setSelected(false);
                        }

                        object.setSelected(true);
                        scene.firstNodeSelect = object;
                    }
                    else{
                        if(scene.firstNodeSelect !=null){
                            if(object.getConnectedWires().size()<1){
                                Wire wire = new Wire(scene.firstNodeSelect, object);
                                parent.addConnection(wire);
                                if(wire.isActivated()){
                                    parent.setActivated(true);
                                }
                                scene.wires.add(wire);
                                scene.firstNodeSelect = null;

                                for(NodeConnector connect : scene.nodeConnectors){
                                    connect.setSelected(false);
                                }

                                for(NodeAttachment attach : scene.attachedNodes){
                                    attach.setSelected(false);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void paint (Graphics graphics){ // рисовка коннектора
        super.paint(graphics); // рекурсия(?)

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(new Color(74,74,74));

        if(type == INOUT.INPUT) {
            graphics.fillRect(offset / 2, (circleHeight / 2) - (rectHeight / 2) + (offset / 2), rectWidth, rectHeight);

            if (selected) {
                graphics.setColor(Color.green);
            } else {
                graphics.setColor(new Color(74, 74, 74));
            }

            graphics.fillOval(offset / 2 + rectWidth - 2, offset / 2, circleWidth, circleHeight);

        }
        else{
            graphics.setColor(new Color(74,74,74));
            graphics.fillRect(offset/2 + circleWidth -2, (circleHeight/2) - (rectHeight/2) + (offset/2), rectWidth, rectHeight);

            if (selected){
                graphics.setColor(Color.green);
            }
            graphics.fillOval(offset/2,offset/2, circleWidth,circleHeight);
        }
    }



    public void addActionListener(ActionListener listener) {

        listeners.add(listener);

    } // добавляем слушателя, который ждет пока не произойдет событие

    public void notifyListeners(MouseEvent e){
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", e.getWhen(), e.getModifiersEx());
        synchronized (listeners){
            for(int i = 0; i<listeners.size(); i++){
                ActionListener temp = listeners.get(i);
                temp.actionPerformed(event);
            }
        }
    } // уведомление всех слушателей о событии (?)

    public void setSelected(boolean selected) {

        this.selected = selected;
        repaint();

    } // сеттер

    @Override
    public void setActivated(boolean activated){

        this.activated = activated;
        parent.setActivated(activated);
    } // сеттер

    public INOUT getType(){
        return this.type;
    } // геттер

    public void removeAllActionListeners(){
        this.listeners.clear();
    } // удаление всех слушателей

    @Override
    public void mouseClicked(MouseEvent e) {
        notifyListeners(e);

        if (e.getClickCount() == 2){
            if (!parent.isSelectionNode()){

                for(Wire wire : connectedWires){
                    wire.turnWireOff();
                    wire.deleteWire(type,false);
                }

                connectedWires.clear();

                this.setVisible(false);
                scene.mainScene.remove(this);
                this.invalidate();

                this.parent.setVisible(false);
                scene.mainScene.remove(this.parent);
                this.parent.invalidate();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sameWire = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // автоматически скопированный метод
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // автоматически скопированный метод
    }
}
