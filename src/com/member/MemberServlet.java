package com.member;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.notice.NoticeDTO;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/member/*")

public class MemberServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;


	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
				
		HttpSession session=req.getSession();
		
		// 戚耕走研 煽舌拝 井稽(pathname)
		String root = session.getServletContext().getRealPath("/");
     	pathname = root + "uploads" + File.separator + "travel";
		
		
		String uri=req.getRequestURI();
		if(uri.indexOf("login.do")!=-1) {
			loginForm(req, resp);
		} else if(uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req, resp);
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		} else if(uri.indexOf("member.do")!=-1) {
			memberForm(req, resp);
		} else if(uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req, resp);
		} else if(uri.indexOf("pwd.do")!=-1) {
			pwdForm(req, resp);
		} else if(uri.indexOf("pwd_ok.do")!=-1) {
			pwdSubmit(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req,resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			deleteSubmit(req, resp);
		} else if(uri.indexOf("myPage.do")!=-1) {
			myPage(req, resp);
		} else if(uri.indexOf("list.do")!=-1) {
			listForm(req, resp);
		}
	}
//稽益昔 廿
	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		String path="/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}
	
//稽益昔 伊装	
	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		
		MemberDAO dao=new MemberDAO();
		
		String userId=req.getParameter("userId");
		String userPwd=req.getParameter("userPwd");
		
		MemberDTO dto=dao.readMember(userId);
		
		if(dto==null || !dto.getUserPwd().equals(userPwd)) {
			String s="焼戚巨 暁澗 鳶什趨球亜 析帖馬走 省柔艦陥. 陥獣 脊径背爽室推.";
			req.setAttribute("messege", s);
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		//稽益昔 失因
		HttpSession session = req.getSession();
		
		//室芝 政走獣娃 30歳
		session.setMaxInactiveInterval(30*60);
		
		//室芝拭 稽益昔 舛左 煽舌
		SessionInfo info=new SessionInfo();
		info.setUserId(dto.getUserId());
		info.setUserName(dto.getUserName());
		
		session.setAttribute("member", info);
	
		resp.sendRedirect(cp);//湛鉢檎生稽 宜焼亜虞たたたたたた
	}
	
//稽益焼数
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		
		session.invalidate();
		
		resp.sendRedirect(cp);
	}
	
//噺据亜脊廿	
	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("title", "Sign up");
		req.setAttribute("mode", "created");
		
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}
	
//噺据亜脊 刃戟
	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao=new MemberDAO();
		MemberDTO dto = new MemberDTO();
		String cp=req.getContextPath();
		
		dto.setUserId(req.getParameter("userId"));
		dto.setUserPwd(req.getParameter("userPwd"));
		dto.setUserName(req.getParameter("userName"));
		
		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");	
		if (tel1.length() != 0 && tel2.length() != 0 && tel3.length() != 0) {
			dto.setUserTel(tel1 + "-" + tel2 + "-" + tel3);
		}
		
		String email1 =req.getParameter("email1");
		String email2 =req.getParameter("email2");		
		if (email1.length() != 0 && email2.length() != 0) {
			dto.setUserEmail(email1 + "@" + email2);
		}
		
		dto.setUserBirth(req.getParameter("userBirth"));
	
		Part p = req.getPart("upload"); 
		Map<String, String> map = doFileUpload(p,pathname);
		  
		 // map戚 null戚檎 揮閃醤敗 督析戚 蒸澗 依績生稽 
		if(map!=null) { 
			String saveFilename = map.get("saveFilename");
			if(saveFilename!=null) {
				dto.setImageFilename(saveFilename);
			}	  
		}
		
		try {
			dao.insertMember(dto);
		}catch(Exception e){
			String message = "噺据 亜脊戚 叔鳶 梅柔艦陥.";
						
			req.setAttribute("title", "Sign up");
			req.setAttribute("mode", "created");	
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
					  
			return;
		}
		
		resp.sendRedirect(cp+"/member/main.do");
	}		 

		
// 鳶什趨球 溌昔 廿	
	private void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session=req.getSession();
		String cp=req.getContextPath();
				
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		// 稽益焼数雌殿戚檎
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
				
		String mode=req.getParameter("mode");
		if(mode.equals("update")) {
			req.setAttribute("title", "噺据 舛左 呪舛");
		}else {
			req.setAttribute("title", "噺据 纏盗");
		}
		
		req.setAttribute("mode", mode);
		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");	
	}
	
//鳶什趨球溌昔	
	private void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		String cp=req.getContextPath();
		MemberDAO dao=new MemberDAO();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// DB拭辞 背雁 噺据 舛左 亜閃神奄
		MemberDTO dto=dao.readMember(info.getUserId());
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
		String userPwd=req.getParameter("userPwd");
		String mode=req.getParameter("mode");
		
		if(! dto.getUserPwd().equals(userPwd)) {
			if(mode.equals("update")) {
				req.setAttribute("title", "噺据 舛左 呪舛");
			}else {
				req.setAttribute("title", "噺据 纏盗");
			}
			req.setAttribute("mode", mode);
			req.setAttribute("message","<span style='color:red;'>鳶什趨球亜 析帖馬走 省柔艦陥.</span>");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}else {
			myPage(req,resp);
		}
		
		if(mode.equals("delete")) {
			// 噺据纏盗
			try {
				dao.deleteMember(info.getUserId());
			} catch (Exception e) {
			}
			
			session.removeAttribute("member");
			session.invalidate();
			
			resp.sendRedirect(cp);
			
			return;
		}
	}	
	
//噺据舛左呪舛	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//室芝 戚遂背辞 噺据舛左 伊事馬奄 (DAO)
		// 噺据舛左呪舛 - 噺据呪舛廿生稽 戚疑
		HttpSession session=req.getSession();
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// DB拭辞 背雁 噺据 舛左 亜閃神奄
		MemberDTO dto=dao.readMember(info.getUserId());
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
			
		req.setAttribute("title", "噺据 舛左 呪舛");
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");

		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}
	
	
//噺据舛左 呪舛刃戟	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		String cp=req.getContextPath();
		MemberDAO dao=new MemberDAO();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		MemberDTO dto = new MemberDTO();
	
		dto.setUserId(req.getParameter("userId"));
		dto.setUserPwd(req.getParameter("userPwd"));
		dto.setUserName(req.getParameter("userName"));
		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");
		if (tel1.length() != 0 && tel2.length() != 0 && tel3.length() != 0) {
			dto.setUserTel(tel1 + "-" + tel2 + "-" + tel3);
		}
		String email1 =req.getParameter("email1");
		String email2 =req.getParameter("email2");
		if (email1.length() != 0 && email2.length() != 0) {
			dto.setUserEmail(email1 + "@" + email2);
		}
		dto.setUserBirth(req.getParameter("userBirth"));

	//督析	

		dto.setUserId(info.getUserId());
		
		Part p =req.getPart("upload");
		Map<String, String> map = doFileUpload(p, pathname);
		if(map!=null) {
			// 奄糎 督析 肢薦
			if(req.getParameter("imageFilename").length()!=0) {
				FileManager.doFiledelete(pathname, req.getParameter("imageFilename"));
			}
			
			//歯稽錘 督析
			String saveFilename = map.get("saveFilename");
			dto.setImageFilename(saveFilename);
		}
		
		try {
			dao.updateMember(dto);
			resp.sendRedirect(cp+"/member/myPage.do");
		} catch (Exception e) {
			String message = "噺据 呪舛戚 叔鳶 梅柔艦陥.";
			
			dto=dao.readMember(info.getUserId());
			if(dto==null) {
				session.invalidate();
				resp.sendRedirect(cp);
				return;
			}
				
			req.setAttribute("title", "噺据 舛左 呪舛");
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			req.setAttribute("message", message);
			
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
		}
	}
//噺据纏盗	
	private void deleteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// DB拭辞 背雁 噺据 舛左 亜閃神奄
		MemberDTO dto=dao.readMember(info.getUserId());
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
			try {
				dao.deleteMember(info.getUserId());
			} catch (Exception e) {
			}
			
			session.removeAttribute("member");
			session.invalidate();
			
			resp.sendRedirect(cp);
			
			return;
		//}
	}
//myPage(鎧舛左 廿)	
	private void myPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		MemberDAO dao=new MemberDAO();
		String cp = req.getContextPath();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// DB拭辞 背雁 噺据 舛左 亜閃神奄
		MemberDTO dto = dao.readMember(info.getUserId());
		
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
		
		req.setAttribute("title", "MyPage");
		 
		req.setAttribute("dto", dto);	

		forward(req, resp, "/WEB-INF/views/member/myPage.jsp");
	}
//噺据軒什闘
	private void listForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		HttpSession session=req.getSession();
		MemberDAO dao = new MemberDAO();
		String cp=req.getContextPath();
		MyUtil myUtil=new MyUtil();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //稽益焼数 吉 井酔
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page=Integer.parseInt(page);
		}
	
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {  //condition亜 null析凶 keyword亜 伊事戚 焼還...
			condition="userId";
			keyword="";
		} 
		//伊事獄動 刊牽檎 Post稽 角嬢身
		
		//昔坪漁聖 背醤馬艦猿 巨坪漁聖 背捜
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword,"utf-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount=dao.dataCount();
		}else {
			dataCount=dao.dataCount(condition,keyword);
		}
		
		int rows=10;
		int total_page=myUtil.pageCount(rows, dataCount);
		if(current_page>total_page) {
			current_page=total_page;
		}
		
		int offset = (current_page - 1) * rows;
	
		List<MemberDTO> list;
		if (keyword.length() != 0)
			list = dao.listBoard(offset, rows, condition, keyword);
		else
			list = dao.listBoard(offset, rows);
		
		// 惟獣弘 腰硲研 仙舛税, 獣碇什亜 肢薦鞠檎 掻姥貝号戚虞辞
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
		}
		//凪戚臓坦軒
		String listUrl=cp+"/member/list.do";
		if (query.length() != 0) {
			listUrl += "?" + query;
		}
		
		String paging = myUtil.paging(current_page, total_page, listUrl);
		
		// DB拭辞 背雁 噺据 舛左 亜閃神奄
		MemberDTO dto=dao.readMember(info.getUserId());
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
		
		//list.jsp拭 角移匝 汽戚斗
		
		req.setAttribute("dto", dto);	
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/member/list.jsp");
	}
	
}
