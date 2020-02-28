
#ifndef __DATE_TIME_H__
#define __DATE_TIME_H__

#include <iostream> 
#include <string>

#if defined _MSC_VER
#include <Windows.h>
#include <time.h>
#else
#include <time.h>
#endif 

using namespace std;

typedef unsigned long long ulong;
typedef long long slong;

namespace arklight
{
namespace datetime
{

/*  星期类  */
enum class DayOfWeek {
	Sunday = 0,          /*  星期日  */
	Monday = 1,          /*  星期一  */
	Tuesday = 2,         /*  星期二  */
	Wednesday = 3,       /*  星期三  */
	Thursday = 4,        /*  星期四  */
	Friday = 5,          /*  星期五  */
	Saturday = 6         /*  星期六  */
};

/*  时间类型类  */
enum class DateTimeKind {
	Unspecified,         /*  未确定  */
	Utc,                 /*  国际标准时  */
	Local                /*  当地时区时间  */
};

/*  时间类  */
struct Time
{
    int Year;           /*  年  */
    int Month;          /*  月  */
    int Day;            /*  日  */
    int Hour;           /*  时  */
    int Minute;         /*  分  */
    int Second;         /*  秒  */
    int Millisecond;    /*  毫秒  */
};

/*  时间间隔类  */
struct TimeSpan
{
public:
    explicit TimeSpan(slong ticks);
    TimeSpan(int hours, int minutes, int seconds);
    TimeSpan(int days, int hours, int minutes, int seconds);
    TimeSpan(int days, int hours, int minutes, int seconds, int milliseconds);
    virtual ~TimeSpan() {}
public:
    static TimeSpan Zero();           /*  零时间间隔  */
    static TimeSpan MinValue();       /*  最小时间间隔  */
    static TimeSpan MaxValue();       /*  最大时间间隔  */
    static TimeSpan FromDays(const double value);
    static TimeSpan FromHours(const double value);
    static TimeSpan FromMilliseconds(const double value);
    static TimeSpan FromMinutes(const double value);
    static TimeSpan FromSeconds(const double value);
    static TimeSpan FromTicks(const slong value);
    static int Compare(const TimeSpan& t1, const TimeSpan& t2);  
    static slong TimeToTicks(int hour, int minute, int second);  
public:
    TimeSpan Add(const TimeSpan& ts);
    TimeSpan Subtract(const TimeSpan& ts);
    bool Equals(const TimeSpan& ts);
    int CompareTo(const TimeSpan& value);
    TimeSpan Duration();
    TimeSpan Negate();
    string ToString();
    string ToString(string& format);
    TimeSpan operator -(const TimeSpan& t);
    TimeSpan operator +(const TimeSpan& t);
    bool operator ==(const TimeSpan& t);
    bool operator !=(const TimeSpan& t);
    bool operator <(const TimeSpan& t);
    bool operator <=(const TimeSpan& t);
    bool operator >(const TimeSpan& t);
    bool operator >=(const TimeSpan& t);
public:
    slong Ticks() const;
    int Days() const;
    int Hours() const;
    int Milliseconds() const;
    int Minutes() const;
    int Seconds() const;
    double TotalDays() const;
    double TotalHours() const;
    double TotalMilliseconds() const;
    double TotalMinutes() const;
    double TotalSeconds() const;
private:
	slong _ticks;
    static volatile bool _legacyConfigChecked;
	static volatile bool _legacyMode;
    static TimeSpan Interval(double value, int scale); 
    void judgeTicksIsMin();
    double TicksToOADate(slong value); 
};

/*  时区类  */
struct TimeZone
{

};

/*  时区信息类  */
class TimeZoneInfo
{
    enum TimeZoneInfoResult
    {
        Success,
		TimeZoneNotFoundException,
		InvalidTimeZoneException,
		SecurityException
    };
};

/*  日期格式类  */
class DateTimeFormat
{
private:
	string RoundtripFormat = "yyyy'-'MM'-'dd'T'HH':'mm':'ss.fffffffK";
	string RoundtripDateTimeUnfixed = "yyyy'-'MM'-'ddTHH':'mm':'ss zzz";
};

struct DateTime
{
public:
    constexpr DateTime() = default;
    constexpr DateTime(const DateTime& dateTime) = default;
    DateTime(slong ticks);
    DateTime(ulong dateData); 
    DateTime(slong ticks, DateTimeKind kind);
    DateTime(int year, int month, int day);
    DateTime(int year, int month, int day, int hour, int minute, int second);
    DateTime(int year, int month, int day, int hour, int minute, int second, DateTimeKind kind);
    DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond);
    DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, DateTimeKind kind);
    virtual ~DateTime() {}
private:
    DateTime(slong ticks, DateTimeKind kind, bool isAmbiguousDst);
    
public:
    static DateTime Now();
    static DateTime UtcNow();
    static DateTime Today();
    static DateTime MinValue();
    static DateTime MaxValue();
    static DateTime FromBinary(const slong dateData);
    static DateTime FromBinaryRaw(const slong dateData);
    static DateTime FromFileTime(const slong fileTime);
    static DateTime FromFileTimeUtc(const slong fileTime);
    static DateTime FromOADate(const double d);
    static DateTime Parse(const string& s);
    static DateTime SpecifyKind(const DateTime& value, const DateTimeKind& kind);
    static int Compare(const DateTime& t1, const  DateTime& t2);
    static int DaysInMonth(const int year, const int month);
    static bool IsLeapYear(const int year);
public:
    Time GetTime();
    DayOfWeek GetDayOfWeek();
    DateTimeKind GetDateTimeKind();
    TimeSpan GetTimeOfDay();
    DateTime Date();
    DateTime Add(const TimeSpan& value);
    DateTime Add(double value, int scale);
    DateTime AddDays(double value);
    DateTime AddHours(double value);
    DateTime AddMilliseconds(double value);
    DateTime AddMinutes(double value);
    DateTime AddMonths(int months);
    DateTime AddSeconds(double value);
    DateTime AddTicks(slong value);
    DateTime AddYears(int value);  
    TimeSpan Subtract(const DateTime& value);
    DateTime Subtract(const TimeSpan& value);
    DateTime ToLocalTime();
    DateTime ToUniversalTime();
    string ToString();
    wstring ToWString();
    string ToLongDateString();
    string ToLongTimeString();
    string ToShortDateString();
    string ToShortTimeString();
    double ToOADate();
    slong ToFileTime();
    slong ToFileTimeUtc();
    slong ToBinary();
    int CompareTo(const DateTime& value);
    bool Equals(const DateTime& value); 
    bool EqualsMonthAndDay(DateTime& value); 
    DateTime operator +(const TimeSpan& t);
    DateTime operator -(const TimeSpan& t);
    TimeSpan operator -(const DateTime& d);
    bool operator ==(const DateTime& d);
    bool operator !=(const DateTime& d);
    bool operator <(const DateTime& d);
    bool operator <=(const DateTime& d);
    bool operator >(const DateTime& d);
    bool operator >=(const DateTime& d);
public:
    int Day();
    int DayOfYear();
    int Hour();
    int Millisecond();
    int Minute();
    int Month();
    int Second();
    int Year();
    slong Ticks();
private:
    ulong _dateData = 0;
    inline slong getInternalTicks() const;
    inline ulong getInternalKind() const;
    inline bool IsAmbiguousDaylightSavingTime() const;
    void judgeMillisecond(int& millisecond);
    void judgeDateTimeKind(DateTimeKind& kind);
    slong judgeNumTicks(int& year, int& month, int& day, int& hour, int& minute, int& second, int& millisecond);
    void judgeTicks(slong& ticks);
    void judgeAllParas(int& year, int& month, int& day, int& hour, int& minute, int& second, int& millisecond, DateTimeKind& kind);
    slong ToBinaryRaw();
    int GetDatePart(int part);
    void GetDatePart(int* year, int* month, int* day);
    DateTime ToLocalTime(bool throwOnOverflow);
    static double TicksToOADate(slong value);
    static slong DateToTicks(int year, int month, int day);
    static slong TimeToTicks(int hour, int minute, int second);
    static slong DoubleDateToTicks(double value);
};

