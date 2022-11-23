package fr.kyo.crkf_web.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator (value = "passwordValidator")
public class PasswordValidator implements Validator<String> {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String password) throws ValidatorException {
        if(password.length() < 5 || password.length() > 32){
            FacesMessage msg = new FacesMessage("Erreur de Validation", "Le mot de passe n'est pas valide, la longueur doit être comprise entre 5 et 32 caractères !");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
