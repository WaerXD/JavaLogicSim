package simulator;

import javax.swing.*;
import java.util.ArrayList;

public class GateComponents extends JComponent {
    ArrayList<Wire> connectedWires = new ArrayList<>(); // массив соединенных проводов

    boolean activated;

    public ArrayList<Wire> getConnectedWires(){ // возвращает соединенные провода
        return this.connectedWires;
    }

    public void addConnection(Wire wire){ // добавляет соединение проводов
        connectedWires.add(wire);
    }

    public void setActivated(boolean activated){ // активирует провода и закрашивает их
        this.activated = activated;
        this.repaint();
    }

    public boolean isActivated(){ // возвращает активирован провод или нет
        return this.activated;
    }
}
