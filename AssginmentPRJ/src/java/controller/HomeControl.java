package controller;

import dal.DAOCategories;
import dal.DAOOrderDetails;
import dal.DAOProducts;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Categories;
import model.Products;

/**
 * HomeControl Servlet to handle home page requests
 */
public class HomeControl extends HttpServlet {

    /**
     * Handles the HTTP GET method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            session.removeAttribute("listorderdetail");

            DAOProducts dao = new DAOProducts();

            String index1 = request.getParameter("index");
            if (index1 == null) {
                index1 = "1";
            }
            int index = Integer.parseInt(index1);

            List<Products> productsList = dao.getAllOffFetchHome(index);
            int totalProducts = dao.getTotalProducts();
            int totalPages = totalProducts / 15;
            if (totalProducts % 15 != 0) {
                totalPages++;
            }

            DAOCategories daoCategories = new DAOCategories();
            List<Categories> categoriesList = daoCategories.getAll();

            DAOOrderDetails daoOrderDetails = new DAOOrderDetails();
            int mostOrderedProductId = daoOrderDetails.getProducIdByOrderDetails();
            Products mostOrderedProduct = dao.getProductByID(mostOrderedProductId);

            request.setAttribute("endpage", totalPages);
            request.setAttribute("categories", categoriesList);
            request.setAttribute("products", productsList);
            request.setAttribute("last", mostOrderedProduct);

            request.getRequestDispatcher("Home.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Handle NumberFormatException specifically
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page index");
        } catch (Exception e) {
            // Handle general exceptions
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implement POST request handling if needed
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "HomeControl Servlet";
    }
}
