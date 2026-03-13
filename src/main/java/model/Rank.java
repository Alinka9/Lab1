package model;

public enum Rank {
    GRADE_4,
    GRADE_3,
    GRADE_2,
    GRADE_1,
    SPECIAL_GRADE,
    UNKNOWN;
    
    public static Rank fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return Rank.valueOf(str.toUpperCase().trim().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}