package com.monster;

import java.io.Serial;
import java.util.ArrayList;

public class AngryMonster extends DNDMonster{
    @Serial
    private static final long serialVersionUID = 108626092339331367l;

        public static final int MOB = 4;
        public static final int GANG = 3;
        public static final int PARTY = 2;
        public static final int PAIR = 1;
        public static final int SOLO = 0;

        public static final int POOR = 0;
        public static final int AVG = 1;
        public static final int GOOD = 2;

        public static final int AC = 0;
        public static final int HP = 1;
        public static final int ATK = 2;
        public static final int DC = 3;
        public static final int DMG = 4;

/*
             int encounterLevel;
            int monsterTier;
            int threatLevel;
            int primaryStat;
        int[] quality = {1, 1, 1, 1, 1};

         int damage;
         int dc;
         int atkBonus;
*/
        //public
        public static final String[] threatLevelNames = {"solo", "pair", "party", "gang", "mob"};
        //seven tiers, three qualities, five monster threat lvs.
        public static final int[][][] hpTable = {
                {
                        {60, 30, 18, 11, 5},
                        {75, 38, 23, 13, 7},
                        {90, 45, 27, 16, 8}
                },
                {
                        {105, 53, 32, 18, 9},
                        {120, 60, 36, 21, 11},
                        {135, 68, 41, 24, 12}
                },
                {
                        {150, 75, 45, 26, 13},
                        {165, 83, 50, 29, 14},
                        {180, 90, 54, 32, 16}
                },
                {
                        {195, 98, 59, 34, 17},
                        {210, 105, 63, 37, 18},
                        {225, 113, 68, 39, 20}
                },
                {
                        {240, 120, 72, 42, 21},
                        {255, 128, 77, 45, 22},
                        {270, 135, 81, 47, 24}
                },
                {
                        {285, 143, 86, 50, 25},
                        {300, 150, 90, 53, 26},
                        {315, 158, 95, 55, 28}
                },
                {
                        {330, 165, 99, 58, 29},
                        {345, 173, 104, 60, 30},
                        {360, 180, 108, 63, 32}
                },
        };

        public static final int[][][] dmgTable = {
                {
                        {6, 4, 3, 2, 1},
                        {12, 8, 5, 3, 2},
                        {18, 12, 8, 5, 3}
                },
                {
                        {24, 16, 10, 6, 3},
                        {30, 20, 13, 8, 4},
                        {36, 24, 15, 9, 5}
                },
                {
                        {42, 28, 18, 11, 5},
                        {48, 32, 20, 12, 6},
                        {54, 36, 23, 14, 7}
                },
                {
                        {60, 40, 25, 15, 7},
                        {66, 44, 28, 17, 8},
                        {72, 48, 30, 18, 9}
                },
                {
                        {78, 52, 33, 20, 9},
                        {84, 56, 35, 21, 10},
                        {90, 60, 38, 23, 11}
                },
                {
                        {96, 64, 40, 24, 12},
                        {102, 68, 43, 26, 13},
                        {108, 72, 45, 27, 14}
                },
                {
                        {114, 76, 48, 29, 14},
                        {120, 80, 50, 30, 15},
                        {126, 84, 53, 32, 16}
                }
        };

        public String getCR(){
            return "lv." + this.monsterTier*3 + "-" + (this.monsterTier*3 + 2) + " " + threatLevelNames[threatLevel];
        }

    public static int[] baselineStats(int playerLevel, int threatLevel){
        int[] stats = new int[6];
        for(int i = 0; i < 6; i++){
            stats[i] = 10 + (playerLevel / (4 + threatLevel));
        }
        return stats;
    }

    public static int calcAC(int playerLevel, int quality){
        return playerLevel / 3 + 13 + (quality - 1) * 2;
    }
    public static int calcAtk(int playerLevel, int quality){
        return playerLevel / 3 + 4 + (quality - 1) * 2;
    }
    public static int calcDC(int playerLevel, int quality){
        return calcAC(playerLevel, quality);
    }

    public static int calcPB(int lv){
        if(lv < 6)  return 2;
        if(lv < 9)  return 3;
        if(lv < 12) return 4;
        if(lv < 18) return 5;
        return 6;
    }

