package model;

public enum TechniqueType {
    INNATE,
    CURSED_TOOL,
    SHIKIGAMI,
    BARRIER,
    UNKNOWN;
    
    public static TechniqueType fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return TechniqueType.valueOf(str.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}