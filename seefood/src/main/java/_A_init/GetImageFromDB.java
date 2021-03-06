﻿package _A_init;

import java.io.*;
import java.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.*;

//本類別會依據傳入的書籍編號(BookID)讀取eBook表格內CoverImage欄位內的圖片，
//然後傳回給提出請求的瀏覽器

//取圖方式
//<img height='長度' width='寬度' src="<c:url value='/package名稱/getImage?cpid=${識別ID.get ID(去get改小寫) }&type=coupon'/>">



@WebServlet("/_A_init/getImage")
public class GetImageFromDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			// 讀取瀏覽器傳送來的書籍代號(cpid)
			String id = request.getParameter("cpid");
			// 分辨讀取哪個表格的圖片欄位
			String type = request.getParameter("type");
			// 取得能夠執行JNDI的Context物件
			Context context = new InitialContext();
			// 透過JNDI取得DataSource物件
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/xxx");
			conn = ds.getConnection();
			PreparedStatement pstmt = null;
			// System.out.println("GetImageFromDB, Type==>" + type);
			// System.out.println("GetImageFromDB, ID==>" + id);
			// if (type.equalsIgnoreCase("coupon")) { // 讀取coupon表格
			pstmt = conn.prepareStatement("SELECT cpid, cpPhoto from coupon where cpid = ?");
			// }
			// else if (type.equalsIgnoreCase("MEMBER")) { // 讀取Member表格
			// pstmt = conn.prepareStatement("SELECT memberBigPhoto from Member
			// where memberId = ?");
			// }
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// Image欄位可以取出InputStream物件
				String fileName = rs.getString(1);// SQL 指令 第一格 cpid
				is = rs.getBinaryStream(2);// SQL 指令 第一格 cpPhoto
				String mimeType = getServletContext().getMimeType(fileName);
				// 設定輸出資料的型態
				response.setContentType(mimeType);
				// 取得能寫出非文字資料的OutputStream物件
				os = response.getOutputStream();

				if (is == null) {
					is = getServletContext().getResourceAsStream("images/NoImage.jpg");//如果沒讀到圖片 讀這張
				}
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