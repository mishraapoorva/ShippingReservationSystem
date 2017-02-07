package com.wipro.srs.util;

public interface Payment {
	boolean findByCardNumber(String userid, String cardnumber);
	String process(Payment payment);
}