template<typename _strT>
struct DateTimeEx : public DateTime
{
public:
    using str_type = _strT;

    str_type ToExString() {
        auto str = ToString();
        return NULL;
    }

};

using DateTimeExWString = DateTimeEx<std::wstring>;

}
}


#include <sstream>

namespace arklight
{
namespace datetime
{

#define readonly const
#define final const
#define private static
#define internal static
#define public

#ifndef boolean
#define boolean bool
#endif

#define True true
#define Flase false

static const double MillisecondsPerTick = 0.0001;
static const double SecondsPerTick = 1E-07;
static const double MinutesPerTick = 1.6666666666666667E-09;
static const double HoursPerTick = 2.7777777777777777E-11;
static const double DaysPerTick = 1.1574074074074074E-12;
static const slong MaxSeconds = 922337203685L;
static const slong MinSeconds = -922337203685L;
static const slong MaxMilliSeconds = 922337203685477L;
static const slong MinMilliSeconds = -922337203685477L;
static const slong TicksPerTenthSecond = 1000000L;
static const slong TicksPerMillisecond = 10000L;
static const slong TicksPerSecond = 10000000L;
static const slong TicksPerMinute = 600000000L;
static const slong TicksPerHour = 36000000000L;
static const slong TicksPerDay = 864000000000L;
static const int MillisPerSecond = 1000;
static const int MillisPerMinute = 60000;
static const int MillisPerHour = 3600000;
static const int MillisPerDay = 86400000;
static const int DaysPerYear = 365;
static const int DaysPer4Years = 1461;
static const int DaysPer100Years = 36524;
static const int DaysPer400Years = 146097;
static const int DaysTo1601 = 584388;
static const int DaysTo1899 = 693593;
static const int DaysTo1970 = 719162;
static const int DaysTo10000 = 3652059;
static const slong MinTicks = 0L;
static const slong MaxTicks = 3155378975999999999L;
static const slong MaxMillis = 315537897600000L;
static const slong FileTimeOffset = 504911232000000000L;
static const slong OADateMinAsTicks = 31241376000000000L;
static const double OADateMinAsDouble = -657435.0;
static const double OADateMaxAsDouble = 2958466.0;
static const int DatePartYear = 0;
static const int DatePartDayOfYear = 1;
static const int DatePartMonth = 2;
static const int DatePartDay = 3;

private
const ulong TicksMask = 4611686018427387903uL;
private
const ulong FlagsMask = 13835058055282163712uL;
private
const ulong LocalMask = 9223372036854775808uL;
private
const slong TicksCeiling = 4611686018427387904L;
private
const ulong KindUnspecified = 0uL;
private
const ulong KindUtc = 4611686018427387904uL;
private
const ulong KindLocal = 9223372036854775808uL;
private
const ulong KindLocalAmbiguousDst = 13835058055282163712uL;
private
const slong MinValueTimeSpanTicks = -9223372036854775807L;
private
const slong MaxValueTimeSpanTicks = 9223372036854775807L;
private
const int KindShift = 62;
private
const string TicksField = "ticks";
private
const string DateDataField = "dateData";

private
readonly int DaysToMonth365[13] = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
private
readonly int DaysToMonth366[13] = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};
private
readonly string DateTimeFormat[] = {"%a", "%A", "%b", "%B", "%c", "%d", "%H", "%I", "%j", "%m", "%M", "%p", "%S", "%U", "%W", "%w", "%x", "%X", "%y", "%Y", "%Z"};

char AllStandardFormats[] = {'d', 'D', 'f', 'F', 'g', 'G', 'm', 'M', 'o', 'O', 'r', 'R', 's', 't', 'T', 'u', 'U', 'y', 'Y'};

/// TimeSpan

TimeSpan::TimeSpan(slong ticks)
{
    _ticks = ticks;
}

TimeSpan::TimeSpan(int hours, int minutes, int seconds)
{
    _ticks = TimeToTicks(hours, minutes, seconds);
}

TimeSpan::TimeSpan(int days, int hours, int minutes, int seconds)
{
    TimeSpan(days, hours, minutes, seconds, 0);
}

TimeSpan::TimeSpan(int days, int hours, int minutes, int seconds, int milliseconds)
{
    slong num = ((slong)days * 3600L * 24 + (slong)hours * 3600L + (slong)minutes * 60L + seconds) * MillisPerSecond + milliseconds;
    if (num > MaxMilliSeconds || num < MinMilliSeconds)
    {
        throw;
    }
    _ticks = num * TicksPerMillisecond;
}

TimeSpan TimeSpan::Zero()
{
    return TimeSpan(0);
}

TimeSpan TimeSpan::MinValue()
{
    return TimeSpan(MaxValueTimeSpanTicks);
}

TimeSpan TimeSpan::MaxValue()
{
    return TimeSpan(MinValueTimeSpanTicks);
}

TimeSpan TimeSpan::FromDays(const double value)
{
    return Interval(value, MillisPerDay);
}

TimeSpan TimeSpan::FromHours(const double value)
{
    return Interval(value, MillisPerHour);
}

TimeSpan TimeSpan::FromMilliseconds(const double value)
{
    return Interval(value, 1);
}

TimeSpan TimeSpan::FromMinutes(const double value)
{
    return Interval(value, MillisPerMinute);
}

TimeSpan TimeSpan::FromSeconds(const double value)
{
    return Interval(value, MillisPerSecond);
}

TimeSpan TimeSpan::FromTicks(const slong value)
{
    return TimeSpan(value);
}

int TimeSpan::Compare(const TimeSpan &t1, const TimeSpan &t2)
{
    if (t1._ticks > t2._ticks)
    {
        return 1;
    }
    if (t1._ticks < t2._ticks)
    {
        return -1;
    }
    return 0;
}

TimeSpan TimeSpan::Add(const TimeSpan &ts)
{
    slong num = _ticks + ts._ticks;
    if (_ticks >> 63 == ts._ticks >> 63 && _ticks >> 63 != num >> 63)
    {
        throw std::exception();
    }
    return TimeSpan(num);
}

TimeSpan TimeSpan::Subtract(const TimeSpan &ts)
{
    slong num = _ticks - ts._ticks;
    if (_ticks >> 63 != ts._ticks >> 63 && _ticks >> 63 != num >> 63)
    {
        throw std::exception();
    }
    return TimeSpan(num);
}

bool TimeSpan::Equals(const TimeSpan &ts)
{
    return _ticks == ts._ticks;
}

int TimeSpan::CompareTo(const TimeSpan &value)
{
    slong ticks = value._ticks;
    if (_ticks > ticks)
    {
        return 1;
    }
    if (_ticks < ticks)
    {
        return -1;
    }
    return 0;
}

TimeSpan TimeSpan::Duration()
{
    judgeTicksIsMin();
    return TimeSpan((_ticks >= 0) ? _ticks : (-_ticks));
}

TimeSpan TimeSpan::Negate()
{
    judgeTicksIsMin();
    return TimeSpan(-_ticks);
}

string TimeSpan::ToString()
{
    int num = (int)(_ticks / TicksPerDay);
    slong num2 = _ticks % TicksPerDay;
    if (_ticks < 0)
    {
        num = -num;
        num2 = -num2;
    }
    int value2 = (int)(num2 / TicksPerHour % 24);
    int value3 = (int)(num2 / TicksPerMinute % 60);
    int value4 = (int)(num2 / TicksPerSecond % 60);
    int num3 = (int)(num2 % TicksPerSecond);
    slong num4 = 0L;
    int i = 0;
    std::stringstream ss;
    ss << Hours() << " " << Minutes() << " " << Seconds();
    return ss.str();
}

void TimeSpan::judgeTicksIsMin()
{
    if (_ticks == MinValueTimeSpanTicks)
    {
        throw std::exception();
    }
}

string TimeSpan::ToString(string &format)
{
    return ToString();
}

TimeSpan TimeSpan::operator-(const TimeSpan &t)
{
    judgeTicksIsMin();
    return this->Subtract(t);
}

TimeSpan TimeSpan::operator+(const TimeSpan &t)
{
    return this->Add(t);
}

bool TimeSpan::operator==(const TimeSpan &t)
{
    return _ticks == t._ticks;
}

bool TimeSpan::operator!=(const TimeSpan &t)
{
    return _ticks != t._ticks;
}

bool TimeSpan::operator<(const TimeSpan &t)
{
    return _ticks < t._ticks;
}

bool TimeSpan::operator<=(const TimeSpan &t)
{
    return _ticks <= t._ticks;
}

bool TimeSpan::operator>(const TimeSpan &t)
{
    return _ticks > t._ticks;
}

bool TimeSpan::operator>=(const TimeSpan &t)
{
    return _ticks >= t._ticks;
}

slong TimeSpan::Ticks() const
{
    return _ticks;
}

int TimeSpan::Days() const
{
    return (int)(_ticks / TicksPerDay);
}

int TimeSpan::Hours() const
{
    return (int)(_ticks / TicksPerHour % 24);
}

int TimeSpan::Milliseconds() const
{
    return (int)(_ticks / TicksPerMillisecond % 1000);
}

int TimeSpan::Minutes() const
{
    return (int)(_ticks / TicksPerMinute % 60);
}

int TimeSpan::Seconds() const
{
    return (int)(_ticks / TicksPerSecond % 60);
}

double TimeSpan::TotalDays() const
{
    return (double)_ticks * DaysPerTick;
}

double TimeSpan::TotalHours() const
{
    return (double)_ticks * HoursPerTick;
}

double TimeSpan::TotalMilliseconds() const
{
    double num = (double)_ticks * 0.0001;
    if (num > (double)MaxMilliSeconds)
    {
        return (double)MaxMilliSeconds;
    }
    if (num < (double)MinMilliSeconds)
    {
        return (double)MinMilliSeconds;
    }
    return num;
}

double TimeSpan::TotalMinutes() const
{
    return (double)_ticks * MinutesPerTick;
}

double TimeSpan::TotalSeconds() const
{
    return (double)_ticks * SecondsPerTick;
}

TimeSpan TimeSpan::Interval(double value, int scale)
{
    if ((slong)value == 0xFFFFFFFFFFFFFFFF)
    {
        throw;
    }
    double num = value * (double)scale;
    double num2 = num + ((value >= 0.0) ? 0.5 : (-0.5));
    if (num2 > (double)MaxMilliSeconds || num2 < (double)MinMilliSeconds)
    {
        throw;
    }
    return TimeSpan((slong)num2 * 10000);
}

slong TimeSpan::TimeToTicks(int hour, int minute, int second)
{
    slong num = (slong)hour * 3600L + (slong)minute * 60L + second;
    if (num > MaxSeconds || num < MinSeconds)
    {
        throw;
    }
    return num * TicksPerSecond;
}

double TimeSpan::TicksToOADate(slong value)
{
    if (value == 0L)
    {
        return 0.0;
    }
    if (value < TicksPerDay)
    {
        value += MaxSeconds;
    }
    if (value < OADateMinAsTicks)
    {
        throw;
    }
    slong num = (value - MaxSeconds) / 10000;
    if (num < 0)
    {
        slong num2 = num % MillisPerDay;
        if (num2 != 0L)
        {
            num -= (MillisPerDay + num2) * 2;
        }
    }
    return (double)num / (double)MillisPerDay;
}

/// DateTime

DateTime::DateTime(slong ticks)
{
    judgeTicks(ticks);
    _dateData = (ulong)ticks;
}

DateTime::DateTime(ulong dateData)
{
    _dateData = dateData;
}

DateTime::DateTime(slong ticks, DateTimeKind kind)
{
    judgeTicks(ticks);
    _dateData = (ulong)(ticks | (static_cast<slong>(kind) << 62));
}

DateTime::DateTime(int year, int month, int day)
{
    _dateData = (ulong)DateToTicks(year, month, day);
}

DateTime::DateTime(int year, int month, int day, int hour, int minute, int second)
{
    _dateData = (ulong)(DateToTicks(year, month, day) + TimeToTicks(hour, minute, second));
}

DateTime::DateTime(int year, int month, int day, int hour, int minute, int second, DateTimeKind kind)
{
    judgeDateTimeKind(kind);
    slong num = DateToTicks(year, month, day) + TimeToTicks(hour, minute, second);
    _dateData = (ulong)(num | ((slong)kind << 62));
}

DateTime::DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
{
    judgeMillisecond(millisecond);
    _dateData = (ulong)judgeNumTicks(year, month, day, hour, minute, second, millisecond);
}

DateTime::DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, DateTimeKind kind)
{
    judgeAllParas(year, month, day, hour, minute, second, millisecond, kind);
    _dateData = (ulong)(judgeNumTicks(year, month, day, hour, minute, second, millisecond) | ((slong)kind << 62));
}

