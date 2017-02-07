package com.wipro.srs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.srs.bean.CredentialsBean;
import com.wipro.srs.dao.Profile;

@Service
public class AuthenticationImplementation implements Authentication {
	
	@Autowired
	private Profile profileDAO;
	
	@Override
	@Transactional
	public boolean authenticate(CredentialsBean credentialsBean) {
		return this.profileDAO.authenticate(credentialsBean);
	}
	
	
	@Override
	@Transactional
	public String authorize(String userId) {
		return this.profileDAO.authorize(userId);
	}

	@Override
	@Transactional
	public boolean changeLoginStatus(CredentialsBean credentialsBean, int loginStatus) {
		return this.profileDAO.changeLoginStatus(credentialsBean, loginStatus);
	}

}
