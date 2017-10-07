import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;

@SuppressWarnings("serial")
public class Main extends HttpServlet {
	
	public static Connection conn;
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  try {
		  if (req.getRequestURI().endsWith("/getall")) {
			  getall(req, resp);
		    }
		  
		  if (req.getRequestURI().endsWith("/getphoto")) {
			  getphoto(req, resp);
		    }

		  if (req.getRequestURI().endsWith("/db")) {
		    	createDatabase(req, resp);
		    }
		  
		  } catch (Exception e) {
		      resp.getWriter().print("There was an error: " + e.getMessage());
		    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  try {
	  if (req.getRequestURI().endsWith("/createuser")) {
	    	createuser(req, resp);
	    }
	  else if (req.getRequestURI().endsWith("/updatephoto")) {
	    	updatephoto(req, resp);
	    }
	  else if (req.getRequestURI().endsWith("/updatestatus")) {
	    	updatestatus(req, resp);
	    }
	  if (req.getRequestURI().endsWith("/getdetails")) {
		  getdetails(req, resp);
	    }
	  
	  } catch (Exception e) {
	  resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
  }
  
  @SuppressWarnings("unchecked")
private void getall(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();
	    	
	    	Statement stmt = conn.createStatement();
		  	ResultSet rs = stmt.executeQuery("select name,number,status from contacts");  
		  	JSONObject output2 = new JSONObject();
		  	output2.put("results", convert(rs));
		  	PrintWriter out = resp.getWriter();
			out.print(output2);
		  	
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
  }
  
	@SuppressWarnings("unchecked")
	public static JSONArray convert(ResultSet rs) throws SQLException,
	JSONException {
		JSONArray json = new JSONArray();
		ResultSetMetaData rsmd = rs.getMetaData();

		while (rs.next()) {
			int numColumns = rsmd.getColumnCount();
			JSONObject obj = new JSONObject();

			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnName(i);

				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(column_name, rs.getArray(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(column_name, rs.getBoolean(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(column_name, rs.getBlob(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(column_name, rs.getDouble(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(column_name, rs.getFloat(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(column_name, rs.getDate(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(column_name, rs.getTimestamp(column_name));
				} else {
					obj.put(column_name, rs.getObject(column_name));
				}
			}

			json.put(obj);

		}
		return json;
	}
  
  
  @SuppressWarnings("unchecked")
private void getdetails(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();

	    	org.json.simple.JSONArray input = new org.json.simple.JSONArray();
	    	JSONArray output = new JSONArray();
			StringBuffer jb = new StringBuffer();
			String line = null;
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);

			JSONObject output2 = new JSONObject();
			JSONObject input2 = (JSONObject) new JSONParser().parse(jb.toString());
			input = (org.json.simple.JSONArray) input2.get("contacts");
			
			for(int i=0;i<input.size();i++)
			{
				JSONObject icnt = (JSONObject) input.get(i);
				String number = icnt.get("number").toString();
				String statusupdated = icnt.get("statusupdated").toString();
				String photoupdated = icnt.get("photoupdated").toString();
				
				Statement stmt = conn.createStatement();
			  	ResultSet rs = stmt.executeQuery("select * from contacts where number LIKE '%"+number +"'");  
			  	
			  	if(rs.next())
			  	{
			  		int flag=0;
			  		JSONObject dcnt = new JSONObject();
			  		dcnt.put("number", number);
			  		dcnt.put("email", rs.getString("email"));
			  		String dstatusupdated = rs.getString("statusupdated");
			  		String dphotoupdated = rs.getString("photoupdated");
			  		if(Long.parseLong(statusupdated)<Long.parseLong(dstatusupdated))
			  		{
			  			dcnt.put("status", rs.getString("status"));
			  			dcnt.put("statusupdated", dstatusupdated);
			  			flag=1;
			  		}
			  		else
			  		{
			  			dcnt.put("status", rs.getString("status") );
			  			dcnt.put("statusupdated", statusupdated);
			  		}
			  		if(Long.parseLong(photoupdated)<Long.parseLong(dphotoupdated))
			  		{
			  			dcnt.put("photoupdated", dphotoupdated);
			  			dcnt.put("photoflag", "true");
			  			flag=1;
			  		}
			  		else
			  		{
			  			dcnt.put("photoupdated", dphotoupdated);
			  			dcnt.put("photoflag", "false");
			  		}
			  		if(flag==1)
			  			output.put(dcnt);
			  	}
			  	
				
			}
				
			
			output2.put("contacts", output);
			PrintWriter out = resp.getWriter();
			out.print(output2);
			resp.setStatus(200);
			resp.setContentType("application/json");
			out.flush();
			
	    	
	    } catch (Exception e) {
	    	resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
	  }
  
  private void getphoto(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();
	    	
	    	resp.setContentType("image");
	    	PreparedStatement ps = conn.prepareStatement("SELECT photo FROM contacts WHERE number=?");
	    	ps.setString(1, req.getHeader("number"));
	    	ResultSet rs = ps.executeQuery();
	    	OutputStream output = resp.getOutputStream();
	    	
	    	  //output.close();
	    	if (rs != null) {
	    	    while(rs.next()) {
	    	        byte[] imgBytes = rs.getBytes(1);
	    	        output.write(imgBytes);
	    	    }
	    	    rs.close();
	    	}
	    	output.close();
	    	ps.close();
	    	
	    } catch (Exception e) {
	    	resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
	  }
 
  
  private void createuser(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();

	    	Statement stmt2 = conn.createStatement();
		  	ResultSet rs = stmt2.executeQuery("select * from contacts where number = '"+req.getHeader("number") +"'");  
		  	
		  	if(rs.next())
		  	{
		  		
		  	}
		  	else{
	      Statement stmt = conn.createStatement();
	  	  stmt.executeUpdate("INSERT INTO contacts VALUES ('"+req.getHeader("name")+"','"+req.getHeader("number")+"','Joined Status App','"+String.valueOf(System.currentTimeMillis())+"','','0','"+req.getHeader("email")+"','"+req.getHeader("password")+"')");
	  	  //send mail to user
		  	}
	  	resp.getWriter().print("user created");
	  	
	    } catch (Exception e) {
	    	resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
	  }
  
  private void updatestatus(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();

	      Statement stmt = conn.createStatement();
	  	  stmt.executeUpdate("UPDATE contacts set status = '"+req.getHeader("status")+"', statusupdated = '"+String.valueOf(System.currentTimeMillis())+"' where number = '"+req.getHeader("number")+"'");  
	  	resp.getWriter().print("status updated");
	    } catch (Exception e) {
	    	resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
	  }
  
  private void updatephoto(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	    try {
	    	if(conn == null||conn.isClosed())
	    		connectdb();

	  	PreparedStatement ps = conn.prepareStatement("UPDATE contacts set photo = ?, photoupdated = ? where number = ?");
	  	ps.setBinaryStream(1, req.getInputStream(), req.getContentLength());
	  	ps.setString(2, String.valueOf(System.currentTimeMillis()));
	  	ps.setString(3, req.getHeader("number"));
	  	ps.executeUpdate();
	  	  
	  	resp.getWriter().print("photo updated");
	    } catch (Exception e) {
	    	resp.setStatus(500);
	      resp.getWriter().print("There was an error: " + e.getMessage());
	    }
	  }
  
  
private void createDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
    	if(conn == null||conn.isClosed())
    		connectdb();

      Statement stmt = conn.createStatement();
      stmt.executeUpdate("DROP TABLE contacts");  
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS contacts (name varchar(50), number varchar(20), status varchar(250),statusupdated varchar(100), photo bytea, photoupdated varchar(100),email varchar(100),password varchar(100))");  
      
      resp.getWriter().print("DB created");
    } catch (Exception e) {
    	resp.setStatus(500);
      resp.getWriter().print("There was an error: " + e.getMessage());
    }
  }

  
  public static void connectdb() throws SQLException
	{	
		conn = DriverManager.getConnection(
				"jdbc:postgresql://ec2-54-83-204-104.compute-1.amazonaws.com:5432/dfaorpn82r4hbe?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
				"yhqltykgbqtpkr", "4O45JiYOW9N_KJxGI71n7YKria");
	}

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
	  //Server server = new Server(8087);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    if(conn == null||conn.isClosed())
		connectdb();
    server.start();
    server.join();
  }
}
