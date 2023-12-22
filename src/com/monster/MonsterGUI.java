package com.monster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.Border;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/*
* to do: Fixed order of displaying basic traits.
* */
public class MonsterGUI implements ActionListener {
    static final int COLOR = 2;
    JFrame frame;
    InputPanel input;
    DisplayPanel display;
    AlertPanel alert;
    DNDMonster mon;
    JButton submit;
    JButton save;
    JButton load;

    public void start(){
        this.frame.setVisible(true);
        this.submit.doClick();
        this.input.ap.next.doClick();
    }

    public void updateDisplay(){
        this.display.updateDisplay();
    }

    public void showAlert(String str){
        this.alert.showAlert(mon.replaceAbbr(str));
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==submit){
            if(input.nameInput.valid())    mon.name = this.input.nameInput.getInput();
            else    input.nameInput.setText(mon.name);
            if(input.challengeInput.valid())    mon.updateBasicTrait("Challenge Rating", input.challengeInput.getInput());
            else    input.challengeInput.setText(mon.getBasicTrait("Challenge Ratinng")==null ? mon.getBasicTrait("Challenge Rating").getContent() : "");
            if(input.pbInput.valid())       mon.pb = input.pbInput.getInput();
            else    input.pbInput.setText(mon.pb);

            mon.size = input.sizeInput.getInput();
            mon.type = input.typeInput.getInput();

            if(input.hpInput.valid())   mon.hp = input.hpInput.getInput();
            else                        input.hpInput.setText(mon.hp);
            if(input.acInput.valid())   mon.ac = input.acInput.getInput();
            else                        input.acInput.setText(mon.ac);
            if(input.atkInput.valid()){
                mon.setAtkBonus(input.atkInput.getInput());
            }else{
                input.atkInput.setText(mon.getAtkBonus());
            }
            if(input.damageInput.valid())   mon.setDamage(input.damageInput.getInput());
            else                            input.damageInput.setText(mon.getDamage());

            if(input.dcInput.valid())       mon.setDc(input.dcInput.getInput());
            else                            input.dcInput.setText(mon.getDc());

            for(int i = 0; i < 6; i ++){
                if(input.abilityScoreInput[i].valid())  mon.stats[i] = input.abilityScoreInput[i].getInput();
                else    input.abilityScoreInput[i].setText(mon.stats[i]);
            }
            if(!input.angry.levelInput.valid()){
                input.angry.levelInput.setText(mon.encounterLevel);
            }
            input.angry.selectThreat.setSelectedIndex(mon.threatLevel);
            updateDisplay();
        }else if(ae.getSource()==save){
            JFileChooser j1 = new JFileChooser("/Users/mrpentagon/Desktop/MonMan");
            j1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = j1.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected file
                try{
                    mon.store(j1.getSelectedFile());
                    showAlert("Saved " + mon.name.toLowerCase() + " to /" + j1.getSelectedFile().getName());
                }catch(IOException ie){
                    showAlert("Save function didn't work");
                }
            }
        }else if(ae.getSource()==load){
            JFileChooser j1 = new JFileChooser("/Users/mrpentagon/Desktop/MonMan");
            int r = j1.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                try{
                    DNDMonster m = DNDMonster.readMonster(j1.getSelectedFile());
                    this.useMonster(m);
                    
                    showAlert("Loaded data for " + mon.name);
                }catch(Exception e){
                    showAlert("Unsuccessful Load.");
                    e.printStackTrace();
                }
            }
        }
        //System.out.println(mon.atkBonus);
        //System.out.println(mon.getAtkBonus());
    }

    public void setButtonReference(){
        this.submit = this.input.submit;
        this.save = this.input.save;
        this.load = this.input.load;
    }

    public MonsterGUI(){
        Colors.setColors(COLOR);
        frame = new JFrame("Statblock Builder");
        //frame.setLayout(new BoxLayout (frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(775, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mon = new AngryMonster(1, 2);

        input = new InputPanel(mon, this);
        display = new DisplayPanel(this, mon, "parchment");
        alert = new AlertPanel();

        setButtonReference();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, input, display);
        split.setDividerLocation(440);
        //frame.add(input);
        //frame.add(display);
        frame.add(split, BorderLayout.CENTER);
        frame.add(alert, BorderLayout.SOUTH);

        frame.setSize(new Dimension(720, 600));
        display.updateDisplay();

        //input.angry.selectThreat.setSelectedIndex(2);
    }

    public void useMonster(DNDMonster mon){
        this.mon = mon;
        this.input.setMonster(mon);
        this.display.setMonster(mon);
        
        //preset input fields based on monster loaded.
        input.nameInput.setText(mon.name);
        input.challengeInput.setText(mon.getBasicTrait("Challenge Ratinng")==null ? mon.getBasicTrait("Challenge Rating").getContent() : "");
        
		input.pbInput.setText(mon.pb);

        input.sizeInput.setInput(mon.getSize());
        input.typeInput.setInput(mon.getType());

        input.hpInput.setText(mon.hp);
        input.acInput.setText(mon.ac);
        input.atkInput.setText(mon.getAtkBonus());
        input.damageInput.setText(mon.getDamage());
        input.dcInput.setText(mon.getDc());

        for(int i = 0; i < 6; i ++){
            input.abilityScoreInput[i].setText(mon.stats[i]);
        }
        input.angry.levelInput.setText(mon.encounterLevel);
        input.angry.selectThreat.setSelectedIndex(mon.threatLevel);
        
    }


    public static void main(String[] args){

        MonsterGUI window = new MonsterGUI();
        //window.useMonster(new AngryMonster(10, AngryMonster.SOLO));
        window.start();

    }
}

