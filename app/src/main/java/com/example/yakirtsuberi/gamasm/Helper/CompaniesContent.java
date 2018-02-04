package com.example.yakirtsuberi.gamasm.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompaniesContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<CompanyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, CompanyItem> ITEM_MAP = new HashMap<>();

    private static final String[] COMPANIES_KEY = {"cellcom", "partner", "pelephon", "mobile_012", "hot_mobile", "golan_telecom", "rami_levi"};
    private static final String[] COMPANIES_VALUE = {"סלקום", "פרטנר", "פלאפון", "012 מובייל", "הוט מובייל", "גולן טלקום", "רמי לוי"};

    static {
        // Add some sample items.
        for (int i = 0; i < COMPANIES_KEY.length; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(CompanyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CompanyItem createDummyItem(int position) {
        return new CompanyItem(String.valueOf(position), COMPANIES_VALUE[position], COMPANIES_KEY[position]);
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class CompanyItem {
        public final String id;
        public final String name;
        public final String image;

        private CompanyItem(String id, String name, String image) {
            this.id = id;
            this.name = name;
            this.image = image;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
