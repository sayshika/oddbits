package org.wso2.carbon.solutions.sf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by seshika on 4/2/15.
 */
public class test {
    public static void main(String[] args) {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int thisQuarter = (Calendar.getInstance().get(Calendar.MONTH) +3)/3;
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
        Date recordDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String recordDatestr = sdf.format(recordDate);

        System.out.println("date : "+recordDatestr.toString());
    }

}
