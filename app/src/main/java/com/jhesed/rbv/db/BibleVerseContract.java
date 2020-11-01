package com.jhesed.rbv.db;

import android.provider.BaseColumns;

public class BibleVerseContract {

    public static final String DB_NAME = "jhesed.rbv.RBVDao";
    public static final int DB_VERSION = 1;

    public class BibleVerseEntry implements BaseColumns {
        public static final String TABLE = "bible_verse";

        // properties
        public static final String COL_TITLE = "title";
        public static final String COL_MBB = "mbb";
        public static final String COL_NIV = "niv";
        public static final String COL_NASB = "nasb";

        // dates and modifications
        public static final String COL_DATE_CREATED = "date_created";
        public static final String COL_DATE_MODIFIED = "date_modified";
        public static final String COL_DATE_LAST_SYNC = "date_last_synced";
        public static final String COL_IS_SYNCED = "is_synced";
    }
}
