package mx.uaememx;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.uaemex.fi.ing_software_ii.faq.model.Tema;
import mx.uaemex.fi.ing_software_ii.faq.model.TemasDAO;
import mx.uaemex.fi.ing_software_ii.faq.model.TemasDAODerbyImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Servlet implementation class ConsultaTemas
 */
public class ConsultaTemas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource ds;
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			if (ctx != null) {
				this.ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ds");
				if (this.ds == null) {
					throw new ServletException("DataSource no encontrado.");
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemasDAO dao;
		List<Tema> temas = new ArrayList<>();
		Connection con;
		
		try {
			con = this.ds.getConnection();
			dao = new TemasDAODerbyImp();
			((TemasDAODerbyImp) dao).setCon(con);
			temas = dao.consultarSubTemas(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		PrintWriter pagina;
		response.setContentType("text/html");
		pagina = response.getWriter();
		pagina.println("<!DOCTYPE html>");
		pagina.println("<html>");
		pagina.println("<head>");
		pagina.println("<title>Temas</title>");
		pagina.println("</head>");
		pagina.println("<body>");
		pagina.println("<form>");
		pagina.println("<select>");
		for(Tema tema: temas) {
			pagina.println("<option>" + tema.getNombre()+ "</option>");
		}
		pagina.println("</select>");
		pagina.println("</form>");
		pagina.println("</body>");
		pagina.println("</html>");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
