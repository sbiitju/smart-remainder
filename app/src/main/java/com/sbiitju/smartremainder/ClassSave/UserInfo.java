package com.sbiitju.smartremainder.ClassSave;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfo {
    private  String name,number,institution;

    public UserInfo(String name, String number, String institution) {
        this.name = name;
        this.number = number;
        this.institution = institution;
    }

    public UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

}