class ComboBoxPanel extends JPanel{
    JLabel label;
    JComboBox<String> selection;

    public ComboBoxPanel(String l, String[] options){
        super();
        this.setLayout(new BoxLayout(this, 0));
        this.setBackground(Colors.smallPanelBg);

        this.label = new JLabel(l + ":");
        label.setFont(new Font("Palatino", 0, 16));

        this.selection = new JComboBox<>(options);
        selection.setMaximumSize(new Dimension(130, 30));
        selection.setBackground(Colors.textFieldBg);

        this.add(label);
        this.add(selection);
    }

    public String getInput(){
        return (String) (this.selection.getSelectedItem());
    }

    public void setInput(String input){
        this.selection.setSelectedItem(input);
    }

    public boolean valid(){
        return true;
    }
}

class LabelFieldPanel extends JPanel{
    JLabel label;
    JTextField text;

    public LabelFieldPanel(String l, int col) {
        super();
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.setBackground(Colors.smallPanelBg);

        this.label = new JLabel(l);
        label.setFont(new Font("Palatino", 0, 16));
        label.setBackground(Colors.smallPanelBg);

        this.text = new JTextField(col);
        text.setFont(new Font("Palatino", 0, 14));
        text.setBackground(Colors.textFieldBg);

        this.add(label);
        this.add(this.text);
    }

    public String getInput(){
        return this.text.getText().trim();
    }

    public void setText(String t){
        this.text.setText(t);
    }

    public boolean valid(){
        if(this.text.getText() == null) return false;
        return this.text.getText().length() > 0;
    }
}

class LabelNumPanel extends JPanel{
    JLabel label;
    JTextField text;

    public LabelNumPanel(String l, int col) {
        super();
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.setBackground(Colors.smallPanelBg);

        this.label = new JLabel(l);
        label.setFont(new Font("Palatino", 0, 16));

        this.text = new JTextField(col);
        text.setFont(new Font("Palatino", 0, 14));
        text.setBackground(Colors.textFieldBg);

        this.add(label);
        this.add(this.text);
    }

    public int getInput(){
        return Integer.parseInt(this.text.getText().trim());
    }

    public void setText(String t){
        this.text.setText(t);
    }

    public void setText(int i){
        this.text.setText("" + i);
    }

    public boolean valid(){
        if(this.text.getText() == null) return false;
        try{
            int i = Integer.parseInt(this.text.getText().trim());
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}

class BasicTraitPanel extends JPanel implements ActionListener{
    private static Color background = Colors.inputBg;
    MonsterGUI gui;
    JLabel label;
    JTextField text;
    JComboBox<String> select;
    JTextField typedTrait;
    JButton update;
    JButton clear;
    JButton delete;

    public BasicTraitPanel(MonsterGUI gui){
        super();
        this.gui = gui;

        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(1000, 100));
        //this.setBackground(background);
        JPanel header = new JPanel();
        header.setBackground(Colors.input2);
        header.setLayout(new BoxLayout(header, 0));
        header.setMinimumSize(new Dimension(10, 32));
        this.add(header, BorderLayout.NORTH);

        this.label = new JLabel("Edit Basic Traits:    ");
        label.setFont(new Font("Palatino", 1, 20));
        label.setMinimumSize(new Dimension(10, 35));
        header.add(label);

        String[] options = {"Other",
                            "Alignment",
                            "Skill",
                            "Speed",
                            "Resistant",
                            "Vulnerable",
                            "Immune",
                            "Equipment",
                            "Languages",

        };

        JPanel selectionPanel = new JPanel();
        //selectionPanel.setBackground(background);
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));
        selectionPanel.setBackground(background);

        JPanel selectionInputGroup = new JPanel();
        //selectionInputGroup.setBackground(background);
        selectionInputGroup.setLayout(new BoxLayout(selectionInputGroup, BoxLayout.Y_AXIS));
        selectionInputGroup.setMaximumSize(new Dimension(160, 75));
        selectionInputGroup.setBackground(background);

        this.select = new JComboBox<>(options);
        select.setMaximumSize(new Dimension(160, 30));
        select.addActionListener(this);
        selectionInputGroup.add(select);

        this.typedTrait = new JTextField(5);
        typedTrait.setFont(new Font("Palatino", 0, 14));
        //typedTrait.setBackground(Colors.textFieldBg);
        selectionInputGroup.add(typedTrait);

        selectionPanel.add(selectionInputGroup);

        text = new JTextField(11);
        text.setFont(new Font("Palatino", 0, 14));
        text.setBackground(Colors.textFieldBg);
        selectionPanel.add(text);

        JPanel buttonPanel = new JPanel();
        //buttonPanel.setBackground(background);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 1));
        buttonPanel.setBackground(Colors.inputBg);

        update = new JButton("Update");
        update.addActionListener(this);
        header.add(update);

        clear = new JButton("Clear");
        clear.addActionListener(this);
        buttonPanel.add(clear);

        delete = new JButton("Delete");
        delete.addActionListener(this);
        header.add(delete);

        selectionPanel.add(buttonPanel);

