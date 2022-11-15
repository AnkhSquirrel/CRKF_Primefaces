package fr.kyo.crkf_web.converter;

import fr.kyo.crkf_web.InstrumentBean;
import fr.kyo.crkf_web.entity.Famille;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value="familleConverter", managed=true)
public class FamilleConverter implements Converter<Famille> {
    @Inject
    private InstrumentBean instrumentBean;

    @Override
    public Famille getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s != null && s.trim().length() > 0){
            for(Famille famille : instrumentBean.getFamilleList()){
                if(famille.getFamilleId() == Integer.parseInt(s)){
                    return famille;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Famille famille) {
        return String.valueOf(famille.getFamilleId());
    }

}
