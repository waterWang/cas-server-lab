package com.github.water.rest;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Dom4j4 {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		try {
			
			String text = "<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'><cas:authenticationSuccess><cas:user>user@localhost</cas:user><cas:attributes><cas:phonenumber></cas:phonenumber><cas:userId>4</cas:userId><cas:email>user@localhost</cas:email><cas:username>user</cas:username></cas:attributes></cas:authenticationSuccess></cas:serviceResponse>";
			Document doc = DocumentHelper.parseText(text);
			Element rootElt = doc.getRootElement(); // 获取根节点
			System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
			Iterator bbbb = rootElt.elementIterator("authenticationSuccess"); // /获取根节点下的子节点bbbb
			while (bbbb.hasNext()) {
				Element recordEless = (Element) bbbb.next();
				Iterator cccc = recordEless.elementIterator("attributes"); // 获取子节点bbbb下的子节点cccc
				while (cccc.hasNext()) {
					Element tableItem = (Element) cccc.next();
					String phonenumber = tableItem.elementTextTrim("phonenumber"); 
					String userId = tableItem.elementTextTrim("userId");
					String email = tableItem.elementTextTrim("email");
					String username = tableItem.elementTextTrim("username");
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}