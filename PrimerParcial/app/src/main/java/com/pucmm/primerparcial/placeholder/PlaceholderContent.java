package com.pucmm.primerparcial.placeholder;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderVersion> ITEMS = new ArrayList<PlaceholderVersion>();


    static {
        ITEMS.add(new PlaceholderVersion("Android 1.0", "N/A", getDetails("Android 1.0"), "1.0", new Date(2008, 9, 23), false, 1, "https://en.wikipedia.org/wiki/Android_1.0"));
        ITEMS.add(new PlaceholderVersion("Android 1.1", "Petit Four", getDetails("Android 1.1"), "1.1", new Date(2009, 2, 9), false, 2, "https://en.wikipedia.org/wiki/Android_1.1"));
        ITEMS.add(new PlaceholderVersion("Android Cupcake", "Cupcake", getDetails("Android Cupcake"), "1.5", new Date(2009, 4, 27), false, 3, "https://en.wikipedia.org/wiki/Android_Cupcake"));
        ITEMS.add(new PlaceholderVersion("Android Donut", "Donut", getDetails("Android Donut"), "1.6", new Date(2009, 9, 15), false, 4, "https://en.wikipedia.org/wiki/Android_Donut"));
        ITEMS.add(new PlaceholderVersion("Android Eclair", "Eclair", getDetails("Android Eclair"), "2.0", new Date(2009, 10, 27), false, 5, "https://en.wikipedia.org/wiki/Android_Eclair"));
        ITEMS.add(new PlaceholderVersion("Android Eclair", "Eclair", getDetails("Android Eclair"), "2.0.1", new Date(2009, 12, 3), false, 6, "https://en.wikipedia.org/wiki/Android_Eclair"));
        ITEMS.add(new PlaceholderVersion("Android Eclair", "Eclair", getDetails("Android Eclair"), "2.1", new Date(2010, 1, 11), false, 7, "https://en.wikipedia.org/wiki/Android_Eclair"));
        ITEMS.add(new PlaceholderVersion("Android Froyo", "Froyo", getDetails("Android Froyo"), "2.2 - 2.2.3", new Date(2010, 5, 20), false, 8, "https://en.wikipedia.org/wiki/Android_Froyo"));
        ITEMS.add(new PlaceholderVersion("Android Gingerbread", "Gingerbread", getDetails("Android Gingerbread"), "2.3 – 2.3.2", new Date(2010, 12, 6), false, 9, "https://en.wikipedia.org/wiki/Android_Gingerbread"));
        ITEMS.add(new PlaceholderVersion("Android Gingerbread", "Gingerbread", getDetails("Android Gingerbread"), "2.3.3 - 2.3.7", new Date(2011, 2, 9), false, 10, "https://en.wikipedia.org/wiki/Android_Gingerbread"));
        ITEMS.add(new PlaceholderVersion("Android Honeycomb", "Honeycomb", getDetails("Android Honeycomb"), "3.0", new Date(2011, 2, 22), false, 11, "https://en.wikipedia.org/wiki/Android_Honeycomb"));
        ITEMS.add(new PlaceholderVersion("Android Honeycomb", "Honeycomb", getDetails("Android Honeycomb"), "3.1", new Date(2011, 5, 10), false, 12, "https://en.wikipedia.org/wiki/Android_Honeycomb"));
        ITEMS.add(new PlaceholderVersion("Android Honeycomb", "Honeycomb", getDetails("Android Honeycomb"), "3.2 - 3.2.6", new Date(2011, 7, 15), false, 13, "https://en.wikipedia.org/wiki/Android_Honeycomb"));
        ITEMS.add(new PlaceholderVersion("Android Ice Cream Sandwich", "Ice Cream Sandwich", getDetails("Android Ice Cream Sandwich"), "4.0 – 4.0.2", new Date(2011, 10, 18), false, 14, "https://en.wikipedia.org/wiki/Android_Ice_Cream_Sandwich"));
        ITEMS.add(new PlaceholderVersion("Android Ice Cream Sandwich", "Ice Cream Sandwich", getDetails("Android Ice Cream Sandwich"), "4.0.3 - 4.0.4", new Date(2011, 12, 16), false, 15, "https://en.wikipedia.org/wiki/Android_Ice_Cream_Sandwich"));
        ITEMS.add(new PlaceholderVersion("Android Jelly Bean", "Jelly Bean", getDetails("Android Jelly Bean"), "4.1 – 4.1.2", new Date(2012, 7, 9), false, 16, "https://en.wikipedia.org/wiki/Android_Jelly_Bean"));
        ITEMS.add(new PlaceholderVersion("Android Jelly Bean", "Jelly Bean", getDetails("Android Jelly Bean"), "4.2 - 4.2.2", new Date(2012, 11, 13), false, 17, "https://en.wikipedia.org/wiki/Android_Jelly_Bean"));
        ITEMS.add(new PlaceholderVersion("Android Jelly Bean", "Jelly Bean", getDetails("Android Jelly Bean"), "4.3 - 4.3.1", new Date(2013, 7, 24), false, 18, "https://en.wikipedia.org/wiki/Android_Jelly_Bean"));
        ITEMS.add(new PlaceholderVersion("Android KitKat", "Key Lime Pie", getDetails("Android KitKat"), "4.4 – 4.4.4", new Date(2013, 10, 31), false, 19, "https://en.wikipedia.org/wiki/Android_KitKat"));
        ITEMS.add(new PlaceholderVersion("Android KitKat", "Key Lime Pie", getDetails("Android KitKat"), "4.4W - 4.4W.2", new Date(2014, 6, 5), false, 20, "https://en.wikipedia.org/wiki/Android_KitKat"));
        ITEMS.add(new PlaceholderVersion("Android Lollipop", "Lemon Meringue Pie", getDetails("Android Lollipop"), "5.0 – 5.0.2", new Date(2014, 11, 4), false, 21, "https://en.wikipedia.org/wiki/Android_Lollipop"));
        ITEMS.add(new PlaceholderVersion("Android Lollipop", "Lemon Meringue Pie", getDetails("Android Lollipop"), "5.1 - 5.1.1", new Date(2015, 3, 2), false, 22, "https://en.wikipedia.org/wiki/Android_Lollipop"));
        ITEMS.add(new PlaceholderVersion("Android Marshmallow", "Macadamia Nut Cookie", getDetails("Android Marshmallow"), "6.0 – 6.0.1", new Date(2015, 10, 3), false, 23, "https://en.wikipedia.org/wiki/Android_Marshmallow"));
        ITEMS.add(new PlaceholderVersion("Android Nougat", "New York Cheesecake", getDetails("Android Nougat"), "7.0", new Date(2016, 8, 22), false, 24, "https://en.wikipedia.org/wiki/Android_Nougat"));
        ITEMS.add(new PlaceholderVersion("Android Nougat", "New York Cheesecake", getDetails("Android Nougat"), "7.1 - 7.1.2", new Date(2016, 10, 4), false, 25, "https://en.wikipedia.org/wiki/Android_Nougat"));
        ITEMS.add(new PlaceholderVersion("Android Oreo", "Oatmeal Cookie", getDetails("Android Oreo"), "8.0", new Date(2017, 8, 21), false, 26, "https://en.wikipedia.org/wiki/Android_Oreo"));
        ITEMS.add(new PlaceholderVersion("Android Oreo", "Oatmeal Cookie", getDetails("Android Oreo"), "8.1", new Date(2017, 12, 5), true, 27, "https://en.wikipedia.org/wiki/Android_Oreo"));
        ITEMS.add(new PlaceholderVersion("Android Pie", "", getDetails("Android Pie"), "9", new Date(2018, 8, 6), true, 28, "https://en.wikipedia.org/wiki/Android_Pie"));
        ITEMS.add(new PlaceholderVersion("Android 10", "Quince Tart", getDetails("Android 10"), "10", new Date(2019, 9, 3), true, 29, "https://en.wikipedia.org/wiki/Android_10"));
        ITEMS.add(new PlaceholderVersion("Android 11", "Red Velvet Cake", getDetails("Android 11"), "11", new Date(2020, 9, 8), true, 30, "https://en.wikipedia.org/wiki/Android_11"));
        ITEMS.add(new PlaceholderVersion("Android 12", "Snow Cone", getDetails("Android 12"), "12", new Date(2021, 10, 4), true, 31, "https://en.wikipedia.org/wiki/Android_12"));
    }


    private static String getDetails(String name) {
        switch (name) {
            case "Android 1.0":
                return "Android 1.0, the first commercial version of the software, was released on September 23, 2008.[47] The first commercially available Android device was the HTC Dream";
            case "Android 1.1":
                return "On February 9, 2009, the Android 1.1 update was released, initially for the HTC Dream only. Android 1.1 was known as \"Petit Four\" internally, though this name was not used officially";
            case "Android Cupcake":
                return "Android Cupcake (version 1.5) is the third version of Android developed by Google, a major platform release deployable to Android-powered handsets starting in April 2009, that is no longer supported";
            case "Android Donut":
                return "Android 1.6 Donut is the fourth version of the open source Android mobile operating system developed by Google that is no longer supported. Among the more prominent features introduced with this update were added support for CDMA smartphones, additional screen sizes, a battery usage indicator, and a text-to-speech engine.";
            case "Android Eclair":
                return "Android Eclair is a codename of the Android mobile operating system developed by Google, the fifth operating system for Android and the second major release of Android, and for the no-longer supported versions 2.0 to 2.1. Unveiled on October 26, 2009, Android 2.1 builds upon the significant changes made in Android 1.6 \"Donut\".";
            case "Android Froyo":
                return "Android Froyo is the sixth version of Android and is a codename of the Android mobile operating system developed by Google, spanning versions between 2.2 and 2.2.3.";
            case "Android Gingerbread":
                return "Android 2.3 Gingerbread is the seventh version of Android, a codename of the Android mobile operating system developed by Google and released in December 2010, for versions that are no longer supported.";
            case "Android Honeycomb":
                return "Android Honeycomb is the codename for the third version of Android, designed for devices with larger screen sizes, particularly tablets. It is the third major release of Android and is no longer supported. Honeycomb debuted with the Motorola Xoom in February 2011.";
            case "Android Ice Cream Sandwich":
                return "Android Ice Cream Sandwich is the 9th major version of the Android mobile operating system developed by Google. Unveiled on October 19, 2011, Android 4.0 builds upon the significant changes made by the tablet-only release Android Honeycomb, in an effort to create a unified platform for both smartphones and tablets.";
            case "Android Jelly Bean":
                return "Android Jelly Bean is the codename given to the tenth version of the Android mobile operating system developed by Google, spanning three major point releases (versions 4.1 through 4.3.1). Among the devices that run Android 4.3 are the Asus Nexus 7 (2013) and the LG Nexus 4.";
            case "Android KitKat":
                return "Android KitKat is the codename for the eleventh Android mobile operating system, representing release version 4.4. Unveiled on September 3, 2013, KitKat focused primarily on optimizing the operating system for improved performance on entry-level devices with limited resources.";
            case "Android Lollipop":
                return "Android Lollipop (codenamed Android L during development) is the fifth major version of the Android mobile operating system developed by Google and the 12th version of Android, spanning versions between 5.0 and 5.1.1.[";
            case "Android Marshmallow":
                return "Android Marshmallow (codenamed Android M during development) is the sixth major version of the Android operating system and the 13th version of Android. First released as a beta build on May 28, 2015, it was officially released on October 5, 2015, with the Nexus devices being the first to receive the update.";
            case "Android Nougat":
                return "Android Nougat (codenamed Android N during development) is the seventh major version and 14th original version of the Android operating system. First released as an alpha test version on March 9, 2016, it was officially released on August 22, 2016, with Nexus devices being the first to receive the update. The LG V20 was the first smartphone released with Nougat.";
            case "Android Oreo":
                return "Android Oreo (codenamed Android O during development) is the eighth major release and the 15th version of the Android mobile operating system. It was first released as an alpha quality developer preview in March 2017 and released to the public on August 21, 2017.";
            case "Android Pie":
                return "Android Pie (codenamed Android P during development) is the ninth major release and the 16th version of the Android mobile operating system. It was first released as a developer preview on March 7, 2018, and was released publicly on August 6, 2018.";
            case "Android 10":
                return "Android 10 (codenamed Android Q during development) is the tenth major release and the 17th version of the Android mobile operating system. It was first released as a developer preview on March 13, 2019, and was released publicly on September 3, 2019.";
            case "Android 11":
                return "Android 11 is the eleventh major release and 18th version of Android, the mobile operating system developed by the Open Handset Alliance led by Google.";
            case "Android 12":
                return "Android 12 is the twelfth major release and 19th version of Android, the mobile operating system developed by the Open Handset Alliance led by Google. The first beta was released on May 18, 2021. Android 12 was released publicly on October 4, 2021 and is the latest Android version to date.";
            default:
                return "";
        }
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderVersion implements Serializable {
        private final String name;
        private final String internalCodeName;
        private final String details;
        private final String versionNumber;
        private final Date releaseDate;
        private final boolean supported;
        private final int level;
        private final String link;

        public PlaceholderVersion(String name, String internalCodeName, String details, String versionNumber, Date releaseDate, boolean supported, int level, String link) {
            this.name = name;
            this.internalCodeName = internalCodeName;
            this.details = details;
            this.versionNumber = versionNumber;
            this.releaseDate = releaseDate;
            this.supported = supported;
            this.level = level;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getInternalCodeName() {
            return internalCodeName;
        }

        public String getDetails() {
            return details;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public boolean isSupported() {
            return supported;
        }

        public int getLevel() {
            return level;
        }

        public String getLink() {
            return link;
        }
    }
}