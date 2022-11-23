package fr.kyo.crkf_web;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Adresse;
import fr.kyo.crkf_web.entity.Ecole;
import fr.kyo.crkf_web.entity.Personne;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("personneBean")
@SessionScoped
public class PersonneBean implements Serializable {
    private transient List<Personne> personneList;
    private transient Personne selectedPersonne;
    private transient List<Personne> selectedPersonnes;
    private transient List<Ecole> ecoleList;
    private transient List<Adresse> adresseList;
    private String query;

    @PostConstruct
    private void init() {
        refresh();
        ecoleList = DAOFactory.getEcoleDAO().getAll(0);
        adresseList = DAOFactory.getAdresseDAO().getLike("");
    }

    private void refresh(){
        personneList = DAOFactory.getPersonneDAO().getAll(0);
    }

    public void refreshAdresseList(){
        adresseList = DAOFactory.getAdresseDAO().getLike(query);
    }

    public void openNew() {
        this.selectedPersonne = new Personne();
    }

    public void deleteOne(){
        if (DAOFactory.getPersonneDAO().delete(selectedPersonne)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Professeur supprimé",""));
            refresh();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur lors de la suppression","Changements annulés"));
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personnes");
    }

    public void deleteList(){
        if (selectedPersonnes != null && !selectedPersonnes.isEmpty() && DAOFactory.getPersonneDAO().delete(selectedPersonnes)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Professeurs supprimés",""));
            refresh();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur lors de la suppression","Changements annulés"));
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personnes");
    }

    public void save(){
        if (selectedPersonne.getPersonneId() == 0){
            create();
        } else {
            update();
        }
        PrimeFaces.current().executeScript("PF('managePersonneDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personnes");
    }

    public void create(){
        if (DAOFactory.getPersonneDAO().insert(selectedPersonne) != 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Professeur ajouté",null));
            refresh();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur lors de la création","Changements annulés"));
        }
    }

    public void update(){
        if (DAOFactory.getPersonneDAO().update(selectedPersonne)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Professeur modifié",null));
            refresh();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur lors de la modifications","Changements annulés"));
        }
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedProfesseurs()) {
            int size = selectedPersonnes.size();
            return size > 1 ? size + " professeurs sélectionnés" : "1 professeur sélectionné";
        }
        return "Delete";
    }

    public boolean hasSelectedProfesseurs() {
        return selectedPersonnes != null && !selectedPersonnes.isEmpty();
    }

    public List<Personne> getPersonneList() {
        return personneList;
    }

    public Personne getSelectedPersonne() {
        return selectedPersonne;
    }

    public void setSelectedPersonne(Personne selectedPersonne) {
        this.selectedPersonne = selectedPersonne;
    }

    public List<Personne> getSelectedPersonnes() {
        return selectedPersonnes;
    }

    public void setPersonneList(List<Personne> personneList) {
        this.personneList = personneList;
    }

    public void setSelectedPersonnes(List<Personne> selectedPersonnes) {
        this.selectedPersonnes = selectedPersonnes;
    }

    public List<Ecole> getEcoleList() {
        return ecoleList;
    }

    public void setEcoleList(List<Ecole> ecoleList) {
        this.ecoleList = ecoleList;
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

}
