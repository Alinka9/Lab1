package model;

public class Technique {
    private String name;
    private TechniqueType type;
    private String owner;
    private int damage;
    
    public Technique() {}
    
    public Technique(String name, TechniqueType type, String owner, int damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }
    
    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public TechniqueType getType() { return type; }
    public void setType(TechniqueType type) { this.type = type; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    
    @Override
    public String toString() {
        return String.format("🔮 %s (%s) - Владелец: %s, Урон: %,d¥", 
                name, type, owner, damage);
    }
}