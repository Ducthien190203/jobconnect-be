package com.jobconnect.job.dto;

import java.math.BigDecimal;

import com.jobconnect.job.EmploymentType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobRequest(
	@NotBlank
	@Size(max = 200)
	String title,
	@NotBlank
	String description,
	@NotBlank
	@Size(max = 120)
	String location,
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	BigDecimal salaryMin,
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	BigDecimal salaryMax,
	@NotNull
	EmploymentType employmentType
) {
	@AssertTrue(message = "salaryMax must be greater than or equal to salaryMin")
	public boolean isSalaryRangeValid() {
		return salaryMax == null
			|| salaryMin == null
			|| salaryMax.compareTo(salaryMin) >= 0;
	}
}
