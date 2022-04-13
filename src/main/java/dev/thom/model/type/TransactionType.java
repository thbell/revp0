package dev.thom.model.type;

public enum TransactionType implements Type {
    WITHDRAWAL("WITHDRAWAL"),
    DEPOSIT("DEPOSIT");

    private String name;

    TransactionType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
