package com.monster;

import javax.swing.*;
import java.awt.*;

public class AlertPanel extends JPanel {
    JLabel message;
    public AlertPanel(){
        super();
        this.message = new JLabel("Create your monster!");
        this.add(message, BorderLayout.WEST);
    }

    public void showAlert(String str){
        this.message.setText(str);
    }
}
