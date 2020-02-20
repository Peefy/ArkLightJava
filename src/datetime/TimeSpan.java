package src.datetime;

/**
 * Timespan
 */
public class TimeSpan {

    TimeSpan(final long ticks) {
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

    }

    public static TimeSpan FromSeconds(final double value) {

    }

    public static TimeSpan fromTicks(final long value) {

    }

    public static int compare(final TimeSpan t1, final TimeSpan t2) {

    }

    public static long timeToTicks(final int hour, final int minute, final int second) {

    }  

    public TimeSpan add(final TimeSpan ts) {

    }

    public TimeSpan subtract(final TimeSpan ts) {
        
    }

    public boolean equals(final TimeSpan ts) {

    }

    public int compareTo(final TimeSpan value) {

    }

    public int compareTo(final Object value) {

    }

    public TimeSpan duration() {

    }

    public TimeSpan negate() {

    }

    public long ticks() {}
    public int days() {}
    public int hours() {}
    public int milliseconds() {}
    public int minutes() {}
    public int seconds() {}
    public double totalDays() {}
    public double totalHours() {}
    public double totalMilliseconds() {}
    public double totalMinutes() {}
    public double totalSeconds() {}

    private long ticks;
    private static volatile boolean legacyConfigChecked;
    private static volatile boolean legacyMode;
    
    private static TimeSpan interval(final double value, final int scale) {

    }

    private void judgeTicksIsMin() {

    }

    private double ticksToOADate(final long value) {

    }

    @Override
    public String toString() {
        return super.toString();
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

    static {
        legacyMode = false;
        legacyConfigChecked = false;
    }

}
