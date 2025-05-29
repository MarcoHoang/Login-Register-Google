package com.example.casestudymodule3.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    // Kiểm tra định dạng email
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Kiểm tra định dạng số điện thoại
    public static boolean isValidPhone(String phone) {
        String phonePattern = "^0[3|5|7|8|9][0-9]{8}$"; // Định dạng điện thoại Việt Nam
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
