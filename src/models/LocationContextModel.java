/**
 * Define the location context model
 * and calculates its recommendation score
 */
package models;

import utils.MathUtils;

/**
 * @author Daniel Gallego Vico
 *
 */
public class LocationContextModel {
	
	// Features appropriateness 
	private double geoLocationFeatureAppr;
	private double timeFeatureAppr;
	
	// Weight
	// TODO: set real values
	private final double GEO_LOCATION_WEIGHT = 2;
	private final double TIME_WEIGHT = 4;
	
	// Geo-location possible values and their appropriateness factor
	private final int GEO_LOCATION_OUT = 0;
	private final int GEO_LOCATION_IN = 1;
	// Geo-location appropriateness factor for every feature value
	// TODO: set real values
	private final double GEO_LOCATION_OUT_APPR = 3;
	private final double GEO_LOCATION_IN_APPR = 3;
	
	// Time possible values
	private final int TIME_MORNING = 0;
	private final int TIME_AFTERNOON = 1;
	private final int TIME_EVENING = 2;
	private final int TIME_NIGHT = 3;
	// Time appropriateness factor for every feature value
	// TODO: set real values
	private final double TIME_MORNING_APPR = 4;
	private final double TIME_AFTERNOON_APPR = 5;
	private final double TIME_EVENING_APPR = 3;
	private final double TIME_NIGHT_APPR = 1;
	
	// Maximum and minimum model recommendation score values
	private final int MAX_REC_SCORE = 5;
	private final int MIN_REC_SCORE = 1;
	
	/**
	 * Constructor: 
	 * assigns appropriateness to every feature value
	 * 
	 * @param geoLocationValue
	 * @param timeValue
	 */
	public LocationContextModel(int geoLocationValue, int timeValue) {
		// identify appropriateness of each feature value received
		this.geoLocationFeatureAppr = appropriatenessOfGeoLocationFeature(geoLocationValue);
		this.timeFeatureAppr = appropriatenessOfTimeFeature(timeValue);
	}

	/**
	 * 
	 * @return the recommendation score for location context
	 */
	public double calculateRecommendationScore() {
		double RSlocation = (geoLocationFeatureAppr * GEO_LOCATION_WEIGHT 
								+ timeFeatureAppr * TIME_WEIGHT
							) /(GEO_LOCATION_WEIGHT + TIME_WEIGHT);
		// normalize
		return MathUtils.normalizeToZeroOne(RSlocation, MAX_REC_SCORE, MIN_REC_SCORE);
	}
	
	/**
	 * Returns the appropriateness for the geo-location feature
	 * considering the possible values to the question:
	 * "Is the user in his city/country?"
	 * 
	 * @param value
	 * @return
	 */
	private double appropriatenessOfGeoLocationFeature(int value) {
		switch (value) {
		// no
		case GEO_LOCATION_OUT:
			return GEO_LOCATION_OUT_APPR;
		// yes
		case GEO_LOCATION_IN:
			return GEO_LOCATION_IN_APPR;
		default:
			// neutral value
			return 3;
		}
	}
	
	/**
	 * Returns the appropriateness for the time feature
	 * considering the possible values to the question:
	 * "Which is the best moment to recommend the user?"
	 * 
	 * @param value
	 * @return
	 */
	private double appropriatenessOfTimeFeature(int value) {
		switch (value) {
		case TIME_MORNING:
			return TIME_MORNING_APPR;
		case TIME_AFTERNOON:
			return TIME_AFTERNOON_APPR;
		case TIME_EVENING:
			return TIME_EVENING_APPR;
		case TIME_NIGHT:
			return TIME_NIGHT_APPR;
		default:
			// neutral value
			return 3;
		}
	}
	
}
