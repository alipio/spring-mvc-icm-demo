package com.github.kolorobot.icm.incident;

import com.github.kolorobot.icm.account.Account;
import com.github.kolorobot.icm.account.User;
import com.github.kolorobot.icm.files.File;
import com.github.kolorobot.icm.support.web.MessageHelper;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/incident")
class IncidentController {

	@Inject
	private IncidentService incidentService;
    
    @Inject
	private AuditFormFactory auditFormFactory;

    @InitBinder
    public void initStringEditor(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute("page")
    public String module() {
        return "home";
    }

    @RequestMapping("/create")
	public String create(Model model) {
		model.addAttribute(new IncidentForm());
        return "incident/create";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(User user, @Valid @ModelAttribute IncidentForm incidentForm, Errors errors, RedirectAttributes ra, Model model) {
		if (errors.hasErrors()) {			
			MessageHelper.addErrorAttribute(model, "incident.create.failed");
            return "incident/create";
		}
		Incident incident = incidentService.createIncident(user,
                incidentForm.getType(), incidentForm.getDescription(), incidentForm.getAddressLine(), incidentForm.getAddressLine());
		MessageHelper.addSuccessAttribute(ra, "incident.create.success", incident.getId());
		return "redirect:/incident/list";
	}	
	
	@RequestMapping(value = "/list")
	public String list(@RequestParam(required = false) Incident.Status status, User user, Model model) {
		model.addAttribute("incidents", incidentService.getUserIncidents(user, status));
        return "incident/list";
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam String q, Model model, RedirectAttributes ra) {
        if (q == null) {
            MessageHelper.addErrorAttribute(ra, "incident.search.emptyQuery");
            return "redirect:list";
        }
        List<Incident> incidents = incidentService.search(q);
        if (incidents.size() == 1) {
            return "redirect:/incident/" + incidents.get(0).getId();
        }
        model.addAttribute("incidents", incidents);
        return "incident/list";
    }

    @RequestMapping(value = "/{id}")
	public String details(User user, @PathVariable("id") Long id, Model model) {
		Incident incident = getIncident(user, id);
		if (incident == null) {			
			throw new IllegalStateException("Exception 0x156DDE");
		}
		model.addAttribute("incident", incident);
        model.addAttribute("audits", getAudits(incident));
        model.addAttribute("incidentCreator", getCreator(incident));
        model.addAttribute("incidentAssignee", getAssignee(incident));
        model.addAttribute("files", getFiles(incident));

		return "incident/details";
	}

    @RequestMapping(value = "/{incidentId}/description", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void update(@RequestParam("description") String description,
                       @PathVariable("incidentId") long incidentId,
                       User user) {
        incidentService.setIncidentDescription(incidentId, description);
    }

	@RequestMapping("/{incidentId}/audit/create")	
	public String createAudit(User user, @PathVariable Long incidentId, Model model) {
		Incident incident = getIncident(user, incidentId);
		if (incident == null) {
			throw new IllegalStateException("Why, Leo, why?");
		}
		model.addAttribute(auditFormFactory.createAuditForm(user, incident));
		return "incident/createAudit";
	}

	@RequestMapping(value = "/{incidentId}/audit/create", method = RequestMethod.POST)
	public String createAudit(User user, @PathVariable Long incidentId, @Valid @ModelAttribute AuditForm auditForm, Errors errors, RedirectAttributes ra, Model model) {
		if (errors.hasErrors()) {
			MessageHelper.addErrorAttribute(model, "incident.audit.create.failed");
            return "incident/createAudit";
		}
		Incident incident = getIncident(user, incidentId);
		Audit audit = incidentService.addUserIncidentAudit(user, incident,
                auditForm.getAssigneeId(), auditForm.getNewStatus(), auditForm.getDescription());
		// FIXME Error shown, but info should be shown
        MessageHelper.addErrorAttribute(ra, "incident.audit.create.success", audit.getId());
		return "redirect:/incident/" + incidentId;
	}

	//
	// private helpers
	//
	
	private Incident getIncident(User user, Long incidentId) {
		return incidentService.getUserIncident(user, incidentId);
	}

    private List<Audit> getAudits(Incident incident) {
        return incidentService.getIncidentAudits(incident.getId());
    }

    private Account getCreator(Incident incident) {
        return incidentService.getAccount(incident.getCreatorId());
    }

    private Account getAssignee(Incident incident) {
        return incidentService.getAccount(incident.getAssigneeId());
    }

    private List<File> getFiles(Incident incident) {
        return incidentService.getIncidentFiles(incident.getId());
    }
}
