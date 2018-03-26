package com.lodekennes.carrental.services;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateService {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Date parse(String d) throws ParseException {
        return simpleDateFormat.parse(d);
    }

    public String formatDate(Date d) {
        return simpleDateFormat.format(d);
    }

    public boolean isInBetween(Date value, Date startDate, Date endDate) {
        return startDate.compareTo(value) * value.compareTo(endDate) >= 0;
    }
}
