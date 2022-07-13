package dk.kvalitetsit.ihexdsapi.dgws;

import org.w3c.dom.Document;

public class DgwsClientInfo {

	private Document sosi;
	
	public DgwsClientInfo(Document sosi) {
		this.sosi = sosi;
	}

	public Document getSosi() {
		return sosi;
	}

	public void setSosi(Document sosi) {
		this.sosi = sosi;
	}
}
