/**
 * Provides the distance metric use in ViSH 
 * to calculate the similarity among users
 */
package distance;

import java.util.Iterator;
import java.util.List;

import entities.UserProfile;


/**
 * @author Daniel Gallego Vico
 *
 */
public class ViSHDistance {
	
	// Weight for subject
	private double wSubject = 0.8;
	private double wLanguage = 0.2;
	
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
	 * Dvish (u1, u2) = [wSubject * editD(u1_subject, u2_subject) + 
	 * 						wLanguage * editD(u1_language, u2_language)]/(wSubject + wLanguage)
	 * 
	 * The userID is ignored when calculating the distance
	 * 
	 * @param u1 user
	 * @param u2 user
	 * @return the distance between u1 and u2
	 */
	public double calculateDistance(UserProfile u1, UserProfile u2) {
		double dist = 0;
		
		// First, calculate the distance among the subjects
		double subjectDist = subjectFolkDistance(u1.getSubjects(), u2.getSubjects());
		// Second, calculate the distance among the languages
		double languageDist = languageFolkDistance(u1.getLanguages(), u2.getLanguages());
		// Finally, calculate the final weighted distance
		dist = (wSubject * subjectDist + wLanguage * languageDist)/(wSubject + wLanguage);
		
		return dist;
	}
	
	/**
	 * Distance metric over the folksonomy of subjects related to ViSH
	 * defined as follows:
	 * Dsubject (S1 , S2) = min {editD(s1i , s2j)}
	 * 
	 * Compare two list of subjects related to an entity in order to calculate
	 * the distance between both
	 *  
	 * @return the distance
	 */
	private double subjectFolkDistance(List<String> subjects1, List<String> subjects2) {
		double dist = 100;
		Iterator<String> iter1 = subjects1.iterator();
		// iterate over all the subjects in the list 1
		while(iter1.hasNext()) {
			String s1 = iter1.next();
			Iterator<String> iter2 = subjects2.iterator();
			// iterate over all the subjects in the list 2 
			// to compare with the element from list 1
			while(iter2.hasNext()) {
				String s2 = iter2.next();
				double editD = LevenshteinDistance.computeLevenshteinDistance(s1, s2);
				// if the distance between 2 subjects is the lowest 
				// until that moment we save it
				if(editD < dist) dist = editD;
			}
		}
		return dist;
	}
	
	/**
	 * Distance metric over the folksonomy of languages related to ViSH
	 * defined as follows:
	 * Dlanguage (L1, L2) = min {editD(l1i , l2j)}
	 * 
	 * Compare two list of languages related to an entity in order to calculate
	 * the distance between both
	 *  
	 * @return the distance
	 */
	private double languageFolkDistance(List<String> languages1, List<String> languages2) {
		double dist = 100;
		Iterator<String> iter1 = languages1.iterator();
		// iterate over all the subjects in the list 1
		while(iter1.hasNext()) {
			String l1 = iter1.next();
			Iterator<String> iter2 = languages2.iterator();
			// iterate over all the subjects in the list 2 
			// to compare with the element from list 1
			while(iter2.hasNext()) {
				String l2 = iter2.next();
				double editD = LevenshteinDistance.computeLevenshteinDistance(l1, l2);
				// if the distance between 2 subjects is the lowest 
				// until that moment we save it
				if(editD < dist) dist = editD;
			}
		}
		return dist;
	}

}
