package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
					  @RequestParam(value = "licenseNumber", required = true) String licenseNumber,
					  @RequestParam(value = "name", required = true) String name,
					  @RequestParam(value = "flyHour", required = true) String flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, Integer.parseInt(flyHour));
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("pilotList", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value= {"/pilot/view/license-number","/pilot/view/license-number/{licenseNumber}"})
	public String viewPilot(@PathVariable Optional<String> licenseNumber, Model model) {
		if(licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if(archive!=null) {
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
		}
		return "error-view";
	}
	
	@RequestMapping(value= {"/pilot/update/license-number/{licenseNumber}/fly-hour/",
							"/pilot/update/license-number//fly-hour/",
							"/pilot/update/license-number/{licenseNumber}/fly-hour/{newFlyHour}",
							"/pilot/update/license-number//fly-hour/{newFlyHour}"})
	public String updateFlyHour(@PathVariable Optional<String> licenseNumber, @PathVariable Optional<String> newFlyHour, Model model) {
		if(licenseNumber.isPresent() && newFlyHour.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if(archive!=null) {
				archive.setFlyHour(Integer.parseInt(newFlyHour.get()));
				return "update-fly-hour";
			}
		}
		return "error-update";
	}
	
	@RequestMapping(value= {"/pilot/delete/id","/pilot/delete/id/{id}"})
	public String deletePilot(@PathVariable Optional<String> id, Model model) {
		if(id.isPresent()) {
			if(pilotService.deletePilot(id.get())!=null) {
				return "delete-pilot";
			}
		}
		return "error-delete";
	}
}
