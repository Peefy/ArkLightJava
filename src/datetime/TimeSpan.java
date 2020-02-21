package src.datetime;

/**
 * Timespan
 */
public class TimeSpan {

    public TimeSpan(final long ticks) {
        this.ticks = ticks;
    }

    public TimeSpan(final int hours, final int minutes, final int seconds) {
        this.ticks = timeToTicks(hours, minutes, seconds);
    }

    public TimeSpan(final int days, final int hours, final int minutes, final int seconds) {
        this(days, hours, minutes, seconds, 0);
    }

    public TimeSpan(final int days, final int hours, final int minutes, final int seconds, final int milliseconds) {
        long num = ((long)days * 3600L * 24 + (long)hours * 3600L + (long)minutes * 60L + seconds) * TimeConstants.millisPerSecond + milliseconds;
        if (num > TimeConstants.maxMilliSeconds || num < TimeConstants.minMilliSeconds)
        {
            throw new Error();
        }
        this.ticks = num * TimeConstants.ticksPerMillisecond;
    }
    
    /**
     * 零时间间隔
     * @return TimeSpan
     */
    public static TimeSpan zero() {
        return new TimeSpan(0);
    }           

    /**
     * 最小时间间隔
     * @return TimeSpan
     */
    public static TimeSpan minValue() {
        return new TimeSpan(TimeConstants.minTicks);
    }      

    /**
     * 最大时间间隔
     * @return TimeSpan
     */
    public static TimeSpan maxValue() {
        return new TimeSpan(TimeConstants.maxTicks);
    }

    public static TimeSpan fromDays(final double value) {
        return interval(value, TimeConstants.millisPerDay);
    }

    public static TimeSpan fromHours(final double value) {
        return interval(value, TimeConstants.millisPerHour);
    }

    public static TimeSpan fromMilliseconds(final double value) {
        return interval(value, 1);
    }

    public static TimeSpan FromMinutes(final double value) {
        return interval(value, TimeConstants.millisPerMinute);
    }

    public static TimeSpan FromSeconds(final double value) {
        return interval(value, TimeConstants.millisPerSecond);
    }

    public static TimeSpan fromTicks(final long value) {
        return new TimeSpan(value);
    }

    public static int compare(final TimeSpan t1, final TimeSpan t2) {
        if (t1.ticks > t2.ticks)
        {
            return 1;
        }
        if (t1.ticks < t2.ticks)
        {
            return -1;
        }
        return 0;
    }

    public static long timeToTicks(final int hour, final int minute, final int second) {
        long num = (long)hour * 3600L + (long)minute * 60L + second;
        if (num > TimeConstants.maxSeconds || num < TimeConstants.minSeconds)
        {
            throw new Error();
        }
        return num * TimeConstants.ticksPerSecond;
    }  

    public TimeSpan add(final TimeSpan ts) {
        long num = this.ticks + ts.ticks;
        if (this.ticks >> 63 == ts.ticks >> 63 && this.ticks >> 63 != num >> 63)
        {
            throw new Error();
        }
        return new TimeSpan(num);
    }

    public TimeSpan subtract(final TimeSpan ts) {
        long num = this.ticks - ts.ticks;
        if (this.ticks >> 63 != ts.ticks >> 63 && this.ticks >> 63 != num >> 63)
        {
            throw new Error();
        }
        return new TimeSpan(num);
    }

    
    public boolean equals(final TimeSpan ts) {
        return this.ticks == ts.ticks;
    }

    public int compareTo(final TimeSpan value) {
        long ticks = value.ticks;
        if (this.ticks > ticks)
        {
            return 1;
        }
        if (this.ticks < ticks)
        {
            return -1;
        }
        return 0;
    }

    public TimeSpan duration() {
        judgeTicksIsMin();
        return new TimeSpan((this.ticks >= 0) ? this.ticks : (-this.ticks));
    }

    public TimeSpan negate() {
        judgeTicksIsMin();
        return new TimeSpan(-this.ticks);
    }

    public long ticks() {
        return this.ticks;
    }

    public int days() {
        return (int)(this.ticks / TimeConstants.ticksPerDay);
    }

    public int hours() {
        return (int)(this.ticks / TimeConstants.ticksPerHour % 24);
    }

    public int milliseconds() {
        return (int)(this.ticks / TimeConstants.ticksPerMillisecond % 1000);
    }

    public int minutes() {
        return (int)(this.ticks / TimeConstants.ticksPerMinute % 60);
    }

    public int seconds() {
        return (int)(this.ticks / TimeConstants.ticksPerSecond % 60);
    }

    public double totalDays() {
        return (double)this.ticks * TimeConstants.daysPerTick;
    }

    public double totalHours() {
        return (double)this.ticks * TimeConstants.hoursPerTick;
    }

    public double totalMilliseconds() {
        double num = (double)this.ticks * 0.0001;
        if (num > (double)TimeConstants.maxMilliSeconds)
        {
            return (double)TimeConstants.maxMilliSeconds;
        }
        if (num < (double)TimeConstants.minMilliSeconds)
        {
            return (double)TimeConstants.minMilliSeconds;
        }
        return num;
    }

    public double totalMinutes() {
        return (double)this.ticks * TimeConstants.minutesPerTick;
    }

    public double totalSeconds() {
        return (double)this.ticks * TimeConstants.secondsPerTick;
    }

    private long ticks;
    /*
    private static volatile boolean legacyConfigChecked;
    private static volatile boolean legacyMode;
    */
    
    private static TimeSpan interval(final double value, final int scale) {
        if ((long)value == Long.MAX_VALUE)
        {
            throw new Error();
        }
        double num = value * (double)scale;
        double num2 = num + ((value >= 0.0) ? 0.5 : (-0.5));
        if (num2 > (double)TimeConstants.maxMilliSeconds || 
            num2 < (double)TimeConstants.minMilliSeconds)
        {
            throw new Error();
        }
        return new TimeSpan((long)num2 * 10000);
    }

    private void judgeTicksIsMin() {
        if (this.ticks == TimeConstants.minValueTimeSpanTicks)
        {
            throw new Error();
        }
    }

    /*
    private double ticksToOADate(long value) {
        if (value == 0L)
        {
            return 0.0;
        }
        if (value < TimeConstants.ticksPerDay)
        {
            value += TimeConstants.maxSeconds;
        }
        if (value < TimeConstants.oaDateMinAsTicks)
        {
            throw new Error();
        }
        long num = (value - TimeConstants.maxSeconds) / 10000;
        if (num < 0)
        {
            long num2 = num % TimeConstants.millisPerDay;
            if (num2 != 0L)
            {
                num -= (TimeConstants.millisPerDay + num2) * 2;
            }
        }
        return (double)num / (double)TimeConstants.millisPerDay;
    }
    */

    @Override
    public String toString() {
        int num = (int)(this.ticks / TimeConstants.ticksPerDay);
        long num2 = this.ticks % TimeConstants.ticksPerDay;
        if (this.ticks < 0)
        {
            num = -num;
            num2 = -num2;
        }
        return (hours() + " " + minutes() + " " + seconds());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == TimeSpan.class) {
            return ticks == ((TimeSpan) obj).ticks;
        }
        return super.equals(obj);
    }

}
