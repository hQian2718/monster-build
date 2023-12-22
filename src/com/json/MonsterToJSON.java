package com.json;

import com.google.gson.Gson;
import com.monster.AngryMonster;
import com.monster.DNDMonster;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class MonsterToJSON {
    private Gson converter;
    private static final String saveDest = "/Users/mrpentagon/Library/CloudStorage/OneDrive-UCLAITServices/TRPG/jsonMonMan";
    MonsterToJSON(){
        this.converter = new Gson();
    }

    public boolean isMonType(Path p){
        String name = p.toString();
        if(name.lastIndexOf(".") == -1) return false;
        if(!name.substring(name.lastIndexOf(".") + 1).equals("mon"))    return false;
        return true;
    }

    public DNDMonster safeReadMonster(Path p){
        try{
            return DNDMonster.readMonster(p.toFile());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String convertToJson(DNDMonster mon){
        return converter.toJson(mon);
    }

    public void saveJSON(String json, String filename){
        try{
            Path filePath = Paths.get(MonsterToJSON.saveDest, filename.concat(".txt"));
            Files.write(filePath, json.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void convertFile(Path p){
        DNDMonster mon = safeReadMonster(p);
        saveJSON(convertToJson(mon), mon.getName().replace(' ', '_'));
    }
    public void convertDirectory(String path){
        try(Stream<Path> paths = Files.walk(Paths.get(path), 2)){
            paths.filter(p -> isType(p, "mon")).forEach(p -> convertFile(p));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public boolean isType(Path p, String type){ //checks for file extension
        if(type == null)    return false;
        String name = p.toString();
        if(type.equals("")) return (name.lastIndexOf(".")== -1);
        if(name.lastIndexOf(".") == -1) return false;
        if(!name.substring(name.lastIndexOf(".") + 1).equals(type))    return false;
        return true;
    }
    public int countOld(String path){
        int i = 0;
        try(Stream<Path> paths = Files.walk(Paths.get(path), 2)){
            i = paths.filter(p -> isType(p, "monster")).toList().size();
        }catch(IOException e){
            e.printStackTrace();
        }
        return i;
    }

    public static void main(String[] args){
        MonsterToJSON machine = new MonsterToJSON();
        Path eg = Paths.get("/Users/mrpentagon/Downloads/Example converter.mon");
        machine.convertFile(eg);
        //machine.convertDirectory("/Users/mrpentagon/Desktop/MonMan");
        //System.out.println(machine.countOld("/Users/mrpentagon/Desktop/MonMan"));
    }

}
