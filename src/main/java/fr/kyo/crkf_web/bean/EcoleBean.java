package fr.kyo.crkf_web.bean;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Adresse;
import fr.kyo.crkf_web.entity.Departement;
import fr.kyo.crkf_web.entity.Ecole;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("ecoleBean")
@ViewScoped
public class EcoleBean implements Serializable {
    private static List<Ecole> allEcole;
    private transient Ecole selectedEcole;
    private transient List<Ecole> selectedEcoles;
    private transient List<Adresse> adresseList;
    private transient List<Departement> departementList;
    private String query;

    @PostConstruct
    private void init(){
        allEcole = DAOFactory.getEcoleDAO().getAll(0);
        adresseList = DAOFactory.getAdresseDAO().getLike("");
        departementList = DAOFactory.getDepartementDAO().getAll(0);
    }

    private void refresh(){
        allEcole = DAOFactory.getEcoleDAO().getAll(0);
    }
    public void refreshAdresseList(){
        adresseList = DAOFactory.getAdresseDAO().getLike(query);
    }

    public void newEcole() {
        selectedEcole = new Ecole(0,"",0);
        selectedEcoles.clear();
    }

    public void saveEcole() {
        if (this.selectedEcole.getEcoleId() == 0) {
            if(DAOFactory.getEcoleDAO().insert(selectedEcole) != 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ecole ajoutée"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"L'école n'a pas été ajoutée", "Erreur lors de l'ajout de l'école, l'école vérifier les informations"));
            }
        }
        else {
            if( DAOFactory.getEcoleDAO().update(selectedEcole)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instrument Modifier"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"L'école n'a pas été modifiée", "Erreur lors de la modification de l'école, les modifications ne sont donc pas prises en compte"));
            }

        }
        refresh();
        PrimeFaces.current().executeScript("PF('manageEcoleDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-ecole");

        selectedEcole = null;
    }

    public boolean hasSelectedEcoles() {
        return selectedEcoles != null && !selectedEcoles.isEmpty();
    }
    public String getDeleteButtonMessage() {
        if (hasSelectedEcoles()) {
            int size = selectedEcoles.size();
            return size > 1 ? size + " écoles sélectionnées" : "1 école sélectionnée";
        }

        return "Delete";
    }
    public void deleteSelectedEcole(){
        if (!DAOFactory.getPersonneDAO().getByEcole(selectedEcole.getEcoleId()).isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'école ne peut pas être supprimée puisque des professeurs sont liées à cette école"));
        }else{
            DAOFactory.getEcoleDAO().delete(selectedEcole);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Suppresion réussie","L'école a été supprimée"));
            refresh();
        }
        selectedEcole = null;
        PrimeFaces.current().ajax().update("form:messages", "form:dt-ecole");
    }
    public void deleteSelectedEcoles() {
        if(!selectedEcoles.isEmpty() && selectedEcole != null && DAOFactory.getEcoleDAO().deleteSeveralsEcoles(selectedEcoles)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Suppresion réussie","Les écoles ont été supprimées"));
            refresh();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Les écoles n'ont pas pu être supprimées"));
        }
        selectedEcoles.clear();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-ecole");

    }
    public List<Ecole> getAllEcole() {
        return allEcole;
    }

    public void setAllEcole(List<Ecole> allEcole) {
        EcoleBean.allEcole = allEcole;
    }

    public Ecole getSelectedEcole() {
        return selectedEcole;
    }

    public void setSelectedEcole(Ecole selectedEcole) {
        this.selectedEcole = selectedEcole;
    }

    public List<Ecole> getSelectedEcoles() {
        return selectedEcoles;
    }

    public void setSelectedEcoles(List<Ecole> selectedEcoles) {
        this.selectedEcoles = selectedEcoles;
    }

    public List<Adresse> getAdresseList() {
        return adresseList;
    }

    public void setAdresseList(List<Adresse> adresseList) {
        this.adresseList = adresseList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Departement> getDepartementList() {
        return departementList;
    }

    public void setDepartementList(List<Departement> departementList) {
        this.departementList = departementList;
    }
}
