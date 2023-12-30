package com.airticket.itc.Utils;

import java.util.HashMap;
import java.util.Map;

import com.airticket.itc.exception.InsuffientAmountException;

public class PaymentUtil {

	private static Map<String,Double> paymentMap=new HashMap<>();
	
	static
	{
		paymentMap.put("acc1", 2000.0);
		paymentMap.put("acc2", 3400.0);
		paymentMap.put("acc3", 500.0);
		paymentMap.put("acc4", 4000.0);
		paymentMap.put("acc5", 5000.0);
		
	}
	
	public static boolean validateCreditLimit(String accNo,double paidAmount) {
		if(paidAmount > paymentMap.get(accNo)) {
			throw new InsuffientAmountException("InsuffientAmount");
		}else {
			return true;
		}
	}
}
