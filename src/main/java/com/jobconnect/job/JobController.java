package com.jobconnect.job;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobconnect.job.dto.JobRequest;
import com.jobconnect.job.dto.JobResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Jobs")
public class JobController {

	private final JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping
	public ResponseEntity<Page<JobResponse>> getJobs(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
		return ResponseEntity.ok(jobService.getJobs(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobResponse> getJob(@PathVariable UUID id) {
		return ResponseEntity.ok(jobService.getJob(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
	public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request, Authentication authentication) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(jobService.createJob(request, authentication.getName()));
	}
}
