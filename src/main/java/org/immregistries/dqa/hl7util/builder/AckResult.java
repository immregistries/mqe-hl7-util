package org.immregistries.dqa.hl7util.builder;

public enum AckResult {
	APP_ACCEPT ("AA")//AA – Application Accept
	,APP_ERROR("AE")//AE – Application Error
	,APP_REJECT("AR");//AR – Application Reject
	
	private String code;
	private AckResult(String codeIn) {
		this.code = codeIn;
	}
	public String getCode() {
		return code;
	}
	
}
