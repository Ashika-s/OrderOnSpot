package com.sas.food_order_application.admin;
public class AdminRegisterClass {
    private String RestorantName;
    private  String Restaurantphno;
    private String Restaurantaddress;
    private String Ownername;
    private String Ownerphno;
    private String password;
    private String confirmpassword;
    private String email;
    private  int tablecount;

    public int getTablecount() {
        return tablecount;
    }

    public void setTablecount(int tablecount) {
        this.tablecount = tablecount;
    }

    public String getRestorantName() {
        return RestorantName;
    }

    public void setRestorantName(String restorantName) {
        RestorantName = restorantName;
    }

    public String getRestaurantphno() {
        return Restaurantphno;
    }

    public void setRestaurantphno(String restaurantphno) {
        Restaurantphno = restaurantphno;
    }

    public String getOwnername() {
        return Ownername;
    }

    public void setOwnername(String ownername) {
        Ownername = ownername;
    }

    public String getOwnerphno() {
        return Ownerphno;
    }

    public void setOwnerphno(String ownerphno) {
        Ownerphno = ownerphno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRestaurantaddress() {
        return Restaurantaddress;
    }

    public void setRestaurantaddress(String restaurantaddress) {
        Restaurantaddress = restaurantaddress;
    }
}
