package fr.kyo.crkf_web;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Famille;
import fr.kyo.crkf_web.entity.Instrument;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import javax.print.attribute.standard.Severity;
import java.io.Serializable;
import java.util.List;

@Named("instrumentBean")
@SessionScoped
public class InstrumentBean implements Serializable {
    private static List<Instrument> instrumentList;
    private static List<Instrument> selectedInstruments;
    private Instrument selectedInstrument;

    private List<Famille> familleList;
    private List<Famille> selectedFamilles;

    @PostConstruct
    private void init(){
        getInstruments();
        familleList = DAOFactory.getFamilleDAO().getAll(0);
    }

    private void getInstruments(){
        instrumentList = DAOFactory.getInstrumentDAO().getAll(0);
    }

    public void saveInstrument() {
        if (this.selectedInstrument.getInstrumentId() == 0) {
            for(Famille famille : selectedFamilles){
                selectedInstrument.addFamille(famille);
            }
            if(DAOFactory.getInstrumentDAO().insert(selectedInstrument) != 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instrument Ajouter"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instrument non Ajouter", "Il y a eu une erreur lors de l'ajout de l'instrument, les changements ont étaient annullées"));
            }
        }
        else {
            selectedInstrument.getFamilles().clear();
            selectedInstrument.setFamilles(selectedFamilles);
            if(DAOFactory.getInstrumentDAO().update(selectedInstrument)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instrument Modifier"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instrument non Modifier", "Il y a eu une erreur lors de la modofication de l'instrument, les changements ont étaient annullées"));
            }

        }
        getInstruments();
        PrimeFaces.current().executeScript("PF('manageInstrumentDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-instruments");

        selectedInstrument = null;
    }

    public void deleteInstrument(){
        if(DAOFactory.getInstrumentDAO().delete(selectedInstrument)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instrument Supprimé"));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instrument non Supprimé","Il y a eu une erreur lors de la suppression, les changement ont étaient annullées"));
        }
        getInstruments();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-instruments");
        selectedInstrument = null;
    }
    public void deleteInstruments(){
        if(DAOFactory.getInstrumentDAO().deleteAll(selectedInstruments)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instruments Supprimés",null));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instruments non Supprimés","Il y a eu une erreur lors de la suppression, les changement ont était annullées"));
        }
        getInstruments();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-instruments");

        selectedInstruments.clear();
    }

    public void newInstrument(){
        selectedInstrument = new Instrument();
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

    public List<Famille> getFamilleList() {
        return familleList;
    }

    public void setFamilleList(List<Famille> familleList) {
        this.familleList = familleList;
    }

    public List<Famille> getSelectedFamilles() {
        return selectedFamilles;
    }

    public void setSelectedFamilles(List<Famille> selectedFamilles) {
        this.selectedFamilles = selectedFamilles;
    }
}
