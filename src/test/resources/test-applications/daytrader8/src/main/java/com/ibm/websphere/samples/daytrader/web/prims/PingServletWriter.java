/**
 * (C) Copyright IBM Corporation 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * PingServlet extends PingServlet by using a PrintWriter for formatted output
 * vs. the output stream used by {@link PingServlet}.
 *
 */
@WebServlet(name = "PingServletWriter", urlPatterns = { "/servlet/PingServletWriter" })
public class PingServletWriter extends HttpServlet {

    private static final long serialVersionUID = -267847365014523225L;
    private static String initTime;
    private static int hitCount;

    /**
     * forwards post requests to the doGet method Creation date: (11/6/2000
     * 10:52:39 AM)
     *
     * @param res
     *            javax.servlet.http.HttpServletRequest
     * @param res2
     *            javax.servlet.http.HttpServletResponse
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    /**
     * this is the main method of the servlet that will service all get
     * requests.
     *
     * @param request
     *            HttpServletRequest
     * @param responce
     *            HttpServletResponce
     **/
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("text/html");

            // The following 2 lines are the difference between PingServlet and
            // PingServletWriter
            // the latter uses a PrintWriter for output versus a binary output
            // stream.
            // ServletOutputStream out = res.getOutputStream();
            java.io.PrintWriter out = res.getWriter();
            hitCount++;
            out.println("<html><head><title>Ping Servlet Writer</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping Servlet Writer:<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : "
                    + initTime + "<BR><BR></FONT>  <B>Hit Count: " + hitCount + "</B></body></html>");
        } catch (Exception e) {
            Log.error(e, "PingServletWriter.doGet(...): general exception caught");
            res.sendError(500, e.toString());
        }
    }

    /**
     * returns a string of information about the servlet
     *
     * @return info String: contains info about the servlet
     **/

    @Override
    public String getServletInfo() {
        return "Basic dynamic HTML generation through a servlet using a PrintWriter";
    }

    /**
     * called when the class is loaded to initialize the servlet
     *
     * @param config
     *            ServletConfig:
     **/
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();

    }
}
