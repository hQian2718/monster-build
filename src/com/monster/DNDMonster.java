package com.monster;

import java.io.*;
import java.util.ArrayList;
import com.monster.AngryMonster;

public class DNDMonster implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 7833945;
    public static final boolean MOD = true;
    public static final int STR = 0;
    public static final int DEX = 1;
    public static final int CON = 2;
    public static final int WIS = 3;
    public static final int INT = 4;
    public static final int CHA = 5;

    String name;
    String size;
    String type;
    String alignment;
    String abbr;

    int pb;
    int ac;
    int hp;
    int[] stats;
    boolean[] saveProf;
    ArrayList<EnumTrait> basicTraits;
    ArrayList<Trait> traits;
    ArrayList<Action> actions;

    int encounterLevel;
    int monsterTier;
    int threatLevel;
    int primaryStat;

    int[] quality = {1, 1, 1, 1, 1};

    int damage;
    int dc;
    int atkBonus;
    //full constructor
    public DNDMonster(String name, String size, String type, String alignment, String abbr, int pb, int ac, int hp, int[] stats, boolean[] saveProf, ArrayList<String> skills, String equipment, ArrayList<Trait> traits, ArrayList<Action> actions) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.alignment = alignment;
        this.abbr = abbr.toLowerCase();
        this.pb = pb;
        this.ac = Math.max(ac, 0);
        this.hp = Math.max(hp, 0);

        if(stats.length == 6)    this.stats = stats;
        else                     this.stats = new int[]{10, 10, 10, 10, 10, 10};
        if(saveProf.length == 6)    this.saveProf = saveProf;
        else                        this.saveProf = new boolean[]{false, false, false, false, false, false};
        this.basicTraits = new ArrayList<>();
        basicTraits.add(new EnumTrait("skill", skills));
        basicTraits.add(new EnumTrait("equipment", equipment));
        this.traits = traits;
        this.actions = actions;
    }
    //full constructor, skills as a list separated by commas
    public DNDMonster(String name, String size, String type, String alignment, String abbr, int pb, int ac, int hp, int[] stats, boolean[] saveProf, String skills, String equipment, ArrayList<Trait> traits, ArrayList<Action> actions) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.alignment = alignment;
        this.abbr = abbr.toLowerCase();
        this.pb = pb;
        this.ac = Math.max(ac, 0);
        this.hp = Math.max(hp, 0);

        if(stats.length == 6)    this.stats = stats;
        else                     this.stats = new int[]{10, 10, 10, 10, 10, 10};
        if(saveProf.length == 6)    this.saveProf = saveProf;
        else                        this.saveProf = new boolean[]{false, false, false, false, false, false};
        this.basicTraits = new ArrayList<>();
        basicTraits.add(new EnumTrait("skill", skills.split(", ")));
        basicTraits.add(new EnumTrait("equipment", equipment));
        this.traits = traits;
        this.actions = actions;
    }
    //constructor that provides input up to stats and save profs.
    public DNDMonster(String name, String size, String type, String alignment, String abbr, int pb, int ac, int hp, int[] stats, boolean[] saveProf) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.alignment = alignment;
        this.abbr = abbr.toLowerCase();
        this.pb = pb;
        this.ac = Math.max(ac, 0);
        this.hp = Math.max(hp, 0);

        if(stats.length == 6) this.stats = stats;
        else                     this.stats = new int[]{10, 10, 10, 10, 10, 10};
        if(saveProf.length == 6)    this.saveProf = saveProf;
        else                        this.saveProf = new boolean[]{false, false, false, false, false, false};
        this.basicTraits = new ArrayList<>();
        this.traits = new ArrayList<>();
        this.actions = new ArrayList<>();
        //System.out.println("In constructor " + this.stats == null);
    }
    //constructor with no save prof
    public DNDMonster(String name, String size, String type, String alignment, String abbr, int pb, int ac, int hp, int[] stats) {
        this(name, size, type, alignment, abbr, pb, ac, hp, stats, new boolean[]{false, false, false, false, false, false});
    }
    //constructor with no save prof, no abbreviated name
    public DNDMonster(String name, String size, String type, String alignment, int pb, int ac, int hp, int[] stats) {
        this(name, size, type, alignment, name, pb, ac, hp, stats, new boolean[]{false, false, false, false, false, false});
    }
    //only bare minimum stats
    public DNDMonster(int pb, int ac, int hp, int[] stats) {
        this("Monster", "Medium", "N/A", "N/A", pb, ac, hp, stats);
        
    }
    //constructor that generates default values.
    public DNDMonster() {
        this("Monster", "Medium", "N/A", "N/A", "monster", 2, 10, 10, new int[]{10, 10, 10, 10, 10, 10}, new boolean[]{false, false, false, false, false, false});
    }

    public void updateStats(){

    }
    //IO methods
    public static DNDMonster readMonster (File f) throws IOException, ClassNotFoundException{
        ObjectInputStream monIn = new ObjectInputStream(new FileInputStream(f));
        Object input = monIn.readObject();
        return (com.monster.DNDMonster) input;
    }

    public void store() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.name));
        out.writeObject(this);
        out.flush();
        out.close();
    }

    public void store(File f) throws IOException {
        if(!f.isDirectory())    return;
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f.getAbsolutePath() + "/" + this.name + ".mon"));
        out.writeObject(this);
        out.flush();
        out.close();
    }

    //setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public int getStat(int stat){
        if(stat > -1 && stat < 6)   return this.stats[stat];
        return -1;
    }

    //get stat, save, skill, attack modifiers, dc
    public int getMod(int stat){
        if(stat > -1 && stat < 6){
            return this.stats[stat] > 11 ? (this.stats[stat] - 10) / 2 : (this.stats[stat] - 11) / 2;
        }
        return 0;
    }

    public int getSave(int stat){
        return this.getMod(stat) + (this.saveProf[stat]? this.pb : 0);
    }

    public int getAtkBonus(int stat){
        if(stat == -1)  return this.atkBonus;
        return this.getMod(stat) + this.pb;
    }

    public int getAtkBonus(){
        return this.atkBonus;
    }

    public int getDamage() {
        return damage;
    }

    public int getDc() {
        return dc;
    }

    public int getDC(int stat){
        return this.getMod(stat) + this.pb + 8;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }

    public void setAtkBonus(int atkBonus) {
        this.atkBonus = atkBonus;
    }

    public EnumTrait getBasicTrait(String name){
        if(name==null || name.length() < 1) return null;
        for(EnumTrait t: this.basicTraits)  if(t.getTitle().equals(name))   return t;
        return null;
    }
	
	//adding skills, traits, actions, and equipment
    //
    public void addBasicTrait(String title, String content) {
        if(getBasicTrait(title) != null)    this.getBasicTrait(title).addContent(content);
        else this.basicTraits.add(new EnumTrait(title, content));
    }

    public void updateBasicTrait(String title, String content){
        if(title == null || title.length() < 1) return;
        if(getBasicTrait(title) != null)    this.getBasicTrait(title).update(content);
        else this.basicTraits.add(new EnumTrait(title, content, true));
    }

    public void removeBasicTrait(String title){
        if(title == null || title.length() < 1) return;
        this.basicTraits.remove(getBasicTrait(title));
    }

    public void addSkill(String skill){
        this.addBasicTrait("Skill", skill);
    }

    public void addTrait(Trait t){
        if(this.traits == null) this.traits = new ArrayList<>();
        for(Trait t1 : traits){
            if(t1.getTitle().equals(t.getTitle())){
                t1.setContent(t.getContent());
                return;
            }
        }
        this.traits.add(t);
    }

    public Trait getTrait(String t){
        if(t==null || t.length() < 1)   return null;
        for(Trait t1: this.traits){
        	//System.out.println("Matching" + t1.getTitle() + " against " + t);
        	if(t1.getTitle().equals(t))   return t1;
        }
        return null;
    }

    public void removeTrait(String t){
        this.traits.remove(getTrait(t));
    }
 
    public void addAction(Action a){
        if(this.actions == null) this.actions = new ArrayList<>();
        for(Action a1 : actions){
            //System.out.println("looking at " + a1.getTitle());
            if(a.getTitle().equals(a1.getTitle()) && a.type.equals(a1.type)){
                //System.out.println("Changed " + a.getTitle());
                a1.setContent(a.getContent());
                if(a1 instanceof Attack && a instanceof Attack){
                    Attack atk = (Attack) a;
                    Attack atk1 = (Attack) a1;
                    atk1.selfValue = atk.selfValue;
                    atk1.otherValue = atk.otherValue;
                    atk1.criteria = atk.criteria;
                    atk1.hitEffect = atk.hitEffect;
                }
                //System.out.println("Now " + a1.getTitle() + " reads " + a1.getContent());
                return;
            }
        }
        //System.out.println("didn't find " + a.getTitle());
        this.actions.add(a);
    }

    public Action getAction(String name){
        if(name==null || name.length() < 1) return null;
        for(Action a: this.actions)  if(a.getTitle().equals(name))   return a;
        return null;
    }

    public void removeAction(String name){
        this.actions.remove(getAction(name));
    }


    public void addEquipment(String str){
        this.addBasicTrait("Equipment", str);
    }

    //setting basic info
    public void setInfo(String name, String size, String type, String alignment, String abbr){
        if(name != null)    this.name = name;
        if(size != null)    this.size = size;
        if(type != null)    this.type = type;
        if(alignment != null)   this.alignment = alignment;
        if(abbr != null)    this.abbr = abbr;
    }

    public void setStats(int str, int dex, int con, int wis, int intel, int cha){
        this.stats[STR] = str;
        this.stats[DEX] = dex;
        this.stats[CON] = con;
        this.stats[WIS] = wis;
        this.stats[INT] = intel;
        this.stats[CHA] = cha;
    }

    public static int statIndex(String str){
        if("STR".equals(str))   return 0;
        if("DEX".equals(str))   return 1;
        if("CON".equals(str))   return 2;
        if("WIS".equals(str))   return 3;
        if("INT".equals(str))   return 4;
        if("CHA".equals(str))   return 5;
        return -1;
    }

    //substitution. Supports [MON] [ABL DC] [ABL MOD] [ABL ATK] [ABL SAVE] [ABL 1d6+3] [1d6] [1d6+3]
    public String replaceAbbr(String str){
        int i = 0;
        int j = 0;
        StringBuilder sb = new StringBuilder(str);
        String text;

        while(i < sb.length() && i != -1){
            //finds each instance of bracket pairs.
            i = sb.indexOf("[", i);
            j = sb.indexOf("]", i);
            if( i > -1 && j > -1){
                //saves what's inside the brackets for identification.
                text = sb.substring(i+1, j);
                sb.delete(i, j+1);
                //see if the first three letters match a stat abbreviation.
                int idx = -1;
                if(text.length() > 2)   idx = statIndex(text.substring(0, 3));

                //name substitution
                if(text.equals("MON"))  {sb.insert(i,this.name); continue;}
                if(text.equals("DMG"))  {sb.insert(i, "" + this.getDamage()); continue;}
                if(text.equals("ATK"))  {sb.insert(i, "+" + this.getAtkBonus()); continue;}
                if(text.equals("DC"))   {sb.insert(i, this.getDc()); continue;}
                if(idx > -1){
                    //attack bonus, save dc, ability modifier, save modifier substitution
                    if(text.endsWith("ATK"))    {sb.insert(i, "+" + this.getAtkBonus(idx)); continue;}
                    if(text.endsWith("DC"))     {sb.insert(i, this.getDC(idx)); continue;}
                    if(text.endsWith("MOD"))    {sb.insert(i, "+" + this.getMod(idx)); continue;}
                    if(text.endsWith("SAVE"))   {sb.insert(i, "+" + this.getSave(idx)); continue;}
                    //dice substitution with ability modifier
                    if(text.substring(4).matches("\\d+d\\d+([+-]\\d+)?")){
                        String[] nums = text.substring(4).split("[^\\d]");
                        int[] ints = new int[3];
                        for(int intID = 0; intID < nums.length; intID++) ints[intID] = Integer.parseInt(nums[intID]);
                        int avg = this.getMod(idx)+ + (ints[0] * ints[1] + ints[0] )/ 2 + ints[2];
                        sb.insert(i, avg + "(" + text.substring(4) + "+" + this.getMod(idx) + ")");
                        continue;
                    }
                }

                // dice number format substitution.
                if(text.matches("\\d+d\\d+([+-]\\d+)?")){
                    String[] nums = text.split("[^\\d]");
                    int[] ints = new int[3];
                    for(int intID = 0; intID < nums.length; intID++) ints[intID] = Integer.parseInt(nums[intID]);
                    int avg = (ints[0] * ints[1] + ints[0] )/ 2 + ints[2];
                    sb.insert(i, avg + "(" + text + ")");
                    continue;
                }
            }
        }
        return sb.toString();
    }

    public String textStatblock() {
        return this.textStatblock(MOD, true, 37);
    }

    public String textStatblock(boolean modifier, boolean smartBreak, int width){
        StringBuilder sb = new StringBuilder(this.getName() + "\n");
        sb.append(this.getSize() + " " + this.getType() + ", " + this.getAlignment() + "\n");
        sb.append("HP: " + this.hp + "\n");
        sb.append("AC: " + this.ac + "\n");
        sb.append("| STR | DEX | CON | WIS | INT | CHA | \n");
        //prints stats. can choose modifiers or numeric values.
        if(!modifier) {
            for (int i = 0; i < 6; i++) sb.append("|  " + this.stats[i] + (this.stats[i] < 10 ? "  " : " "));
        }else{
            for(int i = 0; i < 6; i++) sb.append("| " + (this.stats[i] > 9? "+" : "") + (this.getMod(i) + "  "));
        }
        sb.append("|\n");

        int curr = sb.length();
        //prints basic traits.
        for(Trait t : this.basicTraits){
            sb.append(replaceAbbr(t.getText()));
            //insert line breaks every [width] characters.
            for(int i = curr + width; i < sb.length(); i += width + 1){
                //if smart break is enabled, instead insert break at the last space character
                if(smartBreak)  i = sb.lastIndexOf(" ", i) + 1;
                sb.insert(i, "\n");
            }
            curr = sb.length();
        }
        //prints traits and actions.
        sb.append("Traits: \n");
        for(Trait t: this.traits){
            sb.append(t.getTitle() + ": " + replaceAbbr(t.getContent()) + "\n");
            //insert line breaks every [width] characters.
            for(int i = curr + width; i < sb.length(); i += width + 1){
                //if smart break is enabled, instead insert break at the last space character
                if(smartBreak)  i = sb.lastIndexOf(" ", i) + 1;
                sb.insert(i, "\n");
            }
            curr = sb.length();
        }
        //print actions. See above.
        sb.append("Actions: \n");
        curr = sb.length();
        for(Action a: this.actions){
            sb.append(a.getTitle() + ": " + replaceAbbr(a.getContent()) + "\n");
            for(int i = curr + width; i < sb.length(); i += width + 1){
                if(smartBreak)  i = sb.lastIndexOf(" ", i) + 1;
                sb.insert(i, "\n");
            }
            curr = sb.length();
        }

        return sb.toString();
    }

    public String htmlBreakStatblock(){
        return htmlBreakStatblock(true, true, 80);
    }
    public String htmlBreakStatblock(boolean modifier, boolean smartBreak, int width){
        StringBuilder sb = new StringBuilder("");
        sb.append("<body><div><h1>" + this.getName().toUpperCase() + "</h1>");
        sb.append("<h4><i>" + this.getSize() + " " + this.getType() + ", " + this.getAlignment() + "</i><br>");
        sb.append("<div><h4><b>HP:</b> " + this.hp + " <b>AC:</b> " + this.ac  + " <b>PB:</b> +" + this.pb + "<br>" );
        if(this instanceof AngryMonster){
            sb.append(getBasicTrait("Atk").getHTML() + " ");
            sb.append(getBasicTrait("Damage").getHTML() + " ");
            sb.append(getBasicTrait("DC").getHTML() + " <br>");
        }
        //sb.append("<b>| STR | DEX | CON | WIS | INT | CHA | </b><br>");
        //prints stats. can choose modifiers or numeric values.
        if(!modifier) {
            for (int i = 0; i < 6; i++) sb.append("|  " + this.stats[i] + (this.stats[i] < 10 ? "  " : " "));
        }else{
            for(int i = 0; i < 6; i++) sb.append("| " + (this.stats[i] > 9? "+" : "") + (this.getMod(i) + "  "));
        }
        sb.append("|</b><br>");

        int curr = sb.length();
        //prints basic traits.
        for(Trait t : this.basicTraits){
            if(this instanceof AngryMonster){
                if("Atk Damage DC".contains(t.getTitle()))  continue;
            }
            sb.append(replaceAbbr(t.getTextHTML()));
        }
        sb.append("</h4></div>");
        //prints traits and actions.
        sb.append("<h3>Traits: </h3>");
        curr = sb.length();
        for(Trait t: this.traits){
            sb.append("<div><p>" + t.getTitleHTML() + ": " + replaceAbbr(t.getContent()) + "</p></div>");
        }
        //print actions. See above.
        sb.append("</p><h3>Actions: <br></h3>");
        for(Action a: this.actions){
            sb.append("<div><p>" + a.getTitleHTML() + ": " + replaceAbbr(a.getContent()) + "</p></div>");
            //if(a instanceof Attack) System.out.println(a.getContent());
        }
        sb.append("</p></div></body></html>");
        return sb.toString();
    }

	//@Override
	public String toString(){
		return this.textStatblock();
	}
	
    public static void main(String[] args){
        //DNDMonster gob = new DNDMonster(2, 15, 12, (new int[]{9, 15, 11, 9, 13, 10}));
        //System.out.println("Regex test");
        //System.out.println("4d12+4".matches("\\d+d\\d+([+-]\\d+)?"));
        Trait esc = new Trait("Uncanny Escape", "[MON] can disengage or hide as a BA.");
        Action shortsword = new Action("Shortsword", "+4 to hit, [DEX 12d12+10] slashing dmg.");
        Action shortswordAtk = new Attack("Shortsword", "+4", "AC" , "" , "[DEX 12d12] slashing dmg.");
        
        //System.out.println(shortswordAtk.getContent());
        
        
        
        Action burningHands = new Attack("Burning Hands", "[WIS DC]", "DEX", "15 ft.cone", "[3d6] fire dmg, save halves. f you want to mix fonts or colors within the text, or if you want formatting such as multiple lines, you can use HTML. HTML formatting can be used in all Swing buttons, menu items, labels, tool tips, and tabbed panes, as well as in components such as trees and tables that use labels to render text.");
        DNDMonster goblin = new DNDMonster("Goblin Mage", "small", "humanoid", "N/A", "goblin", 2, 15, 12, new int[]{9, 15, 11, 9, 13, 10});
        goblin.addBasicTrait("Skill", "stealth");
        goblin.addBasicTrait("Skill", "stealth");
        //        System.out.println(goblin.stats == null);
        goblin.addSkill("perception");
        goblin.addTrait(esc);
        //goblin.addAction(shortsword);
        //System.out.println("Trying to add " + shortswordAtk.getTitle());
        goblin.addAction(shortsword);
        goblin.addAction(burningHands);
        //goblin.addAction(new Attack("Burning Hands", "[WIS DC]", "DEXXXX", "15 ft.cone", "[3d6] fire dmg, save halves. f you want to mix fonts or colors within the text, or if you want formatting such as multiple lines, you can use HTML. HTML formatting can be used in all Swing buttons, menu items, labels, tool tips, and tabbed panes, as well as in components such as trees and tables that use labels to render text."));
        //System.out.println(goblin.textStatblock(true, true, 120));
        //System.out.println("\n".length());


        try{
            goblin.store();
            //System.out.println(readMonster(new File("Goblin Mage")).htmlBreakStatblock(true, false, 60));
        }catch(IOException e){
            e.printStackTrace();
        }
        
        goblin = null;
    }
	
}

