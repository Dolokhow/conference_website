/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Conference;
import beans.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.DynamicMenuModel;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author djordjebozic
 */
@Named("menuBuilder")
@SessionScoped
public class MenuBuilder implements Serializable {

    private MenuModel model;
    @Inject
    ConferenceController cc;
    @Inject
    ModeratorController mc;
    @Inject
    UserController uc;

    public MenuBuilder() {
    }

    @PostConstruct
    public void init() {
        model = new DynamicMenuModel();

        DefaultMenuItem home = new DefaultMenuItem("Home");
        home.setOutcome("/homepage.xhtml?faces-redirect=true");
        home.setIcon("ui-icon-home");
        home.setStyle("align-content: center");
        model.addElement(home);

        DefaultMenuItem agenda = new DefaultMenuItem("MyAgenda");
        agenda.setOutcome("/myAgenda.xhtml?faces-redirect=true");
        agenda.setIcon("ui-icon-note");
        agenda.setStyle("align-content: center");
        model.addElement(agenda);

        DefaultSubMenu gallery = new DefaultSubMenu("Gallery");
        Collection<Conference> conferences = cc.getItems();

        for (Conference c : conferences) {
            Collection<Session> sessions = c.getSessionCollection();
            DefaultSubMenu conf_item = new DefaultSubMenu(c.getTitle());

            Date start_date = c.getStartDate();
            Date end_date = c.getEndDate();
            Long d = (end_date.getTime() - start_date.getTime()) / (24 * 60 * 60 * 1000);
            int day_diff = d.intValue() + 1; // inkluzivno za krajnje datume

            List<DefaultSubMenu> subs = new ArrayList<>();
            for (int i = 0; i < day_diff; i++) {
                DefaultSubMenu firstSubmenu = new DefaultSubMenu("Day " + (i + 1));
                conf_item.addElement(firstSubmenu);
                subs.add(firstSubmenu);
            }

            for (Session s : sessions) {
                if (!s.getSessionType().equals("Break")) {
                    DefaultMenuItem item = new DefaultMenuItem(s.getName());
                    item.setCommand("#{galleryController.setCriteria(" + s.getSessionPK().getId() + ")}");
                    item.setAjax(false);

                    Date current_date = s.getDate();
                    Long dd = (current_date.getTime() - start_date.getTime()) / (24 * 60 * 60 * 1000);
                    int day = dd.intValue();

                    DefaultSubMenu cur = subs.get(day);
                    cur.addElement(item);
                }
            }

            gallery.addElement(conf_item);

        }
        
        if (mc.isModerator(uc.getSelected())) {
            DefaultMenuItem moderator_nav = new DefaultMenuItem("Moderate");
            moderator_nav.setOutcome("/moderatorsConferences.xhtml?faces-redirect=true");
            moderator_nav.setIcon("ui-icon-wrench");
            moderator_nav.setStyle("align-content: center");
            model.addElement(moderator_nav);

        }

        model.addElement(gallery);

        DefaultMenuItem logout = new DefaultMenuItem("Logout");
        logout.setCommand("#{userController.logout}");
        logout.setIcon("ui-icon-key");
        logout.setStyle("align-content: center");
        model.addElement(logout);

        

    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
