package simulator;

import javax.swing.*; // необходимо для создания объектов
import java.util.ArrayList;

public class scene {

    public static JFrame mainScene;

    public static final int sceneWidth = 1280; // ширина
    public static final int sceneHeight = 1024; // высота

    public static boolean dragged = false; //если не двигают окно

    public static ArrayList<NodeAttachment> inNodes = new ArrayList<>(); // все входные узлы проводов
    public static ArrayList<NodeAttachment> outNodes = new ArrayList<>(); // все выходные узлы проводов
    public static ArrayList<NodeAttachment> attachedNodes = new ArrayList<>(); // все соединенные узлы проводов
    public static ArrayList<NodeConnector> nodeConnectors = new ArrayList<>(); // все соеденители узлов
    public static ArrayList<Wire> wires = new ArrayList<>(); // все провода

    public static GateComponents firstNodeSelect; // первый выбранный узел соединения проводов

    public scene(){
        mainScene = new JFrame("Симулятор");
        mainScene.setSize(sceneWidth, sceneHeight); // установка размера
        mainScene.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // как происходит закрытие
        mainScene.setLocationRelativeTo(null);
        mainScene.setLayout(null);
        mainScene.setResizable(false); // запрет на изменение размеров окна
    }

    public void setVisibility(boolean visibility){ // установка авидимости
        mainScene.setVisible(visibility);
    }
}
