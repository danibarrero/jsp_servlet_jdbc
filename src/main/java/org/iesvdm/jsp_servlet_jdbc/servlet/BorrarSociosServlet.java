package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BorrarSociosServlet", value = "/BorrarSociosServlet")
public class BorrarSociosServlet extends HttpServlet {

    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = null;

        // Recepción del parámetro enviado por el formulario de borrar
        String codigoSTR = request.getParameter("codigo");

        // Validación del parámetro
        Integer codigo = null;

        try {
            codigo = Integer.parseInt(codigoSTR);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (codigo != null) {
            //Parametro valido
            socioDAO.delete(codigo);

            //SI QUISIERA REDIRECCION DEL LADO DEL NAVEGADOR
            //response.sendRedirect("ListarSociosServlet");

            //REDIRECCION INTERNA
            List<Socio> listado = socioDAO.getAll();
            request.setAttribute("listado", listado);

            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");
            dispatcher.forward(request, response);

        }
    }
}