class Trait implements Serializable{
    private static final long serialVersionUID = 722345;
    String title;
    String content;

    public Trait(){
        this("Trait", "Content");
    }

    public Trait(String t, String c){
        this.title = t;
        this.content = c;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleHTML() {return "<b>" + this.title + "</b>";}

    public String getContent() {
        return content;
    }

    public void addContent(String content) {
        this.content += content;
    }

    public void setContent(String content)  {
        this.content = content;
    }

    public String getText(){
        return (this.getTitle() + ": " + this.getContent() + "\n");
    }

    public String getTextHTML(){
        return (this.getTitleHTML() + ": " + this.getContent() + "<br>");
    }

    public String toString(){
        return "hello?";
    }
}

class EnumTrait extends Trait implements Serializable{

    private static final long serialVersionUID = 6695850;

    ArrayList<String> items;
    EnumTrait(){
        super();
        this.items = new ArrayList<String>();
    }
    EnumTrait(String t, String c){
        super(t, c);
        this.items = new ArrayList<String>();
        this.items.add(c);
    }

    EnumTrait(String t, ArrayList<String> i){
        super(t, "");
        this.items = i;
    }

    EnumTrait(String t, String[] i){
        super(t, "");
        this.items = new ArrayList<>();
        for(String item : i)    this.items.add(item);
    }

