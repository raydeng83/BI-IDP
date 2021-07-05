package com.bi.idp.be.filter;

public class UsersGridFilter extends BaseFilter {
    //we violate naming convention because we use this names in ng2SmartTable
    private String filterByfirstName;
    private String filterBylastName;
    private String filterBylogin;
    private String filterByemail;
    private String filterByage;
    private String filterBystreet;
    private String filterBycity;
    private String filterByzipcode;

    public String getFilterByfirstName() {
        return filterByfirstName;
    }

    public void setFilterByfirstName(String filterByfirstName) {
        this.filterByfirstName = filterByfirstName;
    }

    public String getFilterBylastName() {
        return filterBylastName;
    }

    public void setFilterBylastName(String filterBylastName) {
        this.filterBylastName = filterBylastName;
    }

    public String getFilterBylogin() {
        return filterBylogin;
    }

    public void setFilterBylogin(String filterBylogin) {
        this.filterBylogin = filterBylogin;
    }

    public String getFilterByemail() {
        return filterByemail;
    }

    public void setFilterByemail(String filterByemail) {
        this.filterByemail = filterByemail;
    }

    public String getFilterByage() {
        return filterByage;
    }

    public void setFilterByage(String filterByage) {
        this.filterByage = filterByage;
    }

    public String getFilterBystreet() {
        return filterBystreet;
    }

    public void setFilterBystreet(String filterBystreet) {
        this.filterBystreet = filterBystreet;
    }

    public String getFilterBycity() {
        return filterBycity;
    }

    public void setFilterBycity(String filterBycity) {
        this.filterBycity = filterBycity;
    }

    public String getFilterByzipcode() {
        return filterByzipcode;
    }

    public void setFilterByzipcode(String filterByzipcode) {
        this.filterByzipcode = filterByzipcode;
    }
}
