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
import java.util.Optional;

@WebServlet(name = "EditarSociosServlet", value = "/EditarSociosServlet")
public class EditarSociosServlet extends HttpServlet {

    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = null;

        String codigoSTR = request.getParameter("codigo"); //El codigo sale de listadoSocioB
        int codigo = Integer.parseInt(codigoSTR);

        try {
            Socio socio= this.socioDAO.find(codigo).orElse(null);
            request.setAttribute("socio", socio);
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "No se encontro el codigo");
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = null;

        int numero = 0;
        
        try {
            numero = Integer.parseInt(request.getParameter("codigo"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID Socio no válido");
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request, response);
            return;
        }


        Optional<Socio> socioOptional = this.socioDAO.find(numero);
        if (!socioOptional.isPresent()) {
            request.setAttribute("error", "No existe el socio con el codigo: " + numero);
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Optional<Socio> socioValidado = UtilServlet.validaGrabar(request);
        if (socioValidado.isPresent()) {
            Socio editarSocio = socioValidado.get();
            editarSocio.setSocioId(numero);

            this.socioDAO.update(editarSocio);

            List<Socio> listado = this.socioDAO.getAll();
            request.setAttribute("listado", listado);
            request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error en la validación de los datos, Inténtelo de nuevo");
            request.setAttribute("editarSocio", socioOptional.get());
            request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp").forward(request, response);
        }
    }
}

