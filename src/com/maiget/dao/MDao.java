/**
 * 
 */
package com.maiget.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.maiget.model.NewsBean;

import util.DateUtils;

/**
 * @author hcn
 *	Dao业务层 将数据插入到数据库中
 */
public class MDao extends BaseDao{
	private final Log logger = LogFactory.getLog(MDao.class);
	
	private Connection conn = getConnection();
	private PreparedStatement pstm;
	//添加数据到数据库
	public int addInfo(NewsBean bean) {
		String sql = "insert into mg_article(title,newstime,author,img,content,category,origin,createtime)"
				+ "values(?,?,?,?,?,?,?,?);";
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, bean.getTitle());
			pstm.setString(2, bean.getNewstime());
			pstm.setString(3, bean.getAuthor());
			pstm.setString(4, bean.getImg());
			pstm.setString(5, bean.getContent());
			pstm.setString(6, bean.getCategory());
			pstm.setString(7, bean.getOrigin());
			pstm.setString(8, DateUtils.formatDateToString(new Date()));
//			pstm.setInt(9, 0);
//			pstm.setInt(10, 0);
//			pstm.setInt(11, 0);
			return pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		try {
			conn.close();
			pstm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public boolean findByNews(String title){
		String sql = "select title from mg_article where title like '%"+title+"%'";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs= pstm.executeQuery(sql);
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
			
		}
		
		BaseDao.close();
		return false;
	}
	
	public static void main(String[] ages){
		MDao m = new MDao();
		System.out.println(m.findByNews("曝胡歌薛佳凝复合准备领证 胡歌经纪人:子虚乌有"));
	}
}
