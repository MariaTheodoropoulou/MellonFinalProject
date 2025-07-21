package gr.android.moviesapp.common;

public class TimeFormater {
    public static String formatTime(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
}
