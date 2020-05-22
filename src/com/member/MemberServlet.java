package com.member;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.util.FileManager;
import com.util.MyUploadServlet;

@MultipartConfig
@WebServlet("/member/*")

public class MemberServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;


	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
				
		HttpSession session=req.getSession();
		
		// 戚耕走研 煽舌拝 井稽(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"photo";
		
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
			delete(req, resp);
		} else if(uri.indexOf("myPage.do")!=-1) {
			myPage(req, resp);
		} else if(uri.indexOf("myPageSubmit")!=-1) {
			myPageSubmit(req,resp);
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
	
//噺据亜脊伊装	
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
		dto.setImageFilename(req.getParameter("imageFilename"));
		
		try {
			dao.insertMember(dto);
		} catch (Exception e) {
			String message = "噺据 亜脊戚 叔鳶 梅柔艦陥.";
			
			req.setAttribute("title", "Sign up");
			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}
		
		
		  Part p = req.getPart("upload"); 
		  Map<String, String> map = doFileUpload(p,pathname);
		  
		  // map戚 null戚檎 揮閃醤敗 督析戚 蒸澗 依績生稽 
		  if(map!=null) { 
		  String saveFilename = map.get("saveFilename");
		  String originalFilename = map.get("originalFilename");
		  long fileSize = p.getSize();
		  
		  dto.setSaveFilename(saveFilename); 
		  dto.setOriginalFilename(originalFilename);
		  dto.setFilesize(fileSize); 
		  
		  
		  }
		  resp.sendRedirect(cp+"/member/main.do");
	}
		 

		/*
		 * String filename=null; Part p = req.getPart("upload"); Map<String, String> map
		 * = doFileUpload(p, pathname); if(map != null) { filename =
		 * map.get("saveFilename"); } if(filename!=null) {
		 * dto.setImageFilename(filename); }
		 */
		
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
			req.setAttribute("message", "<span style='color:red;'>鳶什趨球亜 析帖馬走 省柔艦陥.</span>");
			forward(req, resp, "/WEB-INF/views/member/mypage.jsp");
			return;
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
		resp.sendRedirect(cp+"/member/myPage.do");
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
		dto.setUserPwd(req.getParameter("userName"));
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
		if(req.getParameter("filesize")!=null) {
			dto.setFilesize(Long.parseLong(req.getParameter("filesize")));			
		}
		
		dto.setUserId(info.getUserId());
		
		Part p =req.getPart("upload");
		Map<String, String> map = doFileUpload(p, pathname);
		if(map!=null) {
			// 奄糎 督析 肢薦
			if(req.getParameter("saveFilename").length()!=0) {
				FileManager.doFiledelete(pathname, req.getParameter("saveFilename"));
			}
			
			//歯稽錘 督析
			String saveFilename = map.get("saveFilename");
			String originalFilename = map.get("originalFilename");
			long size = p.getSize();
			dto.setSaveFilename(saveFilename);
			dto.setOriginalFilename(originalFilename);
			dto.setFilesize(size);
		}
		
		try {
			dao.updateMember(dto);
		} catch (Exception e) {
			String message = "噺据 呪舛戚 叔鳶 梅柔艦陥.";
			
			req.setAttribute("title", "update");
			req.setAttribute("mode", "update");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/main/main.jsp");
			return;
		}
		resp.sendRedirect(cp+"/main/main.do");
		
	}
//噺据纏盗	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
//myPage(鎧舛左 廿)	
	private void myPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		
		String mode=req.getParameter("mode");
		if(mode.equals("update")) {
			req.setAttribute("title", "噺据 舛左 呪舛");
		}else {
			req.setAttribute("title", "噺据 纏盗");
		}
		req.setAttribute("mode", mode);
			
		req.setAttribute("title", "噺据 舛左 呪舛");
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");

		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	
	}
	
//myPage
	private void myPageSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
	
	
}
