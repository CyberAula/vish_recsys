/**
 * Auxiliary methods 
 */
package utils;

/**
 * @author Daniel Gallego Vico
 *
 */
public class MathUtils {
	
	/**
	 * Normalization by scaling between 0 and 1
	 * a value corresponding to a variable
	 * 
	 * If MAX value is equals to MIN, 
	 * then the normalized value is set to 0.5 
	 * 
	 * @param value to normalize 
	 * @param variableMax
	 * @param variableMin
	 * @return value normalized
	 */
	public static double normalizeToZeroOne(double value, double variableMax, double variableMin) {
		if(variableMax == variableMin) {
			return 0.5;
		}
		return (value-variableMin)/(variableMax-variableMin);
	}

}