        this.add(selectionPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
        String t = (String) select.getSelectedItem();
        String traitTitle = t;
        if("Other".equals(t)) traitTitle = typedTrait.getText();
        if (ae.getSource() == update) {
            this.gui.showAlert("Updated [MON]'s " + traitTitle + ".");
            if(t.equals("Alignment"))   this.gui.mon.setAlignment(text.getText());
            else this.gui.mon.updateBasicTrait(traitTitle, text.getText());
        } else if (ae.getSource() == clear) {
            if(text.isValid() || typedTrait.isValid())this.gui.showAlert("Cleared the contents of [MON]'s " + traitTitle + ".");
            typedTrait.setText("");
            text.setText("");
        } else if(ae.getSource() == delete){
            this.gui.showAlert("Deleted [MON]'s " + traitTitle +" trait.");
            this.gui.mon.removeBasicTrait(traitTitle);
        } else if(ae.getSource() == select){
            //ln("Selection changed.");
            if(!"Other".equals(t)){
                typedTrait.setVisible(false);
                EnumTrait bt = this.gui.mon.getBasicTrait(t);
                this.text.setText(bt==null ? "" : bt.getContent());
            }else{
                typedTrait.setVisible(true);
                this.text.setText("");
            }
        }
        this.gui.updateDisplay();
    }

    public String getInput(){
        return this.text.getText().trim();
    }

    public void setText(String t){
        this.text.setText(t);
    }

    public boolean valid(){
        if(this.text.getText() == null) return false;
        return this.text.getText().length() > 0;
    }
}

class ActionPanel extends JPanel implements ActionListener{
    private static Color background = Colors.input3;
    MonsterGUI gui;
    JLabel label;
    JTextArea text;
    JComboBox<String> select;
    JTextArea typedName;
    JButton update;
    JButton clear;
    JButton delete;
    JButton next;
    JButton last;
    int currTrait, currAction;

    public ActionPanel(MonsterGUI gui){
        super();
        currTrait = -1;
        currAction = -1;

        this.gui = gui;
        //biggest panel
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(1050, 180));
        this.setMinimumSize(new Dimension(10, 140));
        this.setBackground(background);

        JPanel header = new JPanel();
        header.setBackground(Colors.input2);
        header.setMinimumSize(new Dimension(10, 32));
        header.setLayout(new BoxLayout(header, 0));

        this.label = new JLabel("Create Ability/Action:");
        label.setFont(new Font("Palatino", 1, 20));
        label.setMinimumSize(new Dimension(10, 35));
        header.add(label);
        this.add(header, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel();
        //selectionPanel.setBackground(background);
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));

        //panel with text inputs: title and content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        selectionPanel.add(textPanel);
        //handles title.
        JPanel typedNamePanel = new JPanel();
        typedNamePanel.setLayout(new FlowLayout());
        typedNamePanel.setBackground(background);
        typedNamePanel.setMinimumSize(new Dimension(260, 27));
        typedNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 27));
        this.typedName = new JTextArea(1,24);
        typedName.setMaximumSize(new Dimension(280, 24));
        typedName.setFont(new Font("Palatino", 0, 14));
        typedName.setLineWrap(true);
        typedName.setWrapStyleWord(true);
        JLabel colon = new JLabel(":");
        colon.setFont(new Font("Palatino", 0, 20));

        typedNamePanel.add(typedName);
        typedNamePanel.add(colon);

        textPanel.add(typedNamePanel);
        //handles text of trait/action
        JPanel actionTextPanel = new JPanel();
        actionTextPanel.setLayout(new FlowLayout());
        actionTextPanel.setBackground(background);
        actionTextPanel.setMinimumSize(new Dimension(280, 160));
        actionTextPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        text = new JTextArea(6, 25);
        text.setFont(new Font("Palatino", 0, 14));
        text.setMaximumSize(new Dimension(300, 130));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Colors.textFieldBg);
        actionTextPanel.add(text);
        textPanel.add(actionTextPanel);

        //panel with buttons and dropdown
        String[] options = {"Action",
                "Trait",
                "Bonus",
                "Reaction",
                "Legendary",
                "Lair",
        };
        JPanel selectionInputGroup = new JPanel();
        selectionInputGroup.setBackground(background);
        selectionInputGroup.setLayout(new BoxLayout(selectionInputGroup, BoxLayout.Y_AXIS));
        selectionInputGroup.setMaximumSize(new Dimension(90, 180));
        selectionInputGroup.setPreferredSize(new Dimension(90, 180));

        this.select = new JComboBox<>(options);
        select.setMaximumSize(new Dimension(100, 30));
        select.addActionListener(this);
        //select.setSelectedIndex(1);

        //creates buttons.
        //JPanel buttonPanel = new JPanel();
        //buttonPanel.setBackground(background);
        //buttonPanel.setLayout(new BoxLayout(buttonPanel, 1));
        //selectionInputGroup.add(buttonPanel);
        update = new JButton("Update");
        update.addActionListener(this);
        selectionInputGroup.add(update);
        //buttonPanel.add(update);

        clear = new JButton(" Clear ");
        clear.addActionListener(this);
        selectionInputGroup.add(clear);
        //buttonPanel.add(clear);

        delete = new JButton("Delete");
        delete.addActionListener(this);
        selectionInputGroup.add(delete);
        //buttonPanel.add(delete);

        next = new JButton(" Next  ");
        next.addActionListener(this);
        selectionInputGroup.add(next);

        last = new JButton("  Last  ");
        last.addActionListener(this);
        selectionInputGroup.add(last);

        //selectionInputGroup.add(select);
        header.add(select);
        selectionPanel.add(selectionInputGroup);
        this.add(selectionPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
        String t = (String) select.getSelectedItem();
        String actionTitle = typedName.getText();
        if (ae.getSource() == update) {
            if(actionTitle.length() < 1)    this.gui.showAlert("Give your action a title first.");
            else {
                this.gui.showAlert("Updated [MON]'s " + t + ": " + actionTitle + ".");
                if (t.equals("Trait")) this.gui.mon.addTrait(new Trait(actionTitle, text.getText()));
                else this.gui.mon.addAction(new Action(actionTitle, t.toLowerCase(), text.getText()));
            }
        } else if (ae.getSource() == clear) {
            typedName.setText("");
            text.setText("");
        } else if(ae.getSource() == delete){
            if(t.equals("Trait")){
            	//System.out.println("|" + t + "|");
            	this.gui.mon.removeTrait(actionTitle);
            }else{
            	this.gui.mon.removeAction(actionTitle);
            }
            this.gui.showAlert("Deleted [MON]'s " + t + ": " + typedName.getText() +".");
        } else if(ae.getSource() == select){
            //System.out.println("Selection changed.");
        } else if (ae.getSource() == next) {
            if(t.equals("Trait")){
                currTrait++;
                if(currTrait >= this.gui.mon.traits.size())  currTrait = 0;
                if(this.gui.mon.traits.size() > 0) {
                    this.typedName.setText(this.gui.mon.traits.get(currTrait).getTitle());
                    this.text.setText(this.gui.mon.traits.get(currTrait).getContent());
                }else{
                    this.gui.showAlert("There are no traits to display.");
                }
            }else{
                currAction++;
                if(currAction >= this.gui.mon.actions.size())   currAction = 0;
                if(this.gui.mon.actions.size() > 0) {
                    this.typedName.setText(this.gui.mon.actions.get(currAction).getTitle());
                    this.text.setText(this.gui.mon.actions.get(currAction).getContent());
                }else{
                    this.gui.showAlert("There are no actions to display.");
                }
            }
        } else if(ae.getSource() == last){
            if(t.equals("Trait")){
                currTrait--;
                if(currTrait < 0)  currTrait = this.gui.mon.traits.size() - 1;
                if(this.gui.mon.traits.size() > 0) {
                    this.typedName.setText(this.gui.mon.traits.get(currTrait).getTitle());
                    this.text.setText(this.gui.mon.traits.get(currTrait).getContent());
                }else{
                    this.gui.showAlert("There are no traits to display.");
                }
            }else{
                currAction--;
                if(currAction < 0)   currAction = this.gui.mon.actions.size() - 1;
                if(this.gui.mon.actions.size() > 0){
                    this.typedName.setText(this.gui.mon.actions.get(currAction).getTitle());
                    this.text.setText(this.gui.mon.actions.get(currAction).getContent());
                }else{
                    this.gui.showAlert("There are no actions to display.");
                }
            }
        }
        this.gui.updateDisplay();
    }

    public String getInput(){
        return this.text.getText().trim();
    }

    public void setText(String t){
        this.text.setText(t);
    }

    public boolean valid(){
        if(this.text.getText() == null) return false;
        return this.text.getText().length() > 0;
    }
}

