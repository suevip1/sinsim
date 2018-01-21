package com.eservice.api.service.common;

import com.sun.javafx.binding.StringFormatter;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Class Description:
 *
 * @author Wilson Hu
 * @date 26/12/2017
 */
public class Utils {

    /**
     * 生成12位的机器基础编号
     * @return string 机器基础编号
     */
    public static String createMachineBasicId() {

        String[] year = new String[]{"A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] month = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                      "A", "B"};
        String[] date = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                     "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                                     "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                                     "U"};
        StringBuilder builder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //一共15位订单号,同一秒内重复概率1/10000000,26年一次的循环
        builder.append(year[(calendar.get(Calendar.YEAR)- 2018)%26]);
        builder.append(month[(calendar.get(Calendar.MONTH))]);
        builder.append(date[calendar.get(Calendar.DATE) -1]);
        //24小时制
        builder.append(calendar.get(Calendar.HOUR_OF_DAY) <= 9 ? "0" +calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY) + "");
        builder.append(calendar.get(Calendar.MINUTE) <= 9 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "");
        builder.append(calendar.get(Calendar.SECOND) <= 9 ? "0" + calendar.get(Calendar.SECOND) : calendar.get(Calendar.SECOND) + "");
        int randomValue = new Random().nextInt(99);
        builder.append(randomValue < 9 ? "0" + randomValue : randomValue + "");
        return builder.toString();
    }
}
