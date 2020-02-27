package src.datetime;

import src.datetime.DateTime;
import src.datetime.TimeSpan;

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
     * 农历
     */
    private class LunarHolidayStruct {
        public int month;
        public int day;
        public int recess;
        public String holidayName;

        public LunarHolidayStruct(int month, int day, int recess, String name) {
            this.month = month;
            this.day = day;
            this.recess = recess;
            this.holidayName = name;
        }
    }

    private class WeekHolidayStruct {
        public int month;
        public int weekAtMonth;
        public int weekDay;
        public String holidayName;

        public WeekHolidayStruct(int month, int weekAtMonth, int weekDay, String name) {
            this.month = month;
            this.weekAtMonth = weekAtMonth;
            this.weekDay = weekDay;
            this.holidayName = name;
        }
    }

    private DateTime date;
    private DateTime datetime;
    private int cYear;
    private int cMonth;
    private int cDay;
    private boolean cIsLeapMonth; // 当月是否闰月
    private boolean cIsLeapYear; // 当年是否有闰月

    private int MinYear = 1900;
    private int MaxYear = 2050;
    private static DateTime MinDay = new DateTime(1900, 1, 30);
    private static DateTime MaxDay = new DateTime(2049, 12, 31);
    private int GanZhiStartYear = 1864; // 干支计算起始年
    private static DateTime GanZhiStartDay = new DateTime(1899, 12, 22);// 起始日
    private String HZNum = "零一二三四五六七八九";
    private int AnimalStartYear = 1900; // 1900年为鼠年
    private static DateTime ChineseConstellationReferDay = new DateTime(2007, 9, 13);// 28星宿参考值,本日为角

    /**
     * 来源于网上的农历数据 --------------------
     * 
     * 数据结构如下，共使用17位数据 第17位：表示闰月天数，0表示29天，1表示30天 第16位-第5位（共12位）表示12个月，
     * 其中第16位表示第一月，如果该月为30天则为1，29天为0 第4位-第1位（共4位）表示闰月是哪个月， 如果当年没有闰月，则置0
     */
    private static int[] LunarDateArray = new int[] { 0x04BD8, 0x04AE0, 0x0A570, 0x054D5, 0x0D260, 0x0D950, 0x16554,
            0x056A0, 0x09AD0, 0x055D2, 0x04AE0, 0x0A5B6, 0x0A4D0, 0x0D250, 0x1D255, 0x0B540, 0x0D6A0, 0x0ADA2, 0x095B0,
            0x14977, 0x04970, 0x0A4B0, 0x0B4B5, 0x06A50, 0x06D40, 0x1AB54, 0x02B60, 0x09570, 0x052F2, 0x04970, 0x06566,
            0x0D4A0, 0x0EA50, 0x06E95, 0x05AD0, 0x02B60, 0x186E3, 0x092E0, 0x1C8D7, 0x0C950, 0x0D4A0, 0x1D8A6, 0x0B550,
            0x056A0, 0x1A5B4, 0x025D0, 0x092D0, 0x0D2B2, 0x0A950, 0x0B557, 0x06CA0, 0x0B550, 0x15355, 0x04DA0, 0x0A5B0,
            0x14573, 0x052B0, 0x0A9A8, 0x0E950, 0x06AA0, 0x0AEA6, 0x0AB50, 0x04B60, 0x0AAE4, 0x0A570, 0x05260, 0x0F263,
            0x0D950, 0x05B57, 0x056A0, 0x096D0, 0x04DD5, 0x04AD0, 0x0A4D0, 0x0D4D4, 0x0D250, 0x0D558, 0x0B540, 0x0B6A0,
            0x195A6, 0x095B0, 0x049B0, 0x0A974, 0x0A4B0, 0x0B27A, 0x06A50, 0x06D40, 0x0AF46, 0x0AB60, 0x09570, 0x04AF5,
            0x04970, 0x064B0, 0x074A3, 0x0EA50, 0x06B58, 0x055C0, 0x0AB60, 0x096D5, 0x092E0, 0x0C960, 0x0D954, 0x0D4A0,
            0x0DA50, 0x07552, 0x056A0, 0x0ABB7, 0x025D0, 0x092D0, 0x0CAB5, 0x0A950, 0x0B4A0, 0x0BAA4, 0x0AD50, 0x055D9,
            0x04BA0, 0x0A5B0, 0x15176, 0x052B0, 0x0A930, 0x07954, 0x06AA0, 0x0AD50, 0x05B52, 0x04B60, 0x0A6E6, 0x0A4E0,
            0x0D260, 0x0EA65, 0x0D530, 0x05AA0, 0x076A3, 0x096D0, 0x04BD7, 0x04AD0, 0x0A4D0, 0x1D0B6, 0x0D250, 0x0D520,
            0x0DD45, 0x0B5A0, 0x056D0, 0x055B2, 0x049B0, 0x0A577, 0x0A4B0, 0x0AA50, 0x1B255, 0x06D20, 0x0ADA0,
            0x14B63 };

    /**
     * 星座名称
     */
    private static String[] constellationName = { "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座",
            "水瓶座", "双鱼座" };

    /**
     * 二十四节气
     */
    private static String[] lunarHolidayName = { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
            "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

    /**
     * 二十八星宿
     */
    private static String[] chineseConstellationName = {
            // 四 五 六 日 一 二 三
            "角木蛟", "亢金龙", "女土蝠", "房日兔", "心月狐", "尾火虎", "箕水豹", "斗木獬", "牛金牛", "氐土貉", "虚日鼠", "危月燕", "室火猪", "壁水獝", "奎木狼",
            "娄金狗", "胃土彘", "昴日鸡", "毕月乌", "觜火猴", "参水猿", "井木犴", "鬼金羊", "柳土獐", "星日马", "张月鹿", "翼火蛇", "轸水蚓" };

    /**
     * 节气数据
     */
    private static String[] SolarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种",
            "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

    /**
     * 节气数据编码
     */
    private static int[] sTermInfo = new int[] { 0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551,
            218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532,
            504758 };

    private static String ganStr = "甲乙丙丁戊己庚辛壬癸";
    private static String zhiStr = "子丑寅卯辰巳午未申酉戌亥";
    private static String animalStr = "鼠牛虎兔龙蛇马羊猴鸡狗猪";
    private static String nStr1 = "日一二三四五六七八九";
    private static String nStr2 = "初十廿卅";
    private static String[] monthString = { "出错", "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月",
            "腊月" };

    private static SolarHolidayStruct[] sHolidayInfo = new SolarHolidayStruct[] { new SolarHolidayStruct(1, 1, 1, "元旦"),
            new SolarHolidayStruct(2, 2, 0, "世界湿地日"), new SolarHolidayStruct(2, 10, 0, "国际气象节"),
            new SolarHolidayStruct(2, 14, 0, "情人节"), new SolarHolidayStruct(3, 1, 0, "国际海豹日"),
            new SolarHolidayStruct(3, 5, 0, "学雷锋纪念日"), new SolarHolidayStruct(3, 8, 0, "妇女节"),
            new SolarHolidayStruct(3, 12, 0, "植树节 孙中山逝世纪念日"), new SolarHolidayStruct(3, 14, 0, "国际警察日"),
            new SolarHolidayStruct(3, 15, 0, "消费者权益日"), new SolarHolidayStruct(3, 17, 0, "中国国医节 国际航海日"),
            new SolarHolidayStruct(3, 21, 0, "世界森林日 消除种族歧视国际日 世界儿歌日"), new SolarHolidayStruct(3, 22, 0, "世界水日"),
            new SolarHolidayStruct(3, 24, 0, "世界防治结核病日"), new SolarHolidayStruct(4, 1, 0, "愚人节"),
            new SolarHolidayStruct(4, 7, 0, "世界卫生日"), new SolarHolidayStruct(4, 22, 0, "世界地球日"),
            new SolarHolidayStruct(5, 1, 1, "劳动节"), new SolarHolidayStruct(5, 2, 1, "劳动节假日"),
            new SolarHolidayStruct(5, 3, 1, "劳动节假日"), new SolarHolidayStruct(5, 4, 0, "青年节"),
            new SolarHolidayStruct(5, 8, 0, "世界红十字日"), new SolarHolidayStruct(5, 12, 0, "国际护士节"),
            new SolarHolidayStruct(5, 31, 0, "世界无烟日"), new SolarHolidayStruct(6, 1, 0, "国际儿童节"),
            new SolarHolidayStruct(6, 5, 0, "世界环境保护日"), new SolarHolidayStruct(6, 26, 0, "国际禁毒日"),
            new SolarHolidayStruct(7, 1, 0, "建党节 香港回归纪念 世界建筑日"), new SolarHolidayStruct(7, 11, 0, "世界人口日"),
            new SolarHolidayStruct(8, 1, 0, "建军节"), new SolarHolidayStruct(8, 8, 0, "中国男子节 父亲节"),
            new SolarHolidayStruct(8, 15, 0, "抗日战争胜利纪念"), new SolarHolidayStruct(9, 9, 0, "毛主席逝世纪念"),
            new SolarHolidayStruct(9, 10, 0, "教师节"), new SolarHolidayStruct(9, 18, 0, "九·一八事变纪念日"),
            new SolarHolidayStruct(9, 20, 0, "国际爱牙日"), new SolarHolidayStruct(9, 27, 0, "世界旅游日"),
            new SolarHolidayStruct(9, 28, 0, "孔子诞辰"), new SolarHolidayStruct(10, 1, 1, "国庆节 国际音乐日"),
            new SolarHolidayStruct(10, 2, 1, "国庆节假日"), new SolarHolidayStruct(10, 3, 1, "国庆节假日"),
            new SolarHolidayStruct(10, 6, 0, "老人节"), new SolarHolidayStruct(10, 24, 0, "联合国日"),
            new SolarHolidayStruct(11, 10, 0, "世界青年节"), new SolarHolidayStruct(11, 12, 0, "孙中山诞辰纪念"),
            new SolarHolidayStruct(12, 1, 0, "世界艾滋病日"), new SolarHolidayStruct(12, 3, 0, "世界残疾人日"),
            new SolarHolidayStruct(12, 20, 0, "澳门回归纪念"), new SolarHolidayStruct(12, 24, 0, "平安夜"),
            new SolarHolidayStruct(12, 25, 0, "圣诞节"), new SolarHolidayStruct(12, 26, 0, "毛主席诞辰纪念") };

    private static LunarHolidayStruct[] lHolidayInfo = new LunarHolidayStruct[] { new LunarHolidayStruct(1, 1, 1, "春节"),
            new LunarHolidayStruct(1, 15, 0, "元宵节"), new LunarHolidayStruct(5, 5, 0, "端午节"),
            new LunarHolidayStruct(7, 7, 0, "七夕情人节"), new LunarHolidayStruct(7, 15, 0, "中元节 盂兰盆节"),
            new LunarHolidayStruct(8, 15, 0, "中秋节"), new LunarHolidayStruct(9, 9, 0, "重阳节"),
            new LunarHolidayStruct(12, 8, 0, "腊八节"), new LunarHolidayStruct(12, 23, 0, "北方小年(扫房)"),
            new LunarHolidayStruct(12, 24, 0, "南方小年(掸尘)"),
            // new LunarHolidayStruct(12, 30, 0, "除夕") //注意除夕需要其它方法进行计算
    };

    private static WeekHolidayStruct[] wHolidayInfo = new WeekHolidayStruct[]{
        new WeekHolidayStruct(5, 2, 1, "母亲节"),
        new WeekHolidayStruct(5, 3, 1, "全国助残日"),
        new WeekHolidayStruct(6, 3, 1, "父亲节"),
        new WeekHolidayStruct(9, 3, 3, "国际和平日"),
        new WeekHolidayStruct(9, 4, 1, "国际聋人节"),
        new WeekHolidayStruct(10, 1, 2, "国际住房日"),
        new WeekHolidayStruct(10, 1, 4, "国际减轻自然灾害日"),
        new WeekHolidayStruct(11, 4, 5, "感恩节")
    };

    /**
     * 用一个标准的公历日期来初始化
     * @param dt
     * 日期
     */
    public ChinaDate(DateTime dt)
    {
        int i;
        int leap;
        int temp;
        int offset;

        CheckDateLimit(dt);

        this.date = dt.date();
        this.datetime = dt;

        //农历日期计算部分
        leap = 0;
        temp = 0;

        //计算两天的基本差距
        TimeSpan ts = date.subtract(MinDay);
        offset = ts.days();

        for (i = MinYear; i <= MaxYear; i++)
        {
            //求当年农历年天数
            temp = GetChineseYearDays(i);
            if (offset - temp < 1)
                break;
            else
            {
                offset = offset - temp;
            }
        }
        cYear = i;

        //计算该年闰哪个月
        leap = GetChineseLeapMonth(cYear);

        //设定当年是否有闰月
        if (leap > 0)
        {
            cIsLeapYear = true;
        }
        else
        {
            cIsLeapYear = false;
        }

        cIsLeapMonth = false;
        for (i = 1; i <= 12; i++)
        {
            //闰月
            if ((leap > 0) && (i == leap + 1) && (cIsLeapMonth == false))
            {
                cIsLeapMonth = true;
                i = i - 1;
                temp = GetChineseLeapMonthDays(cYear); //计算闰月天数
            }
            else
            {
                cIsLeapMonth = false;
                temp = GetChineseMonthDays(cYear, i);  //计算非闰月天数
            }

            offset = offset - temp;
            if (offset <= 0) break;
        }

        offset = offset + temp;
        cMonth = i;
        cDay = offset;
    }

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
