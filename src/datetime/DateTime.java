package src.datetime;

import java.util.Calendar;
/**
 * Datetime
 */
public class DateTime {

    public enum DayOfWeek {
        SUNDAY(0), /* 星期日 */
        MONDAY(1), /* 星期一 */
        TUESDAY(2), /* 星期二 */
        WENSDAY(3), /* 星期三 */
        THURSDAy(4), /* 星期四 */
        FRIDAY(5), /* 星期五 */
        SATURDAY(6); /* 星期六 */

        int value;

        DayOfWeek(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum DateTimeKind {
        Unspecified(0), /* 未确定 */
        Utc(1), /* 国际标准时 */
        Local(2); /* 当地时区时间 */

        int value;

        DateTimeKind(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* 时间类 */
    public class Time {
        public int year; /* 年 */
        public int month; /* 月 */
        public int day; /* 日 */
        public int hour; /* 时 */
        public int minute; /* 分 */
        public int second; /* 秒 */
        public int millisecond; /* 毫秒 */

        public Time() {
        }
    }

    long dateData = 0;

    public DateTime(long ticks) {
        judgeTicks(ticks);
        dateData = (long) ticks;
    }

    public DateTime(long ticks, DateTimeKind kind) {
        judgeTicks(ticks);
        dateData = (long) (ticks | (kind.getValue() << 62));
    }

    public DateTime(int year, int month, int day) {
        dateData = (long) dateToTicks(year, month, day);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        dateData = (long) (dateToTicks(year, month, day) + timeToTicks(hour, minute, second));
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, DateTimeKind kind) {
        judgeDateTimeKind(kind);
        long num = dateToTicks(year, month, day) + timeToTicks(hour, minute, second);
        dateData = (long) (num);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        judgeMillisecond(millisecond);
        dateData = (long) judgeNumTicks(year, month, day, hour, minute, second, millisecond);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond,
            DateTimeKind kind) {
        judgeAllParas(year, month, day, hour, minute, second, millisecond, kind);
        dateData = (long) (judgeNumTicks(year, month, day, hour, minute, second, millisecond)
                | (kind.getValue() << 62));
    }

    public DateTime(long ticks, DateTimeKind kind, boolean isAmbiguousDst) {
        judgeTicks(ticks);
        dateData = (long) (ticks
                | (isAmbiguousDst ? (-TimeConstants.ticksCeiling) : (TimeConstants.minValueTimeSpanTicks)));
    }

    public static DateTime now() {
        Calendar c = Calendar.getInstance();
        return javaDateToDateTime(c);
    }

    public static DateTime utcNow() {
        Calendar c = Calendar.getInstance();
        return javaDateToDateTime(c);
    }

    public static DateTime today() {
        return now().date();
    }

    public static DateTime minValue() {
        return new DateTime(0L, DateTimeKind.Unspecified);
    }

    public static DateTime maxValue() {
        return new DateTime(TimeConstants.maxTicks, DateTimeKind.Unspecified);
    }

    public static DateTime fromBinary(final long dateData) {
        if ((dateData & TimeConstants.minValueTimeSpanTicks) != 0L) {
            long num = dateData & 0x3FFFFFFFFFFFFFFFL;
            if (num > 4611685154427387904L) {
                num -= 4611686018427387904L;
            }
            boolean isAmbiguousLocalDst = false;
            long ticks = 0;
            if (num < 0) {
                // ticks = TimeZoneInfo::GetLocalUtcOffset(MinValue,
                // TimeZoneInfoOptions::NoThrowOnInvalidTime).Ticks;
            } else if (num > TimeConstants.maxTicks) {
                // ticks = TimeZoneInfo::GetLocalUtcOffset(MaxValue,
                // TimeZoneInfoOptions::NoThrowOnInvalidTime).Ticks;
            } else {
                // DateTime time = new DateTime(num, DateTimeKind.Utc);
                // boolean isDaylightSavings = false;
                // ticks = TimeZoneInfo::GetUtcOffsetFromUtc(time, TimeZoneInfo.Local,
                // isDaylightSavings, isAmbiguousLocalDst).Ticks;
            }
            num += ticks;
            if (num < 0) {
                num += TimeConstants.ticksPerDay;
            }
            if (num < 0 || num > TimeConstants.maxTicks) {
                throw new Error();
            }
            return new DateTime(num, DateTimeKind.Local, isAmbiguousLocalDst);
        }
        return fromBinaryRaw(dateData);
    }

    public static DateTime fromBinaryRaw(final long dateData) {
        return new DateTime((long) dateData);
    }

    public static DateTime fromFileTime(final long fileTime) {
        return fromFileTimeUtc(fileTime).toLocalTime();
    }

    public static DateTime fromFileTimeUtc(final long fileTime) {
        if (fileTime < 0 || fileTime > 2650467743999999999L) {
            throw new Error();
        }
        long ticks = fileTime + TimeConstants.fileTimeOffset;
        return new DateTime(ticks, DateTimeKind.Utc);
    }

    public static DateTime fromOADate(double d) {
        return new DateTime(doubledateToTicks(d), DateTimeKind.Unspecified);
    }

    public static DateTime javaDateToDateTime(Calendar date) {
        return new DateTime(date.get(Calendar.YEAR), 
            date.get(Calendar.MONTH) + 1, 
            date.get(Calendar.DAY_OF_MONTH), 
            date.get(Calendar.HOUR), 
            date.get(Calendar.MINUTE),
            date.get(Calendar.SECOND), DateTimeKind.Local);
    }

    // Unrealized
    public static DateTime parse(final String s) {
        return new DateTime(0, 0, 0);
    }

    public static DateTime specifyKind(final DateTime value, final DateTimeKind kind) {
        return new DateTime(value.getInternalTicks(), kind);
    }

    public static int compare(final DateTime t1, final DateTime t2) {
        long internalTicks = t1.getInternalTicks();
        long internalTicks2 = t2.getInternalTicks();
        if (internalTicks2 > internalTicks) {
            return 1;
        }
        if (internalTicks2 < internalTicks) {
            return -1;
        }
        return 0;
    }

    public static int daysInMonth(final int year, final int month) {
        if (month < 1 || month > 12) {
            throw new Error();
        }
        boolean isleapyear = isLeapYear(year);
        if (isleapyear == true) {
            return TimeConstants.daysToMonth366[month] - TimeConstants.daysToMonth366[month];
        } else {
            return TimeConstants.daysToMonth365[month] - TimeConstants.daysToMonth365[month];
        }
    }

    public static boolean isLeapYear(final int year) {
        if (year < 1 || year > 9999) {
            throw new Error();
        }
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            }
            return true;
        }
        return false;
    }

    public Time getTime() {
        Time rtime = new Time();
        rtime.year = year();
        rtime.month = month();
        rtime.day = day();
        rtime.hour = hour();
        rtime.minute = minute();
        rtime.second = second();
        rtime.millisecond = millisecond();
        return rtime;
    }

    public DayOfWeek getDayOfWeek() {
        int index = (int) ((getInternalTicks() / TimeConstants.ticksPerDay + 1) % 7);
        return DayOfWeek.values()[index];
    }

    public DateTimeKind getDateTimeKind() {
        int internalKind = (int) getInternalKind();
        switch (internalKind) {
            case 0:
                return DateTimeKind.Unspecified;
            case 1:
                return DateTimeKind.Utc;
            case 2:
            default:
                return DateTimeKind.Local;
        }
    }

    public TimeSpan getTimeOfDay() {
        return new TimeSpan(0);
    }

    public DateTime date() {
        long internalTicks = getInternalTicks();
        return new DateTime(
                (long) ((internalTicks - internalTicks % TimeConstants.ticksPerDay) | (long) getInternalKind()));
    }

    public DateTime add(final TimeSpan value) {
        return addTicks(value.ticks());
    }

    public DateTime add(double value, int scale) {
        long num = (long) (value * (double) scale + ((value >= 0.0) ? 0.5 : (-0.5)));
        if (num <= -TimeConstants.maxMillis || num >= TimeConstants.maxMillis) {
            throw new Error();
        }
        return addTicks(num * 10000);
    }

    public DateTime addDays(double value) {
        return add(value, TimeConstants.millisPerDay);
    }

    public DateTime addHours(double value) {
        return add(value, TimeConstants.millisPerHour);
    }

    public DateTime addMilliseconds(double value) {
        return add(value, 1);
    }

    public DateTime addMinutes(double value) {
        return add(value, TimeConstants.millisPerMinute);
    }

    public DateTime addMonths(int months) {
        int year = 0, month = 0, day = 0;
        if (months < -120000 || months > 120000) {
            throw new Error();
        }
        getDatePart(year, month, day);
        int num = month - 1 + months;
        if (num >= 0) {
            month = num % 12 + 1;
            year += num / 12;
        } else {
            month = 12 + (num + 1) % 12;
            year += (num - 11) / 12;
        }
        if (year < 1 || year > 9999) {
            throw new Error();
        }
        int num2 = daysInMonth(year, month);
        if (day > num2) {
            day = num2;
        }
        return new DateTime((long) ((timeToTicks(year, month, day) + 
            getInternalTicks() % TimeConstants.ticksPerDay)
                | (long) getInternalKind()));
    }

    public DateTime addSeconds(double value) {
        return add(value, TimeConstants.millisPerSecond);
    }

    public DateTime addTicks(long value) {
        long internalTicks = getInternalTicks();
        if (value > TimeConstants.maxTicks - internalTicks || value < -internalTicks) {
            throw new Error();
        }
        return new DateTime((long) ((internalTicks + value) | (long) getInternalKind()));
    }

    public DateTime addYears(int value) {
        if (value < -10000 || value > 10000) {
            throw new Error();
        }
        return addMonths(value * 12);
    }

    public TimeSpan subtract(final DateTime value) {
        return new TimeSpan(getInternalTicks() - value.getInternalTicks());
    }

    public DateTime subtract(final TimeSpan value) {
        long internalTicks = getInternalTicks();
        long ticks = value.ticks();
        if (internalTicks - 0 < ticks || internalTicks - TimeConstants.maxTicks > ticks) {
            throw new Error();
        }
        return new DateTime((long) ((internalTicks - ticks) | (long) getInternalTicks()));
    }

    public DateTime toLocalTime() {
        return toLocalTime(false);
    }

    public DateTime toLocalTime(boolean throwOnOverflow) {
        if (getDateTimeKind() == DateTimeKind.Local) {
            return this;
        }
        boolean isDaylightSavings = false;
        boolean isAmbiguousLocalDst = false;
        long ticks = 0; // TimeZoneInfo::GetUtcOffsetFromUtc(this, TimeZoneInfo::Local,
                        // isDaylightSavings, isAmbiguousLocalDst).Ticks();
        long num = getInternalTicks() + ticks;
        if (num > TimeConstants.maxTicks) {
            if (throwOnOverflow) {
                throw new Error();
            }
            return new DateTime(TimeConstants.maxTicks, DateTimeKind.Local);
        }
        if (num < 0) {
            if (throwOnOverflow) {
                throw new Error();
            }
            return new DateTime(0L, DateTimeKind.Local);
        }
        return new DateTime(num, DateTimeKind.Local, isAmbiguousLocalDst);
    }

    public DateTime toUniversalTime() {
        return this;
        // return TimeZoneInfo::ConvertTimeToUtc(*this,
        // TimeZoneInfoOptions::NoThrowOnInvalidTime);
    }

    @Override
    public String toString()
    {
        String ss = year() + "-" + month() + "-" + day() 
            + "-" + hour() + "-" + minute() + "-" + second();
        return ss;
    }

    public double toOADate() {
        return ticksToOADate(getInternalTicks());
    }

    public long toBinary() {
        return (long) dateData;
    }

    public int compareTo(final DateTime value) {
        long internalTicks = value.getInternalTicks();
        long internalTicks2 = getInternalTicks();
        if (internalTicks2 > internalTicks) {
            return 1;
        }
        if (internalTicks2 < internalTicks) {
            return -1;
        }
        return 0;
    }

    public boolean equals(final DateTime value) {
        return getInternalTicks() == value.getInternalTicks();
    }

    public boolean EqualsMonthAndDay(DateTime value) {
        return month() == value.month() && day() == value.day();
    }

    public int day() {
        return getDatePart(3);
    }

    public int dayOfYear() {
        return getDatePart(1);
    }

    public int hour() {
        return (int) (getInternalTicks() / TimeConstants.ticksPerHour % 24);
    }

    public int millisecond() {
        return (int) (getInternalTicks() / TimeConstants.ticksPerMillisecond % 1000);
    }

    public int minute() {
        return (int) (getInternalTicks() / TimeConstants.ticksPerMinute % 60);
    }

    public int month() {
        return getDatePart(2);
    }

    public int second() {
        return (int) (getInternalTicks() / TimeConstants.ticksPerSecond % 60);
    }

    public int year() {
        return getDatePart(0);
    }

    public long ticks() {
        return getInternalTicks();
    }

    public long toBinaryRaw() {
        return (long) dateData;
    }

    public int getDatePart(int part) {
        long internalTicks = getInternalTicks();
        int num = (int) (internalTicks / TimeConstants.ticksPerDay);
        int num2 = num / 146097;
        num -= num2 * 146097;
        int num3 = num / 36524;
        if (num3 == 4) {
            num3 = 3;
        }
        num -= num3 * 36524;
        int num4 = num / 1461;
        num -= num4 * 1461;
        int num5 = num / 365;
        if (num5 == 4) {
            num5 = 3;
        }
        if (part == 0) {
            return num2 * 400 + num3 * 100 + num4 * 4 + num5 + 1;
        }
        num -= num5 * 365;
        if (part == 1) {
            return num + 1;
        }
        int[] array = (num5 == 3 && (num4 != 24 || num3 == 3)) ? 
            TimeConstants.daysToMonth366 :TimeConstants.daysToMonth365;
        int i;
        for (i = num >> 6; num >= array[i]; i++) {
        }
        if (part == 2) {
            return i;
        }
        return num - array[i - 1] + 1;
    }

    public void getDatePart(Integer year, Integer month, Integer day) {
        long internalTicks = getInternalTicks();
        int num = (int) (internalTicks / TimeConstants.ticksPerDay);
        int num2 = num / 146097;
        num -= num2 * 146097;
        int num3 = num / 36524;
        if (num3 == 4) {
            num3 = 3;
        }
        num -= num3 * 36524;
        int num4 = num / 1461;
        num -= num4 * 1461;
        int num5 = num / 365;
        if (num5 == 4) {
            num5 = 3;
        }
        year = num2 * 400 + num3 * 100 + num4 * 4 + num5 + 1;
        num -= num5 * 365;
        int[] array = (num5 == 3 && (num4 != 24 || num3 == 3)) ? 
            TimeConstants.daysToMonth366 : TimeConstants.daysToMonth365;
        int i;
        for (i = (num >> 5) + 1; num >= array[i]; i++) {
        }
        month = i;
        day = num - array[i - 1] + 1;
    }

    public double ticksToOADate(long value) {
        if (value == 0L) {
            return 0.0;
        }
        if (value < TimeConstants.ticksPerDay) {
            value += 599264352000000000L;
        }
        if (value < TimeConstants.oaDateMinAsTicks) {
            throw new Error();
        }
        long num = (value - 599264352000000000L) / 10000;
        if (num < 0) {
            long num2 = num % TimeConstants.millisPerDay;
            if (num2 != 0L) {
                num -= (TimeConstants.millisPerDay + num2) * 2;
            }
        }
        return (double) num / (double) TimeConstants.millisPerDay;
    }

    public long dateToTicks(int year, int month, int day)
    {
        if (year >= 1 && year <= 9999 &&  month >= 1 &&  month <= 12)
        {
            int[] array = isLeapYear(year) ? 
                TimeConstants.daysToMonth366 : TimeConstants.daysToMonth365;
            if (day >= 1 && day <= array[month] - array[month - 1])
            {
                int num = year - 1;
                int num2 = num * 365 + num / 4 - num / 100 + num / 400 + array[month - 1] + day - 1;
                return num2 * TimeConstants.ticksPerDay;
            }
        }
        throw new Error(); 
    }

    public long timeToTicks(int hour, int minute, int second) {
        if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60) {
            return TimeSpan.timeToTicks(hour, minute, second);
        }
        throw new Error();
    }

