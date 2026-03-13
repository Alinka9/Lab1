package model;

public class Sorcerer {
    private String name;
    private Rank rank;
    
    public Sorcerer() {}
    
    public Sorcerer(String name, Rank rank) {
        this.name = name;
        this.rank = rank;
    }
    
    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Rank getRank() { return rank; }
    public void setRank(Rank rank) { this.rank = rank; }
    
    @Override
    public String toString() {
        return String.format("⚔️ %s (%s)", name, rank);
    }
}