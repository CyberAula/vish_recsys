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
	
	// Weights for every user profile feature 
	// They must add up to 1
	private final double SUBJECTS_WEIGHT = 0.8;
	private final double LANGUAGES_WEIGHT = 0.2;
	private final double TARGET_LEVEL_WEIGHT = 0;
	private final double ROLE_WEIGHT = 0;
	private final double ORGANIZATION_WEIGHT = 0;
	private final double COUNTRY_WEIGHT = 0;
	private final double CITY_WEIGHT = 0;
	
	
	/**
	 * Ad hoc measure to calculate the distance between two user profiles in ViSH.
	 * A user profile is defined as follows: 
	 * U:= <subjects, languages, targetLevel, followers, role, organization, country, city> 
	 * 
	 * The userID and followers are ignored when calculating the distance
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
		dist = (SUBJECTS_WEIGHT * subjectDist + LANGUAGES_WEIGHT * languageDist);
		
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
		// if both subjects list are empty, the distance among them is 0 
		if(subjects1.size()==0 && subjects2.size()==0) {
			dist = 0;
		}
		else {
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
		// Distance 2 is the maximum distance possible between two languages
		// as the languages are defined by strings with lenght 2 (e.g. en, es...)
		double dist = 2;
		
		// when both users have languages set
		if(languages1.size()>0 && languages2.size()>0) {
			dist = 100;
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
		
		// when at least one of the users don't have languages set, 
		// the distance returned is 2 
		return dist;
	}

}
