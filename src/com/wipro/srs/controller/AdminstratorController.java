package com.wipro.srs.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.wipro.srs.bean.RouteBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.bean.ShipBean;
import com.wipro.srs.service.Administrator;

@Controller
@SessionAttributes({"profile","userType"})
public class AdminstratorController {
	
	@Autowired
	private Administrator adminService;
	
	@ModelAttribute("shipBean")
    public ShipBean createShipModel() {
        return new ShipBean();
    }
	
	@RequestMapping(value = "/setShip",method = RequestMethod.GET)
	public ModelAndView setShip(@ModelAttribute("userType") String userType){
			if(userType.equalsIgnoreCase("A")){
				return new ModelAndView("setShip");
			}
			else{
				return new ModelAndView("Error");
			}
				
	}
	
	@RequestMapping(value = "/ship/save",method = RequestMethod.POST)
	public ModelAndView saveShip(@ModelAttribute("shipBean") ShipBean shipBean,@ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){	
			if(this.adminService.addShip(shipBean).equals("SUCCESS")){
				return new ModelAndView("Login");
			}
			else{
				return new ModelAndView("Error");
			}			
		}else{
			return new ModelAndView("Error");
		}
	}
	
	@ModelAttribute("routeBean")
    public RouteBean createRouteModel() {
        return new RouteBean();
    }
	
	@RequestMapping(value = "/setRoute",method = RequestMethod.GET)
	public ModelAndView setRoute(@ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){
			return new ModelAndView("setRoute");
		}else{
			return new ModelAndView("Error");
		}
			
	}
	
	@RequestMapping(value = "/saveRoute",method = RequestMethod.POST)
	public ModelAndView saveRoute(@ModelAttribute("routeBean") RouteBean routebean,@ModelAttribute("userType") String userType){		
		if(userType.equalsIgnoreCase("A")){
			if(this.adminService.addRoute(routebean).equals("SUCCESS")){
				return new ModelAndView("Login");
			}
			return new ModelAndView("Error");
		}else{
			return new ModelAndView("Error");
		}	
	}
	
	@ModelAttribute("scheduleBean")
    public ScheduleBean createScheduleModel() {
        return new ScheduleBean();
    }
	
	@RequestMapping(value = "/setSchedule",method = RequestMethod.GET)
	public ModelAndView setSchedule(Model model, @ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){	
			model.addAttribute("ships", this.adminService.viewByAllShips());
			model.addAttribute("routes", this.adminService.viewByAllRoute());
			return new ModelAndView("setSchedule");
		}else{
			return new ModelAndView("Error");
		}
	}
	
	@RequestMapping(value = "/saveSchedule",method = RequestMethod.POST)
	public String saveSchedule(@ModelAttribute("scheduleBean") ScheduleBean schedulebean,@ModelAttribute("userType") String userType){		
		if(userType.equalsIgnoreCase("A")){
			if(this.adminService.checkFor7DaysSchedule(schedulebean.getShipID(),schedulebean.getStartDate())){
				if(this.adminService.addSchedule(schedulebean).equals("SUCCESS")){
					return "Login";
				}else{
				return "Error";
				}
			}
			else{
				return "redirect:/setSchedule"; 
			}
		}else{
			return "Error";
		}
	}
	
	@RequestMapping(value = "/shipEdit",method = RequestMethod.GET)
	public ModelAndView shipEdit(Model model, @ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){
			model.addAttribute("ships", this.adminService.viewByAllShips());
			return new ModelAndView("shipEdit");
		}else{
			return new ModelAndView("Error");
		}
				
	}
	
	@RequestMapping(value = "/allPassengersSchedule",method = RequestMethod.POST)
	public ModelAndView allPassengersShip(Model model, @ModelAttribute("userType") String userType, HttpServletRequest request){
		if(userType.equalsIgnoreCase("A")){
			String scheduleid = request.getParameter("id");
			model.addAttribute("passengers", this.adminService.viewPasengersByShip(scheduleid));
			return new ModelAndView("allPassengersSchedule");
		}else{
			return new ModelAndView("Error");
		}		
	}
	
	@RequestMapping(value = "/allPassengers",method = RequestMethod.GET)
	public ModelAndView schedulePassengersSet(Model model, @ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){
			return new ModelAndView("scheduleID");
		}
		return new ModelAndView("Error");		
	}
	
	
	@RequestMapping(value = "/deleteShips/{deleteIDs}",method = RequestMethod.GET)
	public ModelAndView shipDelete(Model model, @PathVariable("deleteIDs") String deleteIDs){
			ArrayList<String> ids = new ArrayList<String>();
			String[] allIDs = deleteIDs.split("~!");
			for(int i = 0; i < allIDs.length; i++){
				ids.add(allIDs[i]);
			}
			System.out.println(ids.size());
			this.adminService.removeShip(ids);
			return new ModelAndView("login");	
	}
	
	@RequestMapping(value = "/scheduleDelete",method = RequestMethod.GET)
	public ModelAndView scheduleDelete(Model model,@ModelAttribute("userType") String userType){
		if(userType.equalsIgnoreCase("A")){
			model.addAttribute("allSchedules", this.adminService.viewByAllSchedule());
			return new ModelAndView("scheduleDelete");	
		}else{
			return new ModelAndView("login");
		}
	}
	
	@RequestMapping(value = "/deleteSchedules/{deleteIDs}",method = RequestMethod.GET)
	public ModelAndView deleteSchedules(Model model, @PathVariable("deleteIDs") String deleteIDs,@ModelAttribute("profile") String profile){
			ArrayList<String> ids = new ArrayList<String>();
			String[] allIDs = deleteIDs.split("~!");
			for(int i = 0; i < allIDs.length; i++){
				ids.add(allIDs[i]);
			}
			if(this.adminService.removeSchedule(ids) == 1){
				return new ModelAndView("login");
			}else{
				return new ModelAndView("error");
			}
				
	}
		
	@RequestMapping(value = "/editShip/{id}")
	public ModelAndView editship(Model model,@PathVariable("id") String id){
			model.addAttribute("shipBean", this.adminService.viewByShipId(id));
			return new ModelAndView("setShip");	
	}
	
	@RequestMapping(value = "/editSchedule/{id}")
	public ModelAndView scheduleEdit(Model model,@PathVariable("id") String id){
			model.addAttribute("scheduleBean", this.adminService.viewByScheduleId(id));
			model.addAttribute("ships", this.adminService.viewByAllShips());
			model.addAttribute("routes", this.adminService.viewByAllRoute());
			return new ModelAndView("modifySchedule");	
	}
	
	@RequestMapping(value = "/schedule/save",method = RequestMethod.POST)
	public ModelAndView scheduleEdit(@ModelAttribute("scheduleBean") ScheduleBean scheduleBean){
			this.adminService.addSchedule(scheduleBean);
			return new ModelAndView("login");	
	}
}
