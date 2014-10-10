package com.baidu.chopsearch.utils;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

public abstract class FormatsFactory {

    private static final Format RMB_THOUSAND = new DecimalFormat(
            "\u00A4#,##0.00");

    public static final SpannableFormat RMB_SHORT = new RMBShortFormats();

    public static final SpannableFormat RMB = new RMBFormat();

    public static final Format DECIMAL = new DecimalFormat("0.0");

    public static final Format DECIMAL_PERCENT = new DecimalFormat("0.0%");

    /**
     * 日期的周表示（缩略式，例如周一）
     */
    public static final Format WEEK = new SimpleDateFormat("EEE",
            Locale.getDefault());

    public static final Format DATE_DOT_MD = new SimpleDateFormat("MM.dd",
            Locale.getDefault());

    public static final Format DATE_DOT_YMD = new SimpleDateFormat(
            "yyyy.MM.dd", Locale.getDefault());

    public static final Format DATE_DOT_YMDHMS = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static final Format DATE_CN_YM = new SimpleDateFormat("yyyy年MM月",
            Locale.getDefault());

    public static abstract class SpannableFormat {
        abstract public SpannableString format(double value);
    }

    public static class RMBShortFormats extends SpannableFormat {

        public static class Unit {
            public Unit(String magnitude, float dispScaleFactor) {
                super();
                this.magnitude = magnitude;
                this.fracScaleFactor = dispScaleFactor;
            }

            String magnitude;
            float fracScaleFactor;
        }

        private final static float GRADE = 10000f;
        private final static List<Unit> UNITS = new ArrayList<Unit>();
        static {
            UNITS.add(new Unit("", 0.618f));
            UNITS.add(new Unit("万", 1.0f));
            UNITS.add(new Unit("亿", 1.0f));
            UNITS.add(new Unit("兆", 1.0f));
        }

        @Override
        public SpannableString format(double value) {
            Format rmbf = RMB_THOUSAND;

            Unit u = UNITS.get(0);
            for (int loc = 1, n = UNITS.size(); loc < n; loc++) {
                final double nv = value / GRADE;
                if (Math.abs(nv) >= 1.0f) {
                    value = nv;
                    u = UNITS.get(loc);
                } else {
                    break;
                }
            }

            String rmbStr = rmbf.format(value);
            String spanStr = rmbStr;
            if (!TextUtils.isEmpty(u.magnitude)) {
                spanStr += (" " + u.magnitude);
            }

            SpannableString rmbSpan = new SpannableString(spanStr);
            int pointIndex = rmbStr.length();
            if (rmbStr.length() > 2) {
                pointIndex -= 2;
            }
            if (pointIndex != -1) {
                rmbSpan.setSpan(new RelativeSizeSpan(u.fracScaleFactor),
                        pointIndex, rmbStr.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            pointIndex = spanStr.length() - u.magnitude.length();
            rmbSpan.setSpan(new RelativeSizeSpan(0.738f), pointIndex,
                    spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            return rmbSpan;
        }

    }

    public static class RMBFormat extends SpannableFormat {

        @Override
        public SpannableString format(double value) {
            String rmbStr = RMB_THOUSAND.format(value);
            int pointIndex = rmbStr.length();

            if (rmbStr.length() > 2) {
                pointIndex -= 2;
            }

            SpannableString rmbSpan = new SpannableString(rmbStr);

            if (pointIndex != -1) {
                rmbSpan.setSpan(new RelativeSizeSpan(.618f), pointIndex,
                        (rmbStr).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            return rmbSpan;
        }

    }
}
