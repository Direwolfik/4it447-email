package javaee.mail;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FrontControllerFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	 @Override
	    /**
	     * Směruje zpracování akcí do příslušných privátních metod.
	     */
	    public void doFilter(ServletRequest request,
	                         ServletResponse response,
	                         FilterChain filterChain)
	            throws IOException, ServletException {
	        // Přetypujeme na HTTP objekty
	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpServletResponse httpResponse = (HttpServletResponse) response;
	        // sdělíme kontejneru, v jakém kódování
	        // očekáváme data od klienta
	        request.setCharacterEncoding("UTF-8");

	        // Větvení podle akce.
	        String action = httpRequest.getParameter("action");
	        if (action != null && action.equals("doLogin")) {
	            doLogin(httpRequest, httpResponse);
	        } else if (action != null && action.equals("doLogout")) {
	            doLogout(httpRequest, httpResponse);
	        } else {
	            noAction(httpRequest, httpResponse, filterChain);
	        }
	    }

	/**
	     * Voláno pokud v dotazu není specifikovaná akce
	     */
	    private void noAction(HttpServletRequest request,
	                          HttpServletResponse response,
	                          FilterChain filterChain)
	            throws IOException, ServletException {
	        // Zkusíme najít cookie 'cookieNick'
	        Cookie cookie = findNickCookie(request);
	        if (cookie != null) {
	            // Cookie přišlo -> uživatel je přihlášen.
	            // Nastavíme nick uživatele jako atribut dotazu,
	            // aby jej šlo vypisovat na stránkách.
//	            request.setAttribute("user", cookie.getValue());
	        	// Nastavíme nick uživatele jako atribut session.
	        	HttpSession session = request.getSession();
	        	String nickName = cookie.getValue();
	        	// Nastavení pouze pokud ještě není v session.
	        	if (!nickName.equals(session.getAttribute("user"))) {
	        		session.setAttribute("user", nickName);
	        	}

	            // Předáme řízení standardním způsobem
	            filterChain.doFilter(request, response);
	        } else {
	            // Cookie se nenašlo -> uživatel není přihlášen.
	            // Předáme řízení na login stránku.
	            forwardToLogin(request, response);
	        }
	    }


	 /**
     * Odhlašuje uživatele smazáním cookie v prohlížeči.
     */
    private void doLogout(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        // Zkusíme najít cookie, které chceme smazat
        Cookie cookie = findNickCookie(request);
        if (cookie != null) {
            // Nastavení maxAge na 0 vede ke smazání cookie 
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        // Předáme řízení login.jsp
        HttpSession session = request.getSession(false);
        if (session != null) {
        session.invalidate();
        }

        forwardToLogin(request, response);
    }
    
    
    /**
     * Hledá cookie s názvem <code>cookieNick</code>
     *
     * @param request dotaz
     * @return cookie nebo null
     */
    private Cookie findNickCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookieNick".equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }


	/**
     * Reaguje na akci doLogin. Z dotazu zjistí nick. Pokud
     * není přítomen, vrátí řízení na login.jsp. Jinak
     * vytvoří cookie a nastaví do něj nick z dotazu. Každý
     * další dotaz by měl nést toto cookie. Přítomnost cookie
     * v dotazu se chápe jako identifikace aktuálního uživatele.
     */
    private void doLogin(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {
        // login.jsp předává parametr nick
        String nickname = request.getParameter("nick");
        if ("".equals(nickname)) {
            forwardToLogin(request, response);
            return;
        }
        // Název cookie je cookieNick, hodnota je vlastní nick
        Cookie cookie = new Cookie("cookieNick", nickname);

        // Nastavíme 24 hodin platnost.
        //cookie.setMaxAge(60 * 60 * 24);

        // Hodnota -1 by způsobila, že cookie platí pouze
        // do zavření prohlížeče.
        cookie.setMaxAge(-1);
        response.addCookie(cookie);

        // Přesměrujeme aktuální dotaz na emailový formulář
        response.sendRedirect("mailForm.jsp");
    }


    /**
     * Předává řízení na <code>login.jsp</code>.
     */
    private void forwardToLogin(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        // Získáme dispečera stránky
        RequestDispatcher loginDispatcher =
                request.getRequestDispatcher("login.jsp");
        loginDispatcher.forward(request, response);
    }


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
