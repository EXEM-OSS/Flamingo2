Ext.namespace('App.Util');
/**
 * @class App.Util.Lang
 * @singleton
 * @author Cloudine Inc
 * @since 0.1
 */
App.Util.Lang = new (function () {

    /**
     * 지정한 객체의 자료형을 반환한다.
     */
    this.getType = function (obj) {
        return Object.prototype.toString.call(obj).slice(8, -1);
    };

    /**
     * 자료형을 검사한다.
     */
    this.is = function is(type, obj) {
        return obj !== undefined && obj !== null && this.getType(obj) === type;
    };

    /**
     * 배열 여부를 검사한다.
     */
    this.isArray = function (obj) {
        return this.is('Array', obj);
    };

    /**
     * 문자열 여부를 검사한다.
     */
    this.isString = function (obj) {
        return this.is('String', obj);
    };

    /**
     * 숫자 여부를 검사한다.
     */
    this.isNumber = function (obj) {
        return this.is('Number', obj);
    };

    /**
     * Boolean형으로 변환한다.
     */
    this.toBoolean = function (obj) {
        return (/^true$/i).test(obj);
    };

});

/**
 * @class App.Util.String
 * @singleton
 * @author Cloudine Inc
 * @since 0.1
 * @see <a href="https://github.com/edtsech/underscore.string/blob/master/lib/underscore.string.js">underscore string</a>
 */
