package javaee.simpleWebApp_archetype;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p/>
 */
public class SampleServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7512233649961059346L;

	@Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ServletOutputStream output = httpServletResponse.getOutputStream();
        output.print("Servlet test");
        output.flush();
    }
}
