package com.payroll.utils;

public class GeneralUtil {
    public static String getMailUsername(String email) {
        return email.split("@")[0];
    }
}