App.Util.String = new (function () {

    this.fileSize = function (size) {
        var byteLimit = 1024,
            kbLimit = 1048576,
            mbLimit = 1073741824,
            gbLimit = 1073741824 * 1024;

        var out;
        if (size < byteLimit) {
            if (size === 1) {
                out = '1 byte';
            } else {
                out = size + ' bytes';
            }
        } else if (size < kbLimit) {
            out = (Math.round(((size * 10) / byteLimit)) / 10) + ' KB';
        } else if (size < mbLimit) {
            out = (Math.round(((size * 10) / kbLimit)) / 10) + ' MB';
        } else if (size < gbLimit) {
            out = (Math.round(((size * 10) / mbLimit)) / 10) + ' GB';
        } else {
            out = (Math.round(((size * 10) / gbLimit)) / 10) + ' TB';
        }
        return out;
    },

    /**
     * 왼쪽에 문자을 채운다. 최종적으로 구성할 문자열은 지정한 길이가 된다.
     * 입력값의 문자열의 길이가 지정한 길이보다 작다면 문자를 왼쪽에 추가한다.
     */
        this.leftPad = function (value, length, character) {
            value = '' + value; // Stringfy
            while (value.length < length) {
                value = character + value;
            }
            return value;
        };

    /**
     * 3자리 마다 comma를 추가한다.
     */
    this.toCommaNumber = function (num) {
        var len, point, str;

        num = num + "";
        point = num.length % 3
        len = num.length;

        str = num.substring(0, point);
        while (point < len) {
            if (str != "") str += ",";
            str += num.substring(point, point + 3);
            point += 3;
        }
        return str;
    };

    /**
     * 오른쪽에 문자을 채운다. 최종적으로 구성할 문자열은 지정한 길이가 된다.
     * 입력값의 문자열의 길이가 지정한 길이보다 작다면 문자를 오른쪽에 추가한다.
     */
    this.rightPad = function (value, length, character) {
        value = '' + value; // Stringfy
        while (value.length < length) {
            value = value + character;
        }
        return value;
    };

    /**
     * 지정한 문자열을 trim 처리한다.
     */
    this.trim = function (string) {
        if (string == null)
            return null;

        var startingIndex = 0;
        var endingIndex = string.length - 1;

        var singleWhitespaceRegex = /\s/;
        while (string.substring(startingIndex, startingIndex + 1).match(singleWhitespaceRegex))
            startingIndex++;

        while (string.substring(endingIndex, endingIndex + 1).match(singleWhitespaceRegex))
            endingIndex--;

        if (endingIndex < startingIndex)
            return '';

        return string.substring(startingIndex, endingIndex + 1);
    };

    this.stripTags = function (str) {
        if (str == null) return '';
        return String(str).replace(/<\/?[^>]+>/g, '');
    };

    this.surround = function (str, wrapper) {
        return [wrapper, str, wrapper].join('');
    };

    this.quote = function (str) {
        return this.surround(str, '"');
    };

    this.strRepeat = function (str, qty) {
        if (qty < 1) return '';
        var result = '';
        while (qty > 0) {
            if (qty & 1) result += str;
            qty >>= 1, str += str;
        }
        return result;
    };

    /**
     * 문자열이 비어있는지 확인한다.
     */
    this.isBlank = function (string) {
        if (string == undefined || string == null) {
            return true;
        }
        return this.trim(string) == '';
    };

    /**
     * 문자열에서 나타난 문구를 모두 일괄 변환한다.
     */
    this.replaceAll = function (string, from, to) {
        var value = "";

        if (from == null) {
            return string;
        }

        if (string != "" && from != to) {
            value = string;

            while (value.indexOf(from) > -1) {
                value = value.replace(from, to);
            }
        }
        return value;
    };

    /**
     * HTML 태그를 escape 처리한다.
     */
    this.escapeHTML = function (self) {
        return self
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, "&apos;");
    };

    /**
     * Escape 처리한 HTML 태그를 복원한다.
     */
    this.unescapeHTML = function (self) {
        return self
            .replace(/&amp;/g, '&')
            .replace(/&lt;/g, '<')
            .replace(/&gt;/g, '>')
            .replace(/&quot;/g, '"')
            .replace(/&apos;/g, "'");
    };

    /**
     * 문자열이 지정한 문자열로 시작하는지 확인한다.
     */
    this.startsWith = function (self, start) {
        return self.length >= start.length && self.substring(0, start.length) === start;
    };

    /**
     * 문자열이 지정한 문자열로 끝나는지 확인한다.
     */
    this.endsWith = function (self, ends) {
        return self.length >= ends.length && self.substring(self.length - ends.length) === ends;
    };

    /**
     * 문자열 내에 지정한 문자열이 포함되는지 여부를 확인한다.
     */
    this.occurrence = function (self, substr) {
        return self.indexOf(substr) !== -1;
    };

    /**
     *
     */
    this.chop = function (self, step) {
        var result = [], len = self.length, i;

        step || (step = len);

        for (i = 0; i < len; i += step) {
            result.push(self.slice(i, i + step));
        }

        return result;
    };

    /**
     * @see http://jsfromhell.com/string/wordwrap
     */
    this.wrap = function (msg, m, b, c) {
        var i, j, l, s, r;
        if (m < 1)
            return msg;
        for (i = -1, l = (r = msg.split("\n")).length; ++i < l; r[i] += s)
            for (s = r[i], r[i] = ""; s.length > m; r[i] += s.slice(0, j) + ((s = s.slice(j)).length ? b : ""))
                j = c == 2 || (j = s.slice(0, m + 1).match(/\S*(\s)?$/))[1] ? m : j.input.length - j[0].length
                || c == 1 && m || j.input.length + (j = s.slice(m).match(/^\S*/)).input.length;
        return r.join("\n");
    };

    /**
     *
     */
    this.capitalize = function (self) {
        return self.charAt(0).toUpperCase() + self.substring(1).toLowerCase();
    };

    /**
     *
     */
    this.chars = function (self) {
        return self.split('');
    };

    /**
     *
     */
    this.count = function (self, substr) {
        var result = 0, len = self.length, step = substr.length, index = 0, i;

        for (i = 0; i < len; i += index + step) {
            index = self.indexOf(substr, i);
            if (index < 0) {
                return result;
            }
            result += 1;
        }

        return result;
    };
});

/**
 * @class App.Util.Date
 * @singleton
 * @author Cloudine Inc
 * @since 0.1
 * @see JavaScript Date Format (https://raw.github.com/phstc/jquery-dateFormat/master/jquery.dateFormat-1.0.js)
 */