class AngryPresetPanel extends JPanel implements ActionListener{
    private static Color background = Colors.inputBg;
    MonsterGUI gui;
    JLabel label;
    LabelNumPanel levelInput;
    QualitySelectPanel[] qualityInput;
    JComboBox<String> selectThreat;
    JButton update;
    JButton updateTag;

    public AngryPresetPanel(MonsterGUI gui){
        super();
        this.gui = gui;

        this.setLayout(new BoxLayout(this, 1));
        this.setMaximumSize(new Dimension(1000, 193));
        this.setBackground(background);

        JPanel header = new JPanel();
        header.setBackground(Colors.input2);
        header.setLayout(new BoxLayout(header, 0));
        this.label = new JLabel("Apply Monster Preset:");
        label.setFont(new Font("Palatino", 1, 20));
        label.setMinimumSize(new Dimension(10, 32));
        label.setMaximumSize(new Dimension(4000, 32));
        header.add(label);

        this.add(header);

        String[] options = {"Solo",
                            "Pair",
                            "Party",
                            "Gang",
                            "Mob"
        };

        JPanel selectionPanel = new JPanel();
        selectionPanel.setBackground(background);
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));
        selectionPanel.setMinimumSize(new Dimension(10, 170));

        JPanel selectionInputGroup = new JPanel();
        selectionInputGroup.setBackground(background);
        selectionInputGroup.setLayout(new BoxLayout(selectionInputGroup, BoxLayout.X_AXIS));
        selectionInputGroup.setMinimumSize(new Dimension(10, 170));
        selectionInputGroup.setPreferredSize(new Dimension(100, 170));

        this.levelInput = new LabelNumPanel("Lv", 2);
        levelInput.setMinimumSize(new Dimension(40, 30));
        levelInput.setMaximumSize(new Dimension(100, 70));
        levelInput.setPreferredSize(new Dimension(60, 30));
        selectionInputGroup.add(levelInput);
        selectionPanel.add(selectionInputGroup);

        this.selectThreat = new JComboBox<>(options);
        selectThreat.setMaximumSize(new Dimension(200, 30));
        selectThreat.setSelectedIndex(2);
        selectThreat.addActionListener(this);
        selectThreat.setBackground(Colors.textFieldBg);
        selectionInputGroup.add(selectThreat);

        JPanel qualityPanel = new JPanel();
        qualityPanel.setMinimumSize(new Dimension(170, 500));
        qualityPanel.setLayout(new BoxLayout(qualityPanel, 1));
        qualityPanel.setBackground(background);
        qualityInput = new QualitySelectPanel[5];
        qualityInput[0] = new QualitySelectPanel("AC");
        qualityInput[1] = new QualitySelectPanel("HP");
        qualityInput[2] = new QualitySelectPanel("Atk");
        qualityInput[3] = new QualitySelectPanel("DC");
        qualityInput[4] = new QualitySelectPanel("Dmg");
        for(int i = 0; i < 5; i++){
            qualityPanel.add(qualityInput[i]);
        }
        selectionInputGroup.add(qualityPanel);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(background);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 1));

        update = new JButton("Update Stats");
        update.addActionListener(this);
        buttonPanel.add(update);

        updateTag = new JButton("Update Tag  ");
        updateTag.addActionListener(this);
        buttonPanel.add(updateTag);

        selectionPanel.add(buttonPanel);

        this.add(selectionPanel);
    }

    public void actionPerformed(ActionEvent ae) {
        //updates monster level
        if(ae.getSource()==updateTag || ae.getSource()==update) {
            int lv;
            if (levelInput.valid()) {
                this.gui.mon.encounterLevel = levelInput.getInput();
                lv = levelInput.getInput();
                this.gui.mon.monsterTier = levelInput.getInput() / 3;
            } else {
                levelInput.setText(this.gui.mon.encounterLevel);
                lv = gui.mon.encounterLevel;
            }
            //updates monster threat level
            int threat = selectThreat.getSelectedIndex();
            this.gui.mon.threatLevel = threat;
            this.gui.mon.updateBasicTrait("Challenge Rating", "lv." + gui.mon.monsterTier * 3 + "-" + (gui.mon.monsterTier * 3 + 2) + " " + AngryMonster.threatLevelNames[threat]);
            for (int i = 0; i < 5; i++) {
                gui.mon.quality[i] = qualityInput[i].getInput();
            }
            //updates cr...in main panel
            this.gui.input.challengeInput.setText(gui.mon.getBasicTrait("Challenge Rating").getContent());

            this.gui.showAlert("Updated challenge rating for [MON]");
            if (ae.getSource().equals(update)) {
                this.gui.showAlert("Updated stats for [MON] with preset");
                gui.mon.ac = AngryMonster.calcAC(lv, gui.mon.quality[AngryMonster.AC]);
                gui.mon.hp = AngryMonster.hpTable[lv / 3][gui.mon.quality[AngryMonster.HP]][threat];
                gui.mon.pb = AngryMonster.calcPB(lv);
                gui.mon.setDamage(AngryMonster.dmgTable[lv / 3][gui.mon.quality[AngryMonster.DMG]][threat]);
                //System.out.println(gui.mon.damage);
                gui.mon.setDc(AngryMonster.calcDC(lv, gui.mon.quality[AngryMonster.DC]));
                gui.mon.setAtkBonus(AngryMonster.calcAtk(lv, gui.mon.quality[AngryMonster.ATK]));

                gui.mon.stats[gui.mon.primaryStat] = 11 + (gui.mon.atkBonus - gui.mon.pb) * 2;
                //this.updateBasicTrait("Challenge Rating", "lv." + this.monsterTier*3 + "-" + (this.monsterTier*3 + 2) + " " + threatLevelNames[threatLevel]);

                gui.mon.updateBasicTrait("Atk", "+" + gui.mon.getAtkBonus());
                gui.mon.updateBasicTrait("Damage", "" + gui.mon.getDamage());
                gui.mon.updateBasicTrait("DC", "" + gui.mon.getDc());
            }
            gui.input.hpInput.setText(gui.mon.hp);
            //System.out.println("Hello" + gui.mon.getDamage());
            gui.input.acInput.setText(gui.mon.ac);
            gui.input.atkInput.setText(gui.mon.getAtkBonus());
            gui.input.damageInput.setText(gui.mon.getDamage());
            gui.input.dcInput.setText(gui.mon.getDc());
            gui.updateDisplay();
        }
    }

    class QualitySelectPanel extends JPanel implements ActionListener{
        int quality;
        static String[] displayText = {"poor", "avg.", "good"};
        JLabel label;
        JLabel q;
        JButton change;

        public QualitySelectPanel(String l) {
            super();
            quality = 1;
            this.setBackground(Colors.smallPanelBg);
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            this.setPreferredSize(new Dimension(200, 20));

            this.label = new JLabel(l + ":");
            label.setFont(new Font("Palatino", 0, 16));
            label.setPreferredSize(new Dimension(40, 28));

            this.q = new JLabel("avg.");
            q.setFont(new Font("Palatino", 0, 14));
            q.setPreferredSize(new Dimension(35, 28));

            change = new JButton("+");
            change.addActionListener(this);
            change.setMaximumSize(new Dimension(30, 25));


            this.add(label);
            this.add(q);
            this.add(change);
        }

        public int getInput(){
            return this.quality;
        }

        public void actionPerformed(ActionEvent ae){
            quality = ++quality % 3;
            q.setText(displayText[quality]);
        }
    }
}


