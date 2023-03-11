package be.kdg.youthcouncil.utility;

public enum Role {
    GENERAL_ADMIN("ROLE_GENERAL_ADMIN"),
    COUNCIL_ADMIN("ROLE_COUNCIL_ADMIN"),
    MODERATOR("ROLE_MODERATOR"),
    MEMBER("ROLE_MEMBER");
    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
