package com.example.casher.model.database;

import android.provider.BaseColumns;

public class DatabaseSettings {
    public static final String DB_NAME = "casher.db";
    public static final int DB_VERSION = 3;

    public class MonthEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "month";
        public static final String COL_MONTH_NAME = "month_name";
        public static final String COL_MONTH_INCOME = "income";
        public static final String COL_MONTH_EXPENSES = "expenses";
        public static final String COL_MONTH_BALANCE = "balance";
    }

    public class DateEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "date";
        public static final String COL_DATE_NAME = "date_name";
        public static final String COL_DATE_INCOME = "income";
        public static final String COL_DATE_EXPENSES = "expenses";
    }

    public class DetailsInEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "details_in";
        public static final String COL_DATE_NAME = "date_name";
        public static final String COL_IN_CATEGORIES = "categories";
        public static final String COL_IN_TOTAL = "total";
        public static final String COL_IN_NOTES = "notes";
    }

    public class DetailsExEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "details_ex";
        public static final String COL_DATE_NAME = "date_name";
        public static final String COL_EX_CATEGORIES = "categories";
        public static final String COL_EX_TOTAL = "total";
        public static final String COL_EX_NOTES = "notes";
    }
}
