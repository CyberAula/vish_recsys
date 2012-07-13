package distance;

/**
 * Compute Levenshtein distance 
 * to calculate the distance between 2 Strings
 * 
 * @author Daniel Gallego Vico
 *
 */

public class LevenshteinDistance {
    
    /**
     * Distance between 2 Strings
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static int computeLevenshteinDistance(String str1, String str2) {
        return computeLevenshteinDistance(str1.toCharArray(),
                                          str2.toCharArray());
    }
    
    /**
	 * @param a
	 * @param b
	 * @param c
	 * @return the minimum among a, b, c
	 */
	private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
 
    /**
     * Distance between 2 char arrays
     * 
     * @param str1
     * @param str2
     * @return
     */
    private static int computeLevenshteinDistance(char [] str1, char [] str2) {
        int [][]distance = new int[str1.length + 1][str2.length + 1];
 
        for(int i=0; i<=str1.length; i++)
        {
                distance[i][0]=i;
        }
        for(int j=0; j<=str2.length; j++)
        {
                distance[0][j]=j;
        }
        for(int i=1; i<=str1.length; i++)
        {
            for(int j=1; j<=str2.length; j++)
            { 
                  distance[i][j]= minimum(distance[i-1][j] + 1,
                                        distance[i][j-1] + 1,
                                        distance[i-1][j-1] + ((str1[i-1]==str2[j-1]) ? 0 : 1));
            }
        }
        return distance[str1.length][str2.length];
 
    }
    
    /**
	 * Compute the similarity based on the Levenshtein distance
	 * 
	 * @param source
	 * @param target
	 * @return the similarity between two strings
	 */
	private double similarity(String source, String target) {
		double editDist = computeLevenshteinDistance(source, target);
		double sim = 1 / (1 + editDist);
		return sim;
	}
}
