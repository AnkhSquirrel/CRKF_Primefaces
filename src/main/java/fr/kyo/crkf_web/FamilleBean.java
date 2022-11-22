package fr.kyo.crkf_web;

import fr.kyo.crkf_web.dao.DAOFactory;
import fr.kyo.crkf_web.entity.Classification;
import fr.kyo.crkf_web.entity.Famille;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("familleBean")
@SessionScoped
public class FamilleBean implements Serializable {
    private static List<Famille> familleList;
    private static List<Famille> selectedFamilles;
    private static List<Classification> classificationList;
    private Famille selectedFamille;

    @PostConstruct
    private void init(){
        refreshFamille();
        classificationList = DAOFactory.getClassificationDAO().getAll(0);
    }

    private void refreshFamille(){
        familleList = DAOFactory.getFamilleDAO().getAll(0);
    }

    public void newFamille(){
        selectedFamille = new Famille(0, "", 0);
    }

    public void saveFamille() {
        if (this.selectedFamille.getFamilleId() == 0) {
            if(DAOFactory.getFamilleDAO().insert(selectedFamille) != 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Famille Ajouter"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Famille non Ajouter", "Il y a eu une erreur lors de l'ajout de la famille, les changements ont étaient annullées"));
            }
        }
        else {
            if(DAOFactory.getFamilleDAO().update(selectedFamille)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Famille Modifier"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Famille non Modifier", "Il y a eu une erreur lors de la modofication de la famille, les changements ont étaient annullées"));
            }

        }
        refreshFamille();
        PrimeFaces.current().executeScript("PF('manageFamilleDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-familles");

        selectedFamille = null;
    }

    public void deleteFamille(){
        if(DAOFactory.getFamilleDAO().delete(selectedFamille)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Famille Supprimé"));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Famille non Supprimé","Il y a eu une erreur lors de la suppression, vérifiez que aucun instrument n'est lier a cette famille"));
        }
        refreshFamille();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-familles");
        selectedFamille = null;
    }
    public void deleteFamilles(){
        if(DAOFactory.getFamilleDAO().deleteAll(selectedFamilles)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Instruments Supprimés",null));
            selectedFamilles.clear();
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instruments non Supprimés","Il y a eu une erreur lors de la suppression, vérifiez que aucun instrument n'est lier aux famille"));
        }
        refreshFamille();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-familles");
    }

    public List<Famille> getFamilleList() {
        return familleList;
    }

    public void setFamilleList(List<Famille> familleList) {
        FamilleBean.familleList = familleList;
    }

    public List<Famille> getSelectedFamilles() {
        return selectedFamilles;
    }

    public void setSelectedFamilles(List<Famille> selectedFamilles) {
        FamilleBean.selectedFamilles = selectedFamilles;
    }

    public List<Classification> getClassificationList() {
        return classificationList;
    }

    public void setClassificationList(List<Classification> classificationList) {
        FamilleBean.classificationList = classificationList;
    }

    public Famille getSelectedFamille() {
        return selectedFamille;
    }

    public void setSelectedFamille(Famille selectedFamille) {
        this.selectedFamille = selectedFamille;
    }
}