    public static long doubledateToTicks(double value) {
        if (!(value < 2958466.0) || !(value > -657435.0)) {
            throw new Error();
        }
        long num = (long) (value * (double) TimeConstants.millisPerDay + ((value >= 0.0) ? 0.5 : (-0.5)));
        if (num < 0) {
            num -= num % TimeConstants.millisPerDay * 2;
        }
        num += 59926435200000L;
        if (num < 0 || num >= 315537897600000L) {
            throw new Error();
        }
        return num * 10000;
    }

    public long getInternalTicks()
    {
        return (long)(dateData & 0x3FFFFFFFFFFFFFFFL);
    }

    public long getInternalKind() 
    {
        return (long)(dateData & -4611686018427387904L);
    }

    public boolean isAmbiguousDaylightSavingTime() {
        // return getInternalKind() == TimeConstants.kindLocalAmbiguousDst;
        return false;
    }

    public void judgeMillisecond(int millisecond) {
        if (millisecond < 0 || millisecond >= 1000) {
            throw new Error();
        }
    }

    public void judgeDateTimeKind(DateTimeKind kind) {

    }

    public long judgeNumTicks(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        long num = dateToTicks(year, month, day) + timeToTicks(hour, minute, second);
        num += (long) millisecond * 10000L;
        if (num < 0 || num > TimeConstants.maxTicks) {
            throw new Error();
        }
        return num;
    }

    public void judgeTicks(long ticks) {
        if (ticks < 0 || ticks > TimeConstants.maxTicks) {
            throw new Error();
        }
    }

    public void judgeAllParas(int year, int month, int day, int hour, int minute, int second, int millisecond,
            DateTimeKind kind) {
        judgeMillisecond(millisecond);
        judgeDateTimeKind(kind);
        judgeNumTicks(year, month, day, hour, minute, second, millisecond);
    }

}
