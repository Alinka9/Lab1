package parser;

import model.*;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtMissionParser implements MissionParser {
    
    @Override
    public boolean canParse(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".txt") && !name.contains("json") && !name.contains("xml");
    }
    
    @Override
    public Mission parse(File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()));
        Mission mission = new Mission();
        
        // Разбираем построчно
        String[] lines = content.split("\n");
        Map<String, String> kvMap = new HashMap<>();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                kvMap.put(parts[0].trim(), parts[1].trim());
            }
        }
        
        // Основные поля
        mission.setMissionId(kvMap.getOrDefault("missionId", ""));
        mission.setDate(kvMap.getOrDefault("date", ""));
        mission.setLocation(kvMap.getOrDefault("location", ""));
        mission.setOutcome(kvMap.getOrDefault("outcome", "UNKNOWN"));
        mission.setComment(kvMap.getOrDefault("comment", ""));
        
        try {
            mission.setDamageCost(Integer.parseInt(kvMap.getOrDefault("damageCost", "0")));
        } catch (NumberFormatException e) {
            mission.setDamageCost(0);
        }
        
        // Проклятие
        Curse curse = new Curse();
        curse.setName(kvMap.getOrDefault("curse.name", ""));
        curse.setThreatLevel(ThreatLevel.fromString(kvMap.getOrDefault("curse.threatLevel", "")));
        mission.setCurse(curse);
        
        // Маги и техники - ищем по паттерну
        Pattern sorcererPattern = Pattern.compile("sorcerer\\[(\\d+)\\]\\.(\\w+)");
        Pattern techniquePattern = Pattern.compile("technique\\[(\\d+)\\]\\.(\\w+)");
        
        Map<Integer, Sorcerer> sorcererMap = new HashMap<>();
        Map<Integer, Technique> techniqueMap = new HashMap<>();
        
        for (String key : kvMap.keySet()) {
            String value = kvMap.get(key);
            
            // Маги
            Matcher sMatcher = sorcererPattern.matcher(key);
            if (sMatcher.matches()) {
                int idx = Integer.parseInt(sMatcher.group(1));
                String field = sMatcher.group(2);
                
                Sorcerer s = sorcererMap.getOrDefault(idx, new Sorcerer());
                if (field.equals("name")) s.setName(value);
                if (field.equals("rank")) s.setRank(Rank.fromString(value));
                sorcererMap.put(idx, s);
            }
            
            // Техники
            Matcher tMatcher = techniquePattern.matcher(key);
            if (tMatcher.matches()) {
                int idx = Integer.parseInt(tMatcher.group(1));
                String field = tMatcher.group(2);
                
                Technique t = techniqueMap.getOrDefault(idx, new Technique());
                if (field.equals("name")) t.setName(value);
                if (field.equals("type")) t.setType(TechniqueType.fromString(value));
                if (field.equals("owner")) t.setOwner(value);
                if (field.equals("damage")) {
                    try {
                        t.setDamage(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        t.setDamage(0);
                    }
                }
                techniqueMap.put(idx, t);
            }
        }
        
        // Добавляем магов и техники в миссию
        for (Sorcerer s : sorcererMap.values()) {
            mission.addSorcerer(s);
        }
        
        for (Technique t : techniqueMap.values()) {
            mission.addTechnique(t);
        }
        
        return mission;
    }
}