package dev.thom.model;

import dev.thom.model.type.AccountType;
import dev.thom.model.type.TransactionType;

public class AccountTransaction extends DatabaseRecord {

    private TransactionType transactionType;
    private Integer accountId;
    private Double amount;
    private Long accountTransactionDate;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAccountTransactionDate() {
        return accountTransactionDate;
    }

    public void setAccountTransactionDate(Long accountTransactionDate) {
        this.accountTransactionDate = accountTransactionDate;
    }

}