App.Util.Date = new (function () {

    var daysInWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    var daysInWeekKor = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

    var shortMonthsInYear = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var shortMonthsInYearKor = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];

    var longMonthsInYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    var longMonthsInYearKor = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];

    /**
     * 날짜를 포맷팅한다.
     */
    this.format = function (time, type) {
        if (typeof time === 'string' && App.Util.String.isBlank(time)) {
            return '';
        }
        var date = new Date();
        date.setTime(time);
        return type.replace(/(yyyy|yy|MM|dd|E|HH|mm|ss|a\/p)/gi, function (arg) {
            switch (arg) {
                case 'yyyy':
                    return date.getFullYear();
                case 'MM':
                    return App.Util.String.leftPad(date.getMonth() + 1, 2, '0');
                case 'dd':
                    return App.Util.String.leftPad(date.getDate(), 2, '0');
                case 'HH':
                    return App.Util.String.leftPad(date.getHours(), 2, '0');
                case 'mm':
                    return App.Util.String.leftPad(date.getMinutes(), 2, '0');
                case 'ss':
                    return App.Util.String.leftPad(date.getSeconds(), 2, '0');
                default:
                    return arg;
            }
        });
    };

    /**
     * ExtJS 날짜를 포맷팅한다.
     *
     * @param time 문자열 날짜(예; 2012-01-01)
     * @param pattern ExtJS 날짜 패턴(예; Y-m-d)
     */
    this.formatExtJS = function (time, pattern) {
        return Ext.Date.format(time, pattern)
    };

    this.dateFormat = function (time) {
        var date = new Date(Number(time));
        return Ext.Date.format(date, 'Y-m-d H:i:s')
    };

    this.toHumanReadableTime = function (time) {
        // Minutes and seconds
        var mins = ~~(time / 60);
        var secs = time % 60;

        // Hours, minutes and seconds
        var hrs = ~~(time / 3600);
        var mins = ~~((time % 3600) / 60);
        var secs = time % 60;

        // Output like "1:01" or "4:03:59" or "123:03:59"
        var ret = "";

        if (hrs > 0)
            ret += "" + hrs + ":" + (mins < 10 ? "0" : "");

        ret += "" + mins + ":" + (secs < 10 ? "0" : "");
        ret += "" + secs;
        return ret;
    };

});

/**
 * @class App.Util.WebBrowser
 * @singleton
 * @author Cloudine Inc
 * @since 0.1
 */
App.Util.WebBrowser = new (function () {

    /**
     * 모바일 장치인지 확인한다.
     */
    this.isMobile = function () {
        return /iPad|iPhone|Android|Mobile|Opera Mini|Opera Mobi|POLARIS|Symbian|BlackBerry|LG|MOT|SAMSUNG|Nokia|SonyEricsson|webOS|PalmOS|Fennec|Windows CE|MIDP|SHW-M380/.test(navigator.userAgent);
    };

    /**
     * iPad인지 확인한다.
     */
    this.isIPad = function () {
        return /iPad/.test(navigator.userAgent);
    };

    /**
     * 크롬 브라우저인지 확인한다.
     */
    this.isChrome = function () {
        return /Chrome/.test(navigator.userAgent);
    };

    /**
     * WebSocket의 지원 여부를 확인한다.
     */
    this.isSupportWebSocket = function () {
        var supported = ("WebSocket" in window);
        return supported;
    }
});

App.Util.NullKey = new (function () {
});

App.Util.JavaMap = new (function () {

    var keys = new Array();

    this.contains = function (key) {
        var entry = this.findEntry(key);
        return !(entry == null || entry instanceof App.Util.NullKey);
    };

    this.get = function (key) {
        var entry = this.findEntry(key);
        if (!(entry == null || entry instanceof App.Util.NullKey))
            return entry.value;
        else
            return null;
    };

    this.put = function (key, value) {
        var entry = this.findEntry(key);
        if (entry) {
            entry.value = value;
        } else {
            this.addNewEntry(key, value);
        }
    };

    this.remove = function (key) {
        for (var i = 0; i < keys.length; i++) {
            var entry = keys[i];
            if (entry instanceof App.Util.NullKey) continue;
            if (entry.key == key) {
                keys[i] = App.Util.NullKey;
            }
        }
    };

    this.findEntry = function (key) {
        for (var i = 0; i < keys.length; i++) {
            var entry = keys[i];
            if (entry instanceof App.Util.NullKey) continue;
            if (entry.key == key) {
                return entry
            }
        }
        return null;
    };

    this.addNewEntry = function (key, value) {
        var entry = new Object();
        entry.key = key;
        entry.value = value;
        keys[keys.length] = entry;
    };

});

