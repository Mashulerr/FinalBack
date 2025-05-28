package org.example.entity;


public enum ReactionType {
    LIKE("like"),
    DISLIKE("dislike");

    private final String value;

    ReactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ReactionType fromValue(String value) {
        for (ReactionType type : ReactionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown reaction type: " + value +
                ". Allowed values are: like, dislike");
    }
}