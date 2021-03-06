package com.crsms.dto;


import org.joda.time.DateTime;

import com.googlecode.jmapper.annotations.JMap;

public class CourseJsonDto {

	@JMap
	private Long id;

	@JMap
	private String name;

	@JMap
	private String description;

	@JMap
	private DateTime startDate;

	@JMap
	private Integer duration;

	@JMap
	private Boolean open = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

}
