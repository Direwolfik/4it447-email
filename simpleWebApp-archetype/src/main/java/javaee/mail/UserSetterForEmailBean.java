package javaee.mail;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Spojovací článek mezi emailovou agendou a identifikací uživatele.
 */
public class UserSetterForEmailBean
        implements HttpSessionAttributeListener {

    /**
     * Odchytává událost vytvoření nového email beanu. V takovém
     * případě nastavuje beanu aktuálního uživatele.
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if ("emailBean".equals(event.getName())) {
            // Byl založen nový email bean.
            EmailBean emailBean = (EmailBean) event.getValue();
            adjustUser(event, emailBean);
        }
    }

    private void adjustUser(HttpSessionBindingEvent event, EmailBean emailBean) {
        String user = (String)event.getSession().getAttribute("user");
        emailBean.setUser(user);
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        if ("user".equals(event.getName())) {
            // Při změně uživatele odstraníme starý email bean. Jeho
            // nová instance se vytvoří až v okamžiku potřeby (např.
            // <jsp:useBean> v emailForm.jsp nebo doGet v MailServlet).
            session.removeAttribute("emailBean");
        } else
        if ("emailBean".equals(event.getName())) {
            // Byl založen nový email bean nahrazující starý. Novému
            // je třeba nastavit uživatele.
            EmailBean newEmailBean = (EmailBean)
                    session.getAttribute("emailBean");
            adjustUser(event, newEmailBean);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
    }

}
