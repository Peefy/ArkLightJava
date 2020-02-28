package src.datetime;

public class ChinaDate {

    /**
     * 阳历
     **/
    private static class solarHolidayStruct {
        public int month;
        public int day;
        public int recess; // 假期长度
        public String holidayName;

        public solarHolidayStruct(int month, int day, int recess, String name) {
            this.month = month;
            this.day = day;
            this.recess = recess;
            this.holidayName = name;
        }
    }

    /**
     * 农历
     */
    private static class lunarHolidayStruct {
        public int month;
        public int day;
        public int recess;
        public String holidayName;

        public lunarHolidayStruct(int month, int day, int recess, String name) {
            this.month = month;
            this.day = day;
            this.recess = recess;
            this.holidayName = name;
        }
    }

    private static class weekHolidayStruct {
        public int month;
        public int weekAtMonth;
        public int weekDay;
        public String holidayName;

        public weekHolidayStruct(int month, int weekAtMonth, int weekDay, String name) {
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

    private int minYear = 1900;
    private int maxYear = 2050;
    private static DateTime minDay = new DateTime(1900, 1, 30);
    private static DateTime maxDay = new DateTime(2049, 12, 31);
    private int ganZhiStartYear = 1864; // 干支计算起始年
    private static DateTime ganZhiStartDay = new DateTime(1899, 12, 22);// 起始日
    private String hzNum = "零一二三四五六七八九";
    private int animalStartYear = 1900; // 1900年为鼠年
    private static DateTime chineseconstellationReferDay = new DateTime(2007, 9, 13);// 28星宿参考值,本日为角

    /**
     * 来源于网上的农历数据 --------------------
     * 
     * 数据结构如下，共使用17位数据 第17位：表示闰月天数，0表示29天，1表示30天 第16位-第5位（共12位）表示12个月，
     * 其中第16位表示第一月，如果该月为30天则为1，29天为0 第4位-第1位（共4位）表示闰月是哪个月， 如果当年没有闰月，则置0
     */
    private static int[] lunarDateArray = new int[] { 0x04BD8, 0x04AE0, 0x0A570, 0x054D5, 0x0D260, 0x0D950, 0x16554,
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
     * 二十八星宿
     */
    private static String[] chineseconstellationName = {
            // 四 五 六 日 一 二 三
            "角木蛟", "亢金龙", "女土蝠", "房日兔", "心月狐", "尾火虎", "箕水豹", "斗木獬", "牛金牛", "氐土貉", "虚日鼠", "危月燕", "室火猪", "壁水獝", "奎木狼",
            "娄金狗", "胃土彘", "昴日鸡", "毕月乌", "觜火猴", "参水猿", "井木犴", "鬼金羊", "柳土獐", "星日马", "张月鹿", "翼火蛇", "轸水蚓" };

    /**
     * 节气数据
     */
    private static String[] solarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种",
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

    private static solarHolidayStruct[] sHolidayInfo = new solarHolidayStruct[] { new solarHolidayStruct(1, 1, 1, "元旦"),
            new solarHolidayStruct(2, 2, 0, "世界湿地日"), new solarHolidayStruct(2, 10, 0, "国际气象节"),
            new solarHolidayStruct(2, 14, 0, "情人节"), new solarHolidayStruct(3, 1, 0, "国际海豹日"),
            new solarHolidayStruct(3, 5, 0, "学雷锋纪念日"), new solarHolidayStruct(3, 8, 0, "妇女节"),
            new solarHolidayStruct(3, 12, 0, "植树节 孙中山逝世纪念日"), new solarHolidayStruct(3, 14, 0, "国际警察日"),
            new solarHolidayStruct(3, 15, 0, "消费者权益日"), new solarHolidayStruct(3, 17, 0, "中国国医节 国际航海日"),
            new solarHolidayStruct(3, 21, 0, "世界森林日 消除种族歧视国际日 世界儿歌日"), new solarHolidayStruct(3, 22, 0, "世界水日"),
            new solarHolidayStruct(3, 24, 0, "世界防治结核病日"), new solarHolidayStruct(4, 1, 0, "愚人节"),
            new solarHolidayStruct(4, 7, 0, "世界卫生日"), new solarHolidayStruct(4, 22, 0, "世界地球日"),
            new solarHolidayStruct(5, 1, 1, "劳动节"), new solarHolidayStruct(5, 2, 1, "劳动节假日"),
            new solarHolidayStruct(5, 3, 1, "劳动节假日"), new solarHolidayStruct(5, 4, 0, "青年节"),
            new solarHolidayStruct(5, 8, 0, "世界红十字日"), new solarHolidayStruct(5, 12, 0, "国际护士节"),
            new solarHolidayStruct(5, 31, 0, "世界无烟日"), new solarHolidayStruct(6, 1, 0, "国际儿童节"),
            new solarHolidayStruct(6, 5, 0, "世界环境保护日"), new solarHolidayStruct(6, 26, 0, "国际禁毒日"),
            new solarHolidayStruct(7, 1, 0, "建党节 香港回归纪念 世界建筑日"), new solarHolidayStruct(7, 11, 0, "世界人口日"),
            new solarHolidayStruct(8, 1, 0, "建军节"), new solarHolidayStruct(8, 8, 0, "中国男子节 父亲节"),
            new solarHolidayStruct(8, 15, 0, "抗日战争胜利纪念"), new solarHolidayStruct(9, 9, 0, "毛主席逝世纪念"),
            new solarHolidayStruct(9, 10, 0, "教师节"), new solarHolidayStruct(9, 18, 0, "九·一八事变纪念日"),
            new solarHolidayStruct(9, 20, 0, "国际爱牙日"), new solarHolidayStruct(9, 27, 0, "世界旅游日"),
            new solarHolidayStruct(9, 28, 0, "孔子诞辰"), new solarHolidayStruct(10, 1, 1, "国庆节 国际音乐日"),
            new solarHolidayStruct(10, 2, 1, "国庆节假日"), new solarHolidayStruct(10, 3, 1, "国庆节假日"),
            new solarHolidayStruct(10, 6, 0, "老人节"), new solarHolidayStruct(10, 24, 0, "联合国日"),
            new solarHolidayStruct(11, 10, 0, "世界青年节"), new solarHolidayStruct(11, 12, 0, "孙中山诞辰纪念"),
            new solarHolidayStruct(12, 1, 0, "世界艾滋病日"), new solarHolidayStruct(12, 3, 0, "世界残疾人日"),
            new solarHolidayStruct(12, 20, 0, "澳门回归纪念"), new solarHolidayStruct(12, 24, 0, "平安夜"),
            new solarHolidayStruct(12, 25, 0, "圣诞节"), new solarHolidayStruct(12, 26, 0, "毛主席诞辰纪念") };

    private static lunarHolidayStruct[] lHolidayInfo = new lunarHolidayStruct[] { new lunarHolidayStruct(1, 1, 1, "春节"),
            new lunarHolidayStruct(1, 15, 0, "元宵节"), new lunarHolidayStruct(5, 5, 0, "端午节"),
            new lunarHolidayStruct(7, 7, 0, "七夕情人节"), new lunarHolidayStruct(7, 15, 0, "中元节 盂兰盆节"),
            new lunarHolidayStruct(8, 15, 0, "中秋节"), new lunarHolidayStruct(9, 9, 0, "重阳节"),
            new lunarHolidayStruct(12, 8, 0, "腊八节"), new lunarHolidayStruct(12, 23, 0, "北方小年(扫房)"),
            new lunarHolidayStruct(12, 24, 0, "南方小年(掸尘)"),
            // new lunarHolidayStruct(12, 30, 0, "除夕") //注意除夕需要其它方法进行计算
    };

    private static weekHolidayStruct[] wHolidayInfo = new weekHolidayStruct[] { new weekHolidayStruct(5, 2, 1, "母亲节"),
            new weekHolidayStruct(5, 3, 1, "全国助残日"), new weekHolidayStruct(6, 3, 1, "父亲节"),
            new weekHolidayStruct(9, 3, 3, "国际和平日"), new weekHolidayStruct(9, 4, 1, "国际聋人节"),
            new weekHolidayStruct(10, 1, 2, "国际住房日"), new weekHolidayStruct(10, 1, 4, "国际减轻自然灾害日"),
            new weekHolidayStruct(11, 4, 5, "感恩节") };

    /**
     * 用一个标准的公历日期来初始化
     * 
     * @param dt 日期
     */
    public ChinaDate(DateTime dt) {
        int i;
        int leap;
        int temp;
        int offset;

        checkDateLimit(dt);

        this.date = dt.date();
        this.datetime = dt;

        // 农历日期计算部分
        leap = 0;
        temp = 0;

        // 计算两天的基本差距
        TimeSpan ts = date.subtract(minDay);
        offset = ts.days();

        for (i = minYear; i <= maxYear; i++) {
            // 求当年农历年天数
            temp = getchineseYearDays(i);
            if (offset - temp < 1)
                break;
            else {
                offset = offset - temp;
            }
        }
        cYear = i;

        // 计算该年闰哪个月
        leap = getchineseLeapMonth(cYear);

        // 设定当年是否有闰月
        if (leap > 0) {
            cIsLeapYear = true;
        } else {
            cIsLeapYear = false;
        }

        cIsLeapMonth = false;
        for (i = 1; i <= 12; i++) {
            // 闰月
            if ((leap > 0) && (i == leap + 1) && (cIsLeapMonth == false)) {
                cIsLeapMonth = true;
                i = i - 1;
                temp = getchineseLeapMonthDays(cYear); // 计算闰月天数
            } else {
                cIsLeapMonth = false;
                temp = getchineseMonthDays(cYear, i); // 计算非闰月天数
            }

            offset = offset - temp;
            if (offset <= 0)
                break;
        }

        offset = offset + temp;
        cMonth = i;
        cDay = offset;
    }

    /**
     * 用农历的日期来初使化
     * 
     * @param cy            农历年
     * @param cm            农历月
     * @param cd            农历日
     * @param leapMonthFlag 闰月标志
     */
    public ChinaDate(int cy, int cm, int cd, boolean leapMonthFlag) {
        int i, leap, Temp, offset;

        checkchineseDateLimit(cy, cm, cd, leapMonthFlag);

        cYear = cy;
        cMonth = cm;
        cDay = cd;

        offset = 0;

        for (i = minYear; i < cy; i++) {
            // 求当年农历年天数
            Temp = getchineseYearDays(i);
            offset = offset + Temp;
        }

        // 计算该年应该闰哪个月
        leap = getchineseLeapMonth(cy);
        if (leap != 0) {
            this.cIsLeapYear = true;
        } else {
            this.cIsLeapYear = false;
        }

        if (cm != leap) {
            // 当前日期并非闰月
            cIsLeapMonth = false;
        } else {
            // 使用用户输入的是否闰月月份
            cIsLeapMonth = leapMonthFlag;
        }

        // 当年没有闰月||计算月份小于闰月
        if ((cIsLeapYear == false) || (cm < leap)) {
            for (i = 1; i < cm; i++) {
                Temp = getchineseMonthDays(cy, i);// 计算非闰月天数
                offset = offset + Temp;
            }

            // 检查日期是否大于最大天
            if (cd > getchineseMonthDays(cy, cm)) {
                throw new Error("不合法的农历日期");
            }
            // 加上当月的天数
            offset = offset + cd;
        }

        // 是闰年，且计算月份大于或等于闰月
        else {
            for (i = 1; i < cm; i++) {
                // 计算非闰月天数
                Temp = getchineseMonthDays(cy, i);
                offset = offset + Temp;
            }

            // 计算月大于闰月
            if (cm > leap) {
                Temp = getchineseLeapMonthDays(cy); // 计算闰月天数
                offset = offset + Temp; // 加上闰月天数

                if (cd > getchineseMonthDays(cy, cm)) {
                    throw new Error("不合法的农历日期");
                }
                offset = offset + cd;
            }

            // 计算月等于闰月
            else {
                // 如果需要计算的是闰月，则应首先加上与闰月对应的普通月的天数
                if (this.cIsLeapMonth == true) // 计算月为闰月
                {
                    Temp = getchineseMonthDays(cy, cm); // 计算非闰月天数
                    offset = offset + Temp;
                }

                if (cd > getchineseLeapMonthDays(cy)) {
                    throw new Error("不合法的农历日期");
                }
                offset = offset + cd;
            }
        }
        date = minDay.addDays(offset);
    }

    /**
     * 传回农历y年m月的总天数
     * 
     * @param year  年
     * @param month 月
     * @return
     */
    private int getchineseMonthDays(int year, int month) {
        if (bitTest32((lunarDateArray[year - minYear] & 0x0000FFFF), (16 - month))) {
            return 30;
        } else {
            return 29;
        }
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     * 
     * @param year
     * @return
     */
    private int getchineseLeapMonth(int year) {
        return lunarDateArray[year - minYear] & 0xF;
    }

    /**
     * 传回农历y年闰月的天数
     * 
     * @param year
     * @return
     */
    private int getchineseLeapMonthDays(int year) {
        if (getchineseLeapMonth(year) != 0) {
            if ((lunarDateArray[year - minYear] & 0x10000) != 0) {
                return 30;
            } else {
                return 29;
            }
        } else {
            return 0;
        }
    }

    /**
     * 取农历年一年的天数
     * 
     * @param year
     * @return
     */
    private int getchineseYearDays(int year) {
        int i, f, sumDay, info;

        sumDay = 348; // 29天*12个月
        i = 0x8000;
        info = lunarDateArray[year - minYear] & 0x0FFFF;

        // 计算12个月中有多少天为30天
        for (int m = 0; m < 12; m++) {
            f = info & i;
            if (f != 0) {
                sumDay++;
            }
            i = i >> 1;
        }
        return sumDay + getchineseLeapMonthDays(year);
    }

    /**
     * 获得当前时间的时辰
     * 
     * @param dt 当前时间
     * @return 时辰
     */
    private String getchineseHour(DateTime dt) {
        int hour, minute, offset, i;
        int indexGan;
        String tmpGan;

        // 计算时辰的地支
        hour = dt.hour(); // 获得当前时间小时
        minute = dt.minute(); // 获得当前时间分钟

        if (minute != 0)
            hour += 1;
        offset = hour / 2;
        if (offset >= 12)
            offset = 0;
        // zhiHour = zhiStr[offset].ToString();

        // 计算天干
        TimeSpan ts = this.date.subtract(ganZhiStartDay);
        i = ts.days() % 60;

        // ganStr[i % 10] 为日的天干,(n*2-1) %10得出地支对应,n从1开始
        indexGan = ((i % 10 + 1) * 2 - 1) % 10 - 1;

        tmpGan = ganStr.substring(indexGan) + ganStr.substring(0, indexGan + 2);// 凑齐12位
        // ganHour = ganStr[((i % 10 + 1) * 2 - 1) % 10 - 1].ToString();

        return String.valueOf(tmpGan.charAt(offset)) + String.valueOf(zhiStr.charAt(offset));
    }

    /**
     * 检查公历日期是否符合要求
     * 
     * @param dt
     */
    private void checkDateLimit(DateTime dt) {
        if ((dt.getInternalTicks() < minDay.getInternalTicks())
                || (dt.getInternalTicks() > maxDay.getInternalTicks())) {
            throw new Error("超出可转换的日期");
        }
    }

    /**
     * 检查农历日期是否合理
     * 
     * @param year
     * @param month
     * @param day
     * @param leapMonth
     */
    private void checkchineseDateLimit(int year, int month, int day, boolean leapMonth) {
        if ((year < minYear) || (year > maxYear)) {
            throw new Error("非法农历日期");
        }
        if ((month < 1) || (month > 12)) {
            throw new Error("非法农历日期");
        }
        if ((day < 1) || (day > 30)) // 中国的月最多30天
        {
            throw new Error("非法农历日期");
        }
        int leap = getchineseLeapMonth(year);// 计算该年应该闰哪个月
        if ((leapMonth == true) && (month != leap)) {
            throw new Error("非法农历日期");
        }
    }

    /**
     * 将0-9转成汉字形式
     * 
     * @param n
     * @return
     */
    private String convertNumTochineseNum(char n) {
        if ((n < '0') || (n > '9'))
            return "";
        switch (n) {
            case '0':
                return String.valueOf(hzNum.charAt(0));
            case '1':
                return String.valueOf(hzNum.charAt(1));
            case '2':
                return String.valueOf(hzNum.charAt(2));
            case '3':
                return String.valueOf(hzNum.charAt(3));
            case '4':
                return String.valueOf(hzNum.charAt(4));
            case '5':
                return String.valueOf(hzNum.charAt(5));
            case '6':
                return String.valueOf(hzNum.charAt(6));
            case '7':
                return String.valueOf(hzNum.charAt(7));
            case '8':
                return String.valueOf(hzNum.charAt(8));
            case '9':
                return String.valueOf(hzNum.charAt(9));
            default:
                return "";
        }
    }

    /**
     * 测试某位是否为真
     * 
     * @param num
     * @param bitpostion
     * @return
     */
    private boolean bitTest32(int num, int bitpostion) {
        if ((bitpostion > 31) || (bitpostion < 0))
            throw new Error("Error Param: bitpostion[0-31]:" + String.valueOf(bitpostion));

        int bit = 1 << bitpostion;

        if ((num & bit) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将星期几转成数字表示
     * 
     * @param dayOfWeek
     * @return
     */
    private int convertDayOfWeek(DateTime.DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }

    /**
     * 比较当天是不是指定的第周几
     * 
     * @param date
     * @param month
     * @param week
     * @param day
     * @return
     */
    private boolean compareweekDayHoliday(DateTime date, int month, int week, int day) {
        boolean ret = false;

        if (date.month() == month) // 月份相同
        {
            if (convertDayOfWeek(date.getDayOfWeek()) == day) // 星期几相同
            {
                DateTime firstDay = new DateTime(date.year(), date.month(), 1);// 生成当月第一天
                int i = convertDayOfWeek(firstDay.getDayOfWeek());
                int firWeekDays = 7 - convertDayOfWeek(firstDay.getDayOfWeek()) + 1; // 计算第一周剩余天数

                if (i > day) {
                    if ((week - 1) * 7 + day + firWeekDays == date.day()) {
                        ret = true;
                    }
                } else {
                    if (day + firWeekDays + (week - 2) * 7 == date.day()) {
                        ret = true;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 计算中国农历节日
     * 
     * @return
     */
    public String newCalendarHoliday() {
        String tempStr = "";
        if (this.cIsLeapMonth == false) // 闰月不计算节日
        {
            for (lunarHolidayStruct lh : lHolidayInfo) {
                if ((lh.month == this.cMonth) && (lh.day == this.cDay)) {

                    tempStr = lh.holidayName;
                    break;

                }
            }

            // 对除夕进行特别处理
            if (this.cMonth == 12) {
                int i = getchineseMonthDays(this.cYear, 12); // 计算当年农历12月的总天数
                if (this.cDay == i) // 如果为最后一天
                {
                    tempStr = "除夕";
                }
            }
        }
        return tempStr;

    }

    /**
     * 按某月第几周第几日计算的节日
     * 
     * @return
     */
    public String weekDayHoliday() {

        String tempStr = "";
        for (weekHolidayStruct wh : wHolidayInfo) {
            if (compareweekDayHoliday(date, wh.month, wh.weekAtMonth, wh.weekDay)) {
                tempStr = wh.holidayName;
                break;
            }
        }
        return tempStr;

    }

    /**
     * 按公历日计算的节日
     * 
     * @return
     */
    public String dateHoliday() {

        String tempStr = "";

        for (solarHolidayStruct sh : sHolidayInfo) {
            if ((sh.month == date.month()) && (sh.day == date.day())) {
                tempStr = sh.holidayName;
                break;
            }
        }
        return tempStr;

    }

    /**
     * 设置对应的公历日期
     * 
     * @param date
     */
    public void setDate(DateTime date) {
        this.date = date;
    }

    /**
     * 取对应的公历日期
     * 
     * @return
     */
    public DateTime getDate() {
        return this.date;
    }

    /**
     * 取星期几
     * 
     * @return
     */
    public DateTime.DayOfWeek weekDay() {
        return date.getDayOfWeek();
    }

    /**
     * 周几的字符
     */
    public String weekDayStr() {

        switch (date.getDayOfWeek().getValue()) {
            case 0:
                return "星期日";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            default:
                return "星期六";
        }
    }

    /**
     * 公历日期中文表示法 如一九九七年七月一日
     * 
     * @return
     */
    public String dateString() {
        return "公元" + this.date.toString();
    }

    /**
     * 当前是否公历闰年
     * 
     * @return
     */
    public boolean isLeapYear() {
        return DateTime.isLeapYear(this.date.year());
    }

    /**
     * 28星宿计算
     * 
     * @return
     */
    public String chineseconstellation() {

        int offset = 0;
        int modStarDay = 0;

        TimeSpan ts = this.date.subtract(chineseconstellationReferDay);
        offset = ts.days();
        modStarDay = offset % 28;
        return (modStarDay >= 0 ? chineseconstellationName[modStarDay] : chineseconstellationName[27 + modStarDay]);
    }

    /**
     * 时辰
     * 
     * @return
     */
    public String chineseHour() {

        return getchineseHour(datetime);

    }

    /**
     * 是否闰月
     * 
     * @return
     */
    public boolean ischineseLeapMonth() {
        return this.cIsLeapMonth;
    }

    /**
     * 当年是否有闰月
     * 
     * @return
     */
    public boolean ischineseLeapYear() {
        return this.cIsLeapYear;
    }

    /**
     * 农历日
     * 
     * @return
     */
    public int chineseDay() {
        return this.cDay;
    }

    /**
     * 农历日中文表示
     * 
     * @return
     */
    public String chineseDayString() {

        switch (this.cDay) {
            case 0:
                return "";
            case 10:
                return "初十";
            case 20:
                return "二十";
            case 30:
                return "三十";
            default:
                return String.valueOf(nStr2.charAt((int) (cDay / 10)) + String.valueOf(nStr1.charAt(cDay % 10)));

        }

    }

    /**
     * 农历的月份
     * 
     * @return
     */
    public int chineseMonth() {
        return this.cMonth;
    }

    /**
     * 农历月份字符串
     * 
     * @return
     */
    public String chineseMonthString() {
        return monthString[this.cMonth];
    }

    /**
     * 取农历年份
     * 
     * @return
     */
    public int chineseYear() {
        return this.cYear;
    }

    /**
     * 取农历年字符串如，一九九七年
     * 
     * @return
     */
    public String chineseYearString() {
        String tempStr = "";
        String num = String.valueOf(this.cYear);
        for (int i = 0; i < 4; i++) {
            tempStr += convertNumTochineseNum(num.charAt(i));
        }
        return tempStr + "年";
    }

    /**
     * 取农历日期表示法：农历一九九七年正月初五
     */
    public String chineseDateString() {
        if (this.cIsLeapMonth == true) {
            return "农历" + chineseYearString() + "闰" + chineseMonthString() + chineseDayString();
        } else {
            return "农历" + chineseYearString() + chineseMonthString() + chineseDayString();
        }
    }

    /**
     * 节气的定法有两种。古代历法采用的称为"恒气"，即按时间把一年等分为24份， 每一节气平均得15天有余，所以又称"平气"。现代农历采用的称为"定气"，即
     * 按地球在轨道上的位置为标准，一周360°，两节气之间相隔15°。由于冬至时地 球位于近日点附近，运动速度较快，因而太阳在黄道上移动15°的时间不到15天。
     * 夏至前后的情况正好相反，太阳在黄道上移动较慢，一个节气达16天之多。采用 定气时可以保证春、秋两分必然在昼夜平分的那两天。
     * 
     * @return 定气法计算二十四节气,二十四节气是按地球公转来计算的，并非是阴历计算的
     */
    public String chineseTwentyFourDay() {
        DateTime baseDateAndTime = new DateTime(1900, 1, 6, 2, 5, 0); // #1/6/1900 2:05:00 AM#
        DateTime newDate;
        double num;
        int y;
        String tempStr = "";

        y = this.date.year();

        for (int i = 1; i <= 24; i++) {
            num = 525948.76 * (y - 1900) + sTermInfo[i - 1];

            newDate = baseDateAndTime.addMinutes(num);// 按分钟计算
            if (newDate.dayOfYear() == date.dayOfYear()) {
                tempStr = solarTerm[i - 1];
                break;
            }
        }
        return tempStr;
    }

    /**
     * 当前日期前一个最近节气
     * 
     * @return
     */
    public String chineseTwentyFourPrevDay() {

        DateTime baseDateAndTime = new DateTime(1900, 1, 6, 2, 5, 0); // #1/6/1900 2:05:00 AM#
        DateTime newDate;
        double num;
        int y;
        String tempStr = "";

        y = this.date.year();

        for (int i = 24; i >= 1; i--) {
            num = 525948.76 * (y - 1900) + sTermInfo[i - 1];

            newDate = baseDateAndTime.addMinutes(num);// 按分钟计算

            if (newDate.dayOfYear() < date.dayOfYear()) {
                tempStr = String.format("%s[%s]", solarTerm[i - 1], newDate.toString());
                break;
            }
        }

        return tempStr;

    }

    /**
     * 当前日期后一个最近节气
     * 
     * @return
     */
    public String chineseTwentyFourNextDay() {

        DateTime baseDateAndTime = new DateTime(1900, 1, 6, 2, 5, 0); // #1/6/1900 2:05:00 AM#
        DateTime newDate;
        double num;
        int y;
        String tempStr = "";

        y = this.date.year();

        for (int i = 1; i <= 24; i++) {
            num = 525948.76 * (y - 1900) + sTermInfo[i - 1];

            newDate = baseDateAndTime.addMinutes(num);// 按分钟计算

            if (newDate.dayOfYear() > date.dayOfYear()) {
                tempStr = String.format("%s[%s]", solarTerm[i - 1], newDate.toString());
                break;
            }
        }
        return tempStr;

    }

    /**
     * 计算指定日期的星座序号
     * 
     * @return
     */
    public String constellation() {

        int index = 0;
        int y, m, d;
        y = date.year();
        m = date.month();
        d = date.day();
        y = m * 100 + d;

        if (((y >= 321) && (y <= 419))) {
            index = 0;
        } else if ((y >= 420) && (y <= 520)) {
            index = 1;
        } else if ((y >= 521) && (y <= 620)) {
            index = 2;
        } else if ((y >= 621) && (y <= 722)) {
            index = 3;
        } else if ((y >= 723) && (y <= 822)) {
            index = 4;
        } else if ((y >= 823) && (y <= 922)) {
            index = 5;
        } else if ((y >= 923) && (y <= 1022)) {
            index = 6;
        } else if ((y >= 1023) && (y <= 1121)) {
            index = 7;
        } else if ((y >= 1122) && (y <= 1221)) {
            index = 8;
        } else if ((y >= 1222) || (y <= 119)) {
            index = 9;
        } else if ((y >= 120) && (y <= 218)) {
            index = 10;
        } else if ((y >= 219) && (y <= 320)) {
            index = 11;
        } else {
            index = 0;
        }

        return constellationName[index];

    }

    /**
     * 计算属相的索引，注意虽然属相是以农历年来区别的，但是目前在实际使用中是按公历来计算的
     * 
     * @return 鼠年为1,其它类推
     */
    public int animal() {
        int offset = date.year() - animalStartYear;
        return (offset % 12) + 1;
    }

    /**
     * 取属相字符串
     * 
     * @return
     */
    public String animalString() {

        int offset = date.year() - animalStartYear; // 阳历计算
        // int offset = this.cYear - animalStartYear; 农历计算
        return String.valueOf(animalStr.charAt(offset % 12));

    }

    /**
     * 取农历年的干支表示法如 乙丑年
     * 
     * @return
     */
    public String ganZhiYearString() {

        String tempStr;
        int i = (this.cYear - ganZhiStartYear) % 60; // 计算干支
        tempStr = String.valueOf(ganStr.charAt(i % 10)) + String.valueOf(zhiStr.charAt(i % 12)) + "年";
        return tempStr;

    }

    /**
     * 取干支的月表示字符串，注意农历的闰月不记干支
     * 
     * @return
     */
    public String ganZhiMonthString() {

        // 每个月的地支总是固定的,而且总是从寅月开始
        int zhiIndex;
        String zhi;
        if (this.cMonth > 10) {
            zhiIndex = this.cMonth - 10;
        } else {
            zhiIndex = this.cMonth + 2;
        }
        zhi = String.valueOf(zhiStr.charAt(zhiIndex - 1));

        // 根据当年的干支年的干来计算月干的第一个
        int ganIndex = 1;
        String gan;
        int i = (this.cYear - ganZhiStartYear) % 60; // 计算干支
        switch (i % 10) {

            case 0: // 甲
                ganIndex = 3;
                break;
            case 1: // 乙
                ganIndex = 5;
                break;
            case 2: // 丙
                ganIndex = 7;
                break;
            case 3: // 丁
                ganIndex = 9;
                break;
            case 4: // 戊
                ganIndex = 1;
                break;
            case 5: // 己
                ganIndex = 3;
                break;
            case 6: // 庚
                ganIndex = 5;
                break;
            case 7: // 辛
                ganIndex = 7;
                break;
            case 8: // 壬
                ganIndex = 9;
                break;
            case 9: // 癸
                ganIndex = 1;
                break;

        }
        gan = String.valueOf(ganStr.charAt((ganIndex + this.cMonth - 2) % 10));

        return gan + zhi + "月";
    }

    /**
     * 取干支日表示法
     * 
     * @return
     */
    public String ganZhiDayString() {
        int i, offset;
        TimeSpan ts = this.date.subtract(ganZhiStartDay);
        offset = ts.days();
        i = offset % 60;
        return String.valueOf(ganStr.charAt(i % 10)) + String.valueOf(zhiStr.charAt(i % 12)) + "日";
    }

    /**
     * 取当前日期的干支表示法如 甲子年乙丑月丙庚日
     */
    public String ganZhiDateString() {
        return ganZhiYearString() + ganZhiMonthString() + ganZhiDayString();
    }

}