class InputPanel extends JPanel{
    public static Color c1 = Colors.input1;
    public static Color c2 = Colors.input2;

    MonsterGUI gui;
    DNDMonster mon;
    static final Font sectionHeader =  new Font("Palatino", Font.BOLD, 18);
    LabelFieldPanel nameInput;
    LabelFieldPanel challengeInput;

    ComboBoxPanel typeInput;
    ComboBoxPanel sizeInput;

    LabelNumPanel hpInput;
    LabelNumPanel acInput;
    LabelNumPanel pbInput;
    LabelNumPanel atkInput;
    LabelNumPanel dcInput;
    LabelNumPanel damageInput;

    LabelNumPanel[] abilityScoreInput;

    BasicTraitPanel bt;
    ActionPanel ap;
    AngryPresetPanel angry;

    JButton submit;
    JButton save;
    JButton load;

    public InputPanel(DNDMonster mon, MonsterGUI gui){
        this.gui = gui;
        this.mon = mon;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //header, with section title and submit button (to do)
        JPanel inputHeader = new JPanel();
        inputHeader.setLayout(new BoxLayout(inputHeader, 0));
        inputHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel sectionName = new JLabel("   Edit Monster   ");
        sectionName.setFont(sectionHeader);
        inputHeader.setBackground(Colors.inputHeaderC);

        save = new JButton("Save");
        load = new JButton("Load");

        save.addActionListener(gui);
        load.addActionListener(gui);

        inputHeader.add(save);
        inputHeader.add(load);
        inputHeader.add(sectionName);

        this.submit = new JButton("Submit");
        submit.addActionListener(gui);
        inputHeader.add(submit);

        this.add(inputHeader);

        //set size for scrollable input section.
        JPanel inputContainer = new JPanel();
        inputContainer.setLayout(new BoxLayout(inputContainer, 1));
        JScrollPane inputPane = new JScrollPane(inputContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputPane.setPreferredSize(new Dimension(400, 290));
        inputPane.setMinimumSize(new Dimension(300, 290));
        this.add(inputPane);

        //row1 has name and cr input
        JPanel row1 = new JPanel();
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row1.setBackground(c1);
        nameInput = new LabelFieldPanel("Name:", 11);
        row1.add(nameInput);

        challengeInput = new LabelFieldPanel("CR:", 7);
        row1.add(challengeInput);
        inputContainer.add(row1);

        pbInput = new LabelNumPanel("PB:", 1);
        row1.add(pbInput);

        //row2 has size, type, and hp
        JPanel row2 = new JPanel();
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row2.setBackground(Colors.input3);
        hpInput = new LabelNumPanel("HP:", 3);
        hpInput.setBackground(Colors.input3);
        String[] sizes = {
                "Tiny",
                "Small",
                "Medium",
                "Large",
                "Huge",
                "Titanic",
                "N/A"
        };
        sizeInput = new ComboBoxPanel("Size", sizes);
        sizeInput.setMaximumSize(new Dimension(95, 100));
        sizeInput.selection.setMaximumSize(new Dimension(100, 50));
        sizeInput.selection.setSelectedIndex(2);
        String[] types = {
                "Aberration",
                "Beast",
                "Celestial",
                "Construct",
                "Dragon",
                "Elemental",
                "Fey",
                "Fiend",
                "Giant",
                "Humanoid",
                "Monstrosity",
                "Ooze",
                "Plant",
                "Undead",
                "Other"
        };
        sizeInput.setBackground(Colors.input3);

        typeInput = new ComboBoxPanel("Type", types);
        typeInput.selection.setSelectedIndex(9);
        typeInput.setBackground(Colors.input3);
        row2.add(sizeInput);
        row2.add(typeInput);
        row2.add(hpInput);

        inputContainer.add(row2);

        //row3 AC, PB, atk, DC
        JPanel row3 = new JPanel();
        row3.setBackground(c1);
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        acInput = new LabelNumPanel("AC:", 2);
        damageInput = new LabelNumPanel("Damage:", 2);
        atkInput = new LabelNumPanel("ATK: +",2);
        dcInput = new LabelNumPanel("DC:", 2);

        row3.add(acInput);
        row3.add(atkInput);
        row3.add(damageInput);
        row3.add(dcInput);

        inputContainer.add(row3);

        //row4 ability scores
        JPanel row4 = new JPanel();
        row4.setBackground(Colors.input3);
        row4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        abilityScoreInput = new LabelNumPanel[6];
        abilityScoreInput[0] = new LabelNumPanel("STR:", 2);
        abilityScoreInput[1] = new LabelNumPanel("DEX:", 2);
        abilityScoreInput[2] = new LabelNumPanel("CON:", 2);
        abilityScoreInput[3] = new LabelNumPanel("WIS:", 2);
        abilityScoreInput[4] = new LabelNumPanel("INT:", 2);
        abilityScoreInput[5] = new LabelNumPanel("CHA:", 2);

        JLabel abilityLabel= new JLabel("Ability");
        abilityLabel.setFont(new Font("Palatino", 1, 20));
        abilityLabel.setMinimumSize(new Dimension(10, 35));
        row4.add(abilityLabel);

        for(int i = 0; i < 3; i++){
            abilityScoreInput[i].setBackground(Colors.input3);
            row4.add(abilityScoreInput[i]);
        }
        inputContainer.add(row4);

        JPanel row5 = new JPanel();
        JLabel score= new JLabel("Scores:");
        score.setFont(new Font("Palatino", 1, 20));
        score.setMinimumSize(new Dimension(10, 35));
        row5.add(score);

        row5.setBackground(Colors.input3);
        row5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        for(int i = 3; i < 6; i++){
            abilityScoreInput[i].setBackground(Colors.input3);
            row5.add(abilityScoreInput[i]);
        }

        inputContainer.add(row5);

        bt = new BasicTraitPanel(gui);
        inputContainer.add(bt);

        ap = new ActionPanel(gui);
        inputContainer.add(ap);

        angry = new AngryPresetPanel(gui);
        inputContainer.add(angry);

        this.setMinimumSize(new Dimension(250, 250));
        this.setPreferredSize(new Dimension(300, 290));
        this.setBackground(Colors.inputBg);
    }

    public void setMonster(DNDMonster mon){
        this.mon = mon;
        updateInputs();
        this.gui.updateDisplay();
    }

    public void updateInputs(){
        return;
    }
}

class DisplayPanel extends JPanel implements ActionListener{
    MonsterGUI gui;
    Color parchment = new Color(255, 240, 205);
    String colorScheme;
    JComboBox<String> colorSelect;
    DNDMonster mon;
    JEditorPane monsterText;
    protected static int blockMinWidth = 60;

