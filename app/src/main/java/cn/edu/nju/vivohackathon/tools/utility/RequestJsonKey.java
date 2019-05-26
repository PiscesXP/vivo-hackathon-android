package cn.edu.nju.vivohackathon.tools.utility;

public enum RequestJsonKey {

    AMOUNT("amount"),
    USERNAME("userName"),
    PASSWORD("passWord"),
    USERID("userID"),

    ;

    private String name;

    RequestJsonKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
