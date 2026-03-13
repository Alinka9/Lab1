package model;

public enum Outcome {
    SUCCESS,
    FAILURE,
    PARTIAL_SUCCESS,
    UNKNOWN;
    
    public static Outcome fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return Outcome.valueOf(str.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}