App.Util.Map = function () {

    this.map = {};

    this.value = {};

    this.getKey = function (id) {
        return id;
    };

    this.put = function (id, value) {
        var key = this.getKey(id);
        this.value[key] = value;
    };

    this.contains = function (id) {
        var key = this.getKey(id);
        return this.value[key];
    };

    this.get = function (id) {
        var key = this.getKey(id);
        if (this.value[key]) {
            return this.value[key];
        }
        return null;
    };

    this.remove = function (id) {
        var key = this.getKey(id);
        if (this.contains(id)) {
            this.value[key] = undefined;
        }
    };
};

App.Util.Iterator = new (function (arrayList) {

    this.arrayList = arrayList;

    this.index = 0;

    this.hasNext = function () {
        return this.index < this.arrayList.length();
    };

    this.next = function () {
        return this.arrayList.get(this.index++);
    };
});


App.Util.ArrayList = new (function () {

    this.array = new Array();

    this.add = function (obj) {
        this.array[this.array.length] = obj;
    };

    this.iterator = function () {
        return new App.Util.Iterator(this)
    };

    this.length = function () {
        return this.array.length;
    };

    this.get = function (index) {
        return this.array[index];
    };

    this.addAll = function (obj) {
        if (obj instanceof Array) {
            for (var i = 0; i < obj.length; i++) {
                this.add(obj[i]);
            }
        } else if (obj instanceof ArrayList) {
            for (var i = 0; i < obj.length(); i++) {
                this.add(obj.get(i));
            }
        }
    }
});

/**
 * Cron Expression Parser
 *
 * @type {(function}
 */
