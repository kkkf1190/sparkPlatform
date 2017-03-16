package com.mxgraph.examples.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mxgraph.examples.web.Constants;

public class ParseJarUtil {
	String jarFile;
	Map<String,Element> compositeMap = null;
	String SCAName = null;
	String url = null;
	public ParseJarUtil(String path){
		this.jarFile = path;
		this.compositeMap = new HashMap<String,Element>();
	}
	public List<String> getMethod(){
		List<String> ans = new ArrayList<String>();
		try{
	        String interfaceName = parseInterface(this.jarFile);
	        URL url1=new URL("file:"+this.jarFile);
	        URLClassLoader myClassLoader=new URLClassLoader(new URL[]{url1},Thread.currentThread().getContextClassLoader());
	        String ppName = interfaceName;
	        Class myClass = myClassLoader.loadClass(ppName);
	        //通过getMethods得到类中包含的方法
	        Method methods[] = myClass.getMethods();
	        for(Method method:methods){
	            ans.add(method.getName());
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return ans;
	}
	public String getName(){
		return this.SCAName;
	}
	public String getURL(){
		return this.url;
	}
	
	
	public String parseInterface(String jarFilePath){
        InputStream input = null;
        String qName = null;
        String ans = null;
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration enums = jarFile.entries();
            while(enums.hasMoreElements()){
                JarEntry entry = (JarEntry)enums.nextElement();
                if(entry.getName().endsWith(".composite")){
                    input=jarFile.getInputStream(entry);
                    break;
                }
            }
            SAXReader reader = new SAXReader();
            if(input!=null) {
                Document document = reader.read(input);
                Element root = document.getRootElement();
                this.SCAName = root.attributeValue("name");
                parseComposite(root);
                List<Element> list = root.elements();
                for(Element ele:list) {
                    if (ele.getName().contains("service")) {
                        qName = ele.attributeValue("promote");
                        List<Element> innerList = ele.elements();
                        for(Element ele1:innerList){
                        	if(ele1.getName().contains("binding.ws")){
                        		this.url = ele1.attributeValue("uri");
                        	}
                        }
                        break;
                    }
                }
                if(qName!=null){
                    Element ele = this.compositeMap.get("/"+qName);
                    if(ele!=null){
                        List<Element> list1 = ele.elements();
                        for(Element ele1:list1){
                            if(ele1.getName().contains("interface")){
                                ans = ele1.attributeValue("interface");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void parseComposite(Element root){
        parse(root,this.compositeMap,"");
    }

    public void parse(Element root,Map map,String path){
        List<Element> list = root.elements();
        if(list.size()>0) {
            for (Element ele : list) {
                if(ele.attributeValue("name")!=null) {
                    map.put(path + "/" + ele.attributeValue("name"), ele);
                    parse(ele, map, path + "/" + ele.attributeValue("name"));
                }
            }
        }
    }
	
	public static void main(String[] args){
		ParseJarUtil jarParse = new ParseJarUtil("/home/zhou/test/ws.jar");
		List<String> methods = jarParse.getMethod();
		for(String method:methods){
			System.out.println(method);
		}
	}
}
