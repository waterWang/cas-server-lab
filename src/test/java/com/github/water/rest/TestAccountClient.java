package com.github.water.rest;
import java.io.IOException;  
  

import org.junit.Test;
import org.restlet.representation.Representation;  
import org.restlet.resource.ClientResource;  
  
public class TestAccountClient {  
	private String server = "http://wangweiwei:8888/cas/v1/accounts";
    @Test  
    public void test_get() throws IOException{  
        ClientResource client = new ClientResource(server );  
        Representation result =  client.get() ;     //调用get方法  
        System.out.println(result.getText());    
    }  
      
    @Test  
    public void test_post() throws IOException{  
        ClientResource client = new ClientResource(server);    
        Representation result =  client.post(null) ;        //调用post方法  
        System.out.println(result.getText());    
    }  
      
      
    @Test  
    public void test_put() throws IOException{  
        ClientResource client = new ClientResource(server);    
        Representation result =  client.put("www?4530") ;     //调用put方法  
        System.out.println("55555555"+result.getText());    
    }  
      
      
    @Test  
    public void test_delete() throws IOException{  
        ClientResource client = new ClientResource(server);    
        Representation result =  client.delete() ;      //调用delete方法  
        System.out.println(result.getText());    
    }  
      
      
}  