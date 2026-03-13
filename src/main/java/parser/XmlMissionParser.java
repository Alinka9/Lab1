package parser;

import model.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlMissionParser implements MissionParser {
    
    @Override
    public boolean canParse(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".xml") || 
               (name.endsWith(".txt") && file.getAbsolutePath().toLowerCase().contains("xml"));
    }
    
    @Override
    public Mission parse(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        
        Mission mission = new Mission();
        
        // Основные поля
        mission.setMissionId(getTagValue(doc, "missionId"));
        mission.setDate(getTagValue(doc, "date"));
        mission.setLocation(getTagValue(doc, "location"));
        mission.setOutcome(getTagValue(doc, "outcome"));
        mission.setComment(getTagValue(doc, "comment"));
        
        try {
            mission.setDamageCost(Integer.parseInt(getTagValue(doc, "damageCost")));
        } catch (NumberFormatException e) {
            mission.setDamageCost(0);
        }
        
        // Проклятие
        NodeList curseNodes = doc.getElementsByTagName("curse");
        if (curseNodes.getLength() > 0) {
            Element curseElem = (Element) curseNodes.item(0);
            Curse curse = new Curse();
            curse.setName(getElementValue(curseElem, "name"));
            curse.setThreatLevel(ThreatLevel.fromString(getElementValue(curseElem, "threatLevel")));
            mission.setCurse(curse);
        }
        
        // Маги
        NodeList sorcererNodes = doc.getElementsByTagName("sorcerer");
        for (int i = 0; i < sorcererNodes.getLength(); i++) {
            Element sElem = (Element) sorcererNodes.item(i);
            Sorcerer sorcerer = new Sorcerer();
            sorcerer.setName(getElementValue(sElem, "name"));
            sorcerer.setRank(Rank.fromString(getElementValue(sElem, "rank")));
            mission.addSorcerer(sorcerer);
        }
        
        // Техники
        NodeList techniqueNodes = doc.getElementsByTagName("technique");
        for (int i = 0; i < techniqueNodes.getLength(); i++) {
            Element tElem = (Element) techniqueNodes.item(i);
            Technique technique = new Technique();
            technique.setName(getElementValue(tElem, "name"));
            technique.setType(TechniqueType.fromString(getElementValue(tElem, "type")));
            technique.setOwner(getElementValue(tElem, "owner"));
            try {
                technique.setDamage(Integer.parseInt(getElementValue(tElem, "damage")));
            } catch (NumberFormatException e) {
                technique.setDamage(0);
            }
            mission.addTechnique(technique);
        }
        
        return mission;
    }
    
    private String getTagValue(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
    
    private String getElementValue(Element elem, String tagName) {
        NodeList nodes = elem.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}