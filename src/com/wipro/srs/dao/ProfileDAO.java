package com.wipro.srs.dao;

import java.util.UUID;
import java.math.BigDecimal;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wipro.srs.bean.CredentialsBean;
import com.wipro.srs.bean.ProfileBean;

@Repository
public class ProfileDAO implements Profile {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public String registerCustomer(ProfileBean profileBean) {
		int uniqueId = getNextKey().intValueExact();
		profileBean.setUserID(profileBean.getFirstName().substring(0, 2) + uniqueId);
		
		CredentialsBean credentialsBean = new CredentialsBean();
		credentialsBean.setUserID(profileBean.getFirstName().substring(0, 2) + uniqueId);
		credentialsBean.setUserType("C");
		String uuid = UUID.randomUUID().toString();
		credentialsBean.setPassword(uuid);
		credentialsBean.setProfileBean(profileBean);
		try{
			Session session = this.sessionFactory.getCurrentSession();
			session.save(credentialsBean);
			return credentialsBean.getPassword();
		}catch(Exception e){
			return "FAIL";
		}
	}
	
	
	public BigDecimal getNextKey()
	{
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select SRS_SEQ_USER_ID.nextval from dual");
		return  (BigDecimal) query.uniqueResult();
	}


	@Override
	public boolean authenticate(CredentialsBean credentialsBean) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, credentialsBean.getUserID());
		if(bean.getPassword().equals(credentialsBean.getPassword())){
			return true;
		}
		else{
			return false;
		}
		
	}


	@Override
	public String authorize(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, userId);
		if(bean != null){
			return bean.getUserType();
		}
		else{
			return null;
		}
		
	}


	@Override
	public boolean changeLoginStatus(CredentialsBean credentialsBean, int loginStatus) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, credentialsBean.getUserID());
		if(bean != null){
			if(bean.getPassword().equals(credentialsBean.getPassword())){
				bean.setLoginStatus(loginStatus);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	@Override
	public String login(CredentialsBean credentialsBean) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, credentialsBean.getUserID());
		if(bean != null){
			if(bean.getPassword().equals(credentialsBean.getPassword())){
				return bean.getUserType();
			}
			else{
				return "FAIL";
			}
		}
		else{
			return "INVALID";
		}
	}


	@Override
	public boolean logout(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, userId);
		if(bean != null){
			bean.setLoginStatus(0);
			return true;
		}
		else{
			return false;
		}
	}


	@Override
	public String changePassword(CredentialsBean credentialsBean, String newPassword) {
		Session session = this.sessionFactory.getCurrentSession();
		CredentialsBean bean = (CredentialsBean) session.get(CredentialsBean.class, credentialsBean.getUserID());
		if(bean != null){
			if(bean.getPassword().equals(credentialsBean.getPassword())){
				bean.setPassword(newPassword);
				return "SUCCESS";
			}
			else{
				return "FAIL";
			}
		}
		else{
			return "INVALID";
		}
	}


	@Override
	public ProfileBean getProfile(String userID) {
		Session session = this.sessionFactory.getCurrentSession();
		ProfileBean profile = (ProfileBean) session.get(ProfileBean.class, userID);
		return profile;
	}
}
