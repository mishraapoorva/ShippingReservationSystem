package com.wipro.srs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.wipro.srs.bean.CredentialsBean;
import com.wipro.srs.bean.ProfileBean;
import com.wipro.srs.util.Authentication;
import com.wipro.srs.util.User;

@Controller
@SessionAttributes({"profile","userType"})
public class RegistrationController {
	
	@Autowired
	private Authentication authentication;
	@Autowired
	private User user;
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping(value = "/saveCustomer",method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute("profileBean") ProfileBean profileBean){
		String message = this.user.register(profileBean);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(profileBean.getEmailID());
		email.setSubject("OTP for your registration.");
		email.setText("RegistrationID is "+ profileBean.getUserID()+"The default password for your login "+ message);
		try{
			mailSender.send(email);
			return new ModelAndView("changePassword");
		}catch(MailException e){
			return new ModelAndView("Error");
		}
		
	}
	
	@RequestMapping(value = "/setRegistration",method = RequestMethod.GET)
	public ModelAndView home(){
			return new ModelAndView("SetRegistration","profileBean",new ProfileBean());	
	}
	
	
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView login(){
			return new ModelAndView("login","credentialsBean",new CredentialsBean());
	}
	
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public ModelAndView logout(@ModelAttribute("profile") ProfileBean bean, WebRequest request, SessionStatus status){
			this.user.logout(bean.getUserID());
			status.setComplete();
		    request.removeAttribute("profile", WebRequest.SCOPE_SESSION);
			return new ModelAndView("logout");	
	}
	
	@RequestMapping(value = "/ChangePassword",method = RequestMethod.GET)
	public ModelAndView changePassword(){
			//this.user.logout(bean.getUserID());
			return new ModelAndView("changepassword");	
	}
	
	@RequestMapping(value = "/adminLogout",method = RequestMethod.GET)
	public ModelAndView logout(@ModelAttribute("profile") String bean, WebRequest request, SessionStatus status){
			this.user.logout(bean);
			status.setComplete();
		    request.removeAttribute("profile", WebRequest.SCOPE_SESSION);
			return new ModelAndView("logout");	
	}
	
	@RequestMapping(value = "/doLogin",method = RequestMethod.POST)
	public ModelAndView login(Model model,@ModelAttribute("credentialsBean") CredentialsBean credentialsBean){
		String result = this.user.login(credentialsBean);
		if(result.equalsIgnoreCase("c")){
			if(this.authentication.changeLoginStatus(credentialsBean, 1)){
				ProfileBean profileBean = this.user.getProfile(credentialsBean.getUserID());
				model.addAttribute("profile", profileBean);
				model.addAttribute("userType", "C");
				return new ModelAndView("HomeCustomer");
			}else{
				
				return new ModelAndView("Error");
			}
			
		}else if(result.equalsIgnoreCase("A")){
			if(this.authentication.changeLoginStatus(credentialsBean, 1)){
				model.addAttribute("profile", credentialsBean.getUserID());
				model.addAttribute("userType", "A");
				return new ModelAndView("HomeAdmin");
			}else{
				return new ModelAndView("Error");
			}
		}else{
			return new ModelAndView("Error");
		}		
	}
}
