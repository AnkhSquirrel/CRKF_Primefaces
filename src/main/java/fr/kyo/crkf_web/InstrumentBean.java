package fr.kyo.crkf_web;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Instrument;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("instrumentBean")
@SessionScoped
public class InstrumentBean implements Serializable {
    private static List<Instrument> instrumentList;
    private static List<Instrument> selectedInstruments;
    private Instrument selectedInstrument;

    @PostConstruct
    private void init(){
        instrumentList = DAOFactory.getInstrumentDAO().getAll(0);
    }

    public void saveInstrument() {
        if (this.selectedInstrument.getInstrumentId() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-instruments");
    }

    public List<Instrument> getInstrumentList() {
        return instrumentList;
    }

    public void setInstrumentList(List<Instrument> instrumentList) {
        this.instrumentList = instrumentList;
    }

    public Instrument getSelectedInstrument() {
        return selectedInstrument;
    }

    public void setSelectedInstrument(Instrument selectedInstrument) {
        this.selectedInstrument = selectedInstrument;
    }

    public List<Instrument> getSelectedInstruments() {
        return selectedInstruments;
    }

    public void setSelectedInstruments(List<Instrument> selectedInstruments) {
        InstrumentBean.selectedInstruments = selectedInstruments;
    }
}
