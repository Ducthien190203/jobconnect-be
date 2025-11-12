package com.jobconnect.job.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.jobconnect.job.EmploymentType;

public record JobResponse(
	UUID id,
	String title,
	String description,
	String location,
	BigDecimal salaryMin,
	BigDecimal salaryMax,
	EmploymentType employmentType,
	String postedBy,
	Instant createdAt
) {
}
