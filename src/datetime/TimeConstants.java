package src.datetime;

/**
 * TimeConstants
 */
public class TimeConstants {
    public static final double millisecondsPerTick = 0.0001;
    public static final double secondsPerTick = 1E-07;
    public static final double minutesPerTick = 1.6666666666666667E-09;
    public static final double hoursPerTick = 2.7777777777777777E-11;
    public static final double daysPerTick = 1.1574074074074074E-12;
    public static final long maxSeconds = 922337203685L;
    public static final long minSeconds = -922337203685L;
    public static final long maxMilliSeconds = 922337203685477L;
    public static final long minMilliSeconds = -922337203685477L;
    public static final long ticksPerTenthSecond = 1000000L;
    public static final long ticksPerMillisecond = 10000L;
    public static final long ticksPerSecond = 10000000L;
    public static final long ticksPerMinute = 600000000L;
    public static final long ticksPerHour = 36000000000L;
    public static final long ticksPerDay = 864000000000L;
    public static final int millisPerSecond = 1000;
    public static final int millisPerMinute = 60000;
    public static final int millisPerHour = 3600000;
    public static final int millisPerDay = 86400000;
    public static final int daysPerYear = 365;
    public static final int daysPer4Years = 1461;
    public static final int daysPer100Years = 36524;
    public static final int daysPer400Years = 146097;
    public static final int daysTo1601 = 584388;
    public static final int daysTo1899 = 693593;
    public static final int daysTo1970 = 719162;
    public static final int daysTo10000 = 3652059;
    public static final long minTicks = 0L;
    public static final long maxTicks = 3155378975999999999L;
    public static final long maxMillis = 315537897600000L;
    public static final long fileTimeOffset = 504911232000000000L;
    public static final long oaDateMinAsTicks = 31241376000000000L;
    public static final double oaDateMinAsDouble = -657435.0;
    public static final double oaDateMaxAsDouble = 2958466.0;
    public static final int datePartYear = 0;
    public static final int datePartDayOfYear = 1;
    public static final int datePartMonth = 2;
    public static final int datePartDay = 3;
    
    public static final int[] daysToMonth365 = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
    public static final int[] daysToMonth366 = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};
    
    public static final long minValueTimeSpanTicks = -9223372036854775807L;
    public static final long maxValueTimeSpanTicks = 9223372036854775807L;
    public static final long ticksCeiling = 4611686018427387904L;
    public static final long ticksMask = 4611686018427387903L;
    // public static final long flagsMask = 13835058055282163712L;
    // public static final long localMask = 9223372036854775808L;
}