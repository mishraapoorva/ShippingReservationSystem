package com.wipro.srs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.srs.bean.CredentialsBean;
import com.wipro.srs.bean.ProfileBean;
import com.wipro.srs.dao.Profile;

@Service
public class userImplementation implements User {

	@Autowired
	private Profile profileDAO;
	
	@Override
	@Transactional
	public String login(CredentialsBean credentialsBean) {
		return this.profileDAO.login(credentialsBean);
	}

	@Override
	@Transactional
	public boolean logout(String userId) {
		return this.profileDAO.logout(userId);
	}

	@Override
	@Transactional
	public String changePassword(CredentialsBean credentialsBean, String newPassword) {
		return this.profileDAO.changePassword(credentialsBean, newPassword);
	}

	@Override
	@Transactional
	public String register(ProfileBean profileBean) {
		return this.profileDAO.registerCustomer(profileBean);
	}

	@Override
	@Transactional
	public ProfileBean getProfile(String userID) {
		return this.profileDAO.getProfile(userID);
	}

}
