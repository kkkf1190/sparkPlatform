package com.mxgraph.examples.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.mxgraph.examples.db.MysqlUtil;
import com.mxgraph.examples.db.WebServiceDbModel;
import com.mxgraph.examples.web.Constants;

public class ParseSCAModelUtil {
	HashSet<String> models;
	String projectName;
	
	public ParseSCAModelUtil(HashSet<String> modelSet,String projectName){
		this.models = modelSet;
		this.projectName = projectName;
	}
	
	public void parseModels(){
		HashSet<String> SCAModels = new HashSet<String>();
		MysqlUtil mysqlUtil = new MysqlUtil();
		Vector<WebServiceDbModel> webservices = mysqlUtil.getAllType();
		HashMap<Integer,WebServiceDbModel> webservicesMap = new HashMap<Integer, WebServiceDbModel>();
		for(WebServiceDbModel webservice:webservices){
			int i = webservice.getId();
			webservicesMap.put(i, webservice);
		}
		for(String model:models){
			String sql = "select * from methods where URL=\""+model+"\"";
			ResultSet rs = mysqlUtil.executeQuery(sql);
			try {
				int serviceId = -1;
				while(rs.next()){
					serviceId = rs.getInt("serviceId");
					break;
				}
				if(serviceId!=-1){
					String SCAModel = webservicesMap.get(serviceId).getURL();
					if(!SCAModels.contains(SCAModel)){
						SCAModels.add(SCAModel);
					}
				}	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String ans = "";
		for(String model:SCAModels){
			ans += model+";";
		}
		ans = ans.substring(0, ans.length()-1);
		Constants.SESSION_MANAGER.get(this.projectName).put("jars", ans);
	}

}
