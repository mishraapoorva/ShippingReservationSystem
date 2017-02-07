package com.wipro.srs.dao;

import com.wipro.srs.bean.CredentialsBean;
import com.wipro.srs.bean.ProfileBean;

public interface Profile {
	public String registerCustomer(ProfileBean profileBean);

	public boolean authenticate(CredentialsBean credentialsBean);

	public String authorize(String userId);

	public boolean changeLoginStatus(CredentialsBean credentialsBean, int loginStatus);

	public String login(CredentialsBean credentialsBean);

	public boolean logout(String userId);

	public String changePassword(CredentialsBean credentialsBean, String newPassword);

	public ProfileBean getProfile(String userID);
}