    EnumTrait(String t, String content, boolean b){
        this();
        this.title = t;
        this.update(content);
    }

    public String getHTML(){
        return getTitleHTML() + ": " + getContent();
    }
    public String getTitleHTML() {return "<b>" + this.title + "</b>";}

    public String getContent(){
        StringBuilder sb = new StringBuilder();
        for(String item : items){
            sb.append(item);
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public void addContent(String item){
        item = item.trim() + "";
        for(String str : this.items){
            if(item.equals(str))  return;
        }
        this.items.add(item);
    }

    public void update(String itemz){
        this.items.clear();
        this.content = "";
        for(String s : itemz.split(", ")){
            if(s.trim().length() > 0) this.addContent(s);
        }
    }


}

class Action extends Trait implements Serializable{

    private static final long serialVersionUID = 63349550;
    String type;

    public Action(){
        this("Action 1", "action", "Do something.");
    }


    public Action(String t, String c){
        this(t, "action", c);
    }

    public Action(String t, String type, String c){
        this.title = t;
        this.type = type;
        this.content = c;
    }

    public String getTitle() {
        String result = title;
        if(type.equals("bonus"))    result = "(B) " + result;
        if(type.equals("reaction"))	result = "(R) " + result;
        if(type.equals("legendary"))result = "(L) " + result;
        if(type.equals("lair"))     result = "(Lair) " + result; 
        return result;
    }
    
    public String getOGTitle(){
    	return this.title;
    }

    public String getTitleHTML() {return "<b>" + this.getTitle() + "</b>";}

    public String getContent() {
        return content;
    }

    public void setContent(String str) {
        this.content = str;
    }
}

class Attack extends Action{

    private static final long serialVersionUID = 6669550;
    String title;
    String selfValue;
    String otherValue;
    String criteria;
    String hitEffect;

    public Attack(){}

    public Attack(String t, String c){
        super(t, c);
    }

    public Attack(String title, String selfValue, String otherValue, String criteria, String hitEffect) {
        this.title = title;
        this.selfValue = selfValue;
        this.otherValue = otherValue;
        this.criteria = criteria;
        this.hitEffect = hitEffect;
        this.content = getContent();
    }

    public String getContent(){
        return this.selfValue.length() > 0 ? (this.selfValue + " vs " + this.otherValue + ". " + (this.criteria.length() > 0 ? this.criteria + ". " : "")+this.hitEffect) : super.getContent();
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getTitle(){
        return this.title;
    }

    public String getTitleHTML() {return "<b>" + this.title + "</b>";}

}

class StatNotFoundException extends Exception{

}

//
