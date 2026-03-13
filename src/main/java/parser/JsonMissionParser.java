package parser;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.nio.file.Files;

public class JsonMissionParser implements MissionParser {
    
    @Override
    public boolean canParse(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".json") || 
               (name.endsWith(".txt") && isJsonFormat(file));
    }
    
    private boolean isJsonFormat(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath())).trim();
            return content.startsWith("{") && content.endsWith("}");
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Mission parse(File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()));
        
        // Пытаемся найти JSON внутри текста (на случай если есть теги)
        int startIdx = content.indexOf("{");
        int endIdx = content.lastIndexOf("}");
        if (startIdx >= 0 && endIdx > startIdx) {
            content = content.substring(startIdx, endIdx + 1);
        }
        
        JSONObject json = new JSONObject(content);
        Mission mission = new Mission();
        
        // Основные поля
        mission.setMissionId(json.optString("missionId", ""));
        mission.setDate(json.optString("date", ""));
        mission.setLocation(json.optString("location", ""));
        mission.setOutcome(json.optString("outcome", "UNKNOWN"));
        mission.setDamageCost(json.optInt("damageCost", 0));
        mission.setComment(json.optString("comment", ""));
              
        // Проклятие
        if (json.has("curse")) {
            JSONObject curseJson = json.getJSONObject("curse");
            Curse curse = new Curse();
            curse.setName(curseJson.optString("name", ""));
            curse.setThreatLevel(ThreatLevel.fromString(curseJson.optString("threatLevel", "")));
            mission.setCurse(curse);
        }
        
        // Маги
        if (json.has("sorcerers")) {
            JSONArray sorcerersJson = json.getJSONArray("sorcerers");
            for (int i = 0; i < sorcerersJson.length(); i++) {
                JSONObject sJson = sorcerersJson.getJSONObject(i);
                Sorcerer sorcerer = new Sorcerer();
                sorcerer.setName(sJson.optString("name", ""));
                sorcerer.setRank(Rank.fromString(sJson.optString("rank", "")));
                mission.addSorcerer(sorcerer);
            }
        }
        
        // Техники
        if (json.has("techniques")) {
            JSONArray techniquesJson = json.getJSONArray("techniques");
            for (int i = 0; i < techniquesJson.length(); i++) {
                JSONObject tJson = techniquesJson.getJSONObject(i);
                Technique technique = new Technique();
                technique.setName(tJson.optString("name", ""));
                technique.setType(TechniqueType.fromString(tJson.optString("type", "")));
                technique.setOwner(tJson.optString("owner", ""));
                technique.setDamage(tJson.optInt("damage", 0));
                mission.addTechnique(technique);
            }
        }
        
        return mission;
    }
}