package com.lasyankali.erp.config;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Configuration
public class RazorpayConfig {
	@Value("${razorpay.key-id}")
	private String keyId;
	@Value("${razorpay.key-secret}")
	private String keySecret;
	
	@Bean
	public RazorpayClient razorpayClient() throws RazorpayException {
	    return new RazorpayClient(keyId, keySecret);
	}

	public String getKeyId() {
		return keyId;
	}
	
	public boolean verifyPaymentSignature(
	        String providerOrderId,
	        String providerPaymentId,
	        String providerSignature)
	        throws RazorpayException {

	    JSONObject attributes = new JSONObject();

	    attributes.put(
	            "razorpay_order_id",
	            providerOrderId
	    );

	    attributes.put(
	            "razorpay_payment_id",
	            providerPaymentId
	    );

	    attributes.put(
	            "razorpay_signature",
	            providerSignature
	    );

	    return Utils.verifyPaymentSignature(
	            attributes,
	            keySecret
	    );
	}
	
}
