package information;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/pages/information/atriclegetimg")
public class AtricleGetImage extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("================來自atriclegetimg.java的訊息================");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();// 取得連線
		String id = request.getParameter("id");// 取得文章編號
		System.out.println(id);
		Connection conn = null;
		OutputStream os = null;
		InputStream is = null;
		try {

			Context context = new InitialContext();
			// 透過JNDI取得DataSource物件
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/xxx");
			conn = ds.getConnection();
			PreparedStatement pstmt = null;

			pstmt = conn.prepareStatement(" select memberBigPhoto from member where memberId=?");
		
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// Image欄位可以取出InputStream物件
//				String fileName = rs.getString(1);// SQL 指令 第一格 cpid
				is = rs.getBinaryStream("memberBigPhoto");// SQL 指令 第一格 cpPhoto
//				String mimeType = getServletContext().getMimeType(fileName);
				// 設定輸出資料的型態
//				response.setContentType(mimeType);
				// 取得能寫出非文字資料的OutputStream物件
				os = response.getOutputStream();

			
				
				int count = 0;
				byte[] bytes = new byte[8192];
				while ((count = is.read(bytes)) != -1) {
					os.write(bytes, 0, count);
				}
			}
		} catch (NamingException e) {
			throw new ServletException(e);
		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close(); // 一定要註解此行來執行本程式五次
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				os.close();
			}
		}
	}
	
}
