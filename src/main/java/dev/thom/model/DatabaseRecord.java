package dev.thom.model;

import dev.thom.util.ThomList;

public class DatabaseRecord {
    private Integer id;
    private ThomList<String> errorMessageList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ThomList<String> getErrorMessageList() {
        return errorMessageList;
    }

    public void setErrorMessageList(ThomList<String> errorMessageList) {
        this.errorMessageList = errorMessageList;
    }

}
