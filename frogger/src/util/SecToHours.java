package util;

import java.io.PrintStream;
import java.util.Scanner;

public class SecToHours {

    public int[] start(int totalSecs) {

        int[] times = new int[3];
        times[0] = totalSecs / 3600;
        times[1] = (totalSecs % 3600) / 60;
        times[2] = totalSecs % 60;
        return times ;

    }
	
}
