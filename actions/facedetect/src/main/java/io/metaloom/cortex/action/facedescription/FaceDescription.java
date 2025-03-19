package io.metaloom.cortex.action.facedescription;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FaceDescription {

	private boolean nsfw;

	@JsonProperty("eyes_status")
	private String eyes;

	@JsonProperty("hair_color")
	private String hair;

	@JsonProperty("mouth_status")
	private String mouth;

	@JsonProperty("face_age")
	private int age;

	@JsonProperty("face_gender")
	private String gender;

	@JsonProperty("face_race")
	private String race;

	@JsonProperty("face_occluded_by")
	private String occludedBy;

	@JsonProperty("face_occluded")
	private boolean occluded;

	@JsonProperty("face_frontal_view")
	private boolean frontal;

	@JsonProperty("face_profile_view")
	private boolean profile;

	public boolean isNsfw() {
		return nsfw;
	}

	public void setNsfw(boolean nsfw) {
		this.nsfw = nsfw;
	}

	public String getEyes() {
		return eyes;
	}

	public void setEyes(String eyes) {
		this.eyes = eyes;
	}

	public String getHair() {
		return hair;
	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public String getMouth() {
		return mouth;
	}

	public void setMouth(String mouth) {
		this.mouth = mouth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOccludedBy() {
		return occludedBy;
	}

	public void setOccludedBy(String occludedBy) {
		this.occludedBy = occludedBy;
	}

	public boolean isFrontal() {
		return frontal;
	}

	public void setFrontal(boolean frontal) {
		this.frontal = frontal;
	}

	public boolean isOccluded() {
		return occluded;
	}

	public void setOccluded(boolean occluded) {
		this.occluded = occluded;
	}

	public boolean isProfile() {
		return profile;
	}

	public void setProfile(boolean profile) {
		this.profile = profile;
	}

}