    public void setMonster(DNDMonster mon){
        this.mon = mon;
        this.updateDisplay();
    }

    public DisplayPanel(MonsterGUI gui, DNDMonster mon, String colorScheme){
        this.setPreferredSize(new Dimension(200, 290));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(190, 250));
        this.gui = gui;
        this.mon = mon;
        this.colorScheme = colorScheme.length() > 0? colorScheme.toLowerCase() : "parchment";
        String[] options = {"parchment", "dark", "print", "printBox"};

        this.colorSelect = new JComboBox<>(options);
        colorSelect.addActionListener(this);
        colorSelect.setBackground(Colors.textFieldBg);

        JPanel colorSelectPanel = new JPanel();
        colorSelectPanel.setBackground(Colors.input2);
        colorSelectPanel.add(colorSelect);

        monsterText = new JEditorPane();
        monsterText.setContentType("text/html");
        monsterText.setSize(new Dimension(100, 100));
        monsterText.setPreferredSize(new Dimension(100,100));
        monsterText.setMinimumSize(new Dimension(80, 80));
            if(colorScheme.equals("blue")) monsterText.setBackground(new Color(94, 162, 220));
            else if(colorScheme.equals("dark"))    monsterText.setBackground(new Color(51, 76, 102));
            else if(colorScheme.equals("parchment"))    monsterText.setBackground(parchment);

            //Header that says "statblock"
        JPanel sectionHead = new JPanel();
        sectionHead.setBackground(Colors.input2);
        sectionHead.setLayout(new BoxLayout(sectionHead, 0));
        sectionHead.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sectionHead.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
        sectionHead.setBackground(Colors.input2);

        JPanel filler = new JPanel();
        filler.setMinimumSize(new Dimension(55, 10));
        filler.setBackground(Colors.input2);
        sectionHead.add(filler);

        JLabel name = new JLabel("Statblock");
        name.setFont(InputPanel.sectionHeader);

        sectionHead.add(name);
        sectionHead.add(colorSelectPanel);

        this.add(sectionHead);
        this.add(monsterText);
        //JTextArea test  = new JTextArea(100, 100);
    }

    public String htmlHeader(){
        if(colorScheme.equals("blue")) {
            return ("<html style=\"min-width:" + blockMinWidth + "px\">" +
                    "<head><style type=\"text/css\">\n" +
                    "body {  background-color:wheat; " +
                    "padding: 5px 10px 5px 10px;" +
                    "} " +
                    "div {" +
                    "border-style:solid;" +
                    "border-color:black;" +
                    "border-width:2px;" +
                    "padding: 0px 5px 5px 5px;" +
                    "}" +
                    "div div {" +
                    "padding: 3px;" +
                    "margin: 0px 0px -10px 0px;" +
                    "border-width: 1px 2px 1px 2px;" +
                    "} " +
                    "h1 {" +
                    "font-family: Palatino, Georgia, serif;" +
                    "color: #A31712;" +
                    "font-size: 23px;" +
                    "line-height: 1.2em;" +
                    "margin: 5px 0 0 0;" +
                    "padding: 0px" +
                    "letter-spacing: 1px;" +
                    "font-weight: heavy;" +
                    "}\n" +
                    "h3 {" +
                    "color:#FFE80A;" +
                    "font-size:16px;" +
                    "margin: 5px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "h4 {" +
                    "color: #E31712;" +
                    "font-size:12.5px;" +
                    "font-weight: light;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}" +
                    "p {color:black; \n" +
                    "font-size:12.5px;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px 0px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "</style></head>");
        }else if(colorScheme.equals("dark")){
            return "<html style=\"min-width:" + blockMinWidth + "px\">" +
                    "<head><style type=\"text/css\">\n" +
                    "body {" +
                    "padding: 0px 0px 0px 0px;" +
                    "} "+
                    "div {"+
                    "border-style:solid;" +
                    "border-color:black;" +
                    "border-width:2px;" +
                    "padding: 5px 5px 0px 5px;" +
                    "background-color: #341D2F" +
                    "}"+

                    "div div {" +
                    "padding: 3px;" +
                    "margin: 0px 0px -10px 0px;" +
                    "border-width: 1px 2px 1px 2px;" +
                    "} " +

                    "h1 {font-family: Palatino, Georgia, serif;" +
                    "color: #B81E47;" +
                    "font-size: 21px;" +
                    "line-height: 1.2em;" +
                    "margin: 5px 0 0 0;" +
                    "padding: 0px" +
                    "letter-spacing: 1px;" +
                    "font-weight: bold;" +
                    "}\n" +
                    "h3 {" +
                    "color:#B81E47;" +
                    "font-size:16px;" +
                    "margin: 5px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "h4 {" +
                    "color: #A35252;" +
                    "font-size:12.5px;" +
                    "font-weight: light;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}" +

                    "p {color:#A35252; \n" +
                    "font-size:12.5px;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px 0px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "p b{" +
                    "color: #A37A52; " +
                    "}"+
                    "</style></head>";
        }
        else if(colorScheme.equals("parchment")){
            return "<html style=\"min-width:" + blockMinWidth + "px\">" +
                    "<head><style type=\"text/css\">\n" +
                    "body {" +
                    "padding: 0px 0px 0px 0px;" +
                    "} "+
                    "div{"+
                    "padding: 0px 0px 0px 8px;" +
                    "background-color: #FFF0CD" +
                    "border-color: #FFF0CD" +
                    "}"+

                    "div div {" +
                    "padding: 0px;" +
                    "margin: 0px 0px -10px 0px;" +
                    "border-color: #FFF0CD" +
                    "} " +

                    "h1 {font-family: Palatino, Georgia, serif;" +
                    "color: #B81E47;" +
                    "font-size: 21px;" +
                    "line-height: 1.2em;" +
                    "margin: 5px 0 0 0;" +
                    "padding: 0px" +
                    "letter-spacing: 1px;" +
                    "font-weight: bold;" +
                    "}\n" +
                    "h3 {" +
                    "color:#B81E47;" +
                    "font-size:16px;" +
                    "margin: 5px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "h4 {" +
                    "color: #A35252;" +
                    "font-size:12.5px;" +
                    "font-weight: light;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}" +

                    "p {color:#4D0028; \n" +
                    "font-size:12.5px;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px 0px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "p b{" +
                    "color: #330033; " +
                    "}"+
                    "</style></head>";
        } else if(colorScheme.equals("print")){
            return "<html style=\"min-width:" + blockMinWidth + "px\">" +
                    "<head><style type=\"text/css\">\n" +
                    "body {" +
                    "padding: 0px 0px 0px 0px;" +
                    "background-color: white" +
                    "} "+

                    "div{"+
                    "padding: 0px 0px 0px 8px;" +
                    "background-color: #FFFFFF" +
                    "border-style: none;" +
                    "border-color: #FFFFFF" +
                    "}"+

                    "div div{border-color: #FFFFFF}" +

                    "h1 {font-family: Palatino, Georgia, serif;" +
                    "color: #000000;" +
                    "font-size: 21px;" +
                    "line-height: 1.2em;" +
                    "margin: 5px 0 0 0;" +
                    "padding: 0px" +
                    "letter-spacing: 1px;" +
                    "font-weight: bold;" +
                    "}\n" +
                    "h3 {" +
                    "color:#000000;" +
                    "font-size:16px;" +
                    "margin: 5px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "h4 {" +
                    "color: #000000;" +
                    "font-size:12.5px;" +
                    "font-weight: light;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}" +

                    "p {color:#000000; \n" +
                    "font-size:12.5px;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px 0px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "p b{" +
                    "color: #000000; " +
                    "}"+
                    "</style></head>";
        }else if(colorScheme.equals("printbox")) {
            return ("<html style=\"min-width:" + blockMinWidth + "px\">" +
                    "<head><style type=\"text/css\">\n" +
                    "body {  background-color:white; " +
                    "padding: 5px 10px 5px 10px;" +
                    "} " +
                    "div {" +
                    "border-style:solid;" +
                    "border-color:black;" +
                    "border-width:2px;" +
                    "padding: 0px 5px 5px 5px;" +
                    "}" +
                    "div div {" +
                    "padding: 3px;" +
                    "margin: 0px 0px -10px 0px;" +
                    "border-width: 1px 2px 1px 2px;" +
                    "} " +
                    "h1 {" +
                    "font-family: Palatino, Georgia, serif;" +
                    "color: #000000;" +
                    "font-size: 23px;" +
                    "line-height: 1.2em;" +
                    "margin: 5px 0 0 0;" +
                    "padding: 0px" +
                    "letter-spacing: 1px;" +
                    "font-weight: heavy;" +
                    "}\n" +
                    "h3 {" +
                    "color:#000000;" +
                    "font-size:16px;" +
                    "margin: 5px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "h4 {" +
                    "color: #000000;" +
                    "font-size:12.5px;" +
                    "font-weight: light;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px -20px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}" +
                    "p {color:black; \n" +
                    "font-size:12.5px;" +
                    "font-family:  Palatino, Georgia, Times New Roman, serif;" +
                    "margin: 0px 0px 0px 0px;" +
                    "padding: 0px 0px 0px 0px;" +
                    "}\n" +
                    "</style></head>");
        }
        return "<html><head></head>";
    }

    public void actionPerformed(ActionEvent ae){
        this.colorScheme = ((String) colorSelect.getSelectedItem()).toLowerCase();
        if(colorScheme.equals("blue")) monsterText.setBackground(new Color(94, 162, 220));
        else if(colorScheme.equals("dark"))    monsterText.setBackground(new Color(51, 76, 102));
        else if(colorScheme.equals("parchment"))    monsterText.setBackground(parchment);
        else if(colorScheme.contains("print"))      monsterText.setBackground(Color.WHITE);
        gui.showAlert("Changed color scheme to " + colorScheme);
        updateDisplay();
    }
    public void updateDisplay(){
        this.monsterText.setText(this.htmlHeader() + mon.htmlBreakStatblock(true, false, 70));
    }
}
