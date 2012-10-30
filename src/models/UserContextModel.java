/**
 * Defines the user context model
 * and calculates its recommendation score
 */
package models;

import utils.MathUtils;

/**
 * @author Daniel Gallego Vico
 *
 */
public class UserContextModel {
	
	// Features appropriateness 
	private double activityFeatureAppr;
	private double deviceFeatureAppr;
	
	// Weight
	// TODO: set real values
	private final double ACTIVITY_WEIGHT = 4;
	private final double DEVICE_WEIGHT = 2;
	
	// Activity possible values
	private final int ACTIVITY_AWAY = 0;
	private final int ACTIVITY_IDLE = 1;
	private final int ACTIVITY_BROWSING = 2;
	private final int ACTIVITY_AFTER_PROFILE = 3;
	private final int ACTIVITY_CREATING = 4;
	private final int ACTIVITY_EDITING = 5;
	private final int ACTIVITY_SELECTING_RESOURCE = 6;
	private final int ACTIVITY_SAVING = 7;
	private final int ACTIVITY_FINISH_CREATION = 8;
	private final int ACTIVITY_VIEWING = 9;
	private final int ACTIVITY_FINISH_VIEW = 10;
	// Activity appropriateness factor for every feature value
	// TODO: set real values
	private final double ACTIVITY_AWAY_APPR = 3;
	private final double ACTIVITY_IDLE_APPR = 5;
	private final double ACTIVITY_BROWSING_APPR = 2;
	
	// Device possible values and their appropriateness factor
	private final int DEVICE_DESKTOP = 0;
	private final int DEVICE_TABLET = 1;
	private final int DEVICE_MOBILE = 2;
	// Device appropriateness factor for every feature value
	// TODO: set real values
	private final double DEVICE_DESKTOP_APPR = 5;
	private final double DEVICE_TABLET_APPR = 3;
	private final double DEVICE_MOBILE_APPR = 2;
	
	// Maximum and minimum model recommendation score values
	private final int MAX_REC_SCORE = 5;
	private final int MIN_REC_SCORE = 1;
	
	/**
	 * Constructor:
	 * assigns appropriateness to every feature value
	 * 
	 * @param activityValue
	 * @param deviceValue
	 */
	public UserContextModel(int activityValue, int deviceValue) {
		// identify appropriateness of each feature value received
		this.activityFeatureAppr = appropriatenessOfActivityFeature(activityValue);
		this.deviceFeatureAppr = appropriatenessOfDeviceFeature(deviceValue);
	}
	
	/**
	 * 
	 * @return the recommendation score for user context
	 */
	public double calculateRecommendationScore() {
		double RSlocation = (activityFeatureAppr * ACTIVITY_WEIGHT
				+ deviceFeatureAppr * DEVICE_WEIGHT
			) / (ACTIVITY_WEIGHT + DEVICE_WEIGHT);
		return MathUtils.normalizeToZeroOne(RSlocation, MAX_REC_SCORE, MIN_REC_SCORE);
	}
	
	/**
	 * Returns the appropriateness for the activity feature
	 * considering the possible values to the question:
	 * "What activity is doing the user?"
	 * 
	 * @param value
	 * @return
	 */
	private double appropriatenessOfActivityFeature(int value) {
		switch (value) {
		case ACTIVITY_AWAY:
			return ACTIVITY_AWAY_APPR;
		case ACTIVITY_IDLE:
			return ACTIVITY_IDLE_APPR;
		case ACTIVITY_BROWSING:
			return ACTIVITY_BROWSING_APPR;
		default:
			// neutral value
			return 3;
		}
	}

	/**
	 * Returns the appropriateness for the device feature
	 * considering the possible values to the question:
	 * "What device is using the user?"
	 * 
	 * @param value
	 * @return
	 */
	private double appropriatenessOfDeviceFeature(int value) {
		switch (value) {
		case DEVICE_DESKTOP:
			return DEVICE_DESKTOP_APPR;
		case DEVICE_TABLET:
			return DEVICE_TABLET_APPR;
		case DEVICE_MOBILE:
			return DEVICE_MOBILE_APPR;
		default:
			// neutral value
			return 3;
		}
	}
}
