package com.jobconnect.job;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobconnect.job.dto.JobRequest;
import com.jobconnect.job.dto.JobResponse;

@Service
public class JobService {

	private final JobRepository jobRepository;

	public JobService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Transactional(readOnly = true)
	public Page<JobResponse> getJobs(Pageable pageable) {
		return jobRepository.findAll(pageable).map(this::toResponse);
	}

	@Transactional(readOnly = true)
	public JobResponse getJob(UUID id) {
		return jobRepository.findById(id).map(this::toResponse).orElseThrow(() -> new JobNotFoundException(id));
	}

	@Transactional
	public JobResponse createJob(JobRequest request, String postedBy) {
		Job job = new Job();
		job.setTitle(request.title());
		job.setDescription(request.description());
		job.setLocation(request.location());
		job.setSalaryMin(request.salaryMin());
		job.setSalaryMax(request.salaryMax());
		job.setEmploymentType(request.employmentType());
		job.setPostedBy(postedBy);
		return toResponse(jobRepository.save(job));
	}

	private JobResponse toResponse(Job job) {
		return new JobResponse(
			job.getId(),
			job.getTitle(),
			job.getDescription(),
			job.getLocation(),
			job.getSalaryMin(),
			job.getSalaryMax(),
			job.getEmploymentType(),
			job.getPostedBy(),
			job.getCreatedAt()
		);
	}
}
