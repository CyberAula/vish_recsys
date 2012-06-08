/**
 * 
 */
package distance;

import entities.UserProfile;


/**
 * @author Daniel Gallego Vico
 *
 */
public class ViSHDistance {
	
	// Weight for subject
	private double wSubject = 0.5;
	private double wLanguage = 0.5;
	
	/**
	 * Constructor
	 */
	public ViSHDistance() {
		
	}
	
	/**
	 * Specific measure to calculate the distance between two user profiles in ViSH
	 * A user profile is defined as follows: 
	 * <int userId, String subject, String language>
	 * The distance is defined as follows:
	 * Dvish (u1, u2) = wSubject * editD(u1_subject, u2_subject) + wLanguage * editD(u1_language, u2_language)
	 * 
	 * The userID is ignored when calculating the distance
	 * 
	 * @param u1
	 * @param u2
	 */
	public double calculateDistance(UserProfile u1, UserProfile u2) {
		double dist = 0;
		
		// First, calculate the distance between the subjects
		double subjectDist = levenshteinDistance(u1.getSubject(), u2.getSubject());
		// Second, calculate the distance between the languages
		double languageDist = levenshteinDistance(u1.getLanguage(), u2.getLanguage());
		// Finally, calculate the final weighted distance
		dist = (wSubject * subjectDist + wLanguage * languageDist)/(wSubject + wLanguage);
		
		return dist;
	}

	/**
	 * Compute the similarity based on the Levenshtein distance
	 * 
	 * @param source
	 * @param target
	 * @return the similarity between two strings
	 */
	private double similarityLevenshteinDistance(String source, String target) {
		double editDist = levenshteinDistance(source, target);
		double sim = 1 / (1 + editDist);
		return sim;
	}
	
	/**
	 * Compute Levenshtein distance
	 * 
	 * @param source string
	 * @param target string
	 * @return the distance between two strings
	 */
	private int levenshteinDistance (String source, String target) {
		int d[][]; // d
		int n; // length of source  
		int m; // length of target
		int i; // iterates through source
		int j; // iterates through target
		char s_i; // ith character of source
		char t_j; // jth character of target
		int cost; // cost

	    /* Step 1
	     *******************************
	     * Set n to be the length of source
	     * Set m to be the length of target
	     * If n = 0, return m and exit
	     * If m = 0, return n and exit
	     * Construct a d containing 0..m rows and 0..n columns
	     */
	    n = source.length ();
	    m = target.length ();
	    if (n == 0) {
	      return m;
	    }
	    if (m == 0) {
	      return n;
	    }
	    d = new int[n+1][m+1];

	    /* Step 2
	     *******************************
	     * Initialize the first row to 0..n
	     * Initialize the first column to 0..m
	     */
	    for (i = 0; i <= n; i++) {
	      d[i][0] = i;
	    }
	    
	    for (j = 0; j <= m; j++) {
	      d[0][j] = j;
	    }

	    /* Step 3
	     ******************************* 
	     * Examine each character of source (i from 1 to n)
	     */
	    for (i = 1; i <= n; i++) {

	      s_i = source.charAt (i - 1);

	      /* Step 4
	       ******************************* 
	       * Examine each character of target (j from 1 to m)
	       */
	      for (j = 1; j <= m; j++) {

	        t_j = target.charAt (j - 1);

	        /* Step 5
	         *******************************
	         * If s[i] equals t[j], the cost is 0
	         * If s[i] doesn't equal t[j], the cost is 1
	         */
	        if (s_i == t_j) {
	          cost = 0;
	        }
	        else {
	          cost = 1;
	        }

	        /* Step 6
	         *******************************
	         * Set cell d[i,j] of the d equal to the minimum of:
	         * a) The cell immediately above plus 1: d[i-1,j] + 1
	         * b) The cell immediately to the left plus 1: d[i,j-1] + 1
	         * c) The cell diagonally above and to the left plus the cost: d[i-1,j-1] + cost
	         */
	        d[i][j] = minimum (d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1] + cost);

	      }
	    }

	    /* Step 7
	     ******************************* 
	     * After the iteration steps (3, 4, 5, 6) are complete, the distance is found in cell d[n,m]
	     */
	    return d[n][m];
	  }
	
	/**
	 * Get minimum of three values
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the minimum value among a,b,c
	 */
	private int minimum (int a, int b, int c) {
		int min;

		min = a;
		if (b < min) {
			min = b;
		}
		if (c < min) {
			min = c;
		}
		return min;
	  }
}
