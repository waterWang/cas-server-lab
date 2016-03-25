/**
* @author weiwei.Wang
* @date 2016年3月25日 
* @todo TODO
*/ 
package com.github.water.rest; 

/**
 * @author weiwei.Wang
 *
 */
public class test {
	
	public static void main(String[] args) {
		String str = "www?123";
		String[] str1 = str.split("[?]");
		for (int i = 0; i < str1.length; i++) {
			System.out.println(str1[i]);
		}
	}
}
 