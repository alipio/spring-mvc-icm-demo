package com.github.kolorobot.icm.incident;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.github.kolorobot.icm.account.Account;

@Entity(name = "incident")
public class Incident {
	
	public enum Status {
		NEW, CONFIRMED, NOT_CONFIRMED, IN_PROGRESS, SOLVED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "creator_id", nullable = false, updatable = false)
	private Account creator;
	
	@Column(name = "incident_type")
	@NotBlank
	private String incidentType;
	
	@Column(name = "incident_address")
	@NotBlank
	private String incidentAddress;
	
	@NotBlank
	private String description;
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	private Status status = Status.NEW;
	
	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private Account assignee;
	
	@OneToMany(mappedBy = "incident")
	private List<Audit> audits;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getIncidentAddress() {
		return incidentAddress;
	}

	public void setIncidentAddress(String incidentAddress) {
		this.incidentAddress = incidentAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Account getAssignee() {
		return assignee;
	}

	public void setAssignee(Account assignee) {
		this.assignee = assignee;
	}

	public List<Audit> getAudits() {
		return audits;
	}

	public void setAudits(List<Audit> audits) {
		this.audits = audits;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
}