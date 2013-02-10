package com.github.kolorobot.icm.incident;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.github.kolorobot.icm.account.Account;
import com.github.kolorobot.icm.account.AccountRepository;

@Service
public class IncidentService {

	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private IncidentRepository incidentRepository;
	
	public Incident createIncident(UserDetails user, Incident incident) {
		Account reporter = accountRepository.findByEmail(user.getUsername());
		incident.setCreator(reporter);
		incident.setCreated(new Date());
		incidentRepository.save(incident);
		return incident;
	}
}