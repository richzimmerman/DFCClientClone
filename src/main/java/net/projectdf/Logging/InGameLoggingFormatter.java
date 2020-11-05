package net.projectdf.Logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class InGameLoggingFormatter extends Formatter {

    public String format(LogRecord rec) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        df.setTimeZone(TimeZone.getDefault());

        StringBuilder s = new StringBuilder();
        s.append(df.format(date)).append(" ");
        s.append(formatMessage(rec));

        return s.toString();
    }

}
