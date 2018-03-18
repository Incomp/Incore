package me.incomp.plugins.pub.incore.commons;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Incomp
 * @since Mar 18, 2018
 */
public class MathUtil {

	public static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public static double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max + 1);
	}
	
	public static long randomLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max + 1);
	}
}
