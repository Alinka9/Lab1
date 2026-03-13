package model;

public class Curse {
    private String name;
    private ThreatLevel threatLevel;
    
    public Curse() {}
    
    public Curse(String name, ThreatLevel threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }
    
    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }
    
    @Override
    public String toString() {
        return String.format("👹 %s (Уровень: %s)", name, threatLevel);
    }
}