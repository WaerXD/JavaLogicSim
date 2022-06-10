package simulator;

import java.awt.*;

import javax.swing.JComponent;

public class Wire extends JComponent {
    // т.е узлы Узлов(да да) между которыми соединяются провода
    GateComponents node1; // узел 1
    GateComponents node2; // узел 2


    boolean activated;

    public Wire(GateComponents node1, GateComponents node2){

        setSize(scene.sceneWidth, scene.sceneHeight);
        setVisible(true);
        scene.mainScene.add(this);

        node1.addConnection(this);
        node2.addConnection(this);

        this.node1 = node1;
        this.node2 = node2;

        activated = node1.isActivated();
        repaint();
    }

    public boolean isActivated(){
        return this.activated;
    } // активирован ли провод

    public void turnWireOn() {
        this.activated = true;
        node2.setActivated(true);
        repaint();

    } // включение провода

    public void turnWireOff() {
        this.activated = false;
        node2.setActivated(false);
        repaint();

    } // выключение провода

    public void deleteWire(INOUT type, boolean gateNode){
        if(gateNode){
            if(type == INOUT.OUTPUT){
                if(node2.getClass() == NodeConnector.class){
                    NodeConnector node = (NodeConnector) node2;
                    node.parent.connectedWires.remove(this);
                    node.connectedWires.remove(this);
                }
                else{
                    node2.connectedWires.remove(this);
                }


            }
            else{
                node1.connectedWires.remove(this);
            }
        }

        scene.mainScene.remove(this);
        scene.mainScene.repaint();
    } // удаление провода

    public GateComponents getNode2(){
        return this.node2;
    }

    @Override
    public void paint(Graphics g){
        if(!scene.dragged) {

            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(3));

            if(activated) {
                g2d.setColor(new Color(217, 0, 47));
            } else {
                g2d.setColor(Color.black);
            } // задает цвет проводу

            int outwards = 50;

            if((Math.abs(node1.getY() - node2.getY()) < 25) && node2.getX() > node1.getX()) {

                g2d.drawLine(node1.getX() + node1.getWidth() / 2, node1.getY() + node1.getHeight() / 2, node2.getX() + node2.getWidth() / 2, node2.getY() + node2.getHeight() / 2);

            } else if(node2.getX() > node1.getX() && node2.getY() > node1.getY() || node2.getX() > node1.getX() && node2.getY() < node1.getY()) {

                g2d.drawLine(node1.getX() + node1.getWidth() / 2, node1.getY() + node1.getHeight() / 2, (node1.getX() + node1.getWidth() / 2) + ((node2.getX() + node2.getWidth() / 2) - (node1.getX() + node1.getWidth() / 2)) / 2, node1.getY() + node1.getHeight() / 2);
                g2d.drawLine((node1.getX() + node1.getWidth() / 2) + ((node2.getX() + node2.getWidth() / 2) - (node1.getX() + node1.getWidth() / 2)) / 2, node1.getY() + node1.getHeight() / 2, (node1.getX() + node1.getWidth() / 2) + ((node2.getX() + node2.getWidth() / 2) - (node1.getX() + node1.getWidth() / 2)) / 2, node2.getY() + node2.getHeight() / 2);
                g2d.drawLine((node1.getX() + node1.getWidth() / 2) + ((node2.getX() + node2.getWidth() / 2) - (node1.getX() + node1.getWidth() / 2)) / 2, node2.getY() + node2.getHeight() / 2, node2.getX() + node2.getWidth() / 2, node2.getY() + node2.getHeight() / 2);

            } else if (node2.getX() - node1.getX() < 0 && node2.getY() > node1.getY()) {

                g2d.drawLine(node1.getX() + node1.getWidth() / 2, node1.getY() + node1.getHeight() / 2, node1.getX() + node1.getWidth() / 2 + outwards, node1.getY() + node1.getHeight() / 2);
                g2d.drawLine(node1.getX() + node1.getWidth() / 2 + outwards, node1.getY() + node1.getHeight() / 2, node1.getX() + node1.getWidth() / 2 + outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node1.getHeight())) / 2);
                g2d.drawLine(node1.getX() + node1.getWidth() / 2 + outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node1.getHeight())) / 2, node2.getX() + node2.getWidth() / 2 - outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node1.getHeight())) / 2);
                g2d.drawLine(node2.getX() + node2.getWidth() / 2 - outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node1.getHeight())) / 2, node2.getX() + node2.getWidth() / 2 - outwards, node2.getY() + node2.getHeight() / 2);
                g2d.drawLine(node2.getX() + node2.getWidth() / 2 - outwards, node2.getY() + node2.getHeight() / 2, node2.getX() + node2.getWidth() / 2, node2.getY() + node2.getHeight() / 2);

            } else if(node2.getX() - node1.getX() < 0 && node2.getY() < node1.getY()) {

                g2d.drawLine(node1.getX() + node1.getWidth() / 2, node1.getY() + node1.getHeight() / 2, node1.getX() + node1.getWidth() / 2 + outwards, node1.getY() + node1.getHeight() / 2);
                g2d.drawLine(node1.getX() + node1.getWidth() / 2 + outwards, node1.getY() + node1.getHeight() / 2, node1.getX() + node1.getWidth() / 2 + outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node2.getHeight())) / 2);
                g2d.drawLine(node1.getX() + node1.getWidth() / 2 + outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node2.getHeight())) / 2, node2.getX() + node2.getWidth() / 2 - outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node2.getHeight())) / 2);
                g2d.drawLine(node2.getX() + node2.getWidth() / 2 - outwards, ((node1.getY() + node1.getHeight() / 2) + (node2.getY() + node2.getHeight())) / 2, node2.getX() + node2.getWidth() / 2 - outwards, node2.getY() + node2.getWidth() / 2);
                g2d.drawLine(node2.getX() + node2.getWidth() / 2 - outwards, node2.getY() + node2.getWidth() / 2, node2.getX() + node2.getWidth() / 2, node2.getY() + node2.getHeight() / 2);

            }

        }
    } // ОТРИСОВКА ПРОВОДА
}