App.Util.CronParser = new (function () {

    /*
     Quartz Job Scheduler에서 제공하는 Cron Trigger는 cron expression을 충실하게 지원합니다. Cron Expression은 0 0 12 * * ? 와 같이 사용할 수 있으며 각 위치는 다음의 의미를 가집니다.

     필드명	필수여부	허용하는 값	허용하는 특수 문자
     초	예	0-59	, - * /
     분	예	0-59	, - * /
     시	예	0-23	, - * /
     일	예	1-31	, - * ? / L W
     월	예	1-12 또는 JAN-DEC	, - * /
     주	예	1-7 또는 SUN-SAT	, - * ? / L #
     년	아니오	빈값, 1970-2099	, - * /

     사용할 수 있는 특수문자는 다음의 의미를 가진다.

     특수기호	의미	기타	기본 지원
     *	모든 값을 선택	분 필드에 *을 사용하면 "매 분마다"의 의미를 가진다.	O
     ?	특정한 값 없음
     -	범위를 지정	시간 필드에 "10-12"를 입력하면 10,11,12를 의미	O
     ,	값 추가	"MON,WED,FRI"로 입력하면 월, 수 금을 의미	O
     /	증분값을 지정	"0/15"을 입력하면 0,15,30,45를 의미하며 "5/15"를 입력하면 5,20,35,50을 의미
     L	마지막	일에 "L"을 입력하면 매월 마지막날을 의미, 주에 입력하면 7 또는 SAT를 의미
     W	주말
     #
     */

    /**
     * Quartz 의 Cron Expression 을 파싱한다.
     *
     * @param {String} cronExpression
     * @returns {Object} 유효한 Cron Expression 이면 파싱된 Object 를 리턴하고 유효하지 않은 경우 false 를 리턴한다.
     */
    this.parse = function (cronExpression) {
        var secExp = '(0)',
            minExp = '(\\*|([0-5]?[0-9])|([0-5]?[0-9]\\-[0-5]?[0-9])|([0-5]?[0-9]\\/[1-9]+[0-9]*)|(([0-5]?[0-9],)([0-5]?[0-9])(,[0-5]?[0-9])*))',
            hourExp = '(\\*|([0-9]|1[0-9]|2[0-3])|(([0-9]|1[0-9]|2[0-3])\\-([0-9]|1[0-9]|2[0-3]))|(([0-9]|1[0-9]|2[0-3])\\/[1-9]+[0-9]*)|((([0-9]|1[0-9]|2[0-3]),)([0-9]|1[0-9]|2[0-3])(,([0-9]|1[0-9]|2[0-3]))*))',
            dayExp = '(\\*|\\?|L|LW|([1-9]|1[0-9]|2[0-9]|3[0-1])W?|(([1-9]|1[0-9]|2[0-9]|3[0-1])\\-([1-9]|1[0-9]|2[0-9]|3[0-1]))|(([1-9]|1[0-9]|2[0-9]|3[0-1])\\/[1-9]+[0-9]*)|((([1-9]|1[0-9]|2[0-9]|3[0-1]),)([1-9]|1[0-9]|2[0-9]|3[0-1])(,([1-9]|1[0-9]|2[0-9]|3[0-1]))*))',
            monthExp = '(\\*|([1-9]|1[0-2])|(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)|(([1-9]|1[0-2])\\-([1-9]|1[0-2]))|((JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\-(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))|(([1-9]|1[0-2])\\/[1-9]+[0-9]*)|((JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\/[1-9]+[0-9]*)|((([1-9]|1[0-2]),)([1-9]|1[0-2])(,([1-9]|1[0-2]))*)|(((JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC),)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(,(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))*))',
            weekExp = '(\\*|\\?|L|([1-7]L?)|(SUN|MON|TUE|WED|THU|FRI|SAT)|([1-7]-[1-7])|((SUN|MON|TUE|WED|THU|FRI|SAT)-(SUN|MON|TUE|WED|THU|FRI|SAT))|([1-7]\\/[1-9]+[0-9]*)|((SUN|MON|TUE|WED|THU|FRI|SAT)\\/[1-9]+[0-9]*)|([1-7],[1-7](,[1-7])*)|((SUN|MON|TUE|WED|THU|FRI|SAT),(SUN|MON|TUE|WED|THU|FRI|SAT)(,(SUN|MON|TUE|WED|THU|FRI|SAT))*)|([1-7]#[1-5])|((SUN|MON|TUE|WED|THU|FRI|SAT)#[1-5]))',
            yearExp = '(\\*|(19[7-9][0-9]|20[0-9][0-9])|(19[7-9][0-9]|20[0-9][0-9])\\-(19[7-9][0-9]|20[0-9][0-9])|((19[7-9][0-9]|20[0-9][0-9])\\/[1-9]+[0-9]*)|((19[7-9][0-9]|20[0-9][0-9]),)(19[7-9][0-9]|20[0-9][0-9])(,(19[7-9][0-9]|20[0-9][0-9]))*)',
            cronRegExp = new RegExp('^' + secExp + ' ' + minExp + ' ' + hourExp + ' ' + dayExp + ' ' + monthExp + ' ' + weekExp + '( ' + yearExp + ')?$'),
            tokens = cronExpression.split(' '),

            MONTH_CONST = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'],
            WEEK_CONST = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'],
            TYPE_CONST = ['ALL', 'NO_SPECIFIC', 'RANGE', 'SPECIFIC', 'INCREMENT', 'NTH', 'LAST', 'LAST_WEEK', 'NEAREST_WEEK'],
            convertMonth = function (value) {
                if (Ext.isArray(value)) {
                    var values = [];
                    for (var i = 0; i < value.length; i++) {
                        if (/^([1-9]|1[0-2])$/.test(value[i])) {
                            values.push(MONTH_CONST[parseInt(value[i]) - 1]);
                        } else {
                            values.push(value[i]);
                        }
                    }
                    return values;
                } else {
                    if (/^([1-9]|1[0-2])$/.test(value)) {
                        return MONTH_CONST[parseInt(value) - 1];
                    } else {
                        return value;
                    }
                }
            },
            convertWeek = function (value) {
                if (Ext.isArray(value)) {
                    var values = [];
                    for (var i = 0; i < value.length; i++) {
                        if (/^[1-7]$/.test(value[i])) {
                            values.push(WEEK_CONST[parseInt(value[i]) - 1]);
                        } else if (/^(SUN|MON|TUE|WED|THU|FRI|SAT)$/.test(value[i])) {
                            values.push(value[i]);
                        } else {
                            values.push(value[i]);
                        }
                    }
                    return values;
                } else {
                    if (/^[1-7]$/.test(value)) {
                        return WEEK_CONST[parseInt(value) - 1];
                    } else if (/^(SUN|MON|TUE|WED|THU|FRI|SAT)$/.test(value)) {
                        return value;
                    } else {
                        return value;
                    }
                }
            };

        if (cronRegExp.test(cronExpression)) {
            var sec = tokens[0],
                min = tokens[1],
                hour = tokens[2],
                day = tokens[3],
                month = tokens[4],
                week = tokens[5],
                year = tokens[6],
                cronObj = {};

            if ((day == '?' && week == '?') || (day != '?' && week != '?')) {
                return false;
            }

            // second
            cronObj.sec = {
                type: 'SPECIFIC',
                value: [sec],
                orgValue: sec
            };

            // minute
            if (/^[0-5]?[0-9]$/.test(min)) {
                cronObj.min = {
                    type: 'SPECIFIC',
                    value: [min]
                };
            } else if (min == '*') {
                cronObj.min = {
                    type: 'ALL',
                    value: [min]
                };
            } else if (min.indexOf('-') > 0) {
                cronObj.min = {
                    type: 'RANGE',
                    value: min.split('-')
                };
            } else if (min.indexOf(',') > 0) {
                cronObj.min = {
                    type: 'SPECIFIC',
                    value: min.split(',')
                };
            } else if (min.indexOf('/') > 0) {
                cronObj.min = {
                    type: 'INCREMENT',
                    value: min.split('/')
                };
            }
            cronObj.min.orgValue = min;

            // hour
            if (/^([0-9]|1[0-9]|2[0-3])$/.test(hour)) {
                cronObj.hour = {
                    type: 'SPECIFIC',
                    value: [hour]
                };
            } else if (hour == '*') {
                cronObj.hour = {
                    type: 'ALL',
                    value: [hour]
                };
            } else if (hour.indexOf('-') > 0) {
                cronObj.hour = {
                    type: 'RANGE',
                    value: hour.split('-')
                };
            } else if (hour.indexOf(',') > 0) {
                cronObj.hour = {
                    type: 'SPECIFIC',
                    value: hour.split(',')
                };
            } else if (hour.indexOf('/') > 0) {
                cronObj.hour = {
                    type: 'INCREMENT',
                    value: hour.split('/')
                };
            }
            cronObj.hour.orgValue = hour;

            // day
            if (/^([1-9]|1[0-9]|2[0-9]|3[0-1])$/.test(day)) {
                cronObj.day = {
                    type: 'SPECIFIC',
                    value: [day]
                };
            } else if (/^([1-9]|1[0-9]|2[0-9]|3[0-1])W$/.test(day)) {
                cronObj.day = {
                    type: 'NEAREST_WEEK',
                    value: [day.replace('W', '')],
                    '@isNearest': true
                };
            } else if (day == '?') {
                cronObj.day = {
                    type: 'NO_SPECIFIC',
                    value: [day]
                };
            } else if (day == '*') {
                cronObj.day = {
                    type: 'ALL',
                    value: [day]
                };
            } else if (day.indexOf('-') > 0) {
                cronObj.day = {
                    type: 'RANGE',
                    value: day.split('-')
                };
            } else if (day.indexOf(',') > 0) {
                cronObj.day = {
                    type: 'SPECIFIC',
                    value: day.split(',')
                };
            } else if (day.indexOf('/') > 0) {
                cronObj.day = {
                    type: 'INCREMENT',
                    value: day.split('/')
                };
            } else if (day == 'L') {
                cronObj.day = {
                    type: 'LAST',
                    value: [day],
                    '@isLast': true
                };
            } else if (day == 'LW') {
                cronObj.day = {
                    type: 'LAST_WEEK',
                    value: [day.replace('W', '')],
                    '@isLast': true,
                    '@isNearest': true
                };
            }
            cronObj.day.orgValue = day;

            // month
            if (/^([1-9]|1[0-2])$/.test(month)) {
                cronObj.month = {
                    type: 'SPECIFIC',
                    value: [convertMonth(month)]
                };
            } else if (/^(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)$/.test(month)) {
                cronObj.month = {
                    type: 'SPECIFIC',
                    value: [month]
                };
            } else if (month == '*') {
                cronObj.month = {
                    type: 'ALL',
                    value: [month]
                };
            } else if (month.indexOf('-') > 0) {
                cronObj.month = {
                    type: 'RANGE',
                    value: convertMonth(month.split('-'))
                };
            } else if (month.indexOf(',') > 0) {
                cronObj.month = {
                    type: 'SPECIFIC',
                    value: convertMonth(month.split(','))
                };
            } else if (month.indexOf('/') > 0) {
                cronObj.month = {
                    type: 'INCREMENT',
                    value: [convertMonth(month.split('/')[0]), month.split('/')[1]]
                };
            }
            cronObj.month.orgValue = month;

            // week
            if (/^[1-7]$/.test(week)) {
                cronObj.week = {
                    type: 'SPECIFIC',
                    value: [week]
                };
            } else if (/^[1-7]L$/.test(week)) {
                cronObj.week = {
                    type: 'LAST_WEEK',
                    value: [convertWeek(week.replace('L', ''))],
                    '@isLast': true
                };
            } else if (/^(SUN|MON|TUE|WED|THU|FRI|SAT)$/.test(week)) {
                cronObj.week = {
                    type: 'SPECIFIC',
                    value: [week]
                };
            } else if (week == '?') {
                cronObj.week = {
                    type: 'NO_SPECIFIC',
                    value: [week]
                };
            } else if (week == '*') {
                cronObj.week = {
                    type: 'ALL',
                    value: [week]
                };
            } else if (week.indexOf('-') > 0) {
                cronObj.week = {
                    type: 'RANGE',
                    value: convertWeek(week.split('-'))
                };
            } else if (week.indexOf(',') > 0) {
                cronObj.week = {
                    type: 'SPECIFIC',
                    value: convertWeek(week.split(','))
                };
            } else if (week.indexOf('/') > 0) {
                cronObj.week = {
                    type: 'INCREMENT',
                    value: [convertWeek(week.split('/')[0]), week.split('/')[1]]
                };
            } else if (week.indexOf('#') > 0) {
                cronObj.week = {
                    type: 'NTH',
                    value: [convertWeek(week.split('#')[0]), week.split('#')[1]]
                };
            } else if (week == 'L') {
                cronObj.week = {
                    type: 'LAST',
                    value: ['SAT']
                };
            }
            cronObj.week.orgValue = week;

            // year
            if (year !== undefined) {
                if (/^(19[7-9][0-9]|20[0-9][0-9])$/.test(year)) {
                    cronObj.year = {
                        type: 'SPECIFIC',
                        value: [year]
                    };
                } else if (year == '*') {
                    cronObj.year = {
                        type: 'ALL',
                        value: [year]
                    };
                } else if (year.indexOf('-') > 0) {
                    cronObj.year = {
                        type: 'RANGE',
                        value: year.split('-')
                    };
                    if (cronObj.year.value[0] > cronObj.year.value[1]) {
                        return false;
                    }
                } else if (year.indexOf(',') > 0) {
                    cronObj.year = {
                        type: 'SPECIFIC',
                        value: year.split(',')
                    };
                } else if (year.indexOf('/') > 0) {
                    cronObj.year = {
                        type: 'INCREMENT',
                        value: year.split('/')
                    };
                }
                cronObj.year.orgValue = year;
            }

            return cronObj;
        } else {
            return false;
        }
    };

});