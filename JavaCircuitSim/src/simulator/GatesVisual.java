package simulator;

import java.awt.Color; // цвета узлов

public enum GatesVisual { // Тип и цвет узла
    AND(new Color(0,0,204)),
    OR(new Color(0,204,204)),
    XOR(new Color(255,51,51)),
    NOT(new Color(178,102,255))
    ;

    private final Color color;

    GatesVisual(Color color){
        this.color = color;
    } // установка цвета

    public Color getColor(){
        return this.color;
    } // геттер цвета
}