DateTime::DateTime(slong ticks, DateTimeKind kind, bool isAmbiguousDst)
{
    judgeTicks(ticks);
    _dateData = (ulong)(ticks | (isAmbiguousDst ? (-TicksCeiling) : (MinValueTimeSpanTicks)));
}

DateTime DateTime::Now()
{
#if defined _MSC_VER
	slong fileTime = 0;
	SYSTEMTIME st;
	GetLocalTime(&st);
	return DateTime(st.wYear, st.wMonth, st.wDay,
		st.wHour, st.wMinute, st.wSecond, DateTimeKind::Local);
#else
    time_t tt;
    tt = time( &tt );
    tm* t= localtime(&tt);
    return DateTime(t->tm_year + 1900, t->tm_mon + 1,  t->tm_mday, 
        t->tm_hour, t->tm_min, t->tm_sec, DateTimeKind::Local);
#endif
}

DateTime DateTime::UtcNow()
{
#if defined _MSC_VER
    slong fileTime = 0;
    FILETIME t1;
    GetSystemTimeAsFileTime(&t1);
    fileTime = (((slong)t1.dwHighDateTime) << 32) + t1.dwLowDateTime;
    return DateTime((ulong)(fileTime + FileTimeOffset) | 0x4000000000000000, DateTimeKind::Utc);
#else
    time_t tt;
    tt = time( &tt );
    tm* t= gmtime(&tt);
    return DateTime(t->tm_year + 1900, t->tm_mon + 1,  t->tm_mday, 
        t->tm_hour, t->tm_min, t->tm_sec, DateTimeKind::Utc);
#endif
}

