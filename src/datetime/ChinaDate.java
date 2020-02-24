package src.datetime;

public class ChinaDate {

    /**
     * 阳历
     **/
    private class SolarHolidayStruct {
        public int month;
        public int day;
        public int recess; // 假期长度
        public String holidayName;

        private SolarHolidayStruct() {
        }

        public SolarHolidayStruct(int month, int day, int recess, String name) {
            this.month = month;
            this.day = day;
            this.recess = recess;
            this.holidayName = name;
        }
    }

    /**
     * 农历年(整型)
     */
    private final int cnIntYear = 0;
    /**
     * 农历月份(整型)
     */
    private final int cnIntMonth = 0;
    /**
     * 农历天(整型)
     */
    private final int cnIntDay = 0;
    /**
     * 农历年(支干)
     */
    private final String cnStrYear = "";
    /**
     * 农历月份(字符)
     */
    private final String cnStrMonth = "";
    /**
     * 农历天(字符)
     */
    private final String cnStrDay = "";
    /**
     * 农历属象
     */
    private final String cnAnm = "";
    /**
     * 二十四节气
     */
    private final String cnSolarTerm = "";
    /**
     * 阴历节日
     */
    private final String cnFtvl = "";

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
