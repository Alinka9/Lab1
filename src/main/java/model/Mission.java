package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Mission {
    private String missionId;
    private LocalDate date;
    private String location;
    private Outcome outcome;
    private int damageCost;
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;
    private String comment;
    
    public Mission() {
        this.sorcerers = new ArrayList<>();
        this.techniques = new ArrayList<>();
        this.comment = ""; // для комментария
    }
    
    // Геттеры и сеттеры
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDate(String dateStr) {
        try {
            this.date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            this.date = null;
        }
    }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Outcome getOutcome() { return outcome; }
    public void setOutcome(Outcome outcome) { this.outcome = outcome; }
    public void setOutcome(String outcomeStr) {
        this.outcome = Outcome.fromString(outcomeStr);
    }
    
    public int getDamageCost() { return damageCost; }
    public void setDamageCost(int damageCost) { this.damageCost = damageCost; }
    
    public Curse getCurse() { return curse; }
    public void setCurse(Curse curse) { this.curse = curse; }
    
    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<Sorcerer> sorcerers) { this.sorcerers = sorcerers; }
    
    public List<Technique> getTechniques() { return techniques; }
    public void setTechniques(List<Technique> techniques) { this.techniques = techniques; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    // Вспомогательные методы
    public void addSorcerer(Sorcerer sorcerer) {
        this.sorcerers.add(sorcerer);
    }
    
    public void addTechnique(Technique technique) {
        this.techniques.add(technique);
    }
    
    public String getFormattedDate() {
        if (date == null) return "Не указана";
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("🚨 МИССИЯ: ").append(missionId).append("\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("📅 Дата: ").append(getFormattedDate()).append("\n");
        sb.append("📍 Место: ").append(location).append("\n");
        sb.append("🏆 Результат: ").append(outcome).append("\n");
        sb.append("💰 Ущерб: ").append(String.format("%,d¥", damageCost)).append("\n");
        
        if (curse != null) {
            sb.append("\n🎯 ЦЕЛЕВОЕ ПРОКЛЯТИЕ:\n");
            sb.append("   ").append(curse).append("\n");
        }
        
        if (!sorcerers.isEmpty()) {
            sb.append("\n⚡ УЧАСТВУЮЩИЕ МАГИ:\n");
            for (int i = 0; i < sorcerers.size(); i++) {
                sb.append("   ").append(i+1).append(". ").append(sorcerers.get(i)).append("\n");
            }
        }
        
        if (!techniques.isEmpty()) {
            sb.append("\n🔮 ИСПОЛЬЗОВАННЫЕ ТЕХНИКИ:\n");
            for (int i = 0; i < techniques.size(); i++) {
                sb.append("   ").append(i+1).append(". ").append(techniques.get(i)).append("\n");
            }
        }
        
        // Добавляем комментарий, если он есть
        if (comment != null && !comment.isEmpty()) {
            sb.append("📝 Комментарий: ").append(comment).append("\n");
        }
        
        
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }
}