DateTime DateTime::Today()
{
    return Now().Date();
}

DateTime DateTime::MinValue()
{
    return DateTime(0L, DateTimeKind::Unspecified);
}

DateTime DateTime::MaxValue()
{
    return DateTime(MaxTicks, DateTimeKind::Unspecified);
}

DateTime DateTime::FromBinary(const slong dateData)
{
    if ((dateData & MinValueTimeSpanTicks) != 0L)
    {
        slong num = dateData & 0x3FFFFFFFFFFFFFFF;
        if (num > 4611685154427387904L)
        {
            num -= 4611686018427387904L;
        }
        bool isAmbiguousLocalDst = false;
        slong ticks = 0;
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
            // ticks = TimeZoneInfo::GetUtcOffsetFromUtc(time, TimeZoneInfo.Local, &isDaylightSavings, &isAmbiguousLocalDst).Ticks;
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

DateTime DateTime::FromBinaryRaw(const slong dateData)
{
    return DateTime((ulong)dateData);
}

DateTime DateTime::FromFileTime(const slong fileTime)
{
    return FromFileTimeUtc(fileTime).ToLocalTime();
}

DateTime DateTime::FromFileTimeUtc(const slong fileTime)
{
    if (fileTime < 0 || fileTime > 2650467743999999999L)
    {
        throw;
    }
    slong ticks = fileTime + FileTimeOffset;
    return DateTime(ticks, DateTimeKind::Utc);
}

DateTime DateTime::FromOADate(const double d)
{
    return DateTime(DoubleDateToTicks(d), DateTimeKind::Unspecified);
}

// Unrealized
DateTime DateTime::Parse(const string &s)
{
    return DateTime::Now();
}

DateTime DateTime::SpecifyKind(const DateTime &value, const DateTimeKind &kind)
{
    return DateTime(value.getInternalTicks(), kind);
}

int DateTime::Compare(const DateTime &t1, const DateTime &t2)
{
    slong internalTicks = t1.getInternalTicks();
    slong internalTicks2 = t2.getInternalTicks();
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

int DateTime::DaysInMonth(const int year, const int month)
{
    if (month < 1 || month > 12)
    {
        throw;
    }
    auto isleapyear = IsLeapYear(year);
    if (isleapyear == true)
    {
        return DaysToMonth366[month] - DaysToMonth366[month - 1];
    }
    else
    {
        return DaysToMonth365[month] - DaysToMonth365[month - 1];
    }
}

bool DateTime::IsLeapYear(const int year)
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

Time DateTime::GetTime()
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

DayOfWeek DateTime::GetDayOfWeek()
{
    return static_cast<DayOfWeek>((getInternalTicks() / TicksPerDay + 1) % 7);
}

DateTimeKind DateTime::GetDateTimeKind()
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

TimeSpan DateTime::GetTimeOfDay()
{
    return TimeSpan(0);
}

DateTime DateTime::Date()
{
    slong internalTicks = getInternalTicks();
    return DateTime((ulong)((internalTicks - internalTicks % TicksPerDay) | (long)getInternalKind()));
}

DateTime DateTime::Add(const TimeSpan &value)
{
    return AddTicks(value.Ticks());
}

DateTime DateTime::Add(double value, int scale)
{
    slong num = (slong)(value * (double)scale + ((value >= 0.0) ? 0.5 : (-0.5)));
    if (num <= -MaxMillis || num >= MaxMillis)
    {
        throw;
    }
    return AddTicks(num * 10000);
}

DateTime DateTime::AddDays(double value)
{
    return Add(value, MillisPerDay);
}

DateTime DateTime::AddHours(double value)
{
    return Add(value, MillisPerHour);
}

DateTime DateTime::AddMilliseconds(double value)
{
    return Add(value, 1);
}

DateTime DateTime::AddMinutes(double value)
{
    return Add(value, MillisPerMinute);
}

DateTime DateTime::AddMonths(int months)
{
    int year = 0, month = 0, day = 0;
    if (months < -120000 || months > 120000)
    {
        throw;
    }
    GetDatePart(&year, &month, &day);
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
    return DateTime((ulong)((DateToTicks(year, month, day) + getInternalTicks() % TicksPerDay) | (long)getInternalKind()));
}

DateTime DateTime::AddSeconds(double value)
{
    return Add(value, MillisPerSecond);
}

DateTime DateTime::AddTicks(slong value)
{
    slong internalTicks = getInternalTicks();
    if (value > MaxTicks - internalTicks || value < -internalTicks)
    {
        throw;
    }
    return DateTime((ulong)((internalTicks + value) | (slong)getInternalKind()));
}

DateTime DateTime::AddYears(int value)
{
    if (value < -10000 || value > 10000)
    {
        throw;
    }
    return AddMonths(value * 12);
}

TimeSpan DateTime::Subtract(const DateTime &value)
{
    return TimeSpan(getInternalTicks() - value.getInternalTicks());
}

DateTime DateTime::Subtract(const TimeSpan &value)
{
    slong internalTicks = getInternalTicks();
    slong ticks = value.Ticks();
    if (internalTicks - 0 < ticks || internalTicks - MaxTicks > ticks)
    {
        throw;
    }
    return DateTime((ulong)((internalTicks - ticks) | (slong)getInternalTicks()));
}

DateTime DateTime::ToLocalTime()
{
    return ToLocalTime(false);
}

DateTime DateTime::ToLocalTime(bool throwOnOverflow)
{
    if (GetDateTimeKind() == DateTimeKind::Local)
    {
        return *this;
    }
    bool isDaylightSavings = false;
    bool isAmbiguousLocalDst = false;
    slong ticks = 0; // TimeZoneInfo::GetUtcOffsetFromUtc(this, TimeZoneInfo::Local, &isDaylightSavings, &isAmbiguousLocalDst).Ticks();
    slong num = getInternalTicks() + ticks;
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

DateTime DateTime::ToUniversalTime()
{
    return *this;
    // return TimeZoneInfo::ConvertTimeToUtc(*this, TimeZoneInfoOptions::NoThrowOnInvalidTime);
}

string DateTime::ToString()
{
    std::stringstream ss;
    ss << Year() << "-" << Month() << "-" << Day() << "-" << Hour() << "-" << Minute() << "-" << Second();
    return ss.str();
    // return DateTimeFormat::Format(this, nullptr, DateTimeFormatInfo::CurrentInfo);
}

wstring DateTime::ToWString() 
{
    std::wstringstream ss;
    ss << Year() << "-" << Month() << "-" << Day() << "-" << Hour() << "-" << Minute() << "-" << Second();
    return ss.str();
}

string DateTime::ToLongDateString()
{
    return ToString();
    // return DateTimeFormat::Format(this, "D", DateTimeFormatInfo::CurrentInfo);
}

string DateTime::ToLongTimeString()
{
    return ToString();
    // return DateTimeFormat::Format(this, "T", DateTimeFormatInfo::CurrentInfo);
}

string DateTime::ToShortDateString()
{
    return ToString();
    // return DateTimeFormat::Format(this, "d", DateTimeFormatInfo::CurrentInfo);
}

string DateTime::ToShortTimeString()
{
    return ToString();
    // return DateTimeFormat::Format(this, "t", DateTimeFormatInfo::CurrentInfo);
}

double DateTime::ToOADate()
{
    return TicksToOADate(getInternalTicks());
}

slong DateTime::ToFileTime()
{
    return ToUniversalTime().ToFileTimeUtc();
}

slong DateTime::ToFileTimeUtc()
{
    slong num = (((slong)getInternalKind() & -9223372036854775807L) != 0L) ? ToUniversalTime().getInternalTicks() : getInternalTicks();
    num -= FileTimeOffset;
    if (num < 0)
    {
        throw;
    }
    return num;
}

slong DateTime::ToBinary()
{
    return (slong)_dateData;
}

int DateTime::CompareTo(const DateTime &value)
{
    slong internalTicks = value.getInternalTicks();
    slong internalTicks2 = getInternalTicks();
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

bool DateTime::Equals(const DateTime &value)
{
    return getInternalTicks() == value.getInternalTicks();
}

bool DateTime::EqualsMonthAndDay(DateTime& value)
{
    return Month() == value.Month() && Day() == value.Day();
}

DateTime DateTime::operator+(const TimeSpan &t)
{
    slong internalTicks = getInternalTicks();
    slong ticks = t.Ticks();
    if (ticks > MaxTicks - internalTicks || ticks < -internalTicks)
    {
        throw;
    }
    return DateTime((ulong)((internalTicks + ticks) | (slong)getInternalKind()));
}

DateTime DateTime::operator-(const TimeSpan &t)
{
    slong internalTicks = getInternalTicks();
    slong ticks = t.Ticks();
    if (internalTicks - 0 < ticks || internalTicks - MaxTicks > ticks)
    {
        throw; 
    }
    return DateTime((ulong)((internalTicks - ticks) | (slong)getInternalKind()));
}

TimeSpan DateTime::operator-(const DateTime &d)
{
    return TimeSpan(getInternalTicks() - d.getInternalTicks());
}

bool DateTime::operator==(const DateTime &d)
{
    return getInternalTicks() == d.getInternalTicks();
}

bool DateTime::operator!=(const DateTime &d)
{
    return getInternalTicks() != d.getInternalTicks();
}

bool DateTime::operator<(const DateTime &d)
{
    return getInternalTicks() < d.getInternalTicks();
}

bool DateTime::operator<=(const DateTime &d)
{
    return getInternalTicks() <= d.getInternalTicks();
}

bool DateTime::operator>(const DateTime &d)
{
    return getInternalTicks() > d.getInternalTicks();
}

bool DateTime::operator>=(const DateTime &d)
{
    return getInternalTicks() >= d.getInternalTicks();
}

int DateTime::Day()
{
    return GetDatePart(3);
}

int DateTime::DayOfYear()
{
    return GetDatePart(1);
}

int DateTime::Hour()
{
    return (int)(getInternalTicks() / TicksPerHour % 24);
}

int DateTime::Millisecond()
{
    return (int)(getInternalTicks() / TicksPerMillisecond % 1000);
}

int DateTime::Minute()
{
    return (int)(getInternalTicks() / TicksPerMinute % 60);
}

int DateTime::Month()
{
    return GetDatePart(2);
}

int DateTime::Second()
{
    return (int)(getInternalTicks() / TicksPerSecond % 60);
}

int DateTime::Year()
{
    return GetDatePart(0);
}

slong DateTime::Ticks()
{
    return getInternalTicks();
}

slong DateTime::ToBinaryRaw()
{
    return (slong)_dateData;
}

int DateTime::GetDatePart(int part)
{
    slong internalTicks = getInternalTicks();
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
    auto array = (num5 == 3 && (num4 != 24 || num3 == 3)) ? DaysToMonth366 : DaysToMonth365;
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

void DateTime::GetDatePart(int *year, int *month, int *day)
{
    slong internalTicks = getInternalTicks();
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
    auto array = (num5 == 3 && (num4 != 24 || num3 == 3)) ? DaysToMonth366 : DaysToMonth365;
    int i;
    for (i = (num >> 5) + 1; num >= array[i]; i++)
    {
    }
    *month = i;
    *day = num - array[i - 1] + 1;
}

double DateTime::TicksToOADate(slong value)
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
    slong num = (value - 599264352000000000L) / 10000;
    if (num < 0)
    {
        slong num2 = num % MillisPerDay;
        if (num2 != 0L)
        {
            num -= (MillisPerDay + num2) * 2;
        }
    }
    return (double)num / (double)MillisPerDay;
}

slong DateTime::DateToTicks(int year, int month, int day)
{
    if (year >= 1 && year <= 9999 && month >= 1 && month <= 12)
    {
        auto array = IsLeapYear(year) ? DaysToMonth366 : DaysToMonth365;
        if (day >= 1 && day <= array[month] - array[month - 1])
        {
            int num = year - 1;
            int num2 = num * 365 + num / 4 - num / 100 + num / 400 + array[month - 1] + day - 1;
            return num2 * TicksPerDay;
        }
    }
    throw; 
}

slong DateTime::TimeToTicks(int hour, int minute, int second)
{
    if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60)
	{
		return TimeSpan::TimeToTicks(hour, minute, second);
	}
	throw; 
}

slong DateTime::DoubleDateToTicks(double value)
{
    if (!(value < 2958466.0) || !(value > -657435.0))
    {
        throw; 
    }
    slong num = (long)(value * (double)MillisPerDay + ((value >= 0.0) ? 0.5 : (-0.5)));
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

inline slong DateTime::getInternalTicks() const
{
    return (slong)(_dateData & 0x3FFFFFFFFFFFFFFF);
}

inline ulong DateTime::getInternalKind() const
{
    return (ulong)(_dateData & -4611686018427387904L);
}

inline bool DateTime::IsAmbiguousDaylightSavingTime() const
{
    return getInternalKind() == KindLocalAmbiguousDst;
}

void DateTime::judgeMillisecond(int &millisecond)
{
    if (millisecond < 0 || millisecond >= 1000)
    {
        throw;
    }
}
void DateTime::judgeDateTimeKind(DateTimeKind &kind)
{
    if (kind < DateTimeKind::Unspecified || kind > DateTimeKind::Local)
    {
        throw;
    }
}
slong DateTime::judgeNumTicks(int &year, int &month, int &day, int &hour, int &minute, int &second, int &millisecond)
{
    slong num = DateToTicks(year, month, day) + TimeToTicks(hour, minute, second);
    num += (slong)millisecond * 10000L;
    if (num < 0 || num > MaxTicks)
    {
        throw;
    }
    return num;
}
void DateTime::judgeTicks(slong &ticks)
{
    if (ticks < 0 || ticks > MaxTicks)
    {
        throw;
    }
}
void DateTime::judgeAllParas(int &year, int &month, int &day, int &hour, int &minute, int &second, int &millisecond, DateTimeKind &kind)
{
    judgeMillisecond(millisecond);
    judgeDateTimeKind(kind);
    judgeNumTicks(year, month, day, hour, minute, second, millisecond);
}

} // namespace datetime
} // namespace arklight

#endif


