package gatesInterface;
import javax.swing.*;
import simulator.scene;

public class Launcher {

    public static JFrame scene;

    public static void main(String[] args) {

        scene sceneObject = new scene(); // создание объекта сцены
        scene = simulator.scene.mainScene;
        gateSpawner gateSpawn = new gateSpawner(); // создание спавнера(выбора) узлов
        gateSpawn.setLocation(0,0);// где будет находиться спавнер по координатам
        sceneObject.setVisibility(true);// установка видимости сцены

    }
}
