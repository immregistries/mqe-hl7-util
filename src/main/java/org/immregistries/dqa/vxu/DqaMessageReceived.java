package org.immregistries.dqa.vxu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DqaMessageReceived {

	/**
	 * This is metadata about this message. There may be more metadata to add to this. 
	 */
	private Date receivedDate = new Date();

	/*
	 * below here are all objects taken from the message itself.
	 */
	private DqaMessageHeader messageHeader = new DqaMessageHeader();
	private DqaPatient patient = new DqaPatient();
	private List<DqaNextOfKin> nextOfKins = new ArrayList<DqaNextOfKin>();
	private List<DqaVaccination> vaccinations = new ArrayList<DqaVaccination>();

    @Override public String toString() {
        return "DqaMessageReceived{" + "receivedDate=" + receivedDate + ", messageHeader=" + messageHeader + ", patient=" + patient + ", nextOfKins="
            + nextOfKins + ", vaccinations=" + vaccinations + '}';
    }

    public List<DqaNextOfKin> getNextOfKins() {
		return nextOfKins;
	}

	public DqaPatient getPatient() {
		return patient;
	}

	public List<DqaVaccination> getVaccinations() {
		return vaccinations;
	}

	public void setNextOfKins(List<DqaNextOfKin> nextOfKins) {
		this.nextOfKins = nextOfKins;
	}

	public void setPatient(DqaPatient patient) {
		this.patient = patient;
	}

	public void setVaccinations(List<DqaVaccination> vaccinations) {
		this.vaccinations = vaccinations;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public DqaMessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(DqaMessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

}
