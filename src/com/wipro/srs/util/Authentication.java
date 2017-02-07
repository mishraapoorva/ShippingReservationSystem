package com.wipro.srs.util;

import com.wipro.srs.bean.CredentialsBean;

public interface Authentication {
	boolean authenticate(CredentialsBean credentialsBean);
	String authorize(String userId);
	boolean changeLoginStatus(CredentialsBean credentialsBean, int loginStatus);
}
