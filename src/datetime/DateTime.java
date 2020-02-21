package src.datetime;

import java.util.Date;

/**
 * Datetime
 */
public class DateTime {

    public enum DayOfWeek {
	    SUNDAY(0),          /*  星期日  */
	    MONDAY(1),          /*  星期一  */
	    TUESDAY(2),         /*  星期二  */
	    WENSDAY(3),       /*  星期三  */
	    THURSDAy(4),        /*  星期四  */
	    FRIDAY(5),          /*  星期五  */
        SATURDAY(6);         /*  星期六  */
        
        int value;
        DayOfWeek(int value) {
            value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum DateTimeKind {
        Unspecified(0),         /*  未确定  */
        Utc(1),                 /*  国际标准时  */
        Local(2);                /*  当地时区时间  */

        int value;
        DateTimeKind(int value) {
            value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /*  时间类  */
    public class Time {
        public int year;           /*  年  */
        public int month;          /*  月  */
        public int day;            /*  日  */
        public int hour;           /*  时  */
        public int minute;         /*  分  */
        public int second;         /*  秒  */
        public int millisecond;    /*  毫秒  */
        public Time() { }
    }

    long dateData = 0;

    public DateTime(long ticks)
    {
        judgeTicks(ticks);
        dateData = (long)ticks;
    }
        
    public DateTime(long ticks, DateTimeKind kind)
    {
        judgeTicks(ticks);
        dateData = (long)(ticks | (kind.getValue() << 62));
    }
    
    public DateTime(int year, int month, int day)
    {
        dateData = (long)DateToTicks(year, month, day);
    }
    
    public DateTime(int year, int month, int day, int hour, int minute, int second)
    {
        dateData = (long)(DateToTicks(year, month, day) + TimeToTicks(hour, minute, second));
    }
    
    public DateTime(int year, int month, int day, int hour, int minute, int second, DateTimeKind kind)
    {
        judgeDateTimeKind(kind);
        long num = DateToTicks(year, month, day) + TimeToTicks(hour, minute, second);
        dateData = (long)(num | (kind.getValue() << 62));
    }
    
    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        judgeMillisecond(millisecond);
        dateData = (long)judgeNumTicks(year, month, day, hour, minute, second, millisecond);
    }
    
    public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, DateTimeKind kind)
    {
        judgeAllParas(year, month, day, hour, minute, second, millisecond, kind);
        dateData = (long)(judgeNumTicks(year, month, day, hour, minute, second, millisecond) | ((long)kind << 62));
    }
    
    public DateTime(long ticks, DateTimeKind kind, boolean isAmbiguousDst)
    {
        judgeTicks(ticks);
        dateData = (long)(ticks | (isAmbiguousDst ? 
            (-TimeConstants.ticksCeiling) : 
            (TimeConstants.minValueTimeSpanTicks)));
    }
    
    public static DateTime Now()
    {
        Date date = new Date();
        return DateTime(date.getYear(), date.getMonth(), date.getDay(),
            date.getHours(), date.getMinutes(), date.getSeconds(), DateTimeKind.Local);
    }
    
    public static DateTime UtcNow()
    {
        Date date = new Date();
        //Date.UTC(year, month, date, hrs, min, sec)
    }
    
    public static DateTime Today()
    {
        return Now().Date();
    }
    
    public static DateTime MinValue()
    {
        return DateTime(0L, DateTimeKind::Unspecified);
    }
    
    public static DateTime MaxValue()
    {
        return DateTime(MaxTicks, DateTimeKind::Unspecified);
    }
    
    public static DateTime FromBinary(final long dateData)
    {
        if ((dateData  MinValueTimeSpanTicks) != 0L)
        {
            long num = dateData  0x3FFFFFFFFFFFFFFF;
            if (num > 4611685154427387904L)
            {
                num -= 4611686018427387904L;
            }
            bool isAmbiguousLocalDst = false;
            long ticks = 0;
            if (num < 0)
            {
                //ticks = TimeZoneInfo::GetLocalUtcOffset(MinValue, TimeZoneInfoOptions::NoThrowOnInvalidTime).Ticks;
            }
            else if (num > MaxTicks)
            {
                // ticks = TimeZoneInfo::GetLocalUtcOffset(MaxValue, TimeZoneInfoOptions::NoThrowOnInvalidTime).Ticks;
            }
            else
            {
                DateTime time = DateTime(num, DateTimeKind::Utc);
                bool isDaylightSavings = false;
                // ticks = TimeZoneInfo::GetUtcOffsetFromUtc(time, TimeZoneInfo.Local, isDaylightSavings, isAmbiguousLocalDst).Ticks;
            }
            num += ticks;
            if (num < 0)
            {
                num += TicksPerDay;
            }
            if (num < 0 || num > MaxTicks)
            {
                throw;
            }
            return DateTime(num, DateTimeKind::Local, isAmbiguousLocalDst);
        }
        return FromBinaryRaw(dateData);
    }
    
    public static DateTime FromBinaryRaw(final long dateData)
    {
        return DateTime((long)dateData);
    }
    
    public static DateTime FromFileTime(final long fileTime)
    {
        return FromFileTimeUtc(fileTime).ToLocalTime();
    }
    
    public static DateTime FromFileTimeUtc(final long fileTime)
    {
        if (fileTime < 0 || fileTime > 2650467743999999999L)
        {
            throw;
        }
        long ticks = fileTime + FileTimeOffset;
        return DateTime(ticks, DateTimeKind::Utc);
    }
    
    public static DateTime FromOADate(final double d)
    {
        return DateTime(DoubleDateToTicks(d), DateTimeKind::Unspecified);
    }
    
    // Unrealized
    public static DateTime Parse(final string s)
    {
        return Now();
    }
    
    public static DateTime SpecifyKind(final DateTime value, final DateTimeKind kind)
    {
        return DateTime(value.getInternalTicks(), kind);
    }
    
    public static int Compare(final DateTime t1, final DateTime t2)
    {
        long internalTicks = t1.getInternalTicks();
        long internalTicks2 = t2.getInternalTicks();
        if (internalTicks2 > internalTicks)
        {
            return 1;
        }
        if (internalTicks2 < internalTicks)
        {
            return -1;
        }
        return 0;
    }
    
    public static int DaysInMonth(final int year, final int month)
    {
        if (month < 1 || month > 12)
        {
            throw;
        }
        auto isleapyear = IsLeapYear(year);
        if (isleapyear == true)
        {
            return DaysToMonth366[month] - DaysToMonth366[month];
        }
        else
        {
            return DaysToMonth365[month] - DaysToMonth365[month];
        }
    }
    
    public static bool IsLeapYear(final int year)
    {
        if (year < 1 || year > 9999)
        {
            throw;
        }
        if (year % 4 == 0)
        {
            if (year % 100 == 0)
            {
                return year % 400 == 0;
            }
            return true;
        }
        return false;
    }
    
    public Time GetTime()
    {
        Time _rtime;
        _rtime.Year = Year();
        _rtime.Month = Month();
        _rtime.Day = Day();
        _rtime.Hour = Hour();
        _rtime.Minute = Minute();
        _rtime.Second = Second();
        _rtime.Millisecond = Millisecond();
        return _rtime;
    }
    
    public DayOfWeek GetDayOfWeek()
    {
        return static_cast<DayOfWeek>((getInternalTicks() / TicksPerDay + 1) % 7);
    }
    
    DateTimeKind GetDateTimeKind()
    {
        auto internalKind = getInternalKind();
        switch (internalKind)
        {
        case 0uL:
            return DateTimeKind::Unspecified;
        case KindUtc:
            return DateTimeKind::Utc;
        default:
            return DateTimeKind::Local;
        }
    }
    
    TimeSpan GetTimeOfDay()
    {
        return TimeSpan(0);
    }
    
    DateTime Date()
    {
        long internalTicks = getInternalTicks();
        return DateTime((long)((internalTicks - internalTicks % TicksPerDay) | (long)getInternalKind()));
    }
    
    DateTime Add(final TimeSpan value)
    {
        return AddTicks(value.Ticks());
    }
    
    DateTime Add(double value, int scale)
    {
        long num = (long)(value * (double)scale + ((value >= 0.0) ? 0.5 : (-0.5)));
        if (num <= -MaxMillis || num >= MaxMillis)
        {
            throw;
        }
        return AddTicks(num * 10000);
    }
    
    DateTime AddDays(double value)
    {
        return Add(value, MillisPerDay);
    }
    
    DateTime AddHours(double value)
    {
        return Add(value, MillisPerHour);
    }
    
    DateTime AddMilliseconds(double value)
    {
        return Add(value, 1);
    }
    
    DateTime AddMinutes(double value)
    {
        return Add(value, MillisPerMinute);
    }
    
    DateTime AddMonths(int months)
    {
        int year = 0, month = 0, day = 0;
        if (months < -120000 || months > 120000)
        {
            throw;
        }
        GetDatePart(year, month, day);
        int num = month - 1 + months;
        if (num >= 0)
        {
            month = num % 12 + 1;
            year += num / 12;
        }
        else
        {
            month = 12 + (num + 1) % 12;
            year += (num - 11) / 12;
        }
        if (year < 1 || year > 9999)
        {
            throw;
        }
        int num2 = DaysInMonth(year, month);
        if (day > num2)
        {
            day = num2;
        }
        return DateTime((long)((DateToTicks(year, month, day) + getInternalTicks() % TicksPerDay) | (long)getInternalKind()));
    }
    
    DateTime AddSeconds(double value)
    {
        return Add(value, MillisPerSecond);
    }
    
    DateTime AddTicks(long value)
    {
        long internalTicks = getInternalTicks();
        if (value > MaxTicks - internalTicks || value < -internalTicks)
        {
            throw;
        }
        return DateTime((long)((internalTicks + value) | (long)getInternalKind()));
    }
    
    DateTime AddYears(int value)
    {
        if (value < -10000 || value > 10000)
        {
            throw;
        }
        return AddMonths(value * 12);
    }
    
    TimeSpan Subtract(final DateTime value)
    {
        return TimeSpan(getInternalTicks() - value.getInternalTicks());
    }
    
    DateTime Subtract(final TimeSpan value)
    {
        long internalTicks = getInternalTicks();
        long ticks = value.Ticks();
        if (internalTicks - 0 < ticks || internalTicks - MaxTicks > ticks)
        {
            throw;
        }
        return DateTime((long)((internalTicks - ticks) | (long)getInternalTicks()));
    }
    
    DateTime ToLocalTime()
    {
        return ToLocalTime(false);
    }
    
    DateTime ToLocalTime(bool throwOnOverflow)
    {
        if (GetDateTimeKind() == DateTimeKind::Local)
        {
            return *this;
        }
        bool isDaylightSavings = false;
        bool isAmbiguousLocalDst = false;
        long ticks = 0; // TimeZoneInfo::GetUtcOffsetFromUtc(this, TimeZoneInfo::Local, isDaylightSavings, isAmbiguousLocalDst).Ticks();
        long num = getInternalTicks() + ticks;
        if (num > MaxTicks)
        {
            if (throwOnOverflow)
            {
                throw;
            }
            return DateTime(MaxTicks, DateTimeKind::Local);
        }
        if (num < 0)
        {
            if (throwOnOverflow)
            {
                throw;
            }
            return DateTime(0L, DateTimeKind::Local);
        }
        return DateTime(num, DateTimeKind::Local, isAmbiguousLocalDst);
    }
    
    DateTime ToUniversalTime()
    {
        return *this;
        // return TimeZoneInfo::ConvertTimeToUtc(*this, TimeZoneInfoOptions::NoThrowOnInvalidTime);
    }
    
    string ToString()
    {
        std::stringstream ss;
        ss << Year() << "-" << Month() << "-" << Day() << "-" << Hour() << "-" << Minute() << "-" << Second();
        return ss.str();
        // return DateTimeFormat::Format(this, nullptr, DateTimeFormatInfo::CurrentInfo);
    }
    
    wstring ToWString() 
    {
        std::wstringstream ss;
        ss << Year() << "-" << Month() << "-" << Day() << "-" << Hour() << "-" << Minute() << "-" << Second();
        return ss.str();
    }
    
    string ToLongDateString()
    {
        return ToString();
        // return DateTimeFormat::Format(this, "D", DateTimeFormatInfo::CurrentInfo);
    }
    
    string ToLongTimeString()
    {
        return ToString();
        // return DateTimeFormat::Format(this, "T", DateTimeFormatInfo::CurrentInfo);
    }
    
    string ToShortDateString()
    {
        return ToString();
        // return DateTimeFormat::Format(this, "d", DateTimeFormatInfo::CurrentInfo);
    }
    
    string ToShortTimeString()
    {
        return ToString();
        // return DateTimeFormat::Format(this, "t", DateTimeFormatInfo::CurrentInfo);
    }
    
    double ToOADate()
    {
        return TicksToOADate(getInternalTicks());
    }
    
    long ToFileTime()
    {
        return ToUniversalTime().ToFileTimeUtc();
    }
    
    long ToFileTimeUtc()
    {
        long num = (((long)getInternalKind()  -9223372036854775807L) != 0L) ? ToUniversalTime().getInternalTicks() : getInternalTicks();
        num -= FileTimeOffset;
        if (num < 0)
        {
            throw;
        }
        return num;
    }
    
    long ToBinary()
    {
        return (long)dateData;
    }
    
    int CompareTo(final DateTime value)
    {
        long internalTicks = value.getInternalTicks();
        long internalTicks2 = getInternalTicks();
        if (internalTicks2 > internalTicks)
        {
            return 1;
        }
        if (internalTicks2 < internalTicks)
        {
            return -1;
        }
        return 0;
    }
    
    bool Equals(final DateTime value)
    {
        return getInternalTicks() == value.getInternalTicks();
    }
    
    bool EqualsMonthAndDay(DateTime value)
    {
        return Month() == value.Month()  Day() == value.Day();
    }
    
    DateTime operator+(final TimeSpan t)
    {
        long internalTicks = getInternalTicks();
        long ticks = t.Ticks();
        if (ticks > MaxTicks - internalTicks || ticks < -internalTicks)
        {
            throw;
        }
        return DateTime((long)((internalTicks + ticks) | (long)getInternalKind()));
    }
    
    DateTime operator-(final TimeSpan t)
    {
        long internalTicks = getInternalTicks();
        long ticks = t.Ticks();
        if (internalTicks - 0 < ticks || internalTicks - MaxTicks > ticks)
        {
            throw; 
        }
        return DateTime((long)((internalTicks - ticks) | (long)getInternalKind()));
    }
    
    TimeSpan operator-(final DateTime d)
    {
        return TimeSpan(getInternalTicks() - d.getInternalTicks());
    }
    
    bool operator==(final DateTime d)
    {
        return getInternalTicks() == d.getInternalTicks();
    }
    
    bool operator!=(final DateTime d)
    {
        return getInternalTicks() != d.getInternalTicks();
    }
    
    bool operator<(final DateTime d)
    {
        return getInternalTicks() < d.getInternalTicks();
    }
    
    bool operator<=(final DateTime d)
    {
        return getInternalTicks() <= d.getInternalTicks();
    }
    
    bool operator>(final DateTime d)
    {
        return getInternalTicks() > d.getInternalTicks();
    }
    
    bool operator>=(final DateTime d)
    {
        return getInternalTicks() >= d.getInternalTicks();
    }
    
    int Day()
    {
        return GetDatePart(3);
    }
    
    int DayOfYear()
    {
        return GetDatePart(1);
    }
    
    int Hour()
    {
        return (int)(getInternalTicks() / TicksPerHour % 24);
    }
    
    int Millisecond()
    {
        return (int)(getInternalTicks() / TicksPerMillisecond % 1000);
    }
    
    int Minute()
    {
        return (int)(getInternalTicks() / TicksPerMinute % 60);
    }
    
    int Month()
    {
        return GetDatePart(2);
    }
    
    int Second()
    {
        return (int)(getInternalTicks() / TicksPerSecond % 60);
    }
    
    int Year()
    {
        return GetDatePart(0);
    }
    
    long Ticks()
    {
        return getInternalTicks();
    }
    
    long ToBinaryRaw()
    {
        return (long)dateData;
    }
    
    int GetDatePart(int part)
    {
        long internalTicks = getInternalTicks();
        int num = (int)(internalTicks / TicksPerDay);
        int num2 = num / 146097;
        num -= num2 * 146097;
        int num3 = num / 36524;
        if (num3 == 4)
        {
            num3 = 3;
        }
        num -= num3 * 36524;
        int num4 = num / 1461;
        num -= num4 * 1461;
        int num5 = num / 365;
        if (num5 == 4)
        {
            num5 = 3;
        }
        if (part == 0)
        {
            return num2 * 400 + num3 * 100 + num4 * 4 + num5 + 1;
        }
        num -= num5 * 365;
        if (part == 1)
        {
            return num + 1;
        }
        auto array = (num5 == 3  (num4 != 24 || num3 == 3)) ? DaysToMonth366 : DaysToMonth365;
        int i;
        for (i = num >> 6; num >= array[i]; i++)
        {
        }
        if (part == 2)
        {
            return i;
        }
        return num - array[i - 1] + 1;
    }
    
    void GetDatePart(int *year, int *month, int *day)
    {
        long internalTicks = getInternalTicks();
        int num = (int)(internalTicks / TicksPerDay);
        int num2 = num / 146097;
        num -= num2 * 146097;
        int num3 = num / 36524;
        if (num3 == 4)
        {
            num3 = 3;
        }
        num -= num3 * 36524;
        int num4 = num / 1461;
        num -= num4 * 1461;
        int num5 = num / 365;
        if (num5 == 4)
        {
            num5 = 3;
        }
        *year = num2 * 400 + num3 * 100 + num4 * 4 + num5 + 1;
        num -= num5 * 365;
        auto array = (num5 == 3  (num4 != 24 || num3 == 3)) ? DaysToMonth366 : DaysToMonth365;
        int i;
        for (i = (num >> 5) + 1; num >= array[i]; i++)
        {
        }
        *month = i;
        *day = num - array[i - 1] + 1;
    }
    
    double TicksToOADate(long value)
    {
        if (value == 0L)
        {
            return 0.0;
        }
        if (value < TicksPerDay)
        {
            value += 599264352000000000L;
        }
        if (value < OADateMinAsTicks)
        {
            throw; 
        }
        long num = (value - 599264352000000000L) / 10000;
        if (num < 0)
        {
            long num2 = num % MillisPerDay;
            if (num2 != 0L)
            {
                num -= (MillisPerDay + num2) * 2;
            }
        }
        return (double)num / (double)MillisPerDay;
    }
    
    long DateToTicks(int year, int month, int day)
    {
        if (year >= 1  year <= 9999  month >= 1  month <= 12)
        {
            auto array = IsLeapYear(year) ? DaysToMonth366 : DaysToMonth365;
            if (day >= 1  day <= array[month] - array[month - 1])
            {
                int num = year - 1;
                int num2 = num * 365 + num / 4 - num / 100 + num / 400 + array[month - 1] + day - 1;
                return num2 * TicksPerDay;
            }
        }
        throw; 
    }
    
    long TimeToTicks(int hour, int minute, int second)
    {
        if (hour >= 0  hour < 24  minute >= 0  minute < 60  second >= 0  second < 60)
        {
            return TimeSpan::TimeToTicks(hour, minute, second);
        }
        throw; 
    }
    
    long DoubleDateToTicks(double value)
    {
        if (!(value < 2958466.0) || !(value > -657435.0))
        {
            throw; 
        }
        long num = (long)(value * (double)MillisPerDay + ((value >= 0.0) ? 0.5 : (-0.5)));
        if (num < 0)
        {
            num -= num % MillisPerDay * 2;
        }
        num += 59926435200000L;
        if (num < 0 || num >= 315537897600000L)
        {
            throw; 
        }
        return num * 10000;
    }
    
     long getInternalTicks() final
    {
        return (long)(dateData  0x3FFFFFFFFFFFFFFF);
    }
    
     long getInternalKind() final
    {
        return (long)(dateData  -4611686018427387904L);
    }
    
     bool IsAmbiguousDaylightSavingTime() final
    {
        return getInternalKind() == KindLocalAmbiguousDst;
    }
    
    void judgeMillisecond(int millisecond)
    {
        if (millisecond < 0 || millisecond >= 1000)
        {
            throw;
        }
    }
    void judgeDateTimeKind(DateTimeKind kind)
    {
        if (kind < DateTimeKind::Unspecified || kind > DateTimeKind::Local)
        {
            throw;
        }
    }
    long judgeNumTicks(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        long num = DateToTicks(year, month, day) + TimeToTicks(hour, minute, second);
        num += (long)millisecond * 10000L;
        if (num < 0 || num > MaxTicks)
        {
            throw;
        }
        return num;
    }
    void judgeTicks(long ticks)
    {
        if (ticks < 0 || ticks > MaxTicks)
        {
            throw;
        }
    }
    void judgeAllParas(int year, int month, int day, int hour, int minute, int second, int millisecond, DateTimeKind kind)
    {
        judgeMillisecond(millisecond);
        judgeDateTimeKind(kind);
        judgeNumTicks(year, month, day, hour, minute, second, millisecond);
    }
    
    
}
