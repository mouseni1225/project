package _03_listCoupon.model.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import _00_init.GlobalService;
import _01_login.model.MemberBean;
import _03_listCoupon.model.CouponBean;
import _03_listCoupon.model.CouponDAOJndi;

@WebServlet("/_03_listCoupon/DisplayPageCoupons")

//跟_03_listCoupons/listCoupons.jsp 資料交流
public class DisplayPageCoupons extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int pageNo = 1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 先取出session物件
		HttpSession session = request.getSession(false);

		// 紀錄目前請求的RequestURI,以便使用者登入成功後能夠回到原本的畫面
		// 如果session物件不存在
		if (session == null) {
			// 請使用者登入
			response.sendRedirect(response.encodeRedirectURL("../_01_login/login.jsp"));
			return;
		}
		MemberBean mb = (MemberBean) session.getAttribute("mb");
		String memberId = mb.getMemberNicknName();
		// 本類別負責讀取資料庫內coupon表格內某一頁的紀錄，並能新增紀錄、修改紀錄、刪除記錄等
		CouponDAOJndi bab = null;
		try {
			String pageNoStr = request.getParameter("pageNo");
			if (pageNoStr == null) {
				pageNo = 1;
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie c : cookies) {
						// System.out.println("DisplayPageProducts.java,
						// getName=" + c.getName());
						if (c.getName().equals(memberId + "pageNo")) {

							try {
								pageNo = Integer.parseInt(c.getValue().trim());
							} catch (NumberFormatException e) {
								;
							}
							break;
						}
					}
				}
			} else {
				try {
					pageNo = Integer.parseInt(pageNoStr.trim());
				} catch (NumberFormatException e) {
					pageNo = 1;
				}
			}
			bab = new CouponDAOJndi();
			//
			bab.setPageNo(pageNo);
			bab.setRecordsPerPage(GlobalService.RECORDS_PER_PAGE);
			// bab.getPageCoupon():讀取某一頁的所有紀錄
			Collection<CouponBean> coll = bab.getAllCoupon();
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("totalPages", bab.getTotalPages());
			request.setAttribute("recordsPerPage", 2);
			request.setAttribute("coupons", coll);

			// -----------------------
			Cookie pnCookie = new Cookie(memberId + "pageNo", String.valueOf(pageNo));
			pnCookie.setMaxAge(30 * 24 * 60 * 60);
			pnCookie.setPath(request.getContextPath());
			response.addCookie(pnCookie);
			// -----------------------
			// 交由listBooks.jsp來顯示某頁的書籍資料，同時準備『第一頁』、
			// 『前一頁』、『下一頁』、『最末頁』等資料
			RequestDispatcher rd = request.getRequestDispatcher("listCoupon.jsp");
			rd.forward(request, response);
			return;
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}