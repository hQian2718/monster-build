package com.monster;

import java.awt.*;

public class Colors {
    public static Color inputBg = new Color(134, 170, 183);
    public static Color smallPanelBg = inputBg;
    public static Color textFieldBg = new Color(248, 246, 241);
    public static Color input1 = inputBg;
    public static Color input3 = new Color(168, 202, 208);
    public static Color input2 = new Color(78, 162, 204);
    public static Color displayBg = inputBg;
    public static Color inputHeaderC = input2;

    public static void setColors(int i){
        switch(i){
            case 0:
                inputBg = new Color(217, 213, 136);
                input1 = inputBg;
                smallPanelBg = inputBg;
                input3 = new Color(168, 206, 110);
                input2 = new Color(92, 231, 199);
                inputHeaderC = input2;
                break;
            case 1:
                inputBg = new Color(134, 170, 183);
                smallPanelBg = inputBg;
                textFieldBg = new Color(248, 246, 241);
                input1 = inputBg;
                input3 = new Color(168, 202, 208);
                input2 = new Color(78, 162, 204);
                displayBg = inputBg;
                inputHeaderC = input2;
                break;
            case 2:
                inputBg = new Color(160, 179, 182);
                smallPanelBg = inputBg;
                textFieldBg = new Color(248, 246, 241);
                input1 = inputBg;
                input3 = new Color(193, 210, 215);
                input2 = new Color(131, 154, 162);
                displayBg = inputBg;
                inputHeaderC = input2;
                break;
            case 3:
                inputBg = new Color(210, 165, 171);
                smallPanelBg = inputBg;
                textFieldBg = new Color(248, 246, 241);
                input1 = inputBg;
                input3 = new Color(173, 208, 225);
                input2 = new Color(144, 149, 155);
                displayBg = inputBg;
                inputHeaderC = input2;
                break;
            default:
                inputBg = new Color(255, 242, 210);
                input1 = inputBg;
                smallPanelBg = inputBg;
                input3 = new Color(232, 213, 186);
                input2 = new Color(225, 132, 132);
                inputHeaderC = input2;
                break;
        }
    }

    public static void main(String[] args){

    }


    /*
    *
    *
    * */
}