    public int getAtkBonus(){
            return this.atkBonus;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public int getDc() {
        return dc;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void setDc(int dc) {
        this.dc = dc;
    }

    @Override
    public void setAtkBonus(int atkBonus) {
        this.atkBonus = atkBonus;
    }

    public void updateStats(){
            //System.out.println("Tier:" + this.monsterTier);
        this.hp = hpTable[monsterTier][quality[HP]][threatLevel];
        this.pb = calcPB(encounterLevel);
        this.damage = dmgTable[monsterTier][quality[DMG]][threatLevel];
        this.dc = calcDC(encounterLevel, quality[DC]);
        this.atkBonus = calcAtk(encounterLevel, quality[ATK]);

        //System.out.println("HP:" + this.hp);
        this.stats[primaryStat] = 11 + (this.atkBonus - this.pb) * 2;
        //this.updateBasicTrait("Challenge Rating", "lv." + this.monsterTier*3 + "-" + (this.monsterTier*3 + 2) + " " + threatLevelNames[threatLevel]);
        this.updateBasicTrait("Atk", "+" + this.atkBonus);
        this.updateBasicTrait("Damage", "" + this.damage);
        this.updateBasicTrait("DC", "" + this.dc);
    }

    public AngryMonster(String name, String size, String type, String alignment, String abbr, int playerLevel, int threatLevel, int primaryStat, int[] quality, int[] stats, boolean[] saveProf){
        super(name, size, type, alignment, abbr, calcPB(playerLevel), calcAC(playerLevel, quality[AC]), hpTable[playerLevel / 3][quality[HP]][threatLevel], stats, saveProf);
        this.quality = quality;
        this.encounterLevel = playerLevel;
        this.monsterTier = playerLevel / 3;
        this.threatLevel = threatLevel;
        this.damage = dmgTable[monsterTier][quality[DMG]][threatLevel];
        this.dc = calcDC(playerLevel, quality[DC]);
        this.atkBonus = calcAtk(playerLevel, quality[ATK]);
        //System.out.println(atkBonus);
        this.primaryStat = primaryStat;
        if(primaryStat > -1) this.stats[primaryStat] = 11 + (this.atkBonus - this.pb) * 2;
        Action primaryAttack = new Action("Simple Attack", "[ATK] v.s. AC, [DMG] dmg.");
        this.addAction(primaryAttack);
        this.addBasicTrait("Challenge Rating", "lv." + this.monsterTier*3 + "-" + (this.monsterTier*3 + 2) + " " + threatLevelNames[threatLevel]);
        this.addBasicTrait("Atk", "+" + this.atkBonus);
        this.addBasicTrait("Damage", "" + this.damage);
        this.addBasicTrait("DC", "" + this.dc);
    }
    public AngryMonster(String name, int playerLevel, int threatLevel, int[] stats, int[] quality){
        this(name, "medium", "humanoid", "N/A", name, playerLevel, threatLevel, -1, quality, stats, new boolean[]{false,false,false, false, false, false});
    }
    public AngryMonster(int playerLevel, int threatLevel, int[] stats, int[] quality){
        this("Monster", "medium", "humanoid", "N/A", "monster", playerLevel, threatLevel, -1, quality, stats, new boolean[]{false,false,false, false, false, false});
    }
    public AngryMonster(String name, int playerLevel, int threatLevel, int[] quality){
        this(name, "medium", "humanoid", "N/A", name, playerLevel, threatLevel, 0, quality, baselineStats(playerLevel, threatLevel), new boolean[]{false,false,false, false, false, false});
    }

    public AngryMonster(int playerLevel, int threatLevel, int[] quality){
        this("Monster", "medium", "humanoid", "N/A", "monster", playerLevel, threatLevel, 0, quality, baselineStats(playerLevel, threatLevel), new boolean[]{false,false,false,false,false,false});
    }
    public AngryMonster(int playerLevel, int threatLevel){
        this("Monster", "medium", "humanoid", "N/A", "monster", playerLevel, threatLevel, 0, new int[]{1, 1, 1, 1, 1}, baselineStats(playerLevel, threatLevel), new boolean[]{false,false,false, false, false, false});
    }

    public static void main(String[] args){
        DNDMonster angryGoblin = new AngryMonster("Goblin", 10, 0, new int[]{1, 1, 1, 1, 1});
        DNDMonster angryDragon = new AngryMonster("Dragon", 11, SOLO, new int[]{19, 13, 15, 17, 14, 14}, new int[]{GOOD, AVG, GOOD, GOOD, AVG});
        //System.out.println(angryDragon.textStatblock());
    }

}
