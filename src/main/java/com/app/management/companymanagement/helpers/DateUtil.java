package com.app.management.companymanagement.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatDateForInput(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
