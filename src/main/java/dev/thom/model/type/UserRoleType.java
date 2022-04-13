package dev.thom.model.type;

public enum UserRoleType implements Type {
    ADMIN("ADMIN"),
    USER("USER");

    private String name;

    UserRoleType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
