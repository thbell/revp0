package dev.thom.model.type;

public enum AccountType implements Type {
    CHECKING("CHECKING"),
    SAVINGS("SAVINGS");

    private String name;

    AccountType(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return this.name;
    }
